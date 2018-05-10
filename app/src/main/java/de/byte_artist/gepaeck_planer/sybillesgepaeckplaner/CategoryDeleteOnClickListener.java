package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

public class CategoryDeleteOnClickListener implements View.OnClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private CategoryActivity categoryActivity = null;

    CategoryDeleteOnClickListener(CategoryActivity categoryActivity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.categoryActivity = categoryActivity;
    }

    @Override
    public void onClick(View view) {
        final View currentView = view;
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle("Delete : "+this.luggageCategoryEntity.getId());
        alertDialog.setMessage(this.luggageCategoryEntity.getName());

        alertDialog.setButton("Löschen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(currentView.getContext(), null, null, 1);
                luggageCategoryDbModel.delete(luggageCategoryEntity);
                luggageCategoryEntity = null;

                categoryActivity.recreate();
            }
        });

        alertDialog.show();
    }
}
