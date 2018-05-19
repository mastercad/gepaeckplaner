package de.byte_artist.luggage_planner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryDeleteOnClickListener implements View.OnClickListener {

    private LuggageCategoryEntity luggageCategoryEntity;
    private final AppCompatActivity activity;

    public CategoryDeleteOnClickListener(AppCompatActivity activity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(R.string.label_attention)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(String.format(view.getResources().getString(R.string.placeholder_entry_should_be_deleted), this.luggageCategoryEntity.getName()))
            .setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(view.getContext(), null, null, 1);
                    luggageCategoryDbModel.delete(luggageCategoryEntity);
                    luggageCategoryEntity = null;

                    activity.recreate();
                } catch (SQLiteConstraintException exception) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setTitle(R.string.warning_delete_failed)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(String.format(view.getResources().getString(R.string.placeholder_delete_category_constraints_reason), luggageCategoryEntity.getName()))
                        .setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                }
            }
        }).show();
    }
}