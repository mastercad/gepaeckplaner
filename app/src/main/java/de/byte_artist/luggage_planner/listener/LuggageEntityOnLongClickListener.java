package de.byte_artist.luggage_planner.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.dialog.CategoryEditDialogFragment;
import de.byte_artist.luggage_planner.dialog.LuggageEditDialog;
import de.byte_artist.luggage_planner.dialog.LuggageEditDialogFragment;
import de.byte_artist.luggage_planner.entity.LuggageEntity;

public class LuggageEntityOnLongClickListener implements View.OnLongClickListener {

    private final LuggageEntity luggageEntity;
    private final AppCompatActivity activity;

    public LuggageEntityOnLongClickListener(AppCompatActivity activity, LuggageEntity luggageEntity) {
        this.activity = activity;
        this.luggageEntity = luggageEntity;

    }

    @Override
    public boolean onLongClick(View view) {
/*
        LuggageEditDialog editDialog = new LuggageEditDialog(this.activity);
        editDialog.showEditDialog(view, luggageEntity);

        return false;
*/
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("luggage_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        LuggageEditDialogFragment alertDialog = LuggageEditDialogFragment.newInstance(luggageEntity);

        alertDialog.show(ft, "luggage_edit_dialog");

        return false;
    }
}
