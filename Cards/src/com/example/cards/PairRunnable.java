package com.example.cards;

import android.bluetooth.BluetoothDevice;

public class PairRunnable implements Runnable {
	private BluetoothDevice v;
	void setValue(BluetoothDevice inV) {
		this.v = inV;
	}
	BluetoothDevice getValue() {
		return this.v;
	}
	public void run() {
		this.run();
	}
}