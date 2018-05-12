package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.LuggageEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;

public class LuggageEntityOnLongClickListener implements View.OnLongClickListener {

    private LuggageEntity luggageEntity = null;
    private AppCompatActivity activity = null;

    public LuggageEntityOnLongClickListener(AppCompatActivity activity, LuggageEntity luggageEntity) {
        this.activity = activity;
        this.luggageEntity = luggageEntity;

    }

    @Override
    public boolean onLongClick(View view) {
        LuggageEditDialog editDialog = new LuggageEditDialog(this.activity);
        editDialog.showEditDialog(view, luggageEntity);

        return false;
    }
}
