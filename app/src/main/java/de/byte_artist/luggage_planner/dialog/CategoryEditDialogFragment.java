package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryEditDialogFragment extends DialogFragment {

    private LuggageCategoryEntity luggageCategoryEntity;

    public static CategoryEditDialogFragment newInstance(LuggageCategoryEntity luggageCategoryEntity) {
        CategoryEditDialogFragment fragment = new CategoryEditDialogFragment();
        fragment.setComplexVariable(luggageCategoryEntity);

        return fragment;
    }

    private void setComplexVariable(LuggageCategoryEntity luggageCategoryEntity) {
        this.luggageCategoryEntity = luggageCategoryEntity;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        final View categoryEditView = View.inflate(getContext(), R.layout.activity_category_edit_dialog, null);

        final EditText inputCategoryName = categoryEditView.findViewById(R.id.inputCategoryName);
        inputCategoryName.setText(luggageCategoryEntity.getName());

        dialog.setTitle(R.string.title_luggage_edit);
        dialog.setView(categoryEditView);

        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = inputCategoryName.getText().toString();
                luggageCategoryEntity.setName(categoryName);

                LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(getActivity());
                luggageCategoryDbModel.update(luggageCategoryEntity);

                getActivity().finish();
                getActivity().startActivity(getActivity().getIntent());
                Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
            }
        });

        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.create();

        return dialog;
    }
}
