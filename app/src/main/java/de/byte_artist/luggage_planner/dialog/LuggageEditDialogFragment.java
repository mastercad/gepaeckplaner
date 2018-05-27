package de.byte_artist.luggage_planner.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.service.TextSize;

public class LuggageEditDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private long selectedCategory = 0;
    private LuggageEntity luggageEntity;

    public static LuggageEditDialogFragment newInstance(LuggageEntity luggageEntity) {
        LuggageEditDialogFragment fragment = new LuggageEditDialogFragment();
        fragment.setComplexVariable(luggageEntity);

        return fragment;
    }

    private void setComplexVariable(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        final View luggageEditView = View.inflate(getContext(), R.layout.activity_luggage_edit_dialog, null);

        Locale currentLocale = getResources().getConfiguration().locale;

        final EditText luggageNameEdit = luggageEditView.findViewById(R.id.luggageNameEdit);
        luggageNameEdit.setText(luggageEntity.getName());

        final Spinner categorySpinner = luggageEditView.findViewById(R.id.categorySpinner);

        final LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(getActivity());
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();
        luggageCategoryEntities.add(0, new LuggageCategoryEntity(
            getActivity().getResources().getString(R.string.text_please_select))
        );
        ArrayAdapter<LuggageCategoryEntity> spinnerArrayAdapter = new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            luggageCategoryEntities
        );

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
        luggageWeightEdit.setText(String.format(currentLocale, "%.0f", luggageEntity.getWeight()));

        dialog.setTitle(R.string.title_luggage_edit);
        dialog.setView(luggageEditView);

        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (1 <= selectedCategory) {
                    String luggageName = luggageNameEdit.getText().toString();
                    double luggageWeight = 0;

                    if (luggageName.isEmpty()
                        || 0 == luggageWeightEdit.length()
                    ) {
                        showAlertNotAllNeededFieldFilled();
                    } else {
                        luggageWeight = Double.parseDouble(luggageWeightEdit.getText().toString());
                        luggageEntity.setName(luggageName);
                        luggageEntity.setWeight(luggageWeight);
                        luggageEntity.setCategoryId(selectedCategory);

                        LuggageDbModel luggageDbModel = new LuggageDbModel(getActivity());
                        luggageDbModel.update(luggageEntity);

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
            }
        });

        dialog.create();

        TextSize.convert(getActivity(), categorySpinner, TextSize.TEXT_TYPE_MESSAGE);

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
        Fragment prev = getFragmentManager().findFragmentByTag("luggage_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoCategorySelected alertDialog = new AlertNoCategorySelected();

        alertDialog.show(ft, "luggage_edit_dialog");
    }

    private void showAlertNotAllNeededFieldFilled() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("luggage_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNotAllNeededFieldsFilled alertDialog = new AlertNotAllNeededFieldsFilled();

        alertDialog.show(ft, "luggage_edit_dialog");
    }
}
