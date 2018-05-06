package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.view.MotionEvent;
import android.view.View;

public class RowOnTouchListener implements View.OnTouchListener {

    private LuggageListEntryEntity luggageEntity = null;

    RowOnTouchListener(LuggageListEntryEntity luggageEntity) {
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
