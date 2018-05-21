package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

public class CategoryNewDialogFragment extends DialogFragment {

    public static CategoryNewDialogFragment newInstance() {
        return new CategoryNewDialogFragment();
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        final View categoryEditView = View.inflate(getContext(), R.layout.activity_category_edit_dialog, null);

        final EditText inputCategoryName = categoryEditView.findViewById(R.id.inputCategoryName);

        builder.setTitle(R.string.title_luggage_new);
        builder.setView(categoryEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            String categoryName = inputCategoryName.getText().toString();
            LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity(categoryName);

            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(getActivity(), null, null, 1);
            luggageCategoryDbModel.insert(luggageCategoryEntity);

            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
            }
        });

        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        setRetainInstance(true);
        builder.setOnDismissListener(this);

        return builder.create();
    }
}
