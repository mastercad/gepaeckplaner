package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class LuggageListDbModel extends DbModel {

    LuggageListDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public ArrayList<LuggageListEntity> load() {
        String query = "SELECT luggage FROM "+TABLE_LUGGAGE_LIST+" INNER JOIN luggage ON luggage.luggage_id = luggage_list.luggage_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<LuggageListEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            LuggageListEntity luggageListEntity = new LuggageListEntity();

            luggageListEntity.setId(cursor.getLong(0));
            luggageListEntity.setName(cursor.getString(1));
            luggageListEntity.setDate(cursor.getString(2));

            collection.add(luggageListEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void insert(LuggageListEntity luggageListEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_LIST_NAME, luggageListEntity.getName());
        values.put(COLUMN_LUGGAGE_LIST_DATE, luggageListEntity.getDate());

        SQLiteDatabase db = this.getWritableDatabase();

        luggageListEntity.setId(db.insertOrThrow(TABLE_LUGGAGE_LIST, null, values));
        db.close();
    }

    public LuggageListEntity findLuggageById(long luggageListId) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST+" WHERE "+COLUMN_LUGGAGE_LIST_ID+" = '"+luggageListId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageListEntity luggageListEntity = new LuggageListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageListEntity.setId(cursor.getLong(0));
            luggageListEntity.setName(cursor.getString(1));
            luggageListEntity.setDate(cursor.getString(2));
        } else {
            luggageListEntity = null;
        }
        cursor.close();
        db.close();

        return luggageListEntity;
    }

    public LuggageListEntity findLuggageListByDate(String luggageListDate) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST+" WHERE "+COLUMN_LUGGAGE_LIST_DATE+" = '"+luggageListDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageListEntity luggageListEntity = new LuggageListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageListEntity.setId(cursor.getLong(0));
            luggageListEntity.setName(cursor.getString(1));
            luggageListEntity.setDate(cursor.getString(2));
        } else {
            luggageListEntity = null;
        }
        cursor.close();
        db.close();

        return luggageListEntity;
    }
}
