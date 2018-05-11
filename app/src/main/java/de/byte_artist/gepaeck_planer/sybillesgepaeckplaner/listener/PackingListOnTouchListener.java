package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.view.MotionEvent;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;

public class PackingListOnTouchListener implements View.OnTouchListener {

    private PackingListEntryEntity luggageEntity = null;

    PackingListOnTouchListener(PackingListEntryEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
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
