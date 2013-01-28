package org.ade.monitoring.keberadaan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.os.Handler;
import android.util.Log;

public class BinderHandlerMonak{
	
	public void bindStorageHandler(String idHandler, StorageHandler handler){
		addStorageHandlerWaiting(idHandler, handler);
	}
	
	public void unbindStorageHandler(String idHandler, StorageHandler handler){
		removeStorageHandlerWaiting(idHandler, handler);
	}
	
	public void unbindStorageHandler(String idHandler, String idStoragehandler){
		removeStorageHandlerWaiting(idHandler, idStoragehandler);
	}
	
	public void bindUIHandlerWaitingLogLocation(Handler handler){
		addHandlerUIWaiting(MonakService.WAITING_LOG_LOCATION, handler);
	}
	
	public void  unBindUIHandlerWaitingLogLocation(){
		removeUIHandlerWaiting(MonakService.WAITING_LOG_LOCATION);
	}
	
	public void bindUIHandlerWaitingLocation(Handler handler){
		addHandlerUIWaiting(MonakService.WAITING_LOCATION, handler);
	}
	
	public void unBindUIHandlerWaitingLocation(){
		removeUIHandlerWaiting(MonakService.WAITING_LOCATION);
	}
	
	public void bindUIHandlerWaitingKonfirmasiAktif(Handler handler){
		addHandlerUIWaiting(MonakService.WAITING_KONFIRMASI_AKTIF, handler);
	}
	
	public void unBindUIHandlerWaitingKonfirmasiAktif(){
		removeUIHandlerWaiting(MonakService.WAITING_KONFIRMASI_AKTIF);
	}
	
	public Handler getSingleBindUIHandler(String key){
		if(mapUIHandler==null)return null;
		return mapUIHandler.get(key);
	}
	
	public StorageHandler getSingleBindStorageHandler(String key, String storageHandlerKey){
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
	
	private void addStorageHandlerWaiting(String key, StorageHandler storage){
		if(mapStorageHandler==null)mapStorageHandler = new HashMap<String, List<StorageHandler>>();
		List<StorageHandler> list = mapStorageHandler.get(key);
		if(list==null){
			list = new ArrayList<StorageHandler>();
		}	
		list.add(storage);
		Log.d("MonakService", "add storae into list with key : "+key);
		mapStorageHandler.put(key, list);
	}
	
	private void removeStorageHandlerWaiting(String key, StorageHandler storage){
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
	
	private void removeStorageHandlerWaiting(String key, String idStorage){
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
	
	private void addListStorageHandlerWaiting(String key, List<StorageHandler> list){
		if(mapStorageHandler==null)mapStorageHandler = new HashMap<String, List<StorageHandler>>();
		mapStorageHandler.put(key, list);
	}
	
	private void removeListStorageHandlerWaiting(String key){
		if(mapStorageHandler==null) return;
		mapStorageHandler.remove(key);
	}
	
	private void addHandlerUIWaiting(String key, Handler handler){
		if(mapUIHandler==null)mapUIHandler = new HashMap<String, Handler>();
		mapUIHandler.put(key, handler);
	}
	
	private void removeUIHandlerWaiting(String key){
		if(mapUIHandler==null)return;
		mapUIHandler.remove(key);
	}
	
	private Map<String, Handler> 	mapUIHandler 			= new HashMap<String, Handler>();
	private Map<String, List<StorageHandler>> mapStorageHandler = new HashMap<String, List<StorageHandler>>();
	
}