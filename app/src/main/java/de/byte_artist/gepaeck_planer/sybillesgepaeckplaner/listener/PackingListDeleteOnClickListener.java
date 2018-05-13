package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

public class PackingListDeleteOnClickListener implements View.OnClickListener {

    private PackingListEntity packingListEntity;
    private final AppCompatActivity activity;

    public PackingListDeleteOnClickListener(AppCompatActivity activity, PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Delete : "+this.packingListEntity.getId())
            .setMessage(this.packingListEntity.getName()+" "+this.packingListEntity.getDate())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        PackingListDbModel packingListDbModel = new PackingListDbModel(view.getContext(), null, null, 1);
                        packingListDbModel.delete(packingListEntity);
                        packingListEntity = null;

                        activity.recreate();
                    } catch (SQLiteConstraintException exception) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                        alertDialog.setTitle("Löschen fehlgeschlagen")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Packliste \""+packingListEntity.getName()+" "+packingListEntity.getDate()+
                                    "\" konnte nicht gelöscht werden, da sie noch verwendet wird!")
                            .setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    }
                }
            }).show();
    }
}
