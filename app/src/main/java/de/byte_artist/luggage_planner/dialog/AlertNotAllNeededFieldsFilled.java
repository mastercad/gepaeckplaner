package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import de.byte_artist.luggage_planner.R;

public class AlertNotAllNeededFieldsFilled extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
        dialog.setTitle(R.string.title_error);
        dialog.setMessage(R.string.text_not_all_fields_filled);
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getActivity().getResources().getText(R.string.text_understood), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });

        dialog.create();

        return dialog;
    }
}
