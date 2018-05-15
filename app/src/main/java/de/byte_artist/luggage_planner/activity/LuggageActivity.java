package de.byte_artist.luggage_planner.activity;

import android.content.res.Resources;
import android.graphics.Color;
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
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.dialog.LuggageEditDialog;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.listener.LuggageDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.LuggageEntityOnClickListener;
import de.byte_artist.luggage_planner.listener.LuggageEntityOnLongClickListener;

public class LuggageActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage);

        ImageButton btnAddCategory = findViewById(R.id.btnAddLuggage);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LuggageEditDialog editDialog = new LuggageEditDialog(LuggageActivity.this);
                editDialog.showNewDialog(view);
            }
        });

        loadLuggage();
    }

    private void loadLuggage() {
        TableLayout table = findViewById(R.id.luggageTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_luggage_list);
        title.setMaxLines(1);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this, null, null, 1);
        ArrayList<LuggageEntity> luggageEntities = luggageDbModel.load();

        long tempCategory = -1;
        Resources.Theme currentTheme = getTheme();

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
                categoryHeadingLabel.setTextSize(14);
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
            String formattedEntryId = String.format(Locale.getDefault(), "%d%02d", luggageEntity.getCategoryId(), luggageEntity.getCount());
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            idLabel.setText(formattedEntryId);
            idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
            idLabel.setMaxLines(1);
            idLabel.setGravity(Gravity.START);
            idLabel.setWidth(0);
            idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            idLabel.setLayoutParams(lp);
            row.addView(idLabel);

            TextView nameLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            nameLabel.setLayoutParams(lp);
            nameLabel.setMaxLines(1);
            nameLabel.setText(luggageEntity.getName());
            nameLabel.setGravity(Gravity.START);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            row.addView(nameLabel);

            TextView weightLabel = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            weightLabel.setLayoutParams(lp);
            weightLabel.setMaxLines(1);
            weightLabel.setText(String.format(Locale.getDefault(), "%d g", luggageEntity.getWeight()));
            weightLabel.setGravity(Gravity.END);
            weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            row.addView(weightLabel);

            TextView deleteBtn = new TextView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
            lp.setMargins(0, -8, 0, -8);
            deleteBtn.setLayoutParams(lp);
            deleteBtn.setMaxLines(1);
            deleteBtn.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
            deleteBtn.setGravity(Gravity.END);
            deleteBtn.setScaleX(0.8f);
            deleteBtn.setScaleY(0.8f);
            deleteBtn.setOnClickListener(new LuggageDeleteOnClickListener(LuggageActivity.this, luggageEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new LuggageEntityOnClickListener(luggageEntity));
            row.setOnLongClickListener(new LuggageEntityOnLongClickListener(LuggageActivity.this, luggageEntity));
        }

    }
}
