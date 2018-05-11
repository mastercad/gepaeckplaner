package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.content.Intent;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListDetailActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

public class PackingListOnClickListener implements View.OnClickListener {

    private PackingListActivity packingListActivity = null;
    private PackingListEntity packingListEntity = null;

    public PackingListOnClickListener(PackingListActivity packingListActivity, PackingListEntity packingListEntryEntity) {
        this.packingListEntity = packingListEntryEntity;
        this.packingListActivity = packingListActivity;
    }

    @Override
    public void onClick(View view) {
        packingListActivity.finish();
        Intent packingListDetailIntent = new Intent(packingListActivity, PackingListDetailActivity.class);
        packingListDetailIntent.putExtra("packingListId", packingListEntity.getId());
        view.getContext().startActivity(packingListDetailIntent);
    }
}
