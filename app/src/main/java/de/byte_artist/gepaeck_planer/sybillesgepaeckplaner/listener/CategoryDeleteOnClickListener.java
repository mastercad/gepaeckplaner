package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageCategoryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;

public class CategoryDeleteOnClickListener implements View.OnClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private AppCompatActivity activity = null;

    public CategoryDeleteOnClickListener(AppCompatActivity activity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Achtung!")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(this.luggageCategoryEntity.getName()+" soll gelöscht werden!")
            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(view.getContext(), null, null, 1);
                    luggageCategoryDbModel.delete(luggageCategoryEntity);
                    luggageCategoryEntity = null;

                    activity.recreate();
                } catch (SQLiteConstraintException exception) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setTitle("Löschen fehlgeschlagen")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Kategorie "+luggageCategoryEntity.getName()+" konnte nicht gelöscht werden, da es noch verwendet wird!")
                        .setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                }
            }
        }).show();
    }
}
