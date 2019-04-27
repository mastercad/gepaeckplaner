package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.Objects;
import de.byte_artist.luggage_planner.R;

public class AlertNoLuggageExistsInCategory extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(
                Objects.requireNonNull(getActivity()),
                R.style.AlertDialogTheme,
                CustomDialog.TYPE_ALERT
        );
        dialog.setTitle(R.string.title_error);
        dialog.setMessage(R.string.text_create_category);
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getActivity().getResources().getText(R.string.text_understood), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                LuggageNewDialogFragment fragment = new LuggageNewDialogFragment();
                fragment.show(ft, "category_new_dialog");
            }
        });
        dialog.create();

        return dialog;
    }
}