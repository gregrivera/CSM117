package com.example.cards;

import java.io.IOException;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.util.Log;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
 
    public ConnectThread(BluetoothDevice device, BluetoothAdapter b) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mBluetoothAdapter = b;
        mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
        	UUID uuid = UUID.fromString(Constants.MY_UUID);
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run(AlertDialog.Builder builder) {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
            	  System.out.println("client "+ "closed: "+connectException.toString());
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        System.out.println("client "+ "conected: ");
        final ConnectedThread ct = new ConnectedThread(mmSocket);
        ct.write("Hello".getBytes());
        builder.setMessage("send bye").setNeutralButton("OK", new DialogInterface.OnClickListener() {
       		public void onClick(DialogInterface dialog, int id) {                    	
    			dialog.dismiss();   
                System.out.println("client "+ "accepted");
                ct.write("bye".getBytes());
    		}
        });
        
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}