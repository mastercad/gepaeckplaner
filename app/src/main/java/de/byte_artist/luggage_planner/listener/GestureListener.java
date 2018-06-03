package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.MotionEvent;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.activity.LuggageActivity;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.dialog.LuggageEditDialogFragment;
import de.byte_artist.luggage_planner.entity.LuggageEntity;

class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private final LuggageActivity activity;
    private final LuggageEntity luggageEntity;

    GestureListener(LuggageActivity activity, LuggageEntity luggageEntity) {
        this.activity = activity;
        this.luggageEntity = luggageEntity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        onClick();
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        onDoubleClick();
        return super.onDoubleTap(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        onLongClick();
        super.onLongPress(e);
    }

    // Determines the fling velocity and then fires the appropriate swipe event accordingly
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        result = onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeDown();
                    } else {
                        onSwipeUp();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    /*
     * deactivate
     */
    private boolean onSwipeRight() {
        if (luggageEntity.isActive()) {
            LuggageDbModel luggageDbModel = new LuggageDbModel(this.activity);
            luggageEntity.setActive(false);
            luggageDbModel.update(luggageEntity);
            activity.refresh();
            return true;
        }
        return false;
    }

    /*
     * activate
     */
    private void onSwipeLeft() {
        if (!luggageEntity.isActive()) {
            LuggageDbModel luggageDbModel = new LuggageDbModel(this.activity);
            luggageEntity.setActive(true);
            luggageDbModel.update(luggageEntity);
            activity.refresh();
        }
    }

    private void onSwipeUp() {

    }

    private void onSwipeDown() {
    }

    private void onClick() {
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_INFO);
        dialog.setTitle(R.string.title_information);
        dialog.setMessage(String.format(activity.getResources().getString(R.string.text_luggage_information), luggageEntity.getName(), luggageEntity.getCategoryEntity().getName(), luggageEntity.getWeight()));
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void onDoubleClick() {

    }

    private void onLongClick() {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("luggage_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        LuggageEditDialogFragment fragment = new LuggageEditDialogFragment();
        fragment.setComplexVariable(luggageEntity);
        fragment.show(ft, "luggage_edit_dialog");

    }
}
