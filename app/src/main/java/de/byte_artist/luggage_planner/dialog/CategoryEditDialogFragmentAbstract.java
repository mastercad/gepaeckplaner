package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryEditDialogFragmentAbstract extends DialogFragment {

    private LuggageCategoryEntity luggageCategoryEntity;

    public void setComplexVariable(LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);
        final View categoryEditView = View.inflate(getContext(), R.layout.activity_category_edit_dialog, null);
        final Button btnSave = categoryEditView.findViewById(R.id.btnSave);
        final Button btnCancel = categoryEditView.findViewById(R.id.btnCancel);
        final EditText inputCategoryName = categoryEditView.findViewById(R.id.inputCategoryName);

        if (null != luggageCategoryEntity) {
            inputCategoryName.setText(luggageCategoryEntity.getName());
        }

        dialog.setTitle(R.string.title_luggage_edit);
        dialog.setView(categoryEditView);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = getResources().getConfiguration().locale;

                LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(getActivity());
                String categoryName = inputCategoryName.getText().toString();
                LuggageCategoryEntity luggageCategoryEntityInDb = new LuggageCategoryEntity(categoryName);
                luggageCategoryEntityInDb = luggageCategoryDbModel.checkLuggageCategoryAlreadyExists(luggageCategoryEntityInDb);

                try {
                    Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                } catch (Exception exception) {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                if ((null != luggageCategoryEntityInDb
                        && null != luggageCategoryEntity
                        && luggageCategoryEntityInDb.getId() != luggageCategoryEntity.getId()
                    ) || (
                        null != luggageCategoryEntityInDb
                        && null == luggageCategoryEntity
                    )
                ) {
                    final CustomDialog alertDialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_ALERT);
                    alertDialog.setTitle(R.string.title_error);
                    alertDialog.setMessage(String.format(locale, getResources().getString(R.string.error_category_already_exists), categoryName));
                    alertDialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                } else {
                    if (null != luggageCategoryEntity) {
                        luggageCategoryEntity.setName(categoryName);

                        luggageCategoryDbModel.update(luggageCategoryEntity);
                    } else {
                        luggageCategoryEntity = new LuggageCategoryEntity(categoryName);

                        luggageCategoryDbModel.insert(luggageCategoryEntity);
                    }

                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                    Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
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
}
