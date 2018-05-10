package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ScaleDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ImageButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryEditDialog editDialog = new CategoryEditDialog(CategoryActivity.this);
                editDialog.showNewDialog(view);
            }
        });

        loadCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mainMenuMain:
                Intent mainIntent = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.mainMenuLuggage:
                Intent luggageIntent = new Intent(CategoryActivity.this, LuggageActivity.class);
                startActivity(luggageIntent);
                break;
            case R.id.mainMenuPackingLists:
                Intent packingListIntent = new Intent(CategoryActivity.this, PackingListActivity.class);
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

    private CategoryActivity loadCategories() {
        TableLayout table = findViewById(R.id.categoriesTable);

        table.setStretchAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Kategorien");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        rowTitle.addView(title);
        table.addView(rowTitle);

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        ArrayList<LuggageCategoryEntity> luggageCategoryEntities = luggageCategoryDbModel.load();
        Resources.Theme currentTheme = getTheme();

        for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryEntities) {
            TableRow row = new TableRow(this);
            TextView nameLabel = new TextView(this);

            nameLabel.setText(luggageCategoryEntity.getName());
            nameLabel.setGravity(Gravity.CENTER_VERTICAL);

            TextView deleteBtn = new TextView(this);
            deleteBtn.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                this.getResources().getDrawable(android.R.drawable.ic_menu_delete, currentTheme),
                null
            );
            deleteBtn.setScaleX((float)0.8);
            deleteBtn.setScaleY((float)0.8);
            deleteBtn.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);

            deleteBtn.setOnClickListener(new CategoryDeleteOnClickListener(CategoryActivity.this, luggageCategoryEntity));

            row.addView(nameLabel);
            row.addView(deleteBtn);

            table.addView(row);

            row.setOnClickListener(new CategoryEntityOnClickListener(CategoryActivity.this, luggageCategoryEntity));
            row.setOnLongClickListener(new CategoryEntityOnLongClickListener(CategoryActivity.this, luggageCategoryEntity));
        }
        return this;
    }
}
