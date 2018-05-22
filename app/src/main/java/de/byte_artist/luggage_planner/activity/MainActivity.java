package de.byte_artist.luggage_planner.activity;

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

import de.byte_artist.luggage_planner.AbstractActivity;
import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresh();
    }

    public void refresh() {
        fillTable();
    }

    private void fillTable() {
        TableLayout table = findViewById(R.id.PackagesTable);
        table.removeAllViews();
        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_packing_list);
        title.setMaxLines(1);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        PackingListDbModel packingListDbModel = new PackingListDbModel(this, null, null, 1);
        PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(this, null, null, 1);

        PackingListEntity currentPackingList = packingListDbModel.findCurrentPackingList();

        if (null == currentPackingList) {
            title.setText(getResources().getString(R.string.warning_no_packing_list_found));
            return;
        }

        ArrayList<PackingListEntryEntity> luggageListEntryCollection = packingListEntryDbModel.findPackingListById(currentPackingList.getId());

        if (0 < luggageListEntryCollection.size()) {
            int rowCount = 1;
            long tempCategory = -1;
            TableRow.LayoutParams lp;
            double weightSum = 0;

            Locale currentLocale = getResources().getConfiguration().locale;

            for (PackingListEntryEntity packingListEntryEntity : luggageListEntryCollection) {

                if (1 == rowCount) {
                    title.setText(String.format(
                            getResources().getString(R.string.placeholder_concat_packing_list_name),
                            title.getText().toString(),
                            packingListEntryEntity.getPackingListEntity().getName())
                    );
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
                    categoryHeadingLabel.setMaxLines(1);
                    categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);

                    categoryRow.addView(categoryHeadingLabel);
                    table.addView(categoryRow);
                }

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 4f));

                TextView idLabel = new TextView(this);
                String formattedEntryId = String.format(currentLocale, "%d%02d", packingListEntryEntity.getLuggageEntity().getCategoryId(), packingListEntryEntity.getLuggageEntity().getCount());
                idLabel.setText(formattedEntryId);
                idLabel.setMaxLines(1);
                idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                idLabel.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.2f));
                idLabel.setGravity(Gravity.START);
                idLabel.setWidth(0);
                idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                row.addView(idLabel);

                TextView nameLabel = new TextView(this);
                nameLabel.setText(packingListEntryEntity.getLuggageEntity().getName());
                nameLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.5f));
                nameLabel.setGravity(Gravity.START);
                nameLabel.setMaxLines(1);
                nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                row.addView(nameLabel);

                TextView countLabel = new TextView(this);
                countLabel.setText(String.format(currentLocale, "%dx", packingListEntryEntity.getCount()));
                countLabel.setGravity(Gravity.END);
                countLabel.setMaxLines(1);
                countLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                countLabel.setLayoutParams(lp);
                row.addView(countLabel);

                TextView weightLabel = new TextView(this);
                weightLabel.setText(String.format(currentLocale, "%,.0f g", packingListEntryEntity.getLuggageEntity().getWeight()));
                weightLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.1f));
                weightLabel.setGravity(Gravity.END);
                weightLabel.setMaxLines(1);
                weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                row.addView(weightLabel);

                table.addView(row);

                weightSum += (packingListEntryEntity.getLuggageEntity().getWeight() * packingListEntryEntity.getCount());

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
            summary.setText(String.format(currentLocale, "%,.0f g", weightSum));
            summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            summary.setGravity(Gravity.END);
            summary.setTypeface(Typeface.SERIF, Typeface.BOLD);

            rowSummary.addView(summary);
            table.addView(rowSummary);
        } else {
            title.setText(getResources().getString(R.string.warning_no_packing_list_found));
        }
    }
}
