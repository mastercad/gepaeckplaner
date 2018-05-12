package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageCategoryEntity;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class LuggageDbModel extends DbModel {

    private Context context = null;
    private SQLiteDatabase.CursorFactory cursorFactory = null;

    public LuggageDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        this.cursorFactory = factory;
    }

    public ArrayList<LuggageEntity> load() {
        String query = "SELECT * FROM "+TABLE_LUGGAGE+" ORDER BY "+COLUMN_LUGGAGE_CATEGORY_FK+", "+COLUMN_LUGGAGE_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<LuggageEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            LuggageEntity luggageEntity = new LuggageEntity();
            long luggageCategoryId = cursor.getInt(2);
            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setCategoryId(luggageCategoryId);
            luggageEntity.setWeight(cursor.getInt(3));
            luggageEntity.setCount(cursor.getInt(4));

            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            LuggageCategoryEntity luggageCategoryEntity = luggageCategoryDbModel.findCategoryById(luggageCategoryId);

            luggageEntity.setCategoryEntity(luggageCategoryEntity);

            collection.add(luggageEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public int findMaxLuggageCountByCategory(long luggageCategoryId) {
        String query = "SELECT MAX("+COLUMN_LUGGAGE_COUNT+") FROM "+TABLE_LUGGAGE+" WHERE "+COLUMN_LUGGAGE_CATEGORY_FK+" = "+luggageCategoryId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return id;
    }

    public ArrayList<LuggageEntity> findLuggageByCategoryId(long luggageCategoryId) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE+" WHERE "+COLUMN_LUGGAGE_CATEGORY_FK+" = "+
                luggageCategoryId+" ORDER BY "+COLUMN_LUGGAGE_CATEGORY_FK+", "+COLUMN_LUGGAGE_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<LuggageEntity> collection = new ArrayList<>();

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
        LuggageCategoryEntity luggageCategoryEntity = luggageCategoryDbModel.findCategoryById(luggageCategoryId);

        while (cursor.moveToNext()) {
            LuggageEntity luggageEntity = new LuggageEntity();
            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setCategoryId(luggageCategoryId);
            luggageEntity.setWeight(cursor.getInt(3));
            luggageEntity.setCategoryEntity(luggageCategoryEntity);

            collection.add(luggageEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void insert(LuggageEntity luggageEntity) {
        ContentValues values = new ContentValues();

        int currentMaxLuggageId = this.findMaxLuggageCountByCategory(luggageEntity.getCategoryId());
        luggageEntity.setCount(++currentMaxLuggageId);

        values.put(COLUMN_LUGGAGE_COUNT, luggageEntity.getCount());
        values.put(COLUMN_LUGGAGE_NAME, luggageEntity.getName());
        values.put(COLUMN_LUGGAGE_CATEGORY_FK, luggageEntity.getCategoryId());
        values.put(COLUMN_LUGGAGE_WEIGHT, luggageEntity.getWeight());

        SQLiteDatabase db = this.getWritableDatabase();

        luggageEntity.setId(db.insertOrThrow(TABLE_LUGGAGE, null, values));
        db.close();
    }

    public void update(LuggageEntity luggageEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_NAME, luggageEntity.getName());
        values.put(COLUMN_LUGGAGE_CATEGORY_FK, luggageEntity.getCategoryId());
        values.put(COLUMN_LUGGAGE_WEIGHT, luggageEntity.getWeight());

        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(luggageEntity.getId())};
        db.update(TABLE_LUGGAGE, values, COLUMN_LUGGAGE_ID + " = ?", whereArgs);
        db.close();
    }

    public void delete(LuggageEntity luggageEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(luggageEntity.getId())};
        db.delete(TABLE_LUGGAGE,COLUMN_LUGGAGE_ID + " = ?", whereArgs);
    }

    public LuggageEntity findLuggageByName(String luggageName) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE+" WHERE "+COLUMN_LUGGAGE_NAME+" = '"+luggageName+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageEntity luggageEntity = new LuggageEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setCategoryId(cursor.getInt(2));
            luggageEntity.setWeight(cursor.getInt(3));
            luggageEntity.setCount(cursor.getInt(4));
        } else {
            luggageEntity = null;
        }
        cursor.close();
        db.close();

        return luggageEntity;

    }

    public LuggageEntity findLuggageById(long luggageId) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE+" WHERE "+COLUMN_LUGGAGE_ID+" = '"+luggageId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageEntity luggageEntity = new LuggageEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            long categoryId = cursor.getLong(2);
            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setWeight(cursor.getInt(3));
            luggageEntity.setCategoryId(categoryId);
            luggageEntity.setCount(cursor.getInt(4));

            LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            LuggageCategoryEntity luggageCategoryEntity = luggageCategoryDbModel.findCategoryById(categoryId);

            luggageEntity.setCategoryEntity(luggageCategoryEntity);
        } else {
            luggageEntity = null;
        }
        cursor.close();
        db.close();

        return luggageEntity;

    }
}
