package de.byte_artist.luggage_planner.listener;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class BluetoothDeviceOnClickListener implements View.OnClickListener {
    private static final String TAG = "BTDeviceOnClickListener";
    private InputStream in;
    private OutputStream out;
    private final byte[] arrayOfByte = new byte[4096];
    private int bytes;

    private static final LogBroadcastReceiver receiver = new LogBroadcastReceiver();
    public static class LogBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
            Log.d("ZeoReceiver", paramAnonymousIntent.toString());
            Bundle extras = paramAnonymousIntent.getExtras();
            for (String k : extras.keySet()) {
                Log.d("ZeoReceiver", "    Extra: " + extras.get(k).toString());
            }
        }
    }

    private BluetoothSocket sock;

    // Return Intent extra
    private static String EXTRA_DEVICE_ADDRESS = "device_address";

    private final BluetoothDevice device;
    private final Activity activity;

    public BluetoothDeviceOnClickListener(Activity activity, BluetoothDevice device) {
        this.device = device;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        Log.i("BTDeviceObClickListener", "Address: "+this.device.getAddress());
        Log.i("BTDeviceObClickListener", "UUIDS: "+this.device.getUuids()[0].toString());
        /*
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, this.device.getAddress());

        // Set result and finish this Activity
        this.activity.setResult(Activity.RESULT_OK, intent);

        this.activity.finish();
        */

        this.activity.getApplicationContext().registerReceiver(
            receiver,
            new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        );

        this.activity.getApplicationContext().registerReceiver(
            receiver,
            new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        );

//        BluetoothDevice zee = BluetoothAdapter.getDefaultAdapter().
//                getRemoteDevice(this.device.getAddress());

        try {
            BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.device.getAddress());
            Method method = bluetoothDevice.getClass().getMethod("createRfcommSocket", int.class);
            sock = (BluetoothSocket) method.invoke(bluetoothDevice, 1);
        } catch (Exception exception) {
            Log.e("BluetoothDeviceOnClick", exception.getMessage());
        }

//        try {
//            sock = bluetoothDevice.createRfcommSocketToServiceRecord(
//                    UUID.fromString(this.device.getUuids()[0].toString())); // use unique UUID
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }

        Log.d(TAG, "++++ Connecting");
        try {
            sock.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Log.d(TAG, "++++ Connected");


        try {
            in = sock.getInputStream();
            out = sock.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Log.d(TAG, "++++ Listening...");

        while (true) {
            try {
                out.write(1);
            } catch (IOException e1) {
                e1.printStackTrace();
                break;
            }
        }

        while (true) {
            try {
                bytes = in.read(arrayOfByte);
                Log.d(TAG, "++++ Read "+ bytes +" bytes");
                Log.d(TAG, "BYTES: "+ bytes);
            } catch (IOException e1) {
                e1.printStackTrace();
                break;
            }
        }
        Log.d(TAG, "++++ Done: test()");
    }


}


