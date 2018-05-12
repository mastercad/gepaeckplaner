package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;

public class LuggageEntityOnClickListener implements View.OnClickListener {

    private LuggageEntity luggageEntity = null;

    public LuggageEntityOnClickListener(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(""+this.luggageEntity.getId())
            .setMessage(this.luggageEntity.getName())
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // here you can add functions
            }
        }).show();
    }
}
