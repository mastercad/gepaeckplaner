package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.PackingListEntryEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;

public class PackingListEntryOnLongClickListener implements View.OnLongClickListener {

    private final AppCompatActivity activity;
    private final PackingListEntryEntity packingListEntryEntity;

    public PackingListEntryOnLongClickListener(AppCompatActivity activity, PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public boolean onLongClick(View view) {
        PackingListEntryEditDialog editDialog = new PackingListEntryEditDialog(this.activity, packingListEntryEntity.getLuggageListFk());
        editDialog.showEditDialog(view, packingListEntryEntity);

        return false;
    }
}
