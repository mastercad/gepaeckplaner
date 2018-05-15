package de.byte_artist.luggage_planner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;

import de.byte_artist.luggage_planner.activity.CategoryActivity;
import de.byte_artist.luggage_planner.activity.LuggageActivity;
import de.byte_artist.luggage_planner.activity.MainActivity;
import de.byte_artist.luggage_planner.activity.PackingListActivity;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

@SuppressLint("Registered")
public class AbstractActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mainMenuMain:
                finish();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.mainMenuCategories:
                finish();
                Intent categoryIntent = new Intent(this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.mainMenuLuggage:
                finish();
                Intent luggageIntent = new Intent(this, LuggageActivity.class);
                startActivity(luggageIntent);
                break;
            case R.id.mainMenuPackingLists:
                finish();
                Intent packingListIntent = new Intent(this, PackingListActivity.class);
                startActivity(packingListIntent);
                break;
            case R.id.mainMenuExit:
                finishAffinity();
                break;
            case R.id.mainMenuResetDb:
                resetDatabase();
                recreate();
                break;
            case R.id.mainMenuCreateDb:
                recreateDatabase();
                recreate();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void recreateDatabase() {
        this.resetDatabase();

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this, null, null, 1);
        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity(getResources().getString(R.string.category1));

        luggageCategoryDbModel.insert(luggageCategoryEntity);

        PackingListDbModel luggageListDbModel = new PackingListDbModel(this, null, null, 1);
        PackingListEntity luggageListEntity = new PackingListEntity(getResources().getString(R.string.packing_list1), "2018-08-04");
        luggageListDbModel.insert(luggageListEntity);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this, null, null, 1);
        LuggageEntity luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage1_category1), luggageCategoryEntity.getId(), 65);
        luggageDbModel.insert(luggageEntity);

        PackingListEntryDbModel packingListDbModel = new PackingListEntryDbModel(this, null, null, 1);
        PackingListEntryEntity packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage2_category1), luggageCategoryEntity.getId(), 120);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageCategoryEntity = new LuggageCategoryEntity(getResources().getString(R.string.category2));
        luggageCategoryDbModel.insert(luggageCategoryEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage1_category2), luggageCategoryEntity.getId(), 65);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage2_category2), luggageCategoryEntity.getId(), 120);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage3_category2), luggageCategoryEntity.getId(), 120);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageCategoryEntity = new LuggageCategoryEntity(getResources().getString(R.string.category3));
        luggageCategoryDbModel.insert(luggageCategoryEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage1_category3), luggageCategoryEntity.getId(), 65);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage2_category3), luggageCategoryEntity.getId(), 120);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(getResources().getString(R.string.luggage3_category3), luggageCategoryEntity.getId(), 120);
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(luggageListEntity.getId(), luggageEntity.getId(), 2);
        packingListDbModel.insert(packingListEntity);
    }

    public void resetDatabase() {
        String filePathName = getFilesDir().getAbsolutePath()+"/../databases/luggage.db";
        File file = new File(filePathName);

        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .setTitle(R.string.title_exit_application)
            .setMessage(R.string.text_warning_exit_app)
            .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setNegativeButton(R.string.text_no, null)
            .show();
    }
}
