package de.byte_artist.luggage_planner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.entity.LuggageEntity;

/**
 * @deprecated use {@link LuggageEntityOnTouchListener}
 */
class LuggageEntityOnClickListener implements View.OnClickListener {

    private final LuggageEntity luggageEntity;

    public LuggageEntityOnClickListener(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    @Override
    public void onClick(View view) {
    }
}
