package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.service.Database;

public class OptionsFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        final View optionsView = View.inflate(getContext(), R.layout.activity_options, null);

        final Database databaseService = new Database(getContext());

        TextView btnResetDatabase = optionsView.findViewById(R.id.btnResetDatabase);
        TextView btnCreateDemoDatabase = optionsView.findViewById(R.id.btnCreateDemoDatabase);

        builder.setTitle(R.string.title_options);
        builder.setView(optionsView);

        btnResetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setTitle(R.string.title_warning)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.text_warning_reset_database)
                    .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            databaseService.resetDatabase();

                            getActivity().finish();
                            getActivity().startActivity(getActivity().getIntent());
                        }
                    })
                    .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            }
        });

        btnCreateDemoDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setTitle(R.string.title_warning)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.text_warning_reset_database)
                    .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            databaseService.recreateDatabase();

                            getActivity().finish();
                            getActivity().startActivity(getActivity().getIntent());
                        }
                    })
                    .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            }
        });

        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
}
