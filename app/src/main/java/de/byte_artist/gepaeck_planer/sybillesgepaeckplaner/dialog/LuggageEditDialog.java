package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageCategoryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;

public class LuggageEditDialog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private long selectedCategory = 0;
    private AppCompatActivity activity;

    public LuggageEditDialog() {
        this.activity = null;
    }

    public LuggageEditDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage_edit_dialog);
    }

    public LuggageEditDialog showEditDialog(final View view, final LuggageEntity luggageEntity) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View luggageEditView = inflater.inflate(R.layout.activity_luggage_edit_dialog, null);

        final EditText luggageNameEdit = luggageEditView.findViewById(R.id.luggageNameEdit);
        luggageNameEdit.setText(luggageEntity.getName());

        final Spinner categorySpinner = luggageEditView.findViewById(R.id.categorySpinner);

        final LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(view.getContext(), null, null, 1);
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();
        luggageCategoryEntities.add(0, new LuggageCategoryEntity("Bitte wählen!"));
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, luggageCategoryEntities);

        categorySpinner.setAdapter(spinnerArrayAdapter);

        int count = 0;
        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
            if (luggageCategoryEntity.getName().equals(luggageEntity.getCategoryEntity().getName())) {
                categorySpinner.setSelection(count);
                selectedCategory = count;
                break;
            }
            ++count;
        }
        categorySpinner.setOnItemSelectedListener(this);

        final EditText luggageWeightEdit = luggageEditView.findViewById(R.id.luggageWeight);
        luggageWeightEdit.setText(Integer.toString(luggageEntity.getWeight()));

        builder.setTitle(R.string.title_luggage_edit);
        builder.setView(luggageEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (1 <= selectedCategory) {
                    luggageEntity.setName(luggageNameEdit.getText().toString());
                    luggageEntity.setWeight(Integer.valueOf(luggageWeightEdit.getText().toString()));
                    luggageEntity.setCategoryId(selectedCategory);

                    LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext(), null, null, 1);
                    luggageDbModel.update(luggageEntity);

                    activity.recreate();
                } else {
                    showAlertBoxNoCategorySelected(view);
                }
            }
        });

        builder.create().show();
        return this;
    }

    public LuggageEditDialog showNewDialog(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View luggageEditView = inflater.inflate(R.layout.activity_luggage_edit_dialog, null);

        final EditText luggageNameEdit = luggageEditView.findViewById(R.id.luggageNameEdit);
        final Spinner categorySpinner = luggageEditView.findViewById(R.id.categorySpinner);
        final EditText luggageWeightEdit = luggageEditView.findViewById(R.id.luggageWeight);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(view.getContext(), null, null, 1);
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();
        luggageCategoryEntities.add(0, new LuggageCategoryEntity(view.getResources().getText(R.string.text_choose_category).toString()));
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, luggageCategoryEntities);

        categorySpinner.setAdapter(spinnerArrayAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        builder.setTitle(R.string.title_luggage_new);
        builder.setView(luggageEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (1 <= selectedCategory) {
                    String luggageName = luggageNameEdit.getText().toString();
                    int luggageWeight = Integer.valueOf(luggageWeightEdit.getText().toString());

                    if (luggageName.isEmpty()
                        || 0 >= luggageWeight
                    ) {
                        showAlertNotAllNeededFieldFilled(view);
                    } else {
                        LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext(), null, null, 1);
                        LuggageEntity luggageEntity = new LuggageEntity(luggageName, selectedCategory, luggageWeight);
                        luggageDbModel.insert(luggageEntity);

                        activity.recreate();
                    }
                } else {
                    showAlertBoxNoCategorySelected(view);
                }
            }
        });

        builder.create().show();
        return this;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectedCategory = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showAlertBoxNoCategorySelected(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.title_error)
            .setMessage(R.string.text_choose_category)
            .setPositiveButton(view.getResources().getText(R.string.text_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setNegativeButton(view.getResources().getText(R.string.text_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            }).show();
    }

    public void showAlertNotAllNeededFieldFilled(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(R.string.title_error)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(view.getResources().getText(R.string.text_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            }).show();
    }
}
