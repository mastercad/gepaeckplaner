package de.byte_artist.luggage_planner.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import de.byte_artist.luggage_planner.AbstractActivity;
import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.dialog.CategoryNewDialogFragment;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.listener.CategoryDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.CategoryEntityOnClickListener;
import de.byte_artist.luggage_planner.listener.CategoryEntityOnLongClickListener;
import de.byte_artist.luggage_planner.service.TextSize;

public class CategoryActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ImageButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("category_new_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                CategoryNewDialogFragment alertDialog = CategoryNewDialogFragment.newInstance();
                alertDialog.show(ft, "category_new_dialog");
            }
        });

        refresh();
    }

    protected void refresh() {
        loadCategories();
    }

    private void loadCategories() {
        TableLayout table = findViewById(R.id.categoriesTable);
        table.removeAllViews();
        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_categories);
        TextSize.convert(CategoryActivity.this, title, TextSize.TEXT_TYPE_HEADER);
        title.setMaxLines(1);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this);
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();

        Locale currentLocale = getResources().getConfiguration().locale;

        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
            TableRow row = new TableRow(this);
            row.setWeightSum(1);

            TableRow.LayoutParams lp;

            TextView idLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            idLabel.setText(String.format(currentLocale, "%d", luggageCategoryEntity.getId()));
            TextSize.convert(this, idLabel, TextSize.TEXT_TYPE_NORMAL);
            idLabel.setMaxLines(1);
            idLabel.setPadding(10, 0, 0, 0);
            idLabel.setGravity(Gravity.START);
            idLabel.setWidth(0);
            idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            idLabel.setLayoutParams(lp);
            row.addView(idLabel);

            TextView nameLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f);
            nameLabel.setText(luggageCategoryEntity.getName());
            TextSize.convert(this, nameLabel, TextSize.TEXT_TYPE_NORMAL);
            nameLabel.setMaxLines(1);
            nameLabel.setGravity(Gravity.CENTER_VERTICAL);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            nameLabel.setLayoutParams(lp);
            row.addView(nameLabel);

            ImageView deleteBtn = new ImageView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
            deleteBtn.setPadding(0, 2, 0, 2);
            deleteBtn.setBackgroundColor(Color.WHITE);
            deleteBtn.setLayoutParams(lp);
            deleteBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
            deleteBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            deleteBtn.setOnClickListener(new CategoryDeleteOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new CategoryEntityOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.setOnLongClickListener(new CategoryEntityOnLongClickListener(CategoryActivity.this, luggageCategoryEntity));
        }
    }
}
