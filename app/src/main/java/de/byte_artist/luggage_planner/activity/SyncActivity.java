package de.byte_artist.luggage_planner.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.byte_artist.luggage_planner.AbstractActivity;
import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.service.BluetoothSyncService;
import de.byte_artist.luggage_planner.service.Database;
import de.byte_artist.luggage_planner.service.MessageAdapter;

public class SyncActivity extends AbstractActivity {

    // Message types sent from the BluetoothSyncService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_STATUS = 6;

    public static final String MESSAGE_HEADER_START = "start";
    public static final String MESSAGE_HEADER_END = "end";
    public static final String MESSAGE_HEADER_STATUS_START = "status_start";
    public static final String MESSAGE_HEADER_STATUS_END = "status_end";
    public static final String MESSAGE_HEADER_SYNC_START = "sync_start";
    public static final String MESSAGE_HEADER_SYNC_END = "sync_end";
    public static final String MESSAGE_HEADER_FINISHED = "finished";

    // Key names received from the BluetoothSyncService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Name of the connected device
    private String mConnectedDeviceName = null;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Member object for the chat services
    private BluetoothSyncService mSyncService = null;

    private MessageAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private List<de.byte_artist.luggage_planner.entity.Message> messageList = new ArrayList<>();

    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sync);

        mRecyclerView = findViewById(R.id.sync_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messageList);
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (null == mSyncService) {
            setupSync();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (mSyncService != null
            && mSyncService.getState() == BluetoothSyncService.STATE_NONE
        ) {
            mSyncService.start();
        }
    }

    private void setupSync() {
        Button mSendButton = findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetRecyclerView();

                Database databaseService = new Database(getApplicationContext());
                String jsonContent = databaseService.exportDatabaseToJson();

                sendMessage(MESSAGE_HEADER_START);
                sendMessage(jsonContent);
                sendMessage(MESSAGE_HEADER_END);
            }
        });

        // Initialize the BluetoothSyncService to perform bluetooth connections
        mSyncService = new BluetoothSyncService(this, mHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mSyncService != null) {
            mSyncService.stop();
        }
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    public void sendMessage(String message) {

        // Check that we're actually connected before trying anything
        if (mSyncService.getState() != BluetoothSyncService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            mSyncService.write(message.getBytes());
        }
    }

    // The Handler that gets information back from the BluetoothSyncService
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_STATUS:
                    String statusMessage;

                    if (null != msg.getData().getString("status")) {
                        statusMessage = msg.getData().getString("status");
                    } else {
                        byte[] statusBuf = (byte[]) msg.obj;
                        statusMessage = new String(statusBuf, 0, msg.arg1);
                    }
                    // construct a string from the valid bytes in the buffer
                    Log.d("SYNCACTIVITY", "HABE IN MESSAGE_STATUS: "+statusMessage);
                    mAdapter.notifyDataSetChanged();
                    messageList.add(new de.byte_artist.luggage_planner.entity.Message(counter++, statusMessage, mConnectedDeviceName));

                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);

                    Log.d("SYNCACTIVITY", "HABE IN MESSAGE_WRITE: "+writeMessage);
                    mAdapter.notifyDataSetChanged();
                    messageList.add(new de.byte_artist.luggage_planner.entity.Message(counter++, writeMessage, "Me"));

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    resetRecyclerView();

                    // construct a string from the valid bytes in the buffer
                    Database databaseService = new Database(getApplicationContext());
                    databaseService.importDatabaseByJson(readMessage, mSyncService);

//                    messageList.add(new de.byte_artist.luggage_planner.entity.Message(counter++, readMessage, mConnectedDeviceName));
//                    mAdapter.notifyDataSetChanged();
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    // Name of the connected device
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.title_connected_to)+mConnectedDeviceName, Toast.LENGTH_SHORT
                    ).show();

                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(
                        getApplicationContext(),
                        msg.getData().getString(TOAST),
                        Toast.LENGTH_SHORT
                    ).show();

                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = Objects.requireNonNull(data.getExtras()).getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BluetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mSyncService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupSync();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public final void directSendNotification(String message) {
        mAdapter.notifyDataSetChanged();
        messageList.add(new de.byte_artist.luggage_planner.entity.Message(counter++, message, "Me"));
    }

    public void resetRecyclerView() {
        messageList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void connect(View v) {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void discoverable(View v) {
        ensureDiscoverable();
    }
}
