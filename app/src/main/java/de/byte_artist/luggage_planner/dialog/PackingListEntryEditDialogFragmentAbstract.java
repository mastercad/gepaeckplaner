package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
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
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;
import de.byte_artist.luggage_planner.helper.LocaleHelper;
import de.byte_artist.luggage_planner.service.TextSize;

abstract public class PackingListEntryEditDialogFragmentAbstract extends DialogFragment {

    private long selectedLuggage = 0;
    private long selectedCategory = 0;

    // selected luggage
    private PackingListEntryEntity packingListEntryEntity = null;
    // known categories
    private ArrayList<LuggageCategoryEntity> luggageCategoryEntities = null;
    // luggage in categories
    private ArrayList<LuggageEntity> currentLuggageEntities = null;

    private AutoCompleteTextView luggageNames = null;
    private AutoCompleteTextView categoryNames = null;
    private LuggageDbModel luggageDbModel = null;
    private LuggageCategoryDbModel categoryDbModel = null;
    private PackingListEntryDbModel packingListEntryDbModel = null;
    private EditText luggageCount = null;

    private long packingListFk;

    public void setComplexVariable(PackingListEntryEntity packingListEntryEntity) {
        this.packingListEntryEntity = packingListEntryEntity;
    }

    public void setComplexVariable(long packingListFk) {
        this.packingListFk = packingListFk;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        luggageDbModel = new LuggageDbModel(getActivity());
        categoryDbModel = new LuggageCategoryDbModel(getActivity());
        packingListEntryDbModel = new PackingListEntryDbModel(getActivity());

        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        try {
            Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception exception) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        ViewGroup packingListEntryEditView = (ViewGroup) View.inflate(getContext(), R.layout.activity_packing_list_entry_edit_dialog, null);

        if (null == packingListEntryEntity) {
            dialog.setTitle(R.string.title_luggage_new);
        } else {
            dialog.setTitle(R.string.title_luggage_edit);
        }
        dialog.setView(packingListEntryEditView);

        categoryNames = packingListEntryEditView.findViewById(R.id.categoryNames);
        luggageNames = packingListEntryEditView.findViewById(R.id.luggageNames);
        luggageCount = packingListEntryEditView.findViewById(R.id.inputPackingListEntryCount);

        Button btnSave = packingListEntryEditView.findViewById(R.id.btnSave);
        Button btnCancel = packingListEntryEditView.findViewById(R.id.btnCancel);

        fillCategoryDropDown();

        if (null == luggageCategoryEntities
            || 0 == luggageCategoryEntities.size()
        ) {
            showAlertBoxNoCategoryExists();
        }

        fillLuggageDropDown();
        fillLuggageCountEditField();

        categoryNames.setMaxLines(1);

        considerSelectedCategory();

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
                selectedLuggage = 0;
                categoryNames.setText("");
                // first delete old text content from luggage
                // then reset dropdown content from luggage
                if (null != luggageNames.getAdapter()) {
                    luggageNames.setText("");
                    luggageNames.setAdapter(null);
                }
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
//                        long categoryId = 0;
                        selectedCategory = 0;
                        selectedLuggage = 0;
                        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
                            if (luggageCategoryEntity.getName().equals(categoryName.toString())) {
                                selectedCategory = luggageCategoryEntity.getId();
                                break;
                            }
                        }

                        if (0 != selectedCategory) {
                            final LuggageDbModel luggageDbModel = new LuggageDbModel(getContext());
                            currentLuggageEntities = luggageDbModel.findLuggageByCategoryId(selectedCategory, false);

                            final ArrayAdapter<LuggageEntity> luggageArrayAdapter = new ArrayAdapter<>(
                                    Objects.requireNonNull(getActivity()),
                                    android.R.layout.simple_dropdown_item_1line,
                                    currentLuggageEntities
                            );
                            luggageNames.setAdapter(luggageArrayAdapter);
                            luggageNames.setEnabled(true);
                        }
                    }
                });
            }
        });

        considerSelectedLuggage();
        luggageNames.setMaxLines(1);
        TextSize.convert(getActivity(), luggageNames, TextSize.TEXT_TYPE_NORMAL);
        luggageNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (null != luggageNames.getAdapter()) {
                    luggageNames.showDropDown();
                } else {
                    Toast.makeText(getContext(), R.string.text_choose_category, Toast.LENGTH_SHORT).show();
                }
            }
        });

        luggageNames.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedLuggage = 0;
                if (null != luggageNames.getAdapter()) {
                    luggageNames.setText("");
                    luggageNames.showDropDown();
                } else if (0 == selectedCategory
                    && null != luggageCategoryEntities
                ) {
                    Toast.makeText(getContext(), R.string.text_choose_category, Toast.LENGTH_SHORT).show();
                } else if (null == currentLuggageEntities
                    || 0 == currentLuggageEntities.size()
                ) {
                    showAlertBoxNoLuggageExistsInCategory();
                }
                return true;
            }
        });

        luggageNames.post(new Runnable() {
            public void run() {
                luggageNames.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence luggageName, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable luggageName) {
                        selectedLuggage = 0;

                        if (0 == selectedCategory
                            && null != luggageCategoryEntities
                        ) {
                            showAlertBoxNoCategorySelected();
                            return;
                        }
                        if (null == luggageCategoryEntities
                            || 0 == luggageCategoryEntities.size()
                        ) {
                            showAlertBoxNoCategoryExists();
                            return;
                        }
                        if (null == currentLuggageEntities
                            || 0 == currentLuggageEntities.size()
                        ) {
                            showAlertBoxNoLuggageExistsInCategory();
                            return;
                        }
                        for (LuggageEntity luggageEntity: currentLuggageEntities) {
                            if (luggageEntity.getName().equals(luggageName.toString())) {
                                selectedLuggage = luggageEntity.getId();
                                break;
                            }
                        }
                    }
                });
            }
        });

        /*
         * save
         */
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (0 < selectedCategory
                    && 0 < selectedLuggage
                    && 0 < packingListFk
                ) {
                    PackingListEntryEntity packingListEntryEntityFromDb = packingListEntryDbModel.findLuggageInPackingList(
                        selectedLuggage,
                        packingListFk
                    );

                    if (null == packingListEntryEntityFromDb
                        || (null != packingListEntryEntity
                            && packingListEntryEntityFromDb.getId() == packingListEntryEntity.getId())
                    ) {
                        int count;
                        if (luggageCount.getText().toString().isEmpty()) {
                            showAlertNotAllNeededFieldsFilled();
                        } else {
                            count = Integer.parseInt(luggageCount.getText().toString());
                            if (null != packingListEntryEntity) {
                                packingListEntryEntity.setLuggageFk(selectedLuggage);
                                packingListEntryEntity.setLuggageListFk(packingListFk);
                                packingListEntryEntity.setCount(count);
                                packingListEntryDbModel.update(packingListEntryEntity);
                            } else {
                                packingListEntryEntity = new PackingListEntryEntity();
                                packingListEntryEntity.setLuggageFk(selectedLuggage);
                                packingListEntryEntity.setLuggageListFk(packingListFk);
                                packingListEntryEntity.setCount(count);
                                packingListEntryDbModel.insert(packingListEntryEntity);
                            }
                            Objects.requireNonNull(getActivity()).finish();
                            getActivity().startActivity(getActivity().getIntent());
                            Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showAlertLuggageAlreadyExistsInThisPackingList();
                    }
                } else if (0 == selectedCategory
                    && 0 < luggageCategoryEntities.size()
                ) {
                    showAlertBoxNoCategorySelected();
                } else if (0 == luggageCategoryEntities.size()) {
                    showAlertBoxNoCategoryExists();
                } else if (0 == selectedLuggage
                    && 0 == currentLuggageEntities.size()
                ) {
                   showAlertBoxNoLuggageExistsInCategory();
                } else if (0 == selectedLuggage) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_please_select_luggage_from_list), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.create();
        return dialog;
    }

    private void showAlertBoxNoCategorySelected() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoCategorySelected fragment = new AlertNoCategorySelected();

        fragment.show(ft, "packing_list_entry_edit_dialog");
    }

    private void showAlertBoxNoCategoryExists() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoCategoryExists fragment = new AlertNoCategoryExists();

        fragment.show(ft, "packing_list_entry_edit_dialog");
    }

    private void showAlertBoxNoLuggageExistsInCategory() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNoLuggageExistsInCategory fragment = new AlertNoLuggageExistsInCategory();

        fragment.show(ft, "packing_list_entry_edit_dialog");
    }

    private void showAlertNotAllNeededFieldsFilled() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNotAllNeededFieldsFilled alertDialog = new AlertNotAllNeededFieldsFilled();

        alertDialog.show(ft, "packing_list_entry_edit_dialog");
    }

    private void showAlertLuggageAlreadyExistsInThisPackingList() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_entry_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertLuggageAlreadyExists alertDialog = new AlertLuggageAlreadyExists();

        alertDialog.show(ft, "packing_list_entry_edit_dialog");
    }

    private void considerSelectedCategory() {
        if (0 < selectedCategory) {
            LuggageCategoryEntity currentCategoryEntity = categoryDbModel.findCategoryById(selectedCategory);
            categoryNames.setText(currentCategoryEntity.getName());
        } else if (null != packingListEntryEntity){
            selectedCategory = packingListEntryEntity.getLuggageEntity().getCategoryId();
            categoryNames.setText(packingListEntryEntity.getLuggageEntity().getCategoryEntity().getName());
        }
    }

    private void considerSelectedLuggage() {
        if (0 < selectedCategory
            && 0 < selectedLuggage
        ) {
            LuggageEntity currentLuggageEntity = luggageDbModel.findLuggageById(selectedLuggage);
            luggageNames.setText(currentLuggageEntity.getName());
        } else if (null != packingListEntryEntity
            && selectedCategory == packingListEntryEntity.getLuggageEntity().getCategoryId()
        ) {
            selectedCategory = packingListEntryEntity.getLuggageEntity().getCategoryId();
            selectedLuggage = packingListEntryEntity.getLuggageFk();
            luggageNames.setText(packingListEntryEntity.getLuggageEntity().getName());
        }
    }

    private void fillLuggageCountEditField() {
        if (null != packingListEntryEntity
            && 0 < packingListEntryEntity.getCount()
        ) {
            final Locale currentLocale = LocaleHelper.investigateLocale(Objects.requireNonNull(this.getContext()));
            luggageCount.setText(String.format(currentLocale, "%d", packingListEntryEntity.getCount()));
        }
    }

    private void fillCategoryDropDown() {
        luggageCategoryEntities = categoryDbModel.load();

        final ArrayAdapter<LuggageCategoryEntity> arrayAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),
            android.R.layout.simple_dropdown_item_1line,
            luggageCategoryEntities
        );

        categoryNames.setAdapter(arrayAdapter);
    }

    private void fillLuggageDropDown() {
        if (null != packingListEntryEntity) {
            currentLuggageEntities = luggageDbModel.findLuggageByCategoryId(
//                    packingListEntryEntity.getLuggageEntity().getCategoryEntity().getId(),
                selectedCategory,
                false
            );

            final ArrayAdapter<LuggageEntity> luggageArrayAdapter = new ArrayAdapter<>(
                    Objects.requireNonNull(getActivity()),
                android.R.layout.simple_dropdown_item_1line,
                currentLuggageEntities
            );

            luggageNames.setAdapter(luggageArrayAdapter);
        }
    }

    protected void resetSelections() {
        selectedCategory = 0;
        selectedLuggage = 0;
    }
}
