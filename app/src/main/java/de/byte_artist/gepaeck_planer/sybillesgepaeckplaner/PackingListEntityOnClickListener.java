package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class PackingListEntityOnClickListener implements View.OnClickListener {

    private PackingListEntity packingListEntity = null;

    PackingListEntityOnClickListener(PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
        alertDialog.setTitle(""+this.packingListEntity.getId());
        alertDialog.setMessage(this.packingListEntity.getLuggageListEntity().getName() + " " + this.packingListEntity.getLuggageListEntity().getDate());

        alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // here you can add functions
            }
        });

        alertDialog.show();
    }
}
