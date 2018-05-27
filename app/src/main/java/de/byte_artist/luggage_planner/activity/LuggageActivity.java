package de.byte_artist.luggage_planner.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
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
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.dialog.LuggageNewDialogFragment;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.listener.LuggageDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.LuggageEntityOnTouchListener;
import de.byte_artist.luggage_planner.service.TextSize;

public class LuggageActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage);

        ImageButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("luggage_new_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                LuggageNewDialogFragment alertDialog = LuggageNewDialogFragment.newInstance();

                alertDialog.show(ft, "luggage_new_dialog");
            }
        });

        refresh();
    }

    public void refresh() {
        loadLuggage();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void loadLuggage() {
        TableLayout table = findViewById(R.id.luggageTable);
        table.removeAllViews();
        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_luggage_list);
        TextSize.convert(this, title, TextSize.TEXT_TYPE_HEADER);
        title.setMaxLines(1);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this);
        ArrayList<LuggageEntity> luggageEntities = luggageDbModel.load();

        long tempCategory = -1;

        Locale currentLocale = getResources().getConfiguration().locale;

        for (LuggageEntity luggageEntity : luggageEntities) {
            long currentCategory = luggageEntity.getCategoryEntity().getId();

            if (-1 == tempCategory
                || tempCategory != currentCategory
            ) {
                if (-1 != tempCategory) {
                    TableRow addLuggageRow = new TableRow(this);
                    TextView emptyLabel = new TextView(this);

                    emptyLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20, 1f));

                    addLuggageRow.addView(emptyLabel);
                    table.addView(addLuggageRow);
                }
                tempCategory = currentCategory;

                TableRow categoryRow = new TableRow(this);
                TextView categoryHeadingLabel = new TextView(this);
                categoryHeadingLabel.setText(luggageEntity.getCategoryEntity().getName());
                TextSize.convert(this, categoryHeadingLabel, TextSize.TEXT_TYPE_NORMAL);
                categoryHeadingLabel.setMaxLines(1);
                categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);

                categoryRow.addView(categoryHeadingLabel);
                table.addView(categoryRow);
            }

            TableRow row = new TableRow(this);
            row.setWeightSum(1);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT,4f));

            TableRow.LayoutParams lp;
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
            lp.setMargins(0, 0, 0, 0);
            row.setLayoutParams(lp);

            TextView idLabel = new TextView(this);
            String formattedEntryId = String.format(currentLocale, "%d%02d", luggageEntity.getCategoryId(), luggageEntity.getCount());
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            idLabel.setText(formattedEntryId);
            TextSize.convert(this, idLabel, TextSize.TEXT_TYPE_NORMAL);
            if (!luggageEntity.isActive()) {
                idLabel.setPaintFlags(idLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
            idLabel.setMaxLines(1);
            idLabel.setPadding(10, 0, 0, 0);
            idLabel.setGravity(Gravity.START);
            idLabel.setWidth(0);
            idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            idLabel.setLayoutParams(lp);
            row.addView(idLabel);

            TextView nameLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            if (!luggageEntity.isActive()) {
                nameLabel.setPaintFlags(nameLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            nameLabel.setLayoutParams(lp);
            nameLabel.setMaxLines(1);
            nameLabel.setGravity(Gravity.START);
            nameLabel.setText(luggageEntity.getName());
            TextSize.convert(this, nameLabel, TextSize.TEXT_TYPE_NORMAL);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            row.addView(nameLabel);

            TextView weightLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            if (!luggageEntity.isActive()) {
                weightLabel.setPaintFlags(weightLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            weightLabel.setLayoutParams(lp);
            weightLabel.setMaxLines(1);
            weightLabel.setText(String.format(currentLocale, "%.0f g", luggageEntity.getWeight()));
            TextSize.convert(this, weightLabel, TextSize.TEXT_TYPE_NORMAL);
            weightLabel.setGravity(Gravity.END);
            weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            row.addView(weightLabel);

            ImageView deleteBtn = new ImageView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
            deleteBtn.setPadding(0, 2, 0, 2);
            deleteBtn.setBackgroundColor(Color.WHITE);
            deleteBtn.setLayoutParams(lp);
            deleteBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
            deleteBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            deleteBtn.setOnClickListener(new LuggageDeleteOnClickListener(LuggageActivity.this, luggageEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnTouchListener(new LuggageEntityOnTouchListener(LuggageActivity.this, luggageEntity));
        }
    }
}
