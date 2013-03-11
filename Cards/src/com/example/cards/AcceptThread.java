package com.example.cards;

import java.io.IOException;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class AcceptThread extends Thread {
   private final BluetoothServerSocket mmServerSocket;

   public AcceptThread(BluetoothAdapter mBluetoothAdapter) {
       // Use a temporary object that is later assigned to mmServerSocket,
       // because mmServerSocket is final
       BluetoothServerSocket tmp = null;
       try {
           // MY_UUID is the app's UUID string, also used by the client code
    	   UUID uuid = UUID.fromString(Constants.MY_UUID);
           tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(Constants.Name, uuid);
       } catch (IOException e) { }
       mmServerSocket = tmp;
   }

   public void run(AlertDialog.Builder builder) {
        BluetoothSocket socket = null;
       // Keep listening until exception occurs or a socket is returned
       while (true) {
           try {
               socket = mmServerSocket.accept();
               System.out.println("client "+ "conected");

           } catch (IOException e) {
               break;
           }
           // If a connection was accepted
           if (socket != null) {
               // Do work to manage the connection (in a separate thread)
              // manageConnectedSocket(socket);
        	   final BluetoothSocket m_socket =socket;
        	 //  final AlertDialog.Builder builder2 = builder;
               try {
            	   builder.setMessage("accepted").setNeutralButton("OK", new DialogInterface.OnClickListener() {
               		public void onClick(DialogInterface dialog, int id) {                    	
            			dialog.dismiss();   
                        System.out.println("client "+ "accepted");
                        ConnectedThread ct = new ConnectedThread(m_socket);
                        ct.run();
            		}
            	});
            	   
            	AlertDialog d = builder.create();
            	d.show();
				mmServerSocket.close();
               } catch (IOException e) {
				// TODO Auto-generated catch block
            	   break;
               }
               break;
           }
       }
       
   }

   /** Will cancel the listening socket, and cause the thread to finish */
   public void cancel() {
       try {
           mmServerSocket.close();
       } catch (IOException e) { }
   }
}