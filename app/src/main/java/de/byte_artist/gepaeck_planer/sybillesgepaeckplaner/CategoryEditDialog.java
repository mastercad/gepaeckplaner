package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CategoryEditDialog extends AppCompatActivity {

    private CategoryActivity categoryActivity = null;

    CategoryEditDialog(CategoryActivity categoryActivity) {
        this.categoryActivity = categoryActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit_dialog);
    }

    public CategoryEditDialog showEditDialog(View view, final LuggageCategoryEntity luggageCategoryEntity) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View currentView = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View categoryEditView = inflater.inflate(R.layout.activity_category_edit_dialog, null);

        final EditText inputCategoryName = categoryEditView.findViewById(R.id.inputCategoryName);
        inputCategoryName.setText(luggageCategoryEntity.getName());

        builder.setTitle(R.string.title_luggage_edit);
        builder.setView(categoryEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            String categoryName = inputCategoryName.getText().toString();
            luggageCategoryEntity.setName(categoryName);

            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(currentView.getContext(), null, null, 1);
            luggageCategoryDbModel.update(luggageCategoryEntity);

            categoryActivity.recreate();
            }
        });

        builder.create().show();
        return this;
    }

    public CategoryEditDialog showNewDialog(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View currentView = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View categoryEditView = inflater.inflate(R.layout.activity_category_edit_dialog, null);

        final EditText inputCategoryName = categoryEditView.findViewById(R.id.inputCategoryName);

        builder.setTitle(R.string.title_luggage_new);
        builder.setView(categoryEditView);

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            String categoryName = inputCategoryName.getText().toString();
            LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity(categoryName);

            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(currentView.getContext(), null, null, 1);
            luggageCategoryDbModel.insert(luggageCategoryEntity);

            categoryActivity.recreate();
            }
        });

        builder.create().show();
        return this;
    }
}
