package de.byte_artist.luggage_planner.listener;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.DbModel;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.service.Bluetooth;

public class BluetoothDeviceOnClickListener implements View.OnClickListener {

    private final BluetoothDevice device;
    private final Activity activity;

    public BluetoothDeviceOnClickListener(Activity activity, BluetoothDevice device) {
        this.device = device;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        /*
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_INFO);

        dialog.setTitle(R.string.title_information);
        dialog.setMessage(String.format(view.getResources().getString(R.string.text_category_info), luggageCategoryEntity.getName()));
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, view.getResources().getString(R.string.text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
        */

        BluetoothSocket socket = null;
        BluetoothAdapter adapter = null;
        OutputStream outputStream = null;
        File databasePath = activity.getDatabasePath(DbModel.DATABASE_NAME);

        try {
            InputStream inputStream = new FileInputStream(databasePath);
        } catch (FileNotFoundException exception) {
            Toast.makeText(activity, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BluetoothDevice device = adapter.getRemoteDevice(this.device.getAddress());
            adapter.cancelDiscovery();

            Method method = device.getClass().getMethod("createRfcommSocket", int.class);
            socket = (BluetoothSocket) method.invoke(device, Integer.valueOf(1));
            socket.connect();

            Bluetooth bluetoothService = new Bluetooth();
            outputStream = socket.getOutputStream();

        } catch (Exception exception) {
            Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(activity, device.getName(), Toast.LENGTH_SHORT).show();
    }
}
