package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Objects;

import de.byte_artist.luggage_planner.R;

public class AlertLuggageAlreadyExists extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
        dialog.setTitle(R.string.title_error);
        dialog.setMessage(R.string.text_luggage_already_exists_in_packing_list);
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getActivity().getResources().getText(R.string.text_understood), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        return dialog;
    }
}
