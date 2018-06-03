package de.byte_artist.luggage_planner.listener;

import android.view.MotionEvent;
import android.view.View;

import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

/**
 * @deprecated
 */
class PackingListOnTouchListener implements View.OnTouchListener {

//    private PackingListEntryEntity luggageEntity;

    PackingListOnTouchListener(PackingListEntryEntity luggageEntity) {
//        this.luggageEntity = luggageEntity;
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        view.performClick();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // start your timer

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // stop your timer.

        }

        return false;
    }
}
