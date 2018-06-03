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
import de.byte_artist.luggage_planner.dialog.PackingListNewDialogFragment;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.listener.PackingListDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListOnClickListener;
import de.byte_artist.luggage_planner.listener.PackingListOnLongClickListener;
import de.byte_artist.luggage_planner.service.Date;
import de.byte_artist.luggage_planner.service.TextSize;

public class PackingListActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        ImageButton addPackingListEntry = findViewById(R.id.btnAddPackingList2);
        addPackingListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("packing_list_new_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                PackingListNewDialogFragment fragment = new PackingListNewDialogFragment();
                fragment.show(ft, "packing_list_new_dialog");
            }
        });

        refresh();
    }

    protected void refresh() {
        loadPackingLists();
    }

    private void loadPackingLists() {
        TableLayout table = findViewById(R.id.packingListsTable);
        table.removeAllViews();
        table.setShrinkAllColumns(true);
        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_packing_lists);
        title.setMaxLines(1);
        TextSize.convert(this, title, TextSize.TEXT_TYPE_HEADER);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        PackingListDbModel packingListDbModel = new PackingListDbModel(this);
        ArrayList<PackingListEntity> packingListEntities = packingListDbModel.load();

        TableRow.LayoutParams lp;
        Date dateService = new Date();

        Locale currentLocale = getResources().getConfiguration().locale;

        for (PackingListEntity packingListEntity : packingListEntities) {
            TableRow row = new TableRow(this);
            row.setWeightSum(1);

            TextView nameLabel = new TextView(this);
            nameLabel.setText(packingListEntity.getName());
            TextSize.convert(this, nameLabel, TextSize.TEXT_TYPE_NORMAL);
            nameLabel.setMaxLines(1);
            nameLabel.setPadding(10, 0, 0, 0);
            nameLabel.setGravity(Gravity.START);
            nameLabel.setWidth(0);
            nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            nameLabel.setLayoutParams(lp);
            row.addView(nameLabel);

            TextView dateLabel = new TextView(this);
            dateLabel.setText(dateService.localizeDate(
                getApplicationContext(),
                packingListEntity.getDate(),
                "",
                currentLocale
            ));
            TextSize.convert(this, dateLabel, TextSize.TEXT_TYPE_NORMAL);
            dateLabel.setGravity(Gravity.END);
            dateLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
            dateLabel.setMaxLines(1);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f);
            dateLabel.setLayoutParams(lp);
            row.addView(dateLabel);


            ImageView deleteBtn = new ImageView(this);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
            deleteBtn.setPadding(0, 2, 0, 2);
            deleteBtn.setBackgroundColor(Color.WHITE);
            deleteBtn.setLayoutParams(lp);
            deleteBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
            deleteBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            deleteBtn.setOnClickListener(new PackingListDeleteOnClickListener(this, packingListEntity));
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new PackingListOnClickListener(PackingListActivity.this, packingListEntity));
            row.setOnLongClickListener(new PackingListOnLongClickListener(PackingListActivity.this, packingListEntity));
        }

    }
}
