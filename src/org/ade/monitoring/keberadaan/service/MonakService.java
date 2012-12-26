package org.ade.monitoring.keberadaan.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.lokasi.LocationMonitorUtil;
import org.ade.monitoring.keberadaan.lokasi.Tracker;
import org.ade.monitoring.keberadaan.service.koneksi.ReceiverSMS;
import org.ade.monitoring.keberadaan.service.koneksi.SenderMonitoring;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceManager;
import org.ade.monitoring.keberadaan.util.IDGenerator;
import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MonakService extends Service{

    
	@Override
	public void onCreate() {
        Log.d("background service", "creating service");
		pref = new PreferenceManager(this);
		pref.setActiveService();
		mTracker 			= new Tracker(this, null);
		dataMonitorings 	= new DatabaseManager(this).getAllDataMonitorings(false,true);
		locationMonitorUtil = new LocationMonitorUtil();
		senderMonitoring	= new SenderMonitoring(this, null);
		daftarSmsReceiver();
	}

	
	
	@Override
	public void onDestroy() {
		pref.setInActiveService();
		if(receiver!=null){
			unregisterReceiver(receiver);	
		}
		super.onDestroy();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("background service", "service started");
		if(mTracker !=null){
			if(dataMonitorings != null && locationMonitorUtil != null){
				for(DataMonitoring dataMonitoring:dataMonitorings){
					Calendar cal = Calendar.getInstance();
					
					Date	now 	= cal.getTime();	
					int		hari 	= cal.get(Calendar.DAY_OF_WEEK);
					int 	tanggal	= cal.get(Calendar.DAY_OF_MONTH);
				
					Date mulai 		= dataMonitoring.getWaktuMulaiInDate();
					Date selesai 	= dataMonitoring.getWaktuSelesaiInDate();
				
					cal = null;
					
					boolean then = false;
					
					List<DateMonitoring> tanggalMonitorings = dataMonitoring.getTanggals();
					List<DayMonitoring> hariMonitorings = dataMonitoring.getHaris();
					if(tanggalMonitorings!=null){
						for(DateMonitoring tanggalMonitoring:tanggalMonitorings){
							if(tanggal == tanggalMonitoring.getDate()){
								then=true;
								break;
							}
						}
					}
					if(hariMonitorings!=null || !then){
						for(DayMonitoring hariMonitoring:hariMonitorings){
							if(hari == hariMonitoring.getHari()){
								then = true;
								break;
							}
						}
						
					}
					
					if(then){
						if((now.getHours()>mulai.getHours() && now.getHours()<selesai.getHours())
								||now==null){
							if((now.getMinutes()>mulai.getMinutes() && now.getMinutes()<selesai.getMinutes())
									||now==null){
								Lokasi 	lokasiMonitoring 	= dataMonitoring.getLokasi();
								
								Lokasi 	lokasiHp 			= mTracker.getLokasi();
								if(lokasiHp==null)return START_STICKY;
								
								int		tolerancy			= dataMonitoring.getTolerancy();
								locationMonitorUtil.setCurrentLocation(lokasiHp);
								locationMonitorUtil.setMonitorLocation(lokasiMonitoring);
								if(tolerancy!=0){
									locationMonitorUtil.setTolerancy(dataMonitoring.getTolerancy());
								}
								if(locationMonitorUtil.isInTolerancy()){
									if(dataMonitoring.isTerlarang()){
										//TODO : mengirim peringatan
										Peringatan peringatan = new Peringatan();
										peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
										peringatan.setLokasiAnak(lokasiHp);
										peringatan.setTipe(TipePesanData.PERINGATAN_TERLARANG);
										peringatan.setIdOrtu(new IDGenerator(this, null).getIdOrangTua());
										senderMonitoring.sendPeringatanSeharusnya(peringatan);
									}						
								}else{
									if(dataMonitoring.isSeharusnya()){
										//TODO : mengirim peringatan
										Peringatan peringatan = new Peringatan();
										peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
										peringatan.setLokasiAnak(lokasiHp);
										peringatan.setTipe(TipePesanData.PERINGATAN_SEHARUSNYA);
										peringatan.setIdOrtu(new IDGenerator(this, null).getIdOrangTua());
										senderMonitoring.sendPeringatanSeharusnya(peringatan);
									}
								}
							}
							
						}
					}
					

				}
			}
			
		}
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(handlerMonakBinder==null){
			handlerMonakBinder = new MonakBinder(this);
		}
		return handlerMonakBinder;
	}
	
	public void addStorageHandlerWaiting(String key, StorageHandler storage){
		if(mapStorageHandler==null)mapStorageHandler = new HashMap<String, List<StorageHandler>>();
		List<StorageHandler> list = mapStorageHandler.get(key);
		if(list==null){
			list = new ArrayList<StorageHandler>();
		}	
		list.add(storage);
		Log.d("MonakService", "add storae into list with key : "+key);
		mapStorageHandler.put(key, list);
	}
	
	public void removeStorageHandlerWaiting(String key, StorageHandler storage){
		if(mapStorageHandler==null) return;
		List<StorageHandler> list = mapStorageHandler.get(key);
		if(list==null||list.size()<=0)return;
		for(StorageHandler storageFor:list){
			if(storageFor.getIdEntity().equals(storage.getIdEntity())){
				list.remove(storageFor);
				break;
			}
		}
		if(list.size()<=0){
			mapStorageHandler.remove(key);
		}
	}
	
	public void removeStorageHandlerWaiting(String key, String idStorage){
		if(mapStorageHandler==null) return;
		List<StorageHandler> list = mapStorageHandler.get(key);
		if(list==null||list.size()<=0)return;
		for(StorageHandler storageFor:list){
			if(storageFor.getIdEntity().equals(idStorage)){
				list.remove(storageFor);
				break;
			}
		}
		if(list.size()<=0){
			mapStorageHandler.remove(key);
		}
	}

	
	public void addListStorageHandlerWaiting(String key, List<StorageHandler> list){
		if(mapStorageHandler==null)mapStorageHandler = new HashMap<String, List<StorageHandler>>();
		mapStorageHandler.put(key, list);
	}
	
	public void removeListStorageHandlerWaiting(String key){
		if(mapStorageHandler==null) return;
		mapStorageHandler.remove(key);
	}
	
	public void addHandlerUIWaiting(String key, Handler handler){
		if(mapUIHandler==null)mapUIHandler = new HashMap<String, Handler>();
		mapUIHandler.put(key, handler);
	}
	
	public void removeUIHandlerWaiting(String key){
		if(mapUIHandler==null)return;
		mapUIHandler.remove(key);
	}
	
	public Handler getSingleUIHandler(String key){
		if(mapUIHandler==null)return null;
		return mapUIHandler.get(key);
	}
	
	public StorageHandler getSingleStorageHandler(String key, String storageHandlerKey){
		if(mapStorageHandler==null) return null;
		List<StorageHandler> list = mapStorageHandler.get(key);
		StorageHandler result = null;
		Log.d("receiver sms", "get storage handler with key: "+key+" and key handler : "+storageHandlerKey+" size list:"+list.size());
		for(StorageHandler storageHandler: list){
			Log.d("receiver sms", "key handler : "+storageHandler.getIdEntity());
			if(storageHandler.getIdEntity().equals(storageHandlerKey)){
				Log.d("receiver sms", "receive storage handler with key: "+key+" and key handler : "+storageHandlerKey);
				result = storageHandler;
			}
		}
		
		return result;
	}
	
	private void daftarSmsReceiver(){
		receiver = new ReceiverSMS(this);
		IntentFilter intentfilter= new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, intentfilter);
		intentfilter = null;
	}
	
	private Map<String, Handler> 	mapUIHandler 			= new HashMap<String, Handler>();
	private Map<String, List<StorageHandler>> mapStorageHandler = new HashMap<String, List<StorageHandler>>();
	
	private Tracker 				mTracker 			= null;
	private List<DataMonitoring> 	dataMonitorings 	= null;
	private LocationMonitorUtil 	locationMonitorUtil = null;
	private InternetPushMonak		internetPush		= null;
	private SenderMonitoring		senderMonitoring	= null;
	private PreferenceManager 		pref;
	
	private MonakBinder 		handlerMonakBinder;

	private ReceiverSMS receiver;
	
	public final static String MONAK_SERVICE			= "monak_service";
	public final static String WAITING_LOCATION 		= "waiting_location";
	public final static String STORAGE_WAITING_LOCATION = "storage_waiting_location";

}
