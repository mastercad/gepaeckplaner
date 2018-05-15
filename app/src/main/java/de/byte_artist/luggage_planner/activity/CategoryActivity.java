package de.byte_artist.luggage_planner.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import de.byte_artist.luggage_planner.AbstractActivity;
import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.dialog.CategoryEditDialog;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.listener.CategoryDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.CategoryEntityOnClickListener;
import de.byte_artist.luggage_planner.listener.CategoryEntityOnLongClickListener;

public class CategoryActivity extends AbstractActivity {

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

    private void loadCategories() {
        TableLayout table = findViewById(R.id.categoriesTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_categories);
        title.setMaxLines(1);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();

        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
            TableRow row = new TableRow(this);
            row.setWeightSum(1);

            TableRow.LayoutParams lp;

            TextView idLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            idLabel.setText(String.format(Locale.getDefault(), "%d", luggageCategoryEntity.getId()));
            idLabel.setMaxLines(1);
            idLabel.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
            idLabel.setLayoutParams(lp);
            row.addView(idLabel);

            TextView nameLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f);
            nameLabel.setText(luggageCategoryEntity.getName());
            nameLabel.setMaxLines(1);
            nameLabel.setGravity(Gravity.CENTER_VERTICAL);
            nameLabel.setLayoutParams(lp);
            row.addView(nameLabel);

            TextView deleteBtn = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
            lp.setMargins(0, -8, 0, -8);
            deleteBtn.setLayoutParams(lp);
            deleteBtn.setMaxLines(1);
            deleteBtn.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_delete));
            deleteBtn.setGravity(Gravity.END);
            deleteBtn.setScaleX(0.8f);
            deleteBtn.setScaleY(0.8f);
            deleteBtn.setOnClickListener(new CategoryDeleteOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new CategoryEntityOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.setOnLongClickListener(new CategoryEntityOnLongClickListener(CategoryActivity.this, luggageCategoryEntity));
        }
    }
}
