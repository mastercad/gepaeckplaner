package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class LuggageEntityOnClickListener implements View.OnClickListener {

    private LuggageEntity luggageEntity = null;

    LuggageEntityOnClickListener(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
        alertDialog.setTitle(""+this.luggageEntity.getId());
        alertDialog.setMessage(this.luggageEntity.getName());

        alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // here you can add functions
            }
        });

        alertDialog.show();
    }
}
