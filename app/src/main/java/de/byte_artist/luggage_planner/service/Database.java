package de.byte_artist.luggage_planner.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.activity.SyncActivity;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class Database {

    private final Context context;

    public Database(Context context) {
        this.context = context;
    }

    public void recreateDatabase() {
        this.resetDatabase();

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context);
        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity(
            this.context.getResources().getString(R.string.category1)
        );

        luggageCategoryDbModel.insert(luggageCategoryEntity);

        PackingListDbModel luggageListDbModel = new PackingListDbModel(this.context);
        PackingListEntity luggageListEntity = new PackingListEntity(
            this.context.getResources().getString(R.string.packing_list1),
            "2018-08-04"
        );
        luggageListDbModel.insert(luggageListEntity);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this.context);
        LuggageEntity luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage1_category1),
            luggageCategoryEntity.getId(),
            65
        );
        luggageDbModel.insert(luggageEntity);

        PackingListEntryDbModel packingListDbModel = new PackingListEntryDbModel(this.context);
        PackingListEntryEntity packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
                2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage2_category1),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageCategoryEntity = new LuggageCategoryEntity(
            this.context.getResources().getString(R.string.category2)
        );
        luggageCategoryDbModel.insert(luggageCategoryEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage1_category2),
            luggageCategoryEntity.getId(),
            65
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage2_category2),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage3_category2),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageCategoryEntity = new LuggageCategoryEntity(
            this.context.getResources().getString(R.string.category3)
        );
        luggageCategoryDbModel.insert(luggageCategoryEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage1_category3),
            luggageCategoryEntity.getId(),
            65
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage2_category3),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage3_category3),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);
    }

    public void resetDatabase() {
        resetDatabase("luggage.db");
    }

    public void resetDatabase(String name) {
        String filePathName = this.context.getDatabasePath(name).toString();
        File file = new File(filePathName);

        if (file.exists()) {
            file.delete();
        }
    }

    public String exportDatabaseToJson() {
        JSONObject jsonContainer = new JSONObject();
        try {
            PackingListDbModel packingListDbModel = new PackingListDbModel(this.context);
            ArrayList<PackingListEntity> packingListCollection = packingListDbModel.load();
            JSONArray packingListCollectionJsonArray = new JSONArray();

            for (PackingListEntity packingListEntity: packingListCollection) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", packingListEntity.getId());
                jsonObject.put("date", packingListEntity.getDate());
                jsonObject.put("name", packingListEntity.getName());

                packingListCollectionJsonArray.put(jsonObject);
            }

            PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(this.context);
            ArrayList<PackingListEntryEntity> packingListEntryCollection = packingListEntryDbModel.load();
            JSONArray packingListEntryCollectionJsonArray = new JSONArray();

            for (PackingListEntryEntity packingListEntryEntity : packingListEntryCollection) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", packingListEntryEntity.getId());
                jsonObject.put("count", packingListEntryEntity.getCount());
                jsonObject.put("name", packingListEntryEntity.getId());
                jsonObject.put("luggageFk", packingListEntryEntity.getLuggageFk());
                jsonObject.put("packingListFk", packingListEntryEntity.getLuggageListFk());

                packingListEntryCollectionJsonArray.put(jsonObject);
            }

            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context);
            ArrayList<LuggageCategoryEntity> luggageCategoryCollection = luggageCategoryDbModel.load();
            JSONArray luggageCategoryCollectionJsonArray = new JSONArray();

            for (LuggageCategoryEntity luggageCategoryEntity : luggageCategoryCollection) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", luggageCategoryEntity.getId());
                jsonObject.put("name", luggageCategoryEntity.getName());

                luggageCategoryCollectionJsonArray.put(jsonObject);
            }

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context);
            ArrayList<LuggageEntity> luggageCollection = luggageDbModel.load();
            JSONArray luggageCollectionJsonArray = new JSONArray();

            for (LuggageEntity luggageEntity : luggageCollection) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", luggageEntity.getId());
                jsonObject.put("name", luggageEntity.getName());
                jsonObject.put("categoryFk", luggageEntity.getCategoryId());
                jsonObject.put("count", luggageEntity.getCount());
                jsonObject.put("weight", luggageEntity.getWeight());
                jsonObject.put("active", luggageEntity.isActive());

                luggageCollectionJsonArray.put(jsonObject);
            }

            jsonContainer.put("packingListEntries", packingListEntryCollectionJsonArray);
            jsonContainer.put("packingLists", packingListCollectionJsonArray);
            jsonContainer.put("luggageCategories", luggageCategoryCollectionJsonArray);
            jsonContainer.put("luggage", luggageCollectionJsonArray);
        } catch (JSONException exception) {
            Toast.makeText(context, "Error create database output content!", Toast.LENGTH_SHORT).show();
            Log.e("DATABASE SERVICE", "EXPORT DATABASE TO JSON: "+exception.getMessage(), exception);
        }

        return jsonContainer.toString();
    }

    /**
     * Import the given JSON String to database.
     *
     * @param jsonContent the JSON String content to import
     *
     * @return boolean value depending on import status
     */
    public boolean importDatabaseByJson(String jsonContent, BluetoothSyncService bluetoothSyncService) {
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONArray packingListEntryCollection = jsonObject.getJSONArray("packingListEntries");
            JSONArray packingListCollection = jsonObject.getJSONArray("packingLists");
            JSONArray luggageCategoryCollection = jsonObject.getJSONArray("luggageCategories");
            JSONArray luggageCollection = jsonObject.getJSONArray("luggage");

            resetDatabase();

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context);
            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context);
            PackingListDbModel packingListDbModel = new PackingListDbModel(this.context);
            PackingListEntryDbModel packingListEntryDbModel = new PackingListEntryDbModel(this.context);

            int i;

            for (i = 0; i < luggageCategoryCollection.length(); i++) {
                JSONObject row = luggageCategoryCollection.getJSONObject(i);
                LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity();
                luggageCategoryEntity.setId(row.getInt("id"));
                luggageCategoryEntity.setName(row.getString("name"));

                luggageCategoryDbModel.insert(luggageCategoryEntity);
            }

            notifyState(bluetoothSyncService, luggageCategoryCollection.length(), this.context.getString(R.string.label_category));

            for (i = 0; i < luggageCollection.length(); i++) {
                JSONObject row = luggageCollection.getJSONObject(i);
                LuggageEntity luggageEntity = new LuggageEntity();
                luggageEntity.setId(row.getInt("id"));
                luggageEntity.setName(row.getString("name"));
                luggageEntity.setCategoryId(row.getInt("categoryFk"));
                luggageEntity.setCount(row.getInt("count"));
                luggageEntity.setWeight(row.getInt("weight"));
                luggageEntity.setActive(row.getBoolean("active"));

                luggageDbModel.insert(luggageEntity);
            }

            notifyState(bluetoothSyncService, luggageCollection.length(), this.context.getString(R.string.label_luggage));

            for (i = 0; i < packingListCollection.length(); i++) {
                JSONObject row = packingListCollection.getJSONObject(i);
                PackingListEntity packingListEntity = new PackingListEntity();
                packingListEntity.setDate(row.getString("date"));
                packingListEntity.setId(row.getInt("id"));
                packingListEntity.setName(row.getString("name"));

                packingListDbModel.insert(packingListEntity);
            }

            notifyState(bluetoothSyncService, packingListCollection.length(), this.context.getString(R.string.label_packing_list));

            for (i = 0; i < packingListEntryCollection.length(); i++) {
                JSONObject row = packingListEntryCollection.getJSONObject(i);
                PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity();
                packingListEntryEntity.setId(row.getInt("id"));
                packingListEntryEntity.setCount(row.getInt("count"));
                packingListEntryEntity.setLuggageFk(row.getInt("luggageFk"));
                packingListEntryEntity.setLuggageListFk(row.getInt("packingListFk"));

                packingListEntryDbModel.insert(packingListEntryEntity);
            }

            notifyState(bluetoothSyncService, packingListEntryCollection.length(), this.context.getString(R.string.label_packing_list_entry));

            sendMessage(bluetoothSyncService, SyncActivity.MESSAGE_HEADER_STATUS_START);
            sendMessage(bluetoothSyncService, this.context.getString(R.string.text_data_successfully_saved));
            sendMessage(bluetoothSyncService, SyncActivity.MESSAGE_HEADER_STATUS_END);

            return true;

        } catch (JSONException exception) {
            Toast.makeText(context, R.string.error_convert_data, Toast.LENGTH_SHORT).show();
            Log.e("DATABASE SERVICE", "IMPORT DATABASE FROM JSON: "+exception.getMessage(), exception);
        }
        return false;
    }

    private void notifyState(BluetoothSyncService bluetoothSyncService, int count, String tableName) {
        sendMessage(bluetoothSyncService, SyncActivity.MESSAGE_HEADER_STATUS_START);
        sendMessage(bluetoothSyncService, count+" "+tableName+" "+this.context.getString(R.string.text_entries_processed));
        sendMessage(bluetoothSyncService, SyncActivity.MESSAGE_HEADER_STATUS_END);
    }

    private void sendMessage(BluetoothSyncService bluetoothSyncService, String message) {
        // Check that we're actually connected before trying anything
        if (bluetoothSyncService.getState() != BluetoothSyncService.STATE_CONNECTED) {
            Toast.makeText(this.context, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            bluetoothSyncService.write(message.getBytes());
        }
    }
}
