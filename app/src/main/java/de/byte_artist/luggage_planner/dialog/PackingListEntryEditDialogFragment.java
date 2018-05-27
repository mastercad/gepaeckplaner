package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class PackingListEntryEditDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private long selectedLuggage = 0;
    private long selectedCategory = 0;
    private PackingListEntryEntity packingListEntryEntity;

    private Spinner luggageSpinner = null;
    private Spinner categorySpinner = null;
    private ArrayList<LuggageEntity> currentLuggageEntities = null;

    private long packingListFk;

    public static PackingListEntryEditDialogFragment newInstance(PackingListEntryEntity packingListEntryEntity) {
        PackingListEntryEditDialogFragment fragment = new PackingListEntryEditDialogFragment();
        fragment.setComplexVariable(packingListEntryEntity);
        fragment.setComplexVariable(packingListEntryEntity.getLuggageListFk());
        return fragment;
    }

    public static PackingListEntryEditDialogFragment newInstance(long packingListFk) {
        PackingListEntryEditDialogFragment fragment = new PackingListEntryEditDialogFragment();
        fragment.setComplexVariable(packingListFk);

        return fragment;
    }

    private void setComplexVariable(PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
    }

    private void setComplexVariable(long packingListFk) {
        this.packingListFk = packingListFk;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Locale currentLocale = getResources().getConfiguration().locale;
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);
        final View packingListEntryEditView = View.inflate(getContext(), R.layout.activity_packing_list_entry_edit_dialog, null);

        final PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(getActivity());

        categorySpinner = packingListEntryEditView.findViewById(R.id.spinnerCategory);
        luggageSpinner = packingListEntryEditView.findViewById(R.id.spinnerLuggage);

        final EditText luggageCount = packingListEntryEditView.findViewById(R.id.inputPackingListEntryCount);
        luggageCount.setText(String.format(currentLocale, "%d", packingListEntryEntity.getCount()));

        final LuggageCategoryDbModel categoryDbModel = new LuggageCategoryDbModel(getActivity());
        ArrayList<LuggageCategoryEntity> categoryEntities = categoryDbModel.load();
        categoryEntities.add(0, new LuggageCategoryEntity(getActivity().getResources().getString(R.string.text_please_select)));
        ArrayAdapter<LuggageCategoryEntity> categorySpinnerArrayAdapter = new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryEntities
        );
        categorySpinner.setAdapter(categorySpinnerArrayAdapter);

        int count = 0;
        for (LuggageCategoryEntity categoryEntity : categoryEntities) {
            if (packingListEntryEntity.getLuggageEntity().getCategoryEntity().getId() == categoryEntity.getId()) {
                categorySpinner.setSelection(count);
                selectedCategory = categoryEntity.getId();
                break;
            }
            ++count;
        }

        final LuggageDbModel luggageDbModel = new LuggageDbModel(getActivity());
        currentLuggageEntities = luggageDbModel.findLuggageByCategoryId(
            packingListEntryEntity.getLuggageEntity().getCategoryEntity().getId(),
            false
        );
        currentLuggageEntities.add(0, new LuggageEntity(getActivity().getResources().getString(R.string.text_please_select), 0, 0));
        ArrayAdapter<LuggageEntity> luggageSpinnerArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                currentLuggageEntities
        );
        luggageSpinner.setAdapter(luggageSpinnerArrayAdapter);

        count = 0;
        for (LuggageEntity luggageEntity : currentLuggageEntities) {
            if (packingListEntryEntity.getLuggageEntity().getId() == luggageEntity.getId()) {
                luggageSpinner.setSelection(count);
                selectedLuggage = luggageEntity.getId();
                break;
            }
            ++count;
        }

        categorySpinner.post(new Runnable() {
            public void run() {
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext());
                    currentLuggageEntities = luggageDbModel.findLuggageByCategoryId(id, false);
                    currentLuggageEntities.add(0, new LuggageEntity(view.getResources().getString(R.string.text_please_select), 0, 0));
                    ArrayAdapter<LuggageEntity> spinnerArrayAdapter = new ArrayAdapter<>(
                        view.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        currentLuggageEntities
                    );
                    luggageSpinner.setAdapter(spinnerArrayAdapter);
                    luggageSpinner.setEnabled(true);
                    luggageSpinner.setOnItemSelectedListener(PackingListEntryEditDialogFragment.this);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ArrayAdapter<?> voidAdapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        new ArrayList<>()
                    );
                    luggageSpinner.setAdapter(voidAdapter);
                    luggageSpinner.setEnabled(false);
                }
            });
            }
        });

        dialog.setTitle(R.string.title_luggage_edit);
        dialog.setView(packingListEntryEditView);

        /*
         * save
         */
        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            if (0 < selectedCategory
                && 0 < packingListFk
            ) {
                PackingListEntryEntity packingListEntryEntityFromDb = packingListEntryDbModel.findLuggageInPackingList(
                    selectedLuggage,
                    packingListFk
                );

                if (null == packingListEntryEntityFromDb
                    || packingListEntryEntityFromDb.getId() == packingListEntryEntity.getId()
                ) {
                    int count = 0;
                    if (luggageCount.getText().toString().isEmpty()) {
                        showAlertNotAllNeededFieldsFilled();
                    } else {
                        count = Integer.parseInt(luggageCount.getText().toString());
                        packingListEntryEntity.setLuggageFk(selectedLuggage);
                        packingListEntryEntity.setLuggageListFk(packingListEntryEntity.getLuggageListFk());
                        packingListEntryEntity.setCount(count);

                        packingListEntryDbModel.update(packingListEntryEntity);

                        getActivity().finish();
                        getActivity().startActivity(getActivity().getIntent());
                        Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
                    }
                } else {
                    showAlertLuggageAlreadyExistsInThisPackingList();
                }
            } else {
                showAlertBoxNoCategorySelected();
            }
            }
        });

        final PackingListEntryEditDialogFragment self = this;

        luggageSpinner.post(new Runnable() {
            public void run() {
                luggageSpinner.setOnItemSelectedListener(self);
            }
        });

        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int count = 0;
        for (LuggageEntity luggageEntity : currentLuggageEntities) {
            if (0 < count
                && count == id
            ) {
                selectedLuggage = luggageEntity.getId();
                break;
            }
            ++count;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showAlertBoxNoCategorySelected() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoCategorySelected alertDialog = new AlertNoCategorySelected();

        alertDialog.show(ft, "packing_list_entry_edit_dialog");
    }

    private void showAlertNotAllNeededFieldsFilled() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNotAllNeededFieldsFilled alertDialog = new AlertNotAllNeededFieldsFilled();

        alertDialog.show(ft, "packing_list_entry_edit_dialog");
    }

    private void showAlertLuggageAlreadyExistsInThisPackingList() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertLuggageAlreadyExists alertDialog = new AlertLuggageAlreadyExists();

        alertDialog.show(ft, "packing_list_entry_edit_dialog");
    }
}
