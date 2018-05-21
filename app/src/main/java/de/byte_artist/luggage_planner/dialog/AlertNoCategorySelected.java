package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import de.byte_artist.luggage_planner.R;

public class AlertNoCategorySelected extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        return alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.title_error)
            .setMessage(R.string.text_choose_category)
            .setPositiveButton(getActivity().getResources().getText(R.string.text_understood), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
    }
}
