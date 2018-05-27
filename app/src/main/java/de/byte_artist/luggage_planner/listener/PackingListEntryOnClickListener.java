package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class PackingListEntryOnClickListener implements View.OnClickListener {

    private final AppCompatActivity activity;
    private final PackingListEntryEntity packingListEntryEntity;

    public PackingListEntryOnClickListener(AppCompatActivity activity, PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_INFO);
        dialog.setTitle(R.string.title_information);
        dialog.setMessage(
            String.format(activity.getResources().getString(R.string.text_packing_list_entry_entity_information),
                packingListEntryEntity.getLuggageEntity().getName(),
                packingListEntryEntity.getCount(),
                packingListEntryEntity.getPackingListEntity().getName(),
                packingListEntryEntity.getLuggageEntity().getWeight() * packingListEntryEntity.getCount()
            )
        );
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.text_close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
                }
        });
        dialog.create();
        dialog.show();
    }
}
