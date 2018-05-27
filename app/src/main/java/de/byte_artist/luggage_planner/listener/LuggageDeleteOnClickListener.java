package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
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
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);
        dialog.setTitle(R.string.label_delete);
        dialog.setMessage(this.luggageEntity.getName());
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.label_delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(view.getContext());

                if (packingListEntryDbModel.checkLuggageUsed(luggageEntity)) {
                    CustomDialog alertDialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
                    alertDialog.setTitle(R.string.title_error);
                    alertDialog.setMessage(R.string.warning_luggage_still_in_use);
                    alertDialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                        deleteLuggage(luggageEntity, view);
                            }
                    });
                    alertDialog.setButton(CustomDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                } else {
                    deleteLuggage(luggageEntity, view);
                }
            }
        });
        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.create();
        dialog.show();
    }

    private void deleteLuggage(LuggageEntity luggageEntity, View view) {
        try {
            LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext());
            luggageDbModel.delete(luggageEntity);
            this.luggageEntity = null;

            activity.recreate();
        } catch (SQLiteConstraintException exception) {
            CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);
            dialog.setTitle(view.getResources().getString(R.string.warning_delete_failed));
            dialog.setMessage(String.format(view.getResources().getString(R.string.placeholder_delete_constraints_reason), luggageEntity.getName()));
            dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create();
            dialog.show();
        }
    }
}
