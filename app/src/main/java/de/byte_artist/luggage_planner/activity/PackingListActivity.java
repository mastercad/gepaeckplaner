package de.byte_artist.luggage_planner.activity;

import android.content.res.Resources;
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

import de.byte_artist.luggage_planner.AbstractActivity;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.dialog.PackingListEditDialog;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.listener.PackingListDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListOnLongClickListener;
import de.byte_artist.luggage_planner.R;

public class PackingListActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        ImageButton addPackingListEntry = findViewById(R.id.btnAddPackingList2);
        addPackingListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackingListEditDialog editDialog = new PackingListEditDialog(PackingListActivity.this);
                editDialog.showNewDialog(view);
            }
        });

        loadPackingLists();
    }

    private void loadPackingLists() {

        TableLayout table = findViewById(R.id.packingListsTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_packing_lists);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        PackingListDbModel packingListDbModel = new PackingListDbModel(this, null, null, 1);
        ArrayList<PackingListEntity> packingListEntities = packingListDbModel.load();

        Resources.Theme currentTheme = getTheme();

        TableRow.LayoutParams lp;

        for (PackingListEntity packingListEntity : packingListEntities) {
            TableRow row = new TableRow(this);
            row.setWeightSum(1);

            TextView nameLabel = new TextView(this);
            nameLabel.setText(packingListEntity.getName());
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            nameLabel.setLayoutParams(lp);
            row.addView(nameLabel);

            TextView dateLabel = new TextView(this);
            dateLabel.setText(packingListEntity.getDate());
            dateLabel.setGravity(Gravity.END);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f);
            dateLabel.setLayoutParams(lp);
            row.addView(dateLabel);

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
            deleteBtn.setOnClickListener(new PackingListDeleteOnClickListener(this, packingListEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new PackingListOnClickListener(PackingListActivity.this, packingListEntity));
            row.setOnLongClickListener(new PackingListOnLongClickListener(PackingListActivity.this, packingListEntity));
        }

    }
}
