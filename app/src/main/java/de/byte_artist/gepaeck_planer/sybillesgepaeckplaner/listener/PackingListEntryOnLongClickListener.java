package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.PackingListEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

public class PackingListEntryOnLongClickListener implements View.OnLongClickListener {

    private AppCompatActivity activity = null;
    private PackingListEntity packingListEntity = null;

    public PackingListEntryOnLongClickListener(AppCompatActivity activity, PackingListEntity packingListEntity) {
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
