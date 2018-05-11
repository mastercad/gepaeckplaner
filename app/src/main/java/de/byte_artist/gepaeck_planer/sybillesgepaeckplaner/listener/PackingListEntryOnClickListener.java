package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListDetailActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

public class PackingListEntryOnClickListener implements View.OnClickListener {

    private AppCompatActivity activity = null;
    private PackingListEntity packingListEntity = null;

    public PackingListEntryOnClickListener(AppCompatActivity activity, PackingListEntity packingListEntryEntity) {
        this.packingListEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        activity.finish();
        Intent packingListDetailIntent = new Intent(activity, PackingListDetailActivity.class);
        packingListDetailIntent.putExtra("packingListId", packingListEntity.getId());
        view.getContext().startActivity(packingListDetailIntent);
    }
}
