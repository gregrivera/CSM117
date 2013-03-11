package com.example.cards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
	public boolean isServer;
	public boolean show;
	private String ConnectionName;
	BluetoothAdapter mBluetoothAdapter;
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
    	isServer = false;
        super.onCreate(savedInstanceState);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

        	System.out.println("no bluetooth");
        	return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1919);
           
        }
       
    
    }
    

    
    void promptForResult(final PairRunnable postrun) {
    	 final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
         // If there are paired devices
         final List<CharSequence> mArrayAdapter = new ArrayList<CharSequence>();
         if (pairedDevices.size() > 0) {
          // Loop through paired devices
       	   for (BluetoothDevice device : pairedDevices) {
              // Add the name and address to an array adapter to show in a ListView
              mArrayAdapter.add(device.getName());
         	 }
         }
         show = false;
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("Select Device");
         //final CharSequence[] data = {"Foo", "Bar", "Baz"};
         builder.setItems(mArrayAdapter.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int item) {
                  // Do something with the selection
             	//postrun.setValue((mArrayAdapter.get(item)).toString());
             	for(BluetoothDevice p:pairedDevices){
             		if(p.getName() == (mArrayAdapter.get(item)).toString()){
             			postrun.setValue(p);
             			postrun.run();
             			return;
             		}
             	}
             	//postrun.run();
             }
         });
         final Dialog dialog = builder.create();
         //ConnectionName = "hello";
         dialog.show();

    }
    
    
    public Dialog onCreateDialog(Bundle savedInstanceState, String v) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        builder.setMessage(v)
        .setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	
        			// put whatever code you want to run after user enters a result
        		
        				// get the value we stored from the dialog
        				//String value = this.getValue().get();
        				// do something with this value...
        				// In our example we are taking our value and passing it to 
        				// an activity intent, then starting the activity.
            			dialog.dismiss();
            			final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            			Iterator<BluetoothDevice> it =  pairedDevices.iterator();
        				ConnectThread ct = new ConnectThread(it.next(),mBluetoothAdapter);
        				
        				ct.run(builder3);
        			
        		
            }
        })
        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	dialog.dismiss();
                AcceptThread at = new AcceptThread(mBluetoothAdapter);
                at.run(builder2);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == 1919) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it

            }
        }
        // to the user.
    }
    protected void onStart(){
    		super.onStart();
    		Dialog d = onCreateDialog(null,"Are You a Server?");
			d.show();
    }
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        
        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }
}
