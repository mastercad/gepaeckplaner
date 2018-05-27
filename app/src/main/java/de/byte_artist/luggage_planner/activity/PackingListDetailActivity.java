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
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.dialog.PackingListEntryNewDialogFragment;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;
import de.byte_artist.luggage_planner.listener.PackingListEntryDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListEntryOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListEntryOnLongClickListener;
import de.byte_artist.luggage_planner.service.TextSize;

public class PackingListDetailActivity extends AbstractActivity {

    private static long packingListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list_detail);

        packingListId = getIntent().getLongExtra("packingListId", 0);

        ImageButton addPackingListEntry = findViewById(R.id.btnAddPackingList);
        addPackingListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("packing_list_entry_new_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                PackingListEntryNewDialogFragment alertDialog = PackingListEntryNewDialogFragment.newInstance(packingListId);

                alertDialog.show(ft, "packing_list_entry_new_dialog");
            }
        });

        refresh();
    }

    protected void refresh() {
        if (0 < packingListId) {
            fillTable(packingListId);
        }
    }

    private void fillTable(long packingListId) {
        TableLayout table = findViewById(R.id.packingListDetailTable);
        table.removeAllViews();
        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_luggage_list);
        TextSize.convert(this, title, TextSize.TEXT_TYPE_HEADER);
        title.setMaxLines(1);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        double weightSum = 0;

        PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(this);
        ArrayList<PackingListEntryEntity> packingListEntryCollection = packingListEntryDbModel.findPackingListById(packingListId);

        if (packingListEntryCollection.isEmpty()) {
            PackingListDbModel packingListDbModel = new PackingListDbModel(this);
            PackingListEntity packingListEntity = packingListDbModel.findPackingListById(packingListId);

            title.setText(String.format(
                getResources().getString(R.string.placeholder_concat_packing_list_name),
                title.getText(),
                packingListEntity.getName())
            );

            TableRow row = new TableRow(this);
            TextView emptyContent = new TextView(this);
            emptyContent.setText(R.string.warning_packing_list_empty);

            row.addView(emptyContent);
            table.addView(row);

        } else {
            int rowCount = 1;

            long tempCategory = -1;

            TableRow.LayoutParams lp;

            Locale currentLocale = getResources().getConfiguration().locale;

            for (PackingListEntryEntity packingListEntryEntity : packingListEntryCollection) {
                if (1 == rowCount) {
                    title.setText(String.format(
                        getResources().getString(R.string.placeholder_concat_packing_list_name),
                        title.getText(),
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
                        addLuggageRow.addView(emptyLabel);
                        table.addView(addLuggageRow);
                    }
                    tempCategory = currentCategory;

                    TableRow categoryRow = new TableRow(this);
                    TextView categoryHeadingLabel = new TextView(this);
                    categoryHeadingLabel.setMaxLines(1);
                    categoryHeadingLabel.setText(packingListEntryEntity.getLuggageEntity().getCategoryEntity().getName());
                    TextSize.convert(this, categoryHeadingLabel, TextSize.TEXT_TYPE_NORMAL);
                    categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);

                    categoryRow.addView(categoryHeadingLabel);
                    table.addView(categoryRow);
                }

                TableRow row = new TableRow(this);
                row.setWeightSum(1);

                TextView idLabel = new TextView(this);
                String formattedEntryId = String.format(
                        currentLocale,
                        "%d%02d",
                        packingListEntryEntity.getLuggageEntity().getCategoryId(), packingListEntryEntity.getLuggageEntity().getCount()
                );
                idLabel.setText(formattedEntryId);
                TextSize.convert(this, idLabel, TextSize.TEXT_TYPE_NORMAL);
                idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                idLabel.setPadding(15, 0, 0, 0);
                idLabel.setGravity(Gravity.START);
                idLabel.setMaxLines(1);
                idLabel.setWidth(0);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                idLabel.setLayoutParams(lp);
                idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                row.addView(idLabel);

                TextView nameLabel = new TextView(this);
                nameLabel.setText(packingListEntryEntity.getLuggageEntity().getName());
                TextSize.convert(this, nameLabel, TextSize.TEXT_TYPE_NORMAL);
                nameLabel.setGravity(Gravity.START);
                nameLabel.setMaxLines(1);
                nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
                nameLabel.setLayoutParams(lp);
                row.addView(nameLabel);

                TextView countLabel = new TextView(this);
                countLabel.setText(String.format(currentLocale, "%dx", packingListEntryEntity.getCount()));
                TextSize.convert(this, countLabel, TextSize.TEXT_TYPE_NORMAL);
                countLabel.setGravity(Gravity.END);
                countLabel.setMaxLines(1);
                countLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                countLabel.setLayoutParams(lp);
                row.addView(countLabel);

                TextView weightLabel = new TextView(this);
                weightLabel.setText(String.format(currentLocale, "%.0f g", packingListEntryEntity.getLuggageEntity().getWeight()));
                TextSize.convert(this, weightLabel, TextSize.TEXT_TYPE_NORMAL);
                weightLabel.setGravity(Gravity.END);
                weightLabel.setMaxLines(1);
                weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                weightLabel.setLayoutParams(lp);
                row.addView(weightLabel);


                ImageView deleteBtn = new ImageView(this);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
                deleteBtn.setPadding(0, 2, 0, 2);
                deleteBtn.setBackgroundColor(Color.WHITE);
                deleteBtn.setLayoutParams(lp);
                deleteBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
                deleteBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                deleteBtn.setOnClickListener(new PackingListEntryDeleteOnClickListener(this, packingListEntryEntity));
                row.addView(deleteBtn);

                row.setOnClickListener(new PackingListEntryOnClickListener(PackingListDetailActivity.this, packingListEntryEntity));
                row.setOnLongClickListener(new PackingListEntryOnLongClickListener(PackingListDetailActivity.this, packingListEntryEntity));

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
            TextSize.convert(this, summary, TextSize.TEXT_TYPE_FOOTER);
            summary.setGravity(Gravity.END);
            summary.setTypeface(Typeface.SERIF, Typeface.BOLD);

            rowSummary.addView(summary);
            table.addView(rowSummary);
        }
    }
}
