package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.PackingListEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

public class PackingListOnLongClickListener implements View.OnLongClickListener {

    private PackingListActivity packingListActivity = null;
    private PackingListEntity packingListEntity = null;

    public PackingListOnLongClickListener(PackingListActivity packingListActivity, PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
        this.packingListActivity = packingListActivity;
    }

    @Override
    public boolean onLongClick(View view) {
        PackingListEditDialog editDialog = new PackingListEditDialog(this.packingListActivity);
        editDialog.showEditDialog(view, packingListEntity);

        return false;
    }
}
