package com.example.cards;

import android.bluetooth.BluetoothSocket;

public class TempData {
	private BluetoothSocket socket;
	public TempData(){
		socket = null;
	}
	public BluetoothSocket getSocket(){
		return socket;
	}
	
	public void setSocket(BluetoothSocket s){
		socket = s;
	}
}
