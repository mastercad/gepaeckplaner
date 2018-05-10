package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

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

public class PackingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        ImageButton addPackingListEntry = findViewById(R.id.btnAddPackingList);
        addPackingListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        title.setText("Packlisten");
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
            nameLabel.setText(packingListEntity.getLuggageListEntity().getName());

            TextView dateLabel = new TextView(this);
            dateLabel.setText(packingListEntity.getLuggageListEntity().getDate());

            row.addView(nameLabel);
            row.addView(dateLabel);

            table.addView(row);

            row.setOnClickListener(new PackingListEntityOnClickListener(packingListEntity));
            row.setOnLongClickListener(new PackingListEntityOnLongClickListener(packingListEntity));
        }

        return this;
    }
}
