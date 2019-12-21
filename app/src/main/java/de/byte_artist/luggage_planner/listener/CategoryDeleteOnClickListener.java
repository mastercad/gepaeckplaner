package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
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
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);

        dialog.setTitle(R.string.label_attention);
        dialog.setMessage(String.format(view.getResources().getString(R.string.placeholder_entry_should_be_deleted), this.luggageCategoryEntity.getName()));
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.label_delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(view.getContext());
                    luggageCategoryDbModel.delete(luggageCategoryEntity);
                    luggageCategoryEntity = null;

                    activity.recreate();
                } catch (SQLiteConstraintException exception) {
                    CustomDialog saveFailDialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
                    saveFailDialog.setTitle(R.string.label_attention);
                    saveFailDialog.setMessage(String.format(view.getResources().getString(R.string.placeholder_delete_category_constraints_reason), luggageCategoryEntity.getName()));
                    saveFailDialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    saveFailDialog.create();
                    saveFailDialog.show();
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
}
