package de.byte_artist.luggage_planner.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.dialog.PackingListEditDialog;
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
        PackingListEditDialog editDialog = new PackingListEditDialog(this.activity);
        editDialog.showEditDialog(view, packingListEntity);

        return false;
    }
}
