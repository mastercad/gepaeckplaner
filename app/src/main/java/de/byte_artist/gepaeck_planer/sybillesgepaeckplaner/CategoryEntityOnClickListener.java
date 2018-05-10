package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class CategoryEntityOnClickListener implements View.OnClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private CategoryActivity categoryActivity = null;

    CategoryEntityOnClickListener(CategoryActivity categoryActivity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.categoryActivity = categoryActivity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
        alertDialog.setTitle(""+this.luggageCategoryEntity.getId());
        alertDialog.setMessage(this.luggageCategoryEntity.getName());

        alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // here you can add functions
            }
        });

        alertDialog.show();
    }
}
