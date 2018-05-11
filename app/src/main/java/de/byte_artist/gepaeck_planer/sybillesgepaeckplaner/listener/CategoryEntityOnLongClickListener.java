package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;


import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.CategoryActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.CategoryEditDialog;

public class CategoryEntityOnLongClickListener implements View.OnLongClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private CategoryActivity categoryActivity = null;

    public CategoryEntityOnLongClickListener(CategoryActivity categoryActivity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.categoryActivity = categoryActivity;
    }

    @Override
    public boolean onLongClick(View view) {
        CategoryEditDialog editDialog = new CategoryEditDialog(this.categoryActivity);
        editDialog.showEditDialog(view, luggageCategoryEntity);

        return false;
    }
}
