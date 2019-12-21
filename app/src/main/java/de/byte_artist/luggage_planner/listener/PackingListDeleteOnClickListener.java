package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
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
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);
        dialog.setTitle(R.string.label_delete);
        dialog.setMessage(this.packingListEntity.getName()+" "+this.packingListEntity.getDate());
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.label_delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    PackingListDbModel packingListDbModel = new PackingListDbModel(view.getContext());
                    packingListDbModel.delete(packingListEntity);
                    packingListEntity = null;

                    activity.recreate();
                } catch (SQLiteConstraintException exception) {
                    CustomDialog alertDialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
                    alertDialog.setTitle(R.string.warning_delete_failed);
                    alertDialog.setMessage(
                        String.format(view.getResources().getString(R.string.placeholder_delete_packing_list_constraints_reason),
                                packingListEntity.getName(),
                                packingListEntity.getDate()
                        )
                    );
                    alertDialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
            }
        });
        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
}
