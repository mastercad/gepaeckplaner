package de.byte_artist.luggage_planner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.entity.PackingListEntity;

public class PackingListDeleteOnClickListener implements View.OnClickListener {

    private PackingListEntity packingListEntity;
    private final AppCompatActivity activity;

    public PackingListDeleteOnClickListener(AppCompatActivity activity, PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
        alertDialog.setTitle(R.string.label_delete)
            .setMessage(this.packingListEntity.getName()+" "+this.packingListEntity.getDate())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        PackingListDbModel packingListDbModel = new PackingListDbModel(view.getContext(), null, null, 1);
                        packingListDbModel.delete(packingListEntity);
                        packingListEntity = null;

                        activity.recreate();
                    } catch (SQLiteConstraintException exception) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                        alertDialog.setTitle(R.string.warning_delete_failed)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage(
                                String.format(view.getResources().getString(R.string.placeholder_delete_packing_list_constraints_reason),
                                    packingListEntity.getName(),
                                    packingListEntity.getDate()
                                )
                            ).setPositiveButton(R.string.text_understood, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    }
                }
            }).setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
    }
}
