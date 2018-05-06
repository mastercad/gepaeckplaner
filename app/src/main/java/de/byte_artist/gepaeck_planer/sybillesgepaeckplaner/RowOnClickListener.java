package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class RowOnClickListener implements View.OnClickListener {

    private LuggageListEntryEntity luggageListEntryEntity = null;

    RowOnClickListener(LuggageListEntryEntity luggageListEntryEntity) {
        this.luggageListEntryEntity = luggageListEntryEntity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
        alertDialog.setTitle(""+this.luggageListEntryEntity.getId());
        alertDialog.setMessage(this.luggageListEntryEntity.getLuggageEntity().getName());

        alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // here you can add functions
            }
        });

        alertDialog.show();
    }
}
