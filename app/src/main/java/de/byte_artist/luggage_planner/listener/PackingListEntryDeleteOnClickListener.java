package de.byte_artist.luggage_planner.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class PackingListEntryDeleteOnClickListener implements View.OnClickListener {

    private PackingListEntryEntity packingListEntryEntity;
    private final AppCompatActivity activity;

    public PackingListEntryDeleteOnClickListener(AppCompatActivity activity, PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        final View currentView = view;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Delete : "+this.packingListEntryEntity.getId())
            .setMessage(
                    this.packingListEntryEntity.getLuggageEntity().getName()+" "+
                    view.getResources().getText(R.string.text_of)+" "+
                    this.packingListEntryEntity.getPackingListEntity().getName())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(currentView.getContext(), null, null, 1);
                packingListEntryDbModel.delete(packingListEntryEntity);
                packingListEntryEntity = null;

                activity.recreate();
            }
        }).show();
    }
}
