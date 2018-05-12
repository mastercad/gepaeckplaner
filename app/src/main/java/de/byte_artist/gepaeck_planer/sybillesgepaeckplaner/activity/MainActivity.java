package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.AbstractActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListEntryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        super.recreateDatabase();
        this.fillTable();
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
