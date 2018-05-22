package de.byte_artist.luggage_planner.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

        PackingListEntryEditDialogFragment alertDialog = PackingListEntryEditDialogFragment.newInstance(packingListEntryEntity);

        alertDialog.show(ft, "packing_list_entry_edit_dialog");

        return false;
    }
}
