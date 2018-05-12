package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

public class PackingListEditDialog extends AppCompatActivity {

    private AppCompatActivity activity;

    public PackingListEditDialog() {
        this.activity = null;
    }

    public PackingListEditDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list_edit_dialog);
    }

    public PackingListEditDialog showEditDialog(final View view, final PackingListEntity packingListEntity) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View categoryEditView = inflater.inflate(R.layout.activity_packing_list_edit_dialog, null);

        final EditText inputPackingListName = categoryEditView.findViewById(R.id.inputPackingListName);
        inputPackingListName.setText(packingListEntity.getName());

        final EditText inputPackingListDate = categoryEditView.findViewById(R.id.inputPackingListDate);
        inputPackingListDate.setText(packingListEntity.getDate());

        builder.setTitle(R.string.title_luggage_edit);
        builder.setView(categoryEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = inputPackingListName.getText().toString();

                packingListEntity.setName(categoryName);

                PackingListDbModel packingListDbModel = new PackingListDbModel(view.getContext(), null, null, 1);
                packingListDbModel.update(packingListEntity);

                activity.recreate();
            }
        });

        builder.create().show();
        return this;
    }

    public PackingListEditDialog showNewDialog(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View currentView = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View categoryEditView = inflater.inflate(R.layout.activity_packing_list_edit_dialog, null);

        final EditText inputPackingListName = categoryEditView.findViewById(R.id.inputPackingListName);
        final EditText inputPackingListDate = categoryEditView.findViewById(R.id.inputPackingListDate);

        builder.setTitle(R.string.title_luggage_new);
        builder.setView(categoryEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String packingListName = inputPackingListName.getText().toString();
                String packingListDate = inputPackingListDate.getText().toString();

                PackingListEntity packingListEntity = new PackingListEntity(packingListName, packingListDate);

                PackingListDbModel packingListDbModel = new PackingListDbModel(currentView.getContext(), null, null, 1);
                packingListDbModel.insert(packingListEntity);

                activity.recreate();
            }
        });

        builder.create().show();
        return this;
    }
}
