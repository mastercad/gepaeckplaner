package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Locale;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.AbstractActivity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListEntryDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.PackingListEntryEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListEntryDeleteOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListEntryOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListEntryOnLongClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListOnLongClickListener;

public class PackingListDetailActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list_detail);

        final long packingListId = getIntent().getLongExtra("packingListId", 0);

        if (0 < packingListId) {
            fillTable(packingListId);
        }


        ImageButton addPackingListEntry = findViewById(R.id.btnAddPackingList);
        addPackingListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackingListEntryEditDialog editDialog = new PackingListEntryEditDialog(PackingListDetailActivity.this, packingListId);
                editDialog.showNewDialog(view);
            }
        });
    }

    /*
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
                Intent categoryIntent = new Intent(PackingListDetailActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.mainMenuLuggage:
                finish();
                Intent luggageIntent = new Intent(PackingListDetailActivity.this, LuggageActivity.class);
                startActivity(luggageIntent);
                break;
            case R.id.mainMenuPackingLists:
                finish();
                Intent packingListIntent = new Intent(PackingListDetailActivity.this, PackingListActivity.class);
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
*/
    private PackingListDetailActivity fillTable(long packingListId) {
        TableLayout table = findViewById(R.id.packingListDetailTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_packing_lists);
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

            title.setText(title.getText() + " " + getResources().getText(R.string.text_for) + " " + packingListEntity.getName());

            TableRow row = new TableRow(this);
            TextView emptyContent = new TextView(this);
            emptyContent.setText("Diese Packliste enthält noch keine Einträge!");

            row.addView(emptyContent);
            table.addView(row);

        } else {
            int rowCount = 1;

            long tempCategory = -1;
            Resources.Theme currentTheme = getTheme();

            TableRow.LayoutParams lp;

            for (PackingListEntryEntity packingListEntryEntity : packingListEntryCollection) {
                if (1 == rowCount) {
                    title.setText(title.getText() + " " + getResources().getText(R.string.text_for) + " " + packingListEntryEntity.getPackingListEntity().getName());
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
                    Log.i("PackingListDetail", "Change Category!");
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
                row.setWeightSum(1);

                TextView idLabel = new TextView(this);
                String formattedEntryId = String.format(Locale.GERMANY, "%d%02d", packingListEntryEntity.getLuggageEntity().getCategoryId(), packingListEntryEntity.getLuggageEntity().getCount());
                idLabel.setText(formattedEntryId);
                idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                idLabel.setGravity(Gravity.START);
                idLabel.setWidth(0);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                idLabel.setLayoutParams(lp);
                idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                row.addView(idLabel);

                TextView nameLabel = new TextView(this);
                nameLabel.setText(packingListEntryEntity.getLuggageEntity().getName());
                nameLabel.setGravity(Gravity.START);
                nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
                nameLabel.setLayoutParams(lp);
                row.addView(nameLabel);

                TextView weightLabel = new TextView(this);
                weightLabel.setText(Integer.toString(packingListEntryEntity.getLuggageEntity().getWeight()));
                weightLabel.setGravity(Gravity.END);
                weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                weightLabel.setLayoutParams(lp);
                row.addView(weightLabel);

                TextView deleteBtn = new TextView(this);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
                deleteBtn.setLayoutParams(lp);
                deleteBtn.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    this.getResources().getDrawable(android.R.drawable.ic_menu_delete, currentTheme),
                    null
                );
                deleteBtn.setScaleX((float)0.8);
                deleteBtn.setScaleY((float)0.8);
                deleteBtn.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
                deleteBtn.setOnClickListener(new PackingListEntryDeleteOnClickListener(this, packingListEntryEntity));
                row.addView(deleteBtn);

                row.setOnClickListener(new PackingListEntryOnClickListener(PackingListDetailActivity.this, packingListEntryEntity));
                row.setOnLongClickListener(new PackingListEntryOnLongClickListener(PackingListDetailActivity.this, packingListEntryEntity));

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
        return this;
    }
}
