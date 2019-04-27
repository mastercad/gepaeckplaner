package de.byte_artist.luggage_planner.listener;

import android.view.View;

import de.byte_artist.luggage_planner.entity.LuggageEntity;

/**
 * @deprecated use {@link LuggageEntityOnTouchListener}
 */
class LuggageEntityOnClickListener implements View.OnClickListener {

    LuggageEntity luggageEntity = null;

    public LuggageEntityOnClickListener(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    @Override
    public void onClick(View view) {
    }
}
