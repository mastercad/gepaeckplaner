package de.byte_artist.luggage_planner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

public class LuggageCategoryDbModel extends DbModel {

    public LuggageCategoryDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public ArrayList<LuggageCategoryEntity> load() {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<LuggageCategoryEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity();

            luggageCategoryEntity.setId(Integer.parseInt(cursor.getString(0)));
            luggageCategoryEntity.setName(cursor.getString(1));

            collection.add(luggageCategoryEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void update(LuggageCategoryEntity luggageCategoryEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_CATEGORY_NAME, luggageCategoryEntity.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(luggageCategoryEntity.getId())};
        db.update(TABLE_LUGGAGE_CATEGORY, values, COLUMN_LUGGAGE_CATEGORY_ID + " = ?", whereArgs);
    }

    public void insert(LuggageCategoryEntity luggageCategoryEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_CATEGORY_NAME, luggageCategoryEntity.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        luggageCategoryEntity.setId(db.insertOrThrow(TABLE_LUGGAGE_CATEGORY, null, values));
        db.close();
    }

    public void delete(LuggageCategoryEntity luggageCategoryEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(luggageCategoryEntity.getId())};
        db.delete(TABLE_LUGGAGE_CATEGORY,COLUMN_LUGGAGE_CATEGORY_ID + " = ?", whereArgs);
    }

    public LuggageCategoryEntity findCategoryByName(String categoryName) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_CATEGORY+" WHERE "+COLUMN_LUGGAGE_CATEGORY_NAME+" = '"+categoryName+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageCategoryEntity.setId(cursor.getLong(0));
            luggageCategoryEntity.setName(cursor.getString(1));
        } else {
            luggageCategoryEntity = null;
        }
        cursor.close();
        db.close();

        return luggageCategoryEntity;

    }

    public LuggageCategoryEntity findCategoryById(long categoryId) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_CATEGORY+" WHERE "+COLUMN_LUGGAGE_CATEGORY_ID+" = '"+categoryId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageCategoryEntity.setId(cursor.getLong(0));
            luggageCategoryEntity.setName(cursor.getString(1));
        } else {
            luggageCategoryEntity = null;
        }
        cursor.close();
        db.close();

        return luggageCategoryEntity;

    }
}
