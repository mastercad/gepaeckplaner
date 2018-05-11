package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;


import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.LuggageActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.LuggageEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;

public class LuggageEntityOnLongClickListener implements View.OnLongClickListener {

    private LuggageEntity luggageEntity = null;
    private LuggageActivity luggageActivity = null;

    public LuggageEntityOnLongClickListener(LuggageActivity luggageActivity, LuggageEntity luggageEntity) {
        this.luggageActivity = luggageActivity;
        this.luggageEntity = luggageEntity;

    }

    @Override
    public boolean onLongClick(View view) {
        LuggageEditDialog editDialog = new LuggageEditDialog(this.luggageActivity);
        editDialog.showEditDialog(view, luggageEntity);

        return false;
    }
}
