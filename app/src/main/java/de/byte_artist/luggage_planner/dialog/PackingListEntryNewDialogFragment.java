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

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class PackingListEntryNewDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private long selectedLuggage;
    private long selectedCategory;

    private Spinner luggageSpinner = null;
    private Spinner categorySpinner = null;

    private long packingListFk;
    private ArrayList<LuggageEntity> currentLuggageEntities = null;

    public static PackingListEntryNewDialogFragment newInstance(long packingListFk) {
        PackingListEntryNewDialogFragment fragment = new PackingListEntryNewDialogFragment();
        fragment.setComplexVariable(packingListFk);

        return fragment;
    }

    private void setComplexVariable(long packingListFk) {
        this.packingListFk = packingListFk;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        final View packingListEntryEditView = View.inflate(getContext(), R.layout.activity_packing_list_entry_edit_dialog, null);
        final PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(getActivity());

        categorySpinner = packingListEntryEditView.findViewById(R.id.spinnerCategory);
        luggageSpinner = packingListEntryEditView.findViewById(R.id.spinnerLuggage);
        luggageSpinner.setEnabled(false);

        final EditText luggageCount = packingListEntryEditView.findViewById(R.id.inputPackingListEntryCount);

        final LuggageCategoryDbModel categoryDbModel = new LuggageCategoryDbModel(getActivity());
        ArrayList<LuggageCategoryEntity> categoryEntities = categoryDbModel.load();
        categoryEntities.add(0, new LuggageCategoryEntity(getActivity().getResources().getText(R.string.text_choose_category).toString()));
        ArrayAdapter<LuggageCategoryEntity> spinnerArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryEntities
        );

        categorySpinner.setAdapter(spinnerArrayAdapter);

        dialog.setTitle(R.string.title_luggage_new);
        dialog.setView(packingListEntryEditView);

        /*
         * Category Selector change
         */
        categorySpinner.post(new Runnable() {
             public void run() {
             categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     selectedCategory = id;
                     LuggageDbModel luggageDbModel = new LuggageDbModel(view.getContext());
                     currentLuggageEntities = luggageDbModel.findLuggageByCategoryId(id, false);
                     currentLuggageEntities.add(0, new LuggageEntity(view.getResources().getString(R.string.text_please_select), 0, 0));
                     ArrayAdapter<LuggageEntity> spinnerArrayAdapter = new ArrayAdapter<>(
                             view.getContext(),
                             android.R.layout.simple_spinner_dropdown_item,
                             currentLuggageEntities
                     );
                     luggageSpinner.setAdapter(spinnerArrayAdapter);
                     luggageSpinner.setEnabled(true);
                     luggageSpinner.setOnItemSelectedListener(PackingListEntryNewDialogFragment.this);

                     luggageSpinner.post(new Runnable() {
                         public void run() {
                             luggageSpinner.setOnItemSelectedListener(PackingListEntryNewDialogFragment.this);
                         }
                     });
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

        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (0 < selectedLuggage
                    && 0 < packingListFk
                ) {
                    if (0 == luggageCount.length()) {
                        showAlertNotAllNeededFieldFilled();
                    } else {
                        int count = Integer.parseInt(luggageCount.getText().toString());

                        if (0 >= count) {
                            showAlertNotAllNeededFieldFilled();
                        } else {
                            PackingListEntryEntity packingListEntryEntityFromDb = packingListEntryDbModel.findLuggageInPackingList(
                                selectedLuggage,
                                packingListFk
                            );

                            if (null == packingListEntryEntityFromDb
                                || packingListEntryEntityFromDb.getLuggageFk() != selectedLuggage
                            ) {
                                PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity(packingListFk, selectedLuggage, count);
                                packingListEntryDbModel.insert(packingListEntryEntity);

                                getActivity().finish();
                                getActivity().startActivity(getActivity().getIntent());
                                Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
                            } else {
                                showAlertLuggageAlreadyExistsInThisPackingList();
                            }
                        }
                    }
                } else {
                    showAlertBoxNoCategorySelected();
                }
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
        for (LuggageEntity luggageEntity : this.currentLuggageEntities) {
            if (0 < count
                && count == id
            ) {
                selectedLuggage = luggageEntity.getId();
                selectedCategory = luggageEntity.getCategoryId();
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
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_new_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoCategorySelected alertDialog = new AlertNoCategorySelected();

        alertDialog.show(ft, "packing_list_entry_new_dialog");
    }

    private void showAlertNotAllNeededFieldFilled() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_new_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNotAllNeededFieldsFilled alertDialog = new AlertNotAllNeededFieldsFilled();

        alertDialog.show(ft, "packing_list_entry_new_dialog");
    }

    private void showAlertLuggageAlreadyExistsInThisPackingList() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_new_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertLuggageAlreadyExists alertDialog = new AlertLuggageAlreadyExists();

        alertDialog.show(ft, "packing_list_entry_new_dialog");
    }
}
