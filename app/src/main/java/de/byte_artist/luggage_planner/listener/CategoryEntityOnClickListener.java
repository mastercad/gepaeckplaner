package de.byte_artist.luggage_planner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryEntityOnClickListener implements View.OnClickListener {

    private final LuggageCategoryEntity luggageCategoryEntity;
//    private AppCompatActivity activity;

    public CategoryEntityOnClickListener(AppCompatActivity activity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
//        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());

        alertDialog.setTitle(R.string.title_information)
            .setMessage(String.format(view.getResources().getString(R.string.text_category_info), luggageCategoryEntity.getName()))
            .setIcon(android.R.drawable.ic_menu_help)
            .setPositiveButton(R.string.text_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            }).show();
    }
}
