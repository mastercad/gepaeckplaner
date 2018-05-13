package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.PackingListDetailActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;

public class PackingListEntryOnClickListener implements View.OnClickListener {

    private final AppCompatActivity activity;
    private final PackingListEntryEntity packingListEntryEntity;

    public PackingListEntryOnClickListener(AppCompatActivity activity, PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        activity.finish();
        Intent packingListDetailIntent = new Intent(activity, PackingListDetailActivity.class);
        packingListDetailIntent.putExtra("packingListId", packingListEntryEntity.getLuggageListFk());
        view.getContext().startActivity(packingListDetailIntent);
    }
}
