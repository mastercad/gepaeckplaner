package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton btnAddCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layoutContainer = findViewById(R.id.LayoutContainer);

        String filePathName = getFilesDir().getAbsolutePath()+"/../databases/luggage.db";
        File file = new File(filePathName);
        boolean result = file.delete();

        btnAddCategory = findViewById(R.id.btnAddCategorie);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity("Badesachen");

        luggageCategoryDbModel.insert(luggageCategoryEntity);

        LuggageListDbModel luggageListDbModel = new LuggageListDbModel(this, null, null, 1);
        LuggageListEntity luggageListEntity = new LuggageListEntity("Urlaub Spreewald", "2018-08-04");

        luggageListDbModel.insert(luggageListEntity);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this, null, null, 1);
        LuggageEntity luggageEntity = new LuggageEntity("Badelatschen", luggageCategoryEntity.getId(), 120);

        luggageDbModel.insert(luggageEntity);

        LuggageListEntryDbModel luggageListEntryDbModel = new LuggageListEntryDbModel(this, null, null, 1);
        LuggageListEntryEntity luggageListEntryEntity = new LuggageListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);

        luggageListEntryDbModel.insert(luggageListEntryEntity);

        setContentView(layoutContainer);
        this.prepareButtonActions();
        this.fillTable();
    }

    private void prepareButtonActions() {
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
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
        GridLayout table = findViewById(R.id.PackagesTable);

//        table.setStretchAllColumns(true);
//        table.setShrinkAllColumns(true);

//        TableRow rowTitle = new TableRow(this);
//        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        // title column/row
        TextView title = new TextView(this);
        title.setText("Gepäckliste");

        GridLayout.LayoutParams paramsTitle = new GridLayout.LayoutParams();
        paramsTitle.rowSpec = GridLayout.spec(0, 1);
        paramsTitle.columnSpec = GridLayout.spec(0, 4);
        paramsTitle.setGravity(Gravity.CENTER_HORIZONTAL);

//        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
//        title.setGravity(Gravity.CENTER);
//        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

//        rowTitle.addView(title);

        title.setLayoutParams(paramsTitle);
        table.addView(title);

        float weightSum = 0;

        LuggageListEntryDbModel luggageListEntryDbModel = new LuggageListEntryDbModel(this, null, null, 1);
        ArrayList<LuggageListEntryEntity> luggageListEntryCollection = luggageListEntryDbModel.load();

        int row = 0;

        for (LuggageListEntryEntity luggageListEntryEntity : luggageListEntryCollection) {
            if (0 == row) {
                title.setText(title.getText()+" für "+luggageListEntryEntity.getLuggageListEntity().getName());
            }
//            TableRow row = new TableRow(this);
            TextView idLabel = new TextView(this);
            idLabel.setText(""+luggageListEntryEntity.getLuggageEntity().getCategoryId());
            idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
//            idLabel.setBackgroundResource(R.drawable.border);

            GridLayout.LayoutParams paramsId = new GridLayout.LayoutParams();
            paramsId.rowSpec = GridLayout.spec(1, 1);
            paramsId.columnSpec = GridLayout.spec(0, 1);
            paramsId.setGravity(Gravity.START|Gravity.TOP);

            idLabel.setLayoutParams(paramsId);

            TextView nameLabel = new TextView(this);
            nameLabel.setText(luggageListEntryEntity.getLuggageEntity().getName());
//            nameLabel.setBackgroundResource(R.drawable.border);

            GridLayout.LayoutParams paramsName = new GridLayout.LayoutParams();
            paramsName.rowSpec = GridLayout.spec(1, 1);
            paramsName.columnSpec = GridLayout.spec(1, 1);
            paramsName.setGravity(Gravity.END|Gravity.TOP);

            nameLabel.setLayoutParams(paramsName);

            TextView countLabel = new TextView(this);
            countLabel.setText(""+luggageListEntryEntity.getCount());
//            countLabel.setBackgroundResource(R.drawable.border);

            GridLayout.LayoutParams paramsCount = new GridLayout.LayoutParams();
            paramsCount.rowSpec = GridLayout.spec(1, 1);
            paramsCount.columnSpec = GridLayout.spec(2, 1);
            paramsCount.setGravity(Gravity.END|Gravity.TOP);

            countLabel.setLayoutParams(paramsCount);

            TextView weightLabel = new TextView(this);
            weightLabel.setText(""+luggageListEntryEntity.getLuggageEntity().getWeight());
//            weightLabel.setBackgroundResource(R.drawable.border);

            GridLayout.LayoutParams paramsWeight = new GridLayout.LayoutParams();
            paramsWeight.rowSpec = GridLayout.spec(1, 1);
            paramsWeight.columnSpec = GridLayout.spec(3, 1);
            paramsWeight.setGravity(Gravity.END|Gravity.TOP);

            weightLabel.setLayoutParams(paramsWeight);
/*
            table.addView(idLabel, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(1, GridLayout.CENTER))
            );
            table.addView(nameLabel, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(2, GridLayout.CENTER))
            );
            table.addView(countLabel, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(3, GridLayout.CENTER))
            );
            table.addView(weightLabel, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(4, GridLayout.CENTER))
            );
*/
            table.addView(idLabel, 1);
            table.addView(nameLabel, 2);
            table.addView(countLabel, 3);
            table.addView(weightLabel, 4);

//            table.addView(row);

            nameLabel.setOnClickListener(new RowOnClickListener(luggageListEntryEntity));
            nameLabel.setOnLongClickListener(new RowOnLongClickListener(luggageListEntryEntity));

            weightSum += 100;
            row += 1;
        }

//        TableRow rowSummary = new TableRow(this);
        TextView summary = new TextView(this);
        summary.setText(""+weightSum);
        summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        summary.setGravity(Gravity.END);
        summary.setTypeface(Typeface.SERIF, Typeface.BOLD);

        GridLayout.LayoutParams paramsSummary = new GridLayout.LayoutParams();
        paramsSummary.rowSpec = GridLayout.spec(3, 1);
        paramsSummary.columnSpec = GridLayout.spec(0, 4);
        paramsSummary.setGravity(Gravity.END);

//        rowSummary.addView(summary, paramsSummary);
        summary.setLayoutParams(paramsSummary);
        table.addView(summary);
    }
}
