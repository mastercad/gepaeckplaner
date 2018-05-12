package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.AbstractActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.CategoryDeleteOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.CategoryEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.CategoryEntityOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.CategoryEntityOnLongClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageCategoryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;

public class CategoryActivity extends AbstractActivity {

    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ImageButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryEditDialog editDialog = new CategoryEditDialog(CategoryActivity.this);
                editDialog.showNewDialog(view);
            }
        });

        loadCategories();
    }

    private CategoryActivity loadCategories() {
        TableLayout table = findViewById(R.id.categoriesTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_categories);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();
        Resources.Theme currentTheme = getTheme();

        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
            TableRow row = new TableRow(this);
            row.setWeightSum(1);

            TableRow.LayoutParams lp;
//            lp.weight = 1;

            TextView idLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            idLabel.setText(Long.toString(luggageCategoryEntity.getId()));
            idLabel.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
            idLabel.setLayoutParams(lp);
            row.addView(idLabel);

            TextView nameLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f);
            nameLabel.setText(luggageCategoryEntity.getName());
            nameLabel.setGravity(Gravity.CENTER_VERTICAL);
            nameLabel.setLayoutParams(lp);
            row.addView(nameLabel);

            TextView deleteBtn = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
            deleteBtn.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                this.getResources().getDrawable(android.R.drawable.ic_menu_delete, currentTheme),
                null
            );
            deleteBtn.setScaleX((float)0.8);
            deleteBtn.setScaleY((float)0.8);
            deleteBtn.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
            deleteBtn.setLayoutParams(lp);

            deleteBtn.setOnClickListener(new CategoryDeleteOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new CategoryEntityOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.setOnLongClickListener(new CategoryEntityOnLongClickListener(CategoryActivity.this, luggageCategoryEntity));
        }
        return this;
    }
}
