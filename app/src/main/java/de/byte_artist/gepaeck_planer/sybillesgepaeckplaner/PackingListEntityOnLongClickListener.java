package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

class PackingListEntityOnLongClickListener implements View.OnLongClickListener {

    private PackingListEntity packingListEntity = null;

    PackingListEntityOnLongClickListener(PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
    }

    @Override
    public boolean onLongClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
        alertDialog.setTitle("LONG CLICK ! : "+this.packingListEntity.getId());
        alertDialog.setMessage(this.packingListEntity.getLuggageEntity().getName());

        alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });

        alertDialog.show();

        return false;
    }
}
