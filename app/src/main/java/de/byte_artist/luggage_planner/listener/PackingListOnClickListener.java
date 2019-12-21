package de.byte_artist.luggage_planner.listener;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.activity.PackingListDetailActivity;
import de.byte_artist.luggage_planner.entity.PackingListEntity;

public class PackingListOnClickListener implements View.OnClickListener {

    private final AppCompatActivity activity;
    private final PackingListEntity packingListEntity;

    public PackingListOnClickListener(AppCompatActivity activity, PackingListEntity packingListEntryEntity) {
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
