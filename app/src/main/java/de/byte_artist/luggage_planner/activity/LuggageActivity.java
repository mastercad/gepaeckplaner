package de.byte_artist.luggage_planner.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.dialog.LuggageNewDialogFragment;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PreferencesEntity;
import de.byte_artist.luggage_planner.helper.LocaleHelper;
import de.byte_artist.luggage_planner.listener.CategoryCollapseOnClickListener;
import de.byte_artist.luggage_planner.listener.LuggageDeleteOnClickListener;
import de.byte_artist.luggage_planner.listener.LuggageEntityOnTouchListener;
import de.byte_artist.luggage_planner.service.Preferences;
import de.byte_artist.luggage_planner.service.TextSize;

public class LuggageActivity extends AbstractActivity {

    final static private String UP = "ASC";
    final static private String DOWN = "DESC";

    private String idOrder = "";
    private String nameOrder = "";
    private String weightOrder = "";

//    ImageView btnIdOrder;
//    ImageView btnNameOrder;
//    ImageView btnWeightOrder;

    private TextView btnIdOrder;
    private TextView btnNameOrder;
    private TextView btnWeightOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage);

        ImageButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("luggage_new_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                final LuggageNewDialogFragment fragment = new LuggageNewDialogFragment();
                fragment.show(ft, "luggage_new_dialog");
            }
        });

        refresh();
    }

    public void refresh() {
        loadLuggage();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void loadLuggage() {
        TableRow.LayoutParams lp;

        TableLayout table = findViewById(R.id.luggageTable);
        table.removeAllViews();
        table.setStretchAllColumns(true);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this);
        ArrayList<LuggageEntity> luggageEntities = luggageDbModel.load(idOrder, nameOrder, weightOrder);

        if (0 < luggageEntities.size()) {
            TableRow rowTitle = new TableRow(this);
            rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView title = new TextView(this);
            title.setText(R.string.label_luggage_list);
            TextSize.convert(this, title, TextSize.TEXT_TYPE_HEADER);
            title.setMaxLines(1);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);

            rowTitle.addView(title);
            table.addView(rowTitle);

            TableRow rowHeader = new TableRow(this);
            rowHeader.setWeightSum(1);
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
            lp.setMargins(0, 0, 0, 0);
            rowHeader.setLayoutParams(lp);

            TextView idColumnHeader = new TextView(this);
            idColumnHeader.setId(R.id.id_order_text_field);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
            idColumnHeader.setText(R.string.id);
            TextSize.convert(this, idColumnHeader, TextSize.TEXT_TYPE_NORMAL);
            idColumnHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
            idColumnHeader.setMaxLines(1);
            idColumnHeader.setPadding(10, 0, 0, 0);
            idColumnHeader.setGravity(Gravity.START);
            idColumnHeader.setWidth(0);
            idColumnHeader.setBackgroundColor(Color.parseColor("#FFFFFF"));
            idColumnHeader.setLayoutParams(lp);
            idColumnHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (idOrder) {
                        case DOWN:
                            idOrder = UP;
                            break;
                        case UP:
                            idOrder = "";
                            break;
                        default:
                            idOrder = DOWN;
                            break;
                    }
                    nameOrder = "";
                    weightOrder = "";
                    refresh();
                }
            });
            rowHeader.addView(idColumnHeader);

            btnIdOrder = new TextView(this);
            btnIdOrder.setId(R.id.id_order_button);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
            btnIdOrder.setPadding(0, 2, 0, 2);
            btnIdOrder.setBackgroundColor(Color.WHITE);
            btnIdOrder.setLayoutParams(lp);
            btnIdOrder.setGravity(Gravity.END);
            btnIdOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (idOrder) {
                        case DOWN:
                            idOrder = UP;
                            break;
                        case UP:
                            idOrder = "";
                            break;
                        default:
                            idOrder = DOWN;
                            break;
                    }
                    nameOrder = "";
                    weightOrder = "";
                    refresh();
                }
            });
            //        btnIdOrder.setScaleType(ImageView.ScaleType.FIT_CENTER);
            TextSize.convert(this, btnIdOrder, TextSize.TEXT_TYPE_NORMAL);
            rowHeader.addView(btnIdOrder);

            TextView nameColumnHeader = new TextView(this);
            nameColumnHeader.setId(R.id.name_order_text_field);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f);
            nameColumnHeader.setText(R.string.name);
            TextSize.convert(this, nameColumnHeader, TextSize.TEXT_TYPE_NORMAL);
            nameColumnHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
            nameColumnHeader.setMaxLines(1);
            nameColumnHeader.setPadding(10, 0, 0, 0);
            nameColumnHeader.setGravity(Gravity.START);
            nameColumnHeader.setWidth(0);
            nameColumnHeader.setBackgroundColor(Color.parseColor("#FFFFFF"));
            nameColumnHeader.setLayoutParams(lp);
            nameColumnHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (nameOrder) {
                        case UP:
                            nameOrder = DOWN;
                            break;
                        case DOWN:
                            nameOrder = "";
                            break;
                        default:
                            nameOrder = UP;
                            break;
                    }
                    idOrder = "";
                    weightOrder = "";
                    refresh();
                }
            });
            rowHeader.addView(nameColumnHeader);

            //        btnNameOrder = new ImageView(this);
            btnNameOrder = new TextView(this);
            nameColumnHeader.setId(R.id.name_order_button);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
            btnNameOrder.setPadding(0, 2, 0, 2);
            btnNameOrder.setBackgroundColor(Color.WHITE);
            btnNameOrder.setLayoutParams(lp);
            btnNameOrder.setGravity(Gravity.END);
            btnNameOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (nameOrder) {
                        case UP:
                            nameOrder = DOWN;
                            break;
                        case DOWN:
                            nameOrder = "";
                            break;
                        default:
                            nameOrder = UP;
                            break;
                    }
                    idOrder = "";
                    weightOrder = "";
                    refresh();
                }
            });
            //        btnNameOrder.setScaleType(ImageView.ScaleType.FIT_CENTER);
            TextSize.convert(this, btnNameOrder, TextSize.TEXT_TYPE_NORMAL);
            rowHeader.addView(btnNameOrder);

            TextView weightColumnHeader = new TextView(this);
            weightColumnHeader.setId(R.id.weight_order_text_field);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            weightColumnHeader.setText(R.string.weight);
            TextSize.convert(this, weightColumnHeader, TextSize.TEXT_TYPE_NORMAL);
            weightColumnHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
            weightColumnHeader.setMaxLines(1);
            weightColumnHeader.setPadding(10, 0, 0, 0);
            weightColumnHeader.setGravity(Gravity.START);
            weightColumnHeader.setWidth(0);
            weightColumnHeader.setBackgroundColor(Color.parseColor("#FFFFFF"));
            weightColumnHeader.setLayoutParams(lp);
            weightColumnHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (weightOrder) {
                        case UP:
                            weightOrder = DOWN;
                            break;
                        case DOWN:
                            weightOrder = "";
                            break;
                        default:
                            weightOrder = UP;
                            break;
                    }
                    idOrder = "";
                    nameOrder = "";
                    refresh();
                }
            });
            rowHeader.addView(weightColumnHeader);

            //        btnWeightOrder = new ImageView(this);
            btnWeightOrder = new TextView(this);
            btnWeightOrder.setId(R.id.weight_order_button);
            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
            btnWeightOrder.setPadding(0, 2, 0, 2);
            btnWeightOrder.setBackgroundColor(Color.WHITE);
            btnWeightOrder.setLayoutParams(lp);
            btnWeightOrder.setGravity(Gravity.END);
            //        btnWeightOrder.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btnWeightOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (weightOrder) {
                        case UP:
                            weightOrder = DOWN;
                            break;
                        case DOWN:
                            weightOrder = "";
                            break;
                        default:
                            weightOrder = UP;
                            break;
                    }
                    idOrder = "";
                    nameOrder = "";
                    refresh();
                }
            });
            TextSize.convert(this, btnWeightOrder, TextSize.TEXT_TYPE_NORMAL);
            rowHeader.addView(btnWeightOrder);

            table.addView(rowHeader);

            long tempCategory = -1;
            boolean categoryVisible = true;
            String visibilityPostfix = "CategoryVisible";
            Locale currentLocale = LocaleHelper.investigateLocale(this);

            for (LuggageEntity luggageEntity : luggageEntities) {
                long currentCategory = luggageEntity.getCategoryEntity().getId();

                // new Category
                if (-1 == tempCategory
                    || tempCategory != currentCategory
                ) {
                    categoryVisible = true;
                    if (-1 != tempCategory) {
                        TableRow addLuggageRow = new TableRow(this);
                        TextView emptyLabel = new TextView(this);

                        emptyLabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20, 1f));

                        addLuggageRow.addView(emptyLabel);
                        table.addView(addLuggageRow);
                    }

                    PreferencesEntity preferencesEntity = Preferences.get(
                        currentCategory+"_"+visibilityPostfix,
                        getApplicationContext()
                    );

                    if (null != preferencesEntity) {
                        categoryVisible = preferencesEntity.getValue().equals("1");
                    }

                    tempCategory = currentCategory;

                    TableRow categoryRow = new TableRow(this);

                    TextView categoryHeadingLabel = new TextView(this);
                    categoryHeadingLabel.setText(luggageEntity.getCategoryEntity().getName());
                    lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                    TextSize.convert(this, categoryHeadingLabel, TextSize.TEXT_TYPE_NORMAL);
                    categoryHeadingLabel.setLayoutParams(lp);
                    categoryHeadingLabel.setMaxLines(1);
                    categoryHeadingLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                    categoryHeadingLabel.setOnClickListener(
                        new CategoryCollapseOnClickListener(
                            this,
                            luggageEntity.getCategoryEntity(),
                            currentCategory+"_"+visibilityPostfix
                        )
                    );

                    categoryRow.addView(categoryHeadingLabel);

                    TextView btnCategoryVisible = new TextView(this);
                    btnCategoryVisible.setText(categoryVisible ? "↑" : "↓");
                    lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
                    TextSize.convert(this, btnCategoryVisible, TextSize.TEXT_TYPE_NORMAL);
                    btnCategoryVisible.setLayoutParams(lp);
                    btnCategoryVisible.setMaxLines(1);
                    btnCategoryVisible.setGravity(Gravity.START);
                    btnCategoryVisible.setTypeface(Typeface.SERIF, Typeface.BOLD);
                    btnCategoryVisible.setOnClickListener(
                        new CategoryCollapseOnClickListener(
                            this,
                            luggageEntity.getCategoryEntity(),
                            currentCategory+"_"+visibilityPostfix
                        )
                    );

                    categoryRow.addView(btnCategoryVisible);

                    table.addView(categoryRow);
                }

                if (categoryVisible) {
                    TableRow row = new TableRow(this);
                    row.setWeightSum(1);
                    lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    lp.setMargins(0, 0, 0, 0);
                    row.setLayoutParams(lp);

                    TextView idLabel = new TextView(this);
                    String formattedEntryId = String.format(currentLocale, "%d%02d", luggageEntity.getCategoryId(), luggageEntity.getCount());
                    lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                    idLabel.setText(formattedEntryId);
                    TextSize.convert(this, idLabel, TextSize.TEXT_TYPE_NORMAL);
                    if (!luggageEntity.isActive()) {
                        idLabel.setPaintFlags(idLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    idLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                    idLabel.setMaxLines(1);
                    idLabel.setPadding(10, 0, 0, 0);
                    idLabel.setGravity(Gravity.START);
                    idLabel.setWidth(0);
                    idLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    idLabel.setLayoutParams(lp);
                    row.addView(idLabel);

                    TextView nameLabel = new TextView(this);
                    lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
                    if (!luggageEntity.isActive()) {
                        nameLabel.setPaintFlags(nameLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    nameLabel.setLayoutParams(lp);
                    nameLabel.setMaxLines(1);
                    nameLabel.setGravity(Gravity.START);
                    nameLabel.setText(luggageEntity.getName());
                    TextSize.convert(this, nameLabel, TextSize.TEXT_TYPE_NORMAL);
                    nameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    row.addView(nameLabel);

                    TextView weightLabel = new TextView(this);
                    lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                    if (!luggageEntity.isActive()) {
                        weightLabel.setPaintFlags(weightLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    weightLabel.setLayoutParams(lp);
                    weightLabel.setMaxLines(1);
                    weightLabel.setText(String.format(currentLocale, "%.0f g", luggageEntity.getWeight()));
                    TextSize.convert(this, weightLabel, TextSize.TEXT_TYPE_NORMAL);
                    weightLabel.setGravity(Gravity.END);
                    weightLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    row.addView(weightLabel);

                    ImageView deleteBtn = new ImageView(this);
                    lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
                    deleteBtn.setPadding(0, 2, 0, 2);
                    deleteBtn.setBackgroundColor(Color.WHITE);
                    deleteBtn.setLayoutParams(lp);
                    deleteBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
                    deleteBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    deleteBtn.setOnClickListener(new LuggageDeleteOnClickListener(LuggageActivity.this, luggageEntity));
                    row.addView(deleteBtn);

                    table.addView(row);

                    row.setOnTouchListener(new LuggageEntityOnTouchListener(LuggageActivity.this, luggageEntity));
                }
            }

            considerCurrentOrderStateForView();
        } else {
            TableRow rowTitle = new TableRow(this);
            rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView title = new TextView(this);
            title.setText(R.string.warning_luggage_list_empty);
            TextSize.convert(this, title, TextSize.TEXT_TYPE_HEADER);
            title.setMaxLines(1);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);

            rowTitle.addView(title);
            table.addView(rowTitle);
        }
    }

    private void considerCurrentOrderStateForView() {

        btnIdOrder.setText("");
        btnNameOrder.setText("");
        btnWeightOrder.setText("");

        switch (this.idOrder) {
            case UP:
//                btnIdOrder.setImageDrawable(getDrawable(android.R.drawable.arrow_up_float));
                btnIdOrder.setText("↑");
                break;
            case DOWN:
//                btnIdOrder.setImageDrawable(getDrawable(android.R.drawable.arrow_down_float));
                btnIdOrder.setText("↓");
                break;
//            default:
//                btnIdOrder.setImageDrawable(getDrawable(android.R.drawable.screen_background_light_transparent));
//                btnIdOrder.setText("");
        }
        switch (this.nameOrder) {
            case UP:
//                btnNameOrder.setImageDrawable(getDrawable(android.R.drawable.arrow_up_float));
                btnNameOrder.setText("↑");
                break;
            case DOWN:
//                btnNameOrder.setImageDrawable(getDrawable(android.R.drawable.arrow_down_float));
                btnNameOrder.setText("↓");
                break;
//            default:
//                btnNameOrder.setImageDrawable(getDrawable(android.R.drawable.screen_background_light_transparent));
////                btnNameOrder.setText("");
        }
        switch (this.weightOrder) {
            case UP:
//                btnWeightOrder.setImageDrawable(getDrawable(android.R.drawable.arrow_up_float));
                btnWeightOrder.setText("↑");
                break;
            case DOWN:
//                btnWeightOrder.setImageDrawable(getDrawable(android.R.drawable.arrow_down_float));
                btnWeightOrder.setText("↓");
                break;
//            default:
//                btnWeightOrder.setImageDrawable(getDrawable(android.R.drawable.screen_background_light_transparent));
        }
    }
}
