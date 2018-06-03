package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PreferencesDbModel;
import de.byte_artist.luggage_planner.entity.PreferencesEntity;
import de.byte_artist.luggage_planner.service.Database;
import de.byte_artist.luggage_planner.service.Preferences;

public class OptionsFragment extends DialogFragment {

    private TextView tvProgressLabel;
    private SeekBar seekBar;
    private ViewGroup optionsView;
    private int fontSize;
    private final int MIN = 6;

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
