package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;

public class LuggageNewDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private long selectedCategory = 0;

    public static LuggageNewDialogFragment newInstance() {
        return new LuggageNewDialogFragment();
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        final View luggageEditView = View.inflate(getContext(), R.layout.activity_luggage_edit_dialog, null);

        final EditText luggageNameEdit = luggageEditView.findViewById(R.id.luggageNameEdit);
        final Spinner categorySpinner = luggageEditView.findViewById(R.id.categorySpinner);
        final EditText luggageWeightEdit = luggageEditView.findViewById(R.id.luggageWeight);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(getActivity());
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();
        luggageCategoryEntities.add(0, new LuggageCategoryEntity(getActivity().getResources().getText(R.string.text_choose_category).toString()));
        ArrayAdapter<LuggageCategoryEntity> spinnerArrayAdapter = new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            luggageCategoryEntities
        );

        categorySpinner.setAdapter(spinnerArrayAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        dialog.setTitle(R.string.title_luggage_new);
        dialog.setView(luggageEditView);

        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            if (1 <= selectedCategory) {
                String luggageName = luggageNameEdit.getText().toString();
                double luggageWeight = 0;

                if (0 < luggageWeightEdit.getText().length()) {
                    luggageWeight = Double.parseDouble(luggageWeightEdit.getText().toString());
                }

                if (luggageName.isEmpty()
                    || 0 >= luggageWeight
                ) {
                    showAlertNotAllNeededFieldsFilled();
                } else {
                    LuggageDbModel luggageDbModel = new LuggageDbModel(getActivity());
                    LuggageEntity luggageEntity = new LuggageEntity(luggageName, selectedCategory, luggageWeight);
                    luggageDbModel.insert(luggageEntity);

                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                    Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
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
        this.selectedCategory = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showAlertBoxNoCategorySelected() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("luggage_new_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoCategorySelected alertDialog = new AlertNoCategorySelected();

        alertDialog.show(ft, "luggage_new_dialog");
    }

    private void showAlertNotAllNeededFieldsFilled() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("luggage_new_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNotAllNeededFieldsFilled alertDialog = new AlertNotAllNeededFieldsFilled();

        alertDialog.show(ft, "luggage_new_dialog");
    }
}
