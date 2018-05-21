package de.byte_artist.luggage_planner.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.dialog.PackingListEntryEditDialog;
import de.byte_artist.luggage_planner.dialog.PackingListEntryNewDialogFragment;
import de.byte_artist.luggage_planner.dialog.PackingListNewDialogFragment;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;
import de.byte_artist.luggage_planner.listener.PackingListEntryDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListEntryOnLongClickListener;

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

    public void refresh() {
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
        title.setText(R.string.label_packing_list);
        title.setMaxLines(1);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        int weightSum = 0;

        PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(this, null, null, 1);
        ArrayList<PackingListEntryEntity> packingListEntryCollection = packingListEntryDbModel.findPackingListById(packingListId);

        if (packingListEntryCollection.isEmpty()) {
            PackingListDbModel packingListDbModel = new PackingListDbModel(this, null, null, 1);
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
                    categoryHeadingLabel.setTextSize(14);
                    categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);

                    categoryRow.addView(categoryHeadingLabel);
                    table.addView(categoryRow);
                }

                TableRow row = new TableRow(this);
                row.setWeightSum(1);

                TextView idLabel = new TextView(this);
                String formattedEntryId = String.format(
                        Locale.getDefault(),
                        "%d%02d",
                        packingListEntryEntity.getLuggageEntity().getCategoryId(), packingListEntryEntity.getLuggageEntity().getCount()
                );
                idLabel.setText(formattedEntryId);
                idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                idLabel.setGravity(Gravity.START);
                idLabel.setMaxLines(1);
                idLabel.setWidth(0);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                idLabel.setLayoutParams(lp);
                idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                row.addView(idLabel);

                TextView nameLabel = new TextView(this);
                nameLabel.setText(packingListEntryEntity.getLuggageEntity().getName());
                nameLabel.setGravity(Gravity.START);
                nameLabel.setMaxLines(1);
                nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
                nameLabel.setLayoutParams(lp);
                row.addView(nameLabel);

                TextView countLabel = new TextView(this);
                countLabel.setText(String.format(Locale.getDefault(), "%dx", packingListEntryEntity.getCount()));
                countLabel.setGravity(Gravity.END);
                countLabel.setMaxLines(1);
                countLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                countLabel.setLayoutParams(lp);
                row.addView(countLabel);

                TextView weightLabel = new TextView(this);
                weightLabel.setText(String.format(Locale.getDefault(), "%d g", packingListEntryEntity.getLuggageEntity().getWeight()));
                weightLabel.setGravity(Gravity.END);
                weightLabel.setMaxLines(1);
                weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                weightLabel.setLayoutParams(lp);
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
                deleteBtn.setOnClickListener(new PackingListEntryDeleteOnClickListener(this, packingListEntryEntity));

                row.addView(deleteBtn);

//                row.setOnClickListener(new PackingListEntryOnClickListener(PackingListDetailActivity.this, packingListEntryEntity));
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
            summary.setText(String.format(Locale.getDefault(), "%d g", weightSum));
            summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            summary.setGravity(Gravity.END);
            summary.setTypeface(Typeface.SERIF, Typeface.BOLD);

            rowSummary.addView(summary);
            table.addView(rowSummary);
        }
    }
}
