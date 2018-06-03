package de.byte_artist.luggage_planner.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.dialog.PackingListEditDialogFragment;
import de.byte_artist.luggage_planner.entity.PackingListEntity;

public class PackingListOnLongClickListener implements View.OnLongClickListener {

    private final AppCompatActivity activity;
    private final PackingListEntity packingListEntity;

    public PackingListOnLongClickListener(AppCompatActivity activity, PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
        this.activity = activity;
    }

    @Override
    public boolean onLongClick(View view) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("packing_list_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        PackingListEditDialogFragment fragment = new PackingListEditDialogFragment();
        fragment.setComplexVariable(packingListEntity);

        fragment.show(ft, "packing_list_edit_dialog");
        return false;
    }
}
