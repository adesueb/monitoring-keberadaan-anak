/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codegero.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ade
 */
public class SocketClientGero{
    
    public SocketClientGero(String ip, int port){
        
        this.ip			= ip;
        this.port		= port;
    }
   
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}


	public void bukaKoneksi(){
        try {
        	
            this.socket= new Socket(ip,port);
            counterErrorSocket = 0;
        } catch (UnknownHostException ex) {
        	if(counterErrorSocket<3){
        		bukaKoneksi();
        		counterErrorSocket++;
        	}
            Logger.getLogger(SocketClientGero.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
        	if(counterErrorSocket<3){
        		counterErrorSocket++;
        		bukaKoneksi();
        	}
        	Logger.getLogger(SocketClientGero.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void tutupKoneksi(){
        try {
        	this.getOutputStream().close();
            this.socket.close();
        } catch (IOException ex) {
        	Logger.getLogger(SocketClientGero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public InputStream getInputStream(){
        try {
        	InputStream in = this.socket.getInputStream();
        	counterErrorInput = 0;
            return in;
        } catch (IOException ex) {
        	if(counterErrorInput<3){
        		counterErrorInput++;
        		bukaKoneksi();
        		getInputStream();
        	}
        	
            Logger.getLogger(SocketClientGero.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public OutputStream getOutputStream(){
        try {
        	OutputStream out = this.socket.getOutputStream();
            counterErrorOutput = 0;
        	return out;
        } catch (IOException ex) {
        	if(counterErrorOutput<3){
        		counterErrorOutput++;
        		bukaKoneksi();
            	getOutputStream();
        	}
        	Logger.getLogger(SocketClientGero.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private Socket  socket;
    
    private	String			ip;
    private	int				port;
    
    private int counterErrorSocket = 0;
    private int counterErrorInput = 0;
    private int counterErrorOutput = 0;
}
