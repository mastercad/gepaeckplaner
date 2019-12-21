package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.helper.LocaleHelper;
import de.byte_artist.luggage_planner.service.TextSize;

abstract class LuggageEditDialogFragmentAbstract extends DialogFragment {

    private long selectedCategory = 0;
    private LuggageEntity luggageEntity;
    private ArrayList<LuggageCategoryEntity> luggageCategoryEntities;
    private AutoCompleteTextView categoryNames;
    private LuggageCategoryDbModel categoryDbModel;

    public void setComplexVariable(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);
        final View luggageEditView = View.inflate(getContext(), R.layout.activity_luggage_edit_dialog, null);
        final Button btnSave = luggageEditView.findViewById(R.id.btnSave);
        final Button btnCancel = luggageEditView.findViewById(R.id.btnCancel);
        final EditText luggageNameEdit = luggageEditView.findViewById(R.id.luggageNameEdit);
        final EditText luggageWeightEdit = luggageEditView.findViewById(R.id.luggageWeight);
        categoryNames = luggageEditView.findViewById(R.id.categoryNames);
        categoryDbModel = new LuggageCategoryDbModel(getActivity());
        final Locale currentLocale = LocaleHelper.investigateLocale(this.getContext());

        try {
            Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception exception) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (null != luggageEntity) {
            luggageNameEdit.setText(luggageEntity.getName());
        }

        luggageCategoryEntities = categoryDbModel.load();

        ArrayAdapter<LuggageCategoryEntity> spinnerArrayAdapter = new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_dropdown_item_1line,
            luggageCategoryEntities
        );

        categoryNames.setAdapter(spinnerArrayAdapter);
        categoryNames.setMaxLines(1);

        TextSize.convert(getActivity(), categoryNames, TextSize.TEXT_TYPE_NORMAL);
        categoryNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                categoryNames.showDropDown();
            }
        });

        categoryNames.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedCategory = 0;
                categoryNames.setText("");
                categoryNames.showDropDown();
                return true;
            }
        });

        categoryNames.post(new Runnable() {
            public void run() {
                categoryNames.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence category, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable categoryName) {
                        selectedCategory = 0;
                        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
                            if (luggageCategoryEntity.getName().equals(categoryName.toString())) {
                                selectedCategory = luggageCategoryEntity.getId();
                                break;
                            }
                        }
                    }
                });
            }
        });

        considerSelectedCategory();

        if (null != luggageEntity) {
            luggageWeightEdit.setText(String.format(currentLocale, "%.0f", luggageEntity.getWeight()));
        }

        dialog.setTitle(R.string.title_luggage_edit);
        dialog.setView(luggageEditView);
        final Locale locale = LocaleHelper.investigateLocale(this.getContext());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 <= selectedCategory) {
                    String luggageName = luggageNameEdit.getText().toString();
                    double luggageWeight;

                    if (luggageName.isEmpty()
                        || 0 == luggageWeightEdit.length()
                    ) {
                        showAlertNotAllNeededFieldFilled();
                    } else {
                        luggageWeight = Double.parseDouble(luggageWeightEdit.getText().toString());
                        LuggageDbModel luggageDbModel = new LuggageDbModel(getActivity());
                        LuggageEntity luggageEntityInDb = new LuggageEntity(luggageName, selectedCategory, luggageWeight);
                        luggageEntityInDb = luggageDbModel.checkLuggageAlreadyExists(luggageEntityInDb);

                        if ((null != luggageEntityInDb
                                && null != luggageEntity
                                && luggageEntityInDb.getId() != luggageEntity.getId()
                            ) || (
                                null != luggageEntityInDb
                                && null == luggageEntity
                            )
                        ) {
                            final CustomDialog alertDialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
                            alertDialog.setTitle(R.string.title_error);
                            alertDialog.setMessage(String.format(locale, getResources().getString(R.string.error_luggage_already_exists), luggageName, luggageEntityInDb.getCategoryEntity().getName()));
                            alertDialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();
                        } else {
                            try {
                                if (null != luggageEntity) {
                                    luggageEntity.setName(luggageName);
                                    luggageEntity.setWeight(luggageWeight);
                                    luggageEntity.setCategoryId(selectedCategory);

                                    luggageDbModel.update(luggageEntity);
                                } else {
                                    luggageEntity = new LuggageEntity();
                                    luggageEntity.setName(luggageName);
                                    luggageEntity.setWeight(luggageWeight);
                                    luggageEntity.setCategoryId(selectedCategory);

                                    luggageDbModel.insert(luggageEntity);
                                }

                                getActivity().finish();
                                getActivity().startActivity(getActivity().getIntent());
                                Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
                            } catch (Exception exception) {
                                Toast.makeText(getContext(), getResources().getString(R.string.error_saving), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    showAlertBoxNoCategorySelected();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.create();

        return dialog;
    }

    private void considerSelectedCategory() {
        if (0 < selectedCategory) {
            LuggageCategoryEntity currentCategoryEntity = categoryDbModel.findCategoryById(selectedCategory);
            categoryNames.setText(currentCategoryEntity.getName());
        } else if (null != luggageEntity){
            selectedCategory = luggageEntity.getCategoryId();
            categoryNames.setText(luggageEntity.getCategoryEntity().getName());
        }
    }

    private void showAlertBoxNoCategorySelected() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("luggage_edit_dialog_alert");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final AlertNoCategorySelected fragment = new AlertNoCategorySelected();

        fragment.show(ft, "luggage_edit_dialog_alert");
    }

    private void showAlertNotAllNeededFieldFilled() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("luggage_edit_dialog_alert");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final AlertNotAllNeededFieldsFilled fragment = new AlertNotAllNeededFieldsFilled();

        fragment.show(ft, "luggage_edit_dialog_alert");
    }
}
