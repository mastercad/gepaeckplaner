package de.byte_artist.luggage_planner.listener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.dialog.PackingListEntryEditDialogFragment;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class PackingListEntryOnLongClickListener implements View.OnLongClickListener {

    private final AppCompatActivity activity;
    private final PackingListEntryEntity packingListEntryEntity;

    public PackingListEntryOnLongClickListener(AppCompatActivity activity, PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public boolean onLongClick(View view) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        PackingListEntryEditDialogFragment fragment = new PackingListEntryEditDialogFragment();
        fragment.setComplexVariable(packingListEntryEntity);
        fragment.setComplexVariable(packingListEntryEntity.getLuggageListFk());

//        alertDialog.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        fragment.show(ft, "packing_list_entry_edit_dialog");

        return false;
    }
}
