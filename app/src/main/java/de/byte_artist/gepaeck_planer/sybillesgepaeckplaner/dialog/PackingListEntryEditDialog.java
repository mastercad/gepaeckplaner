package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Locale;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageCategoryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListEntryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;

public class PackingListEntryEditDialog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private long selectedLuggage = 0;
    private long selectedCategory = 0;
    private Spinner luggageSpinner = null;
    private ArrayList<LuggageEntity> currentLuggageEntities = null;

    private final long luggageListFk;
    private final AppCompatActivity activity;

    public PackingListEntryEditDialog() {
        this.activity = null;
        this.luggageListFk = 0;
    }

    public PackingListEntryEditDialog(AppCompatActivity activity, long luggageListFk) {
        this.activity = activity;
        this.luggageListFk = luggageListFk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage_edit_dialog);
    }

    public void showEditDialog(final View view, final PackingListEntryEntity packingListEntryEntity) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View packingListEntryEditView = inflater.inflate(R.layout.activity_packing_list_entry_edit_dialog, null);

        final Spinner packingListSpinner = packingListEntryEditView.findViewById(R.id.spinnerCategory);
        final Spinner luggageSpinner = packingListEntryEditView.findViewById(R.id.spinnerLuggage);

        final EditText luggageCount = packingListEntryEditView.findViewById(R.id.inputPackingListEntryCount);
        luggageCount.setText(String.format(Locale.getDefault(), "%d", packingListEntryEntity.getCount()));

        final PackingListDbModel packingListDbModel = new PackingListDbModel(view.getContext(), null, null, 1);
        ArrayList<PackingListEntity> packingListEntities = packingListDbModel.load();
        packingListEntities.add(0, new PackingListEntity("Bitte wählen!", ""));
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, packingListEntities);
        packingListSpinner.setAdapter(spinnerArrayAdapter);

        int count = 0;
        for (PackingListEntity packingListEntity : packingListEntities) {
            if (packingListEntryEntity.getPackingListEntity().getId() == packingListEntity.getId()) {
                packingListSpinner.setSelection(count);
                selectedCategory = count;
                break;
            }
            ++count;
        }

        builder.setTitle(R.string.title_luggage_edit);
        builder.setView(packingListEntryEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (1 <= selectedCategory) {
                    packingListEntryEntity.setLuggageFk(selectedLuggage);
                    packingListEntryEntity.setLuggageListFk(packingListEntryEntity.getLuggageListFk());
                    packingListEntryEntity.setCount(Integer.parseInt(luggageCount.getText().toString()));

                    PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(view.getContext(), null, null, 1);
                    packingListEntryDbModel.update(packingListEntryEntity);

                    activity.recreate();
                } else {
                    showAlertBoxNoCategorySelected(view);
                }
            }
        });
        luggageSpinner.setOnItemSelectedListener(this);

        builder.create().show();
    }

    public void showNewDialog(final View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View packingListEntryEditView = inflater.inflate(R.layout.activity_packing_list_entry_edit_dialog, null);

        Spinner categorySpinner = packingListEntryEditView.findViewById(R.id.spinnerCategory);
        this.luggageSpinner = packingListEntryEditView.findViewById(R.id.spinnerLuggage);
        luggageSpinner.setEnabled(false);

        final EditText luggageCount = packingListEntryEditView.findViewById(R.id.inputPackingListEntryCount);

        final LuggageCategoryDbModel categoryDbModel = new LuggageCategoryDbModel(view.getContext(), null, null, 1);
        ArrayList<LuggageCategoryEntity> categoryEntities = categoryDbModel.load();
        categoryEntities.add(0, new LuggageCategoryEntity(view.getResources().getText(R.string.text_choose_category).toString()));
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, categoryEntities);

        categorySpinner.setAdapter(spinnerArrayAdapter);

        builder.setTitle(R.string.title_luggage_new);
        builder.setView(packingListEntryEditView);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext(), null, null, 1);
                currentLuggageEntities = luggageDbModel.findLuggageByCategoryId(id);
                currentLuggageEntities.add(0, new LuggageEntity("Bitte wählen!", 0, 0));
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, currentLuggageEntities);
                luggageSpinner.setAdapter(spinnerArrayAdapter);
                luggageSpinner.setEnabled(true);
                luggageSpinner.setOnItemSelectedListener(PackingListEntryEditDialog.this);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayAdapter voidAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, new ArrayList());
                luggageSpinner.setAdapter(voidAdapter);
                luggageSpinner.setEnabled(false);
            }
        });

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (1 <= selectedLuggage) {
                    if (0 == luggageCount.length()) {
                        showAlertNotAllNeededFieldFilled(view);
                    } else {
                        int count = Integer.parseInt(luggageCount.getText().toString());

                        if (0 >= count) {
                            showAlertNotAllNeededFieldFilled(view);
                        } else {
                            PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity(luggageListFk, selectedLuggage, count);
                            PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(view.getContext(), null, null, 1);
                            packingListEntryDbModel.insert(packingListEntryEntity);

                            activity.recreate();
                        }
                    }
                } else {
                    showAlertBoxNoCategorySelected(view);
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int count = 0;
        for (LuggageEntity luggageEntity : this.currentLuggageEntities) {
            if (0 < count
                && count == id
            ) {
                this.selectedLuggage = luggageEntity.getId();
                break;
            }
            ++count;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showAlertBoxNoCategorySelected(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(R.string.title_error);
        alertDialog.setMessage(R.string.text_choose_category);

        alertDialog.setPositiveButton(view.getResources().getText(R.string.text_understood), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton(view.getResources().getText(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });

        alertDialog.show();
    }

    private void showAlertNotAllNeededFieldFilled(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(R.string.title_error)
            .setMessage(view.getResources().getText(R.string.text_not_all_fields_filled))
            .setPositiveButton(view.getResources().getText(R.string.text_understood), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            }).show();
    }
}
