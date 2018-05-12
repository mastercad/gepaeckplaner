package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;

public class CategoryEntityOnClickListener implements View.OnClickListener {

    private LuggageCategoryEntity luggageCategoryEntity = null;
    private AppCompatActivity activity = null;

    public CategoryEntityOnClickListener(AppCompatActivity activity, LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());

        alertDialog.setTitle(""+this.luggageCategoryEntity.getId())
            .setMessage(this.luggageCategoryEntity.getName())
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            }).show();
    }
}
