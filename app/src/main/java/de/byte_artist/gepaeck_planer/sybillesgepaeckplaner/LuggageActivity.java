package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class LuggageActivity extends AppCompatActivity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.luggage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mainMenuMain:
                Intent mainIntent = new Intent(LuggageActivity.this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.mainMenuCategories:
                Intent categoryIntent = new Intent(LuggageActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.mainMenuPackingLists:
                Intent packingListIntent = new Intent(LuggageActivity.this, PackingListActivity.class);
                startActivity(packingListIntent);
                break;
            case R.id.mainMenuExit:
                finishAffinity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private LuggageActivity loadLuggage() {
        TableLayout table = findViewById(R.id.luggageTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Gepäckstücke");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this, null, null, 1);
        ArrayList<LuggageEntity> luggageEntities = luggageDbModel.load();

        String tempCategory = null;

        for (LuggageEntity luggageEntity : luggageEntities) {
            boolean categoryChanged = false;
            String currentCategory = luggageEntity.getCategoryEntity().getName().toString();

            if (null == tempCategory
                || !tempCategory.equals(currentCategory)
            ) {
                if (null != tempCategory) {
                    categoryChanged = true;
                }
                tempCategory = currentCategory;

                TableRow categoryRow = new TableRow(this);
                TextView categoryHeadingLabel = new TextView(this);
                categoryHeadingLabel.setText(currentCategory);
                categoryHeadingLabel.setTextSize(14);
                categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);

                categoryRow.addView(categoryHeadingLabel);
                table.addView(categoryRow);
            }

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT,4f));

            TextView idLabel = new TextView(this);
            idLabel.setText(""+ luggageEntity.getCategoryId());
            idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
            idLabel.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
            idLabel.setGravity(Gravity.START);
            idLabel.setWidth(0);
            idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView nameLabel = new TextView(this);
            nameLabel.setText(luggageEntity.getName());
            nameLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 1f));
            nameLabel.setGravity(Gravity.START);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView weightLabel = new TextView(this);
            weightLabel.setText(""+ luggageEntity.getWeight());
            weightLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 1f));
            weightLabel.setGravity(Gravity.END);
            weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            row.addView(idLabel);
            row.addView(nameLabel);
            row.addView(weightLabel);

            table.addView(row);

            if (categoryChanged) {
                TableRow addLuggageRow = new TableRow(this);
                TextView emptyLabel = new TextView(this);

                emptyLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20, 1f));

                addLuggageRow.addView(emptyLabel);
                table.addView(addLuggageRow);
            }

            row.setOnClickListener(new LuggageEntityOnClickListener(luggageEntity));
            row.setOnLongClickListener(new LuggageEntityOnLongClickListener(LuggageActivity.this, luggageEntity));
        }

        return this;
    }
}
