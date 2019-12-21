package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryEntityOnClickListener implements View.OnClickListener {

    private final LuggageCategoryEntity luggageCategoryEntity;
    private final AppCompatActivity activity;

    public CategoryEntityOnClickListener(AppCompatActivity activity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_INFO);

        dialog.setTitle(R.string.title_information);
        dialog.setMessage(String.format(view.getResources().getString(R.string.text_category_info), luggageCategoryEntity.getName()));
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, view.getResources().getString(R.string.text_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }
}
