package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

class CategoryEntityOnLongClickListener implements View.OnLongClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private CategoryActivity categoryActivity = null;

    CategoryEntityOnLongClickListener(CategoryActivity categoryActivity, LuggageCategoryEntity luggageCategoryEntity) {
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
