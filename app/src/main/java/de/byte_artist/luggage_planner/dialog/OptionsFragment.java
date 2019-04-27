package de.byte_artist.luggage_planner.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.activity.CategoryActivity;
import de.byte_artist.luggage_planner.activity.ExportActivity;
import de.byte_artist.luggage_planner.db.PreferencesDbModel;
import de.byte_artist.luggage_planner.entity.PreferencesEntity;
import de.byte_artist.luggage_planner.listener.BluetoothDeviceOnClickListener;
import de.byte_artist.luggage_planner.service.Database;
import de.byte_artist.luggage_planner.service.Preferences;
import de.byte_artist.luggage_planner.service.TextSize;

public class OptionsFragment extends DialogFragment {

    private TextView tvProgressLabel;
    private SeekBar seekBar;
    private ViewGroup optionsView;
    private int fontSize;
    private final int MIN = 6;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> boundDevices = null;
    private static boolean bluetoothActivatedOnStart;

    final private static int REQUEST_ENABLE_BT = 1;
    final private static int REQUEST_MAKE_DEVICE_VISIBLE = 2;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        optionsView = (ViewGroup)View.inflate(getContext(), R.layout.activity_options, null);
        final PreferencesDbModel preferencesDbModel = new PreferencesDbModel(getContext());
        Locale locale = getResources().getConfiguration().locale;
        tvProgressLabel = optionsView.findViewById(R.id.fontSizeText);

        final Database databaseService = new Database(getContext());

        final TextView btnResetDatabase = optionsView.findViewById(R.id.btnResetDatabase);
        final TextView btnCreateDemoDatabase = optionsView.findViewById(R.id.btnCreateDemoDatabase);
        final TextView btnExportDatabase = optionsView.findViewById(R.id.btnExportDatabase);
        final TextView btnImportDatabase = optionsView.findViewById(R.id.btnImportDatabase);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiver, filter);


        dialog.setTitle(R.string.title_options);
        dialog.setView(optionsView);

        btnResetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomDialog alertDialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);

                alertDialog.setTitle(R.string.title_warning);
                alertDialog.setMessage(R.string.text_warning_reset_database);
                alertDialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseService.resetDatabase();

                        getActivity().finish();
                        getActivity().startActivity(getActivity().getIntent());
                    }
                });
                alertDialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                        @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });

        btnCreateDemoDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final CustomDialog alertDialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);

            alertDialog.setTitle(R.string.title_warning);
            alertDialog.setMessage(R.string.text_warning_reset_database);
            alertDialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    databaseService.recreateDatabase();

                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                }
            });
            alertDialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.create();
            alertDialog.show();
            }
        });

        btnExportDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (null == bluetoothAdapter) {
                    Toast.makeText(getContext(), "Bluetooth not supported!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bluetoothActivatedOnStart = bluetoothAdapter.isEnabled();

                // activate bluetooth
                if (!bluetoothActivatedOnStart) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    openPairingDialog();
                }


//                Intent categoryIntent = new Intent(getActivity(), ExportActivity.class);
//                startActivity(categoryIntent);
            }
        });

        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            int currentFontSize = seekBar.getProgress();

            if (currentFontSize != fontSize) {
                PreferencesEntity preferencesEntity = Preferences.get("font_size", getContext());
                preferencesEntity.setValue(Integer.toString(currentFontSize + MIN));

                preferencesDbModel.update(preferencesEntity);

                getActivity().finish();
                getActivity().startActivity(getActivity().getIntent());
                Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
            }
            }
        });

        fontSize = Integer.parseInt(
            String.format(
                locale,
                "%.0f",
                Float.parseFloat(Preferences.get("font_size", getContext()).getValue()) - MIN
            )
        );

        seekBar = optionsView.findViewById(R.id.seekBar);
        seekBar.setMax(40);
        seekBar.setProgress(fontSize);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshProgressText(progress);
            }
        });

        refreshProgressText(seekBar.getProgress());

        dialog.create();

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Request for activation bluetooth
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                openPairingDialog();
                Toast.makeText(getContext(), "Bluetooth activated!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_MAKE_DEVICE_VISIBLE) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getContext(), "Device visible for pairing!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openPairingDialog() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        boundDevices = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                int deviceClass = device.getBluetoothClass().getDeviceClass();

                if (12 == device.getBondState()
                    && (
                        BluetoothClass.Device.COMPUTER_DESKTOP == deviceClass
                        || BluetoothClass.Device.COMPUTER_LAPTOP == deviceClass
                        || BluetoothClass.Device.COMPUTER_UNCATEGORIZED == deviceClass
                        || BluetoothClass.Device.PHONE_SMART == deviceClass
                        || BluetoothClass.Device.PHONE_UNCATEGORIZED == deviceClass
                )
                ) {
                    boundDevices.add(device);
                }
            }
        }

        if (boundDevices.isEmpty()) {
            makeDeviceVisibleForPairing();
        } else {
            LinearLayout outerLayout = new LinearLayout(getContext());
            ScrollView scrollView = new ScrollView(getContext());
            LinearLayout innerLayout = new LinearLayout(getContext());

            for (BluetoothDevice device: boundDevices) {
                TextView textView = new TextView(getContext());
                textView.setText(device.getName());
                TextSize.convert(getContext(), textView, TextSize.TEXT_TYPE_NORMAL);

                textView.setOnClickListener(new BluetoothDeviceOnClickListener(getActivity(), device));
                innerLayout.addView(textView);
            }

            scrollView.addView(innerLayout);
            outerLayout.addView(scrollView);
            CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_INFO);

            dialog.setTitle(R.string.title_pairing_dialog);
            dialog.setView(outerLayout);
            dialog.setButton(CustomDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.text_close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.create();
            dialog.show();
        }
    }

    private void makeDeviceVisibleForPairing() {
        // dialog to enable visibility to pairing for 300 sec
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, REQUEST_MAKE_DEVICE_VISIBLE);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        getActivity().unregisterReceiver(receiver);
    }

    private void refreshProgressText(int progress) {
        Locale locale = Objects.requireNonNull(getActivity()).getResources().getConfiguration().locale;
        tvProgressLabel.setText(
            String.format(
                locale,
                "%s: %d",
                getActivity().getResources().getString(R.string.text_size),
                (progress + MIN)
            )
        );
    }

    @Override
    public void onResume() {
        super.onResume();

        SeekBar seekBar = optionsView.findViewById(R.id.seekBar);
        seekBar.setProgress(seekBar.getProgress());
    }
}
