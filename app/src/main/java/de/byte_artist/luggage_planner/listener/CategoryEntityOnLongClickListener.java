package de.byte_artist.luggage_planner.listener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.dialog.CategoryEditDialogFragment;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryEntityOnLongClickListener implements View.OnLongClickListener {

    private final LuggageCategoryEntity luggageCategoryEntity ;
    private final AppCompatActivity activity;

    public CategoryEntityOnLongClickListener(AppCompatActivity activity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.activity = activity;
    }

    @Override
    public boolean onLongClick(View view) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("category_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        CategoryEditDialogFragment fragment = new CategoryEditDialogFragment();
        fragment.setComplexVariable(luggageCategoryEntity);

        fragment.show(ft, "category_edit_dialog");

        return false;
    }
}
