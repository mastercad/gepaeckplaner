package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class LuggageDbModel extends DbModel {

    LuggageDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public ArrayList<LuggageEntity> load() {
        String query = "SELECT * FROM "+TABLE_LUGGAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<LuggageEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            LuggageEntity luggageEntity = new LuggageEntity();

            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setCategoryId(cursor.getInt(2));
            luggageEntity.setWeight(cursor.getDouble(3));

            collection.add(luggageEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void insert(LuggageEntity luggageEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_NAME, luggageEntity.getName());
        values.put(COLUMN_LUGGAGE_CATEGORY_FK, luggageEntity.getCategoryId());
        values.put(COLUMN_LUGGAGE_WEIGHT, luggageEntity.getWeight());

        SQLiteDatabase db = this.getWritableDatabase();

        luggageEntity.setId(db.insertOrThrow(TABLE_LUGGAGE, null, values));
        db.close();
    }

    public LuggageEntity findLuggage(String luggageName) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE+" WHERE "+COLUMN_LUGGAGE_NAME+" = '"+luggageName+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageEntity luggageEntity = new LuggageEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setCategoryId(cursor.getInt(2));
            luggageEntity.setWeight(cursor.getDouble(3));
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

            luggageEntity.setId(cursor.getLong(0));
            luggageEntity.setName(cursor.getString(1));
            luggageEntity.setCategoryId(cursor.getInt(2));
            luggageEntity.setWeight(cursor.getDouble(3));
        } else {
            luggageEntity = null;
        }
        cursor.close();
        db.close();

        return luggageEntity;

    }
}
