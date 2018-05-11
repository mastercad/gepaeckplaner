package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.activity;

import android.content.Intent;
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

import java.util.ArrayList;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db.PackingListDbModel;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.dialog.PackingListEditDialog;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListOnClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.listener.PackingListOnLongClickListener;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.R;

public class PackingListActivity extends AppCompatActivity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.packing_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mainMenuMain:
                Intent mainIntent = new Intent(PackingListActivity.this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.mainMenuLuggage:
                Intent luggageIntent = new Intent(PackingListActivity.this, LuggageActivity.class);
                startActivity(luggageIntent);
                break;
            case R.id.mainMenuCategories:
                Intent categoryIntent = new Intent(PackingListActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.mainMenuExit:
                finishAffinity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private PackingListActivity loadPackingLists() {

        TableLayout table = findViewById(R.id.packingListsTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.label_packing_list);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        PackingListDbModel packingListDbModel = new PackingListDbModel(this, null, null, 1);
        ArrayList<PackingListEntity> packingListEntities = packingListDbModel.load();

        for (PackingListEntity packingListEntity : packingListEntities) {
            TableRow row = new TableRow(this);
            TextView nameLabel = new TextView(this);
            nameLabel.setText(packingListEntity.getName());

            TextView dateLabel = new TextView(this);
            dateLabel.setText(packingListEntity.getDate());
            dateLabel.setGravity(Gravity.END);

            row.addView(nameLabel);
            row.addView(dateLabel);

            table.addView(row);

            row.setOnClickListener(new PackingListOnClickListener(PackingListActivity.this, packingListEntity));
            row.setOnLongClickListener(new PackingListOnLongClickListener(PackingListActivity.this, packingListEntity));
        }

        return this;
    }
}
