package de.byte_artist.luggage_planner.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.dialog.CategoryEditDialog;
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
        CategoryEditDialog editDialog = new CategoryEditDialog(this.activity);
        editDialog.showEditDialog(view, luggageCategoryEntity);

        return false;
    }
}
