package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageCategoryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.LuggageDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListEntryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListOnLongClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;

public class MainActivity extends AppCompatActivity {

    ImageButton btnAddPackingListEntry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btnAddPackingListEntry = findViewById(R.id.btnAddEntryToCurrentPackingList);
        /** @TODO delete entfernen vor release! **/
/*
        String filePathName = getFilesDir().getAbsolutePath()+"/../databases/luggage.db";
        File file = new File(filePathName);
        boolean result = file.delete();

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity("Badesachen");

        luggageCategoryDbModel.insert(luggageCategoryEntity);

        PackingListDbModel luggageListDbModel = new PackingListDbModel(this, null, null, 1);
        PackingListEntity luggageListEntity = new PackingListEntity("Urlaub Spreewald", "2018-08-04");

        luggageListDbModel.insert(luggageListEntity);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this, null, null, 1);
        LuggageEntity luggageEntity = new LuggageEntity("Badelatschen", luggageCategoryEntity.getId(), 120);

        luggageDbModel.insert(luggageEntity);
        PackingListEntryDbModel packingListDbModel = new PackingListEntryDbModel(this, null, null, 1);
        PackingListEntryEntity packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);

        packingListDbModel.insert(packingListEntity);
*/
//        this.prepareButtonActions();
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

    private void fillTable() {
        TableLayout table = findViewById(R.id.PackagesTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_packing_list);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        int weightSum = 0;

        PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(this, null, null, 1);
        ArrayList<PackingListEntryEntity> luggageListEntryCollection = packingListEntryDbModel.load();

        int rowCount = 1;

        long tempCategory = -1;

        for (PackingListEntryEntity packingListEntryEntity : luggageListEntryCollection) {

            if (1 == rowCount) {
                title.setText(title.getText()+" "+getResources().getText(R.string.text_for)+" "+ packingListEntryEntity.getPackingListEntity().getName());
            }

            long currentCategory = packingListEntryEntity.getLuggageEntity().getCategoryEntity().getId();

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
                categoryHeadingLabel.setText(packingListEntryEntity.getLuggageEntity().getCategoryEntity().getName());
                categoryHeadingLabel.setTextSize(14);
                categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);

                categoryRow.addView(categoryHeadingLabel);
                table.addView(categoryRow);
            }

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT,4f));

            TextView idLabel = new TextView(this);
            String formattedEntryId = String.format(Locale.GERMANY, "%d%02d", packingListEntryEntity.getLuggageEntity().getCategoryId(), packingListEntryEntity.getLuggageEntity().getCount());
            idLabel.setText(formattedEntryId);
            idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
            idLabel.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
            idLabel.setGravity(Gravity.START);
            idLabel.setWidth(0);
            idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView nameLabel = new TextView(this);
            nameLabel.setText(packingListEntryEntity.getLuggageEntity().getName());
            nameLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 1f));
            nameLabel.setGravity(Gravity.START);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            TextView weightLabel = new TextView(this);
            weightLabel.setText(Integer.toString(packingListEntryEntity.getLuggageEntity().getWeight()));
            weightLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 1f));
            weightLabel.setGravity(Gravity.END);
            weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

            row.addView(idLabel);
            row.addView(nameLabel);
            row.addView(weightLabel);

//            row.setOnClickListener(new PackingListOnClickListener(PackingListActivity, packingListEntryEntity.getPackingListEntity()));
//            row.setOnLongClickListener(new PackingListOnLongClickListener(PackingListActivity.class, packingListEntryEntity.getPackingListEntity()));

            table.addView(row);

            weightSum += (packingListEntryEntity.getLuggageEntity().getWeight());

            ++rowCount;
        }

        TableRow addLuggageRow = new TableRow(this);
        TextView emptyLabel = new TextView(this);

        emptyLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20, 1f));

        addLuggageRow.addView(emptyLabel);
        table.addView(addLuggageRow);

        TableRow rowSummary = new TableRow(this);
        rowSummary.setGravity(Gravity.END);

        TextView summary = new TextView(this);
        summary.setText(Integer.toString(weightSum));
        summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        summary.setGravity(Gravity.END);
        summary.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowSummary.addView(summary);
        table.addView(rowSummary);
    }
}
