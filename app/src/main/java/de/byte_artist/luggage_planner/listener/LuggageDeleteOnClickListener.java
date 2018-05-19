package de.byte_artist.luggage_planner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageEntity;

public class LuggageDeleteOnClickListener implements View.OnClickListener {

    private LuggageEntity luggageEntity;
    private final AppCompatActivity activity;

    public LuggageDeleteOnClickListener(AppCompatActivity activity, LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.label_delete)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(this.luggageEntity.getName())
            .setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(view.getContext(), null, null, 1);

                    if (packingListEntryDbModel.checkLuggageUsed(luggageEntity)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.title_error)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage(R.string.warning_luggage_still_in_use)
                            .setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteLuggage(luggageEntity, view);
                                }
                            })
                            .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    } else {
                        deleteLuggage(luggageEntity, view);
                    }
                }
            }).show();
    }

    private void deleteLuggage(LuggageEntity luggageEntity, View view) {
        try {
            LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext(), null, null, 1);
            luggageDbModel.delete(luggageEntity);
            this.luggageEntity = null;

            activity.recreate();
        } catch (SQLiteConstraintException exception) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
            alertDialog.setTitle(view.getResources().getString(R.string.warning_delete_failed))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(String.format(view.getResources().getString(R.string.placeholder_delete_constraints_reason), luggageEntity.getName()))
                .setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        }
    }
}