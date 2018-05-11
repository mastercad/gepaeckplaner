package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageCategoryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity.CategoryActivity;

public class CategoryDeleteOnClickListener implements View.OnClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private CategoryActivity categoryActivity = null;

    public CategoryDeleteOnClickListener(CategoryActivity categoryActivity, LuggageCategoryEntity luggageCategoryEntity) {
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
