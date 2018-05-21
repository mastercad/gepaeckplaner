package de.byte_artist.luggage_planner.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import de.byte_artist.luggage_planner.activity.LuggageActivity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;

public class LuggageEntityOnTouchListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;

    public LuggageEntityOnTouchListener(LuggageActivity activity, LuggageEntity luggageEntity) {
        gestureDetector = new GestureDetector(activity, new GestureListener(activity, luggageEntity));
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.performClick();
        }
        return gestureDetector.onTouchEvent(motionEvent);
    }
}
