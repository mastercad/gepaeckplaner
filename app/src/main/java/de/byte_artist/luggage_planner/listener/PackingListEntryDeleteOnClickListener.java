package de.byte_artist.luggage_planner.listener;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class PackingListEntryDeleteOnClickListener implements View.OnClickListener {

    private PackingListEntryEntity packingListEntryEntity;
    private final AppCompatActivity activity;

    public PackingListEntryDeleteOnClickListener(AppCompatActivity activity, PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
        this.activity = activity;
    }

    @Override
    public void onClick(final View view) {
        CustomDialog dialog = new CustomDialog(activity, R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);
        dialog.setTitle(R.string.label_delete);
        dialog.setMessage(String.format(view.getResources().getString(R.string.placeholder_luggage_of_packing_list),
                this.packingListEntryEntity.getLuggageEntity().getName(),
                this.packingListEntryEntity.getPackingListEntity().getName()
            )
        );
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.label_delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(view.getContext());
                packingListEntryDbModel.delete(packingListEntryEntity);
                packingListEntryEntity = null;

                activity.recreate();
            }
        });
        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
}
