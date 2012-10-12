package com.codegero.internet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;


/*
 * there is no control for post metod....
 * so i hope add control for post metod..
 * 
 */
public class HttpGero {

	public HttpGero(String url, String ip, int port, String metod){
		mSocket 	= new SocketClientGero(ip==null?"localhost":ip, port==0?getPortFromUrl(url):port);
		this.metod 	= metod==null? "GET":metod;
		mUrl 		= getFixUrl(url);
	}
	
	
	public HttpGero(String url, int port, String metod){
		this.metod 	= metod==null? "GET":metod;
		mUrl 		= getFixUrl(url==null?"http://localhost":url);
		mSocket 	= new SocketClientGero(getIpFromUrl(mUrl), port==0?getPortFromUrl(url):port);
	}
	
	public HttpGero(SocketClientGero socket){
		mSocket = socket;
	}

	public String getUrl() {
		return mUrl;
	}


	public void setUrl(String url) {
		this.mUrl = getFixUrl(url);
		mSocket.setIp(getIpFromUrl(mUrl));
		mSocket.setPort(getPortFromUrl(mUrl));
	}

	public List<HttpParameterGero> getParameters() {
		if(parameters==null){
			parameters = new ArrayList<HttpParameterGero>();
		}
		return parameters;
	}


	public void setParameters(List<HttpParameterGero> parameters) {
		this.parameters = parameters;
	}


	public String getMethod() {
		return metod;
	}


	public void setMethod(String method) {
		this.metod = method;
	}

	

	public List<HttpHeaderGero> getHeaders() {
		if(headers==null){
			headers = new ArrayList<HttpHeaderGero>();
		}
		return headers;
	}


	public void setHeaders(List<HttpHeaderGero> headers) {
		this.headers = headers;
	}


	public SocketClientGero getSocket() {
		return mSocket;
	}


	public void setSocket(SocketClientGero mSocket) {
		this.mSocket = mSocket;
	}


	public void sendRequest(){
		mSocket.bukaKoneksi();
		OutputStream 		os	= mSocket.getOutputStream();
    	DataOutputStream 	dos	= new DataOutputStream(os);

    	
    	try {
			String request = makeRequestText();
			Log.d("request", "request is : "+request);
			dos.writeBytes(request);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponse(){
		String result = "";
    	
    	InputStream 		in	= mSocket.getInputStream();
    	DataInputStream 	dis	= new DataInputStream(in);
    	
    	int karakter 			= 0;
    	try {
			while((karakter=dis.read())!=-1){
				result = result+(char)karakter;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	String splitter = "\r\n\r\n";
    	String[] arrSplitResult = result.split(splitter);
    	String response = "";
    	if(arrSplitResult.length>1){
    		response = arrSplitResult[1];
    	}
    	
    	return response;
	}
	
	public InputStream getInputStream(){
		return mSocket.getInputStream();
	}
	
	private String getIpFromUrl(String url){
		String [] splittingUrl = url.split("/");
		String ip = "";
		ip = splittingUrl[2].split(":")[0];
		
		return ip;
	}
	
	private int getPortFromUrl(String url){
		String [] splittingUrl = url.split("/");
		int port;
		String [] urlPortSplit = splittingUrl[2].split(":");
		port = 
			urlPortSplit.length==1?
					80:Integer.parseInt(urlPortSplit[1]);
		
		return port;
	}
	
	private String getFixUrl(String url){
		if(url.startsWith("http")){
			return url;
		}else{
			return "http://"+url;
		}
		
	}
	
	private String makeRequestText(){
		
		String parameterText = getParameterText();
		
    	if(parameterText!=""&&!mUrl.endsWith("?")){
    		mUrl = mUrl+"?";
    	}    	
    	return metod+" "+mUrl+parameterText+" HTTP/1.0\r\n"+
			getHeaderText()+
	        "\r\n";
	}
	
	private String getParameterText(){

		String parameterText = "";
    	
    	if(parameters!=null){
    		for(HttpParameterGero parameter:parameters){
    			parameterText +=parameter.toString();
    		}
    	}
    	return parameterText;
	}
	
	private String getHeaderText(){
		String headerText="";
    	if(headers!=null){
    		for(HttpHeaderGero header:headers){
    			headerText += header.toString();
    			Log.d("request", "header is : "+headerText);
    		}

    	}
    	return headerText;
	}
	
	private String metod		= "";
	private String mUrl			= "";
	
	private List<HttpHeaderGero> 	headers;
	private List<HttpParameterGero>	parameters;
	private SocketClientGero mSocket;
}
