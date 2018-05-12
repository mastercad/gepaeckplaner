package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListEntryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;

public class LuggageDeleteOnClickListener implements View.OnClickListener {

    private LuggageEntity luggageEntity = null;
    private AppCompatActivity activity = null;

    public LuggageDeleteOnClickListener(AppCompatActivity activity, LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Delete : "+this.luggageEntity.getId())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(this.luggageEntity.getName())
            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(view.getContext(), null, null, 1);

                    if (packingListEntryDbModel.checkLuggageUsed(luggageEntity)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.title_error)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Für dieses Gepäckstück sind noch Einträge in Gepäcklisten vorhanden!")
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
            alertDialog.setTitle("Löschen fehlgeschlagen")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Gepäckstück \""+luggageEntity.getName()+"\" konnte nicht gelöscht werden, da es noch verwendet wird!")
                .setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        }
    }
}
