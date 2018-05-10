package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton btnAddPackingListEntry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** @TODO delete entfernen vor release! **/
/*
        String filePathName = getFilesDir().getAbsolutePath()+"/../databases/luggage.db";
        File file = new File(filePathName);
        boolean result = file.delete();

        btnAddPackingListEntry = findViewById(R.id.btnAddEntryToCurrentPackingList);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity("Badesachen");

        luggageCategoryDbModel.insert(luggageCategoryEntity);

        LuggageListDbModel luggageListDbModel = new LuggageListDbModel(this, null, null, 1);
        LuggageListEntity luggageListEntity = new LuggageListEntity("Urlaub Spreewald", "2018-08-04");

        luggageListDbModel.insert(luggageListEntity);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this, null, null, 1);
        LuggageEntity luggageEntity = new LuggageEntity("Badelatschen", luggageCategoryEntity.getId(), 120);

        luggageDbModel.insert(luggageEntity);
        PackingListDbModel packingListDbModel = new PackingListDbModel(this, null, null, 1);
        PackingListEntity packingListEntity = new PackingListEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);

        packingListDbModel.insert(packingListEntity);

*/
        this.prepareButtonActions();
        this.fillTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mainMenuCategories:
                finish();
                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.mainMenuLuggage:
                finish();
                Intent luggageIntent = new Intent(MainActivity.this, LuggageActivity.class);
                startActivity(luggageIntent);
                break;
            case R.id.mainMenuPackingLists:
                finish();
                Intent packingListIntent = new Intent(MainActivity.this, PackingListActivity.class);
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

    private void prepareButtonActions() {
        btnAddPackingListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
            alertDialog.setTitle("hi");
            alertDialog.setMessage("this is my app");

            alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            });

            alertDialog.show();  //<-- See This!
            }
        });
    }

    private void fillTable() {
        TableLayout table = findViewById(R.id.PackagesTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Packliste");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        int weightSum = 0;

        PackingListDbModel packingListDbModel = new PackingListDbModel(this, null, null, 1);
        ArrayList<PackingListEntity> luggageListEntryCollection = packingListDbModel.load();

        int rowCount = 1;

        String tempCategory = null;

        for (PackingListEntity packingListEntity : luggageListEntryCollection) {
            boolean categoryChanged = false;

            if (1 == rowCount) {
                title.setText(title.getText()+" für "+ packingListEntity.getLuggageListEntity().getName());
            }
            String currentCategory = packingListEntity.getLuggageEntity().getCategoryEntity().getName();

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
            idLabel.setText(""+ packingListEntity.getLuggageEntity().getCategoryId());
            idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
            idLabel.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
            idLabel.setGravity(Gravity.START);
            idLabel.setWidth(0);
            idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView nameLabel = new TextView(this);
            nameLabel.setText(packingListEntity.getLuggageEntity().getName());
            nameLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 1f));
            nameLabel.setGravity(Gravity.START);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView weightLabel = new TextView(this);
            weightLabel.setText(""+ packingListEntity.getLuggageEntity().getWeight());
            weightLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 1f));
            weightLabel.setGravity(Gravity.END);
            weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            row.addView(idLabel);
            row.addView(nameLabel);
            row.addView(weightLabel);

            row.setOnClickListener(new PackingListEntityOnClickListener(packingListEntity));
            row.setOnLongClickListener(new PackingListEntityOnLongClickListener(packingListEntity));

            table.addView(row);

            weightSum += (packingListEntity.getLuggageEntity().getWeight());

            if (categoryChanged) {
                TableRow addLuggageRow = new TableRow(this);
                TextView emptyLabel = new TextView(this);

                emptyLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20, 1f));

                addLuggageRow.addView(emptyLabel);
                table.addView(addLuggageRow);
            }
            ++rowCount;
        }

        TableRow rowSummary = new TableRow(this);
        rowSummary.setGravity(Gravity.END);

        TextView summary = new TextView(this);
        summary.setText(""+weightSum);
        summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        summary.setGravity(Gravity.END);
        summary.setTypeface(Typeface.SERIF, Typeface.BOLD);

//        TableRow.LayoutParams summaryParams = new TableRow.LayoutParams();
//        summaryParams.span = 3;
//        summaryParams.column = 3;

//        rowSummary.addView(summary, summaryParams);
        rowSummary.addView(summary);
        table.addView(rowSummary);
    }
}
