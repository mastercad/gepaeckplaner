package de.byte_artist.luggage_planner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.byte_artist.luggage_planner.entity.PackingListEntity;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class PackingListDbModel extends DbModel {

    public PackingListDbModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PackingListDbModel(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public ArrayList<PackingListEntity> load() {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST+" ORDER BY "+COLUMN_PACKING_LIST_DATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<PackingListEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            PackingListEntity packingListEntity = new PackingListEntity();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setName(cursor.getString(1));
            packingListEntity.setDate(cursor.getString(2));

            collection.add(packingListEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void update(PackingListEntity packingListEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PACKING_LIST_NAME, packingListEntity.getName());
        values.put(COLUMN_PACKING_LIST_DATE, packingListEntity.getDate());

        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(packingListEntity.getId())};
        db.update(TABLE_PACKING_LIST, values, COLUMN_PACKING_LIST_ID + " = ?", whereArgs);
        db.close();
    }

    public void insert(PackingListEntity packingListEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PACKING_LIST_NAME, packingListEntity.getName());
        values.put(COLUMN_PACKING_LIST_DATE, packingListEntity.getDate());

        SQLiteDatabase db = this.getWritableDatabase();

        packingListEntity.setId(db.insertOrThrow(TABLE_PACKING_LIST, null, values));
        db.close();
    }

    public void delete(PackingListEntity packingListEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgsDeletePackingListEntry = {Long.toString(packingListEntity.getId())};
        db.delete(TABLE_PACKING_LIST_ENTRY, COLUMN_PACKING_LIST_FK + " = ?", whereArgsDeletePackingListEntry);

        final String[] whereArgsDeletePackingList = {Long.toString(packingListEntity.getId())};
        db.delete(TABLE_PACKING_LIST,COLUMN_PACKING_LIST_ID + " = ?", whereArgsDeletePackingList);
    }

    public PackingListEntity findPackingListById(long luggageListId) {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST+
            " LEFT JOIN "+TABLE_PACKING_LIST_ENTRY+" ON "+TABLE_PACKING_LIST_ENTRY+"."+COLUMN_PACKING_LIST_FK+" = "+TABLE_PACKING_LIST+"."+COLUMN_PACKING_LIST_ID+
            " LEFT JOIN "+TABLE_LUGGAGE+" ON "+TABLE_LUGGAGE+"."+COLUMN_LUGGAGE_ID+" = "+TABLE_PACKING_LIST_ENTRY+"."+COLUMN_LUGGAGE_FK+
            " LEFT JOIN "+TABLE_LUGGAGE_CATEGORY+" ON "+TABLE_LUGGAGE_CATEGORY+"."+COLUMN_LUGGAGE_CATEGORY_ID+" = "+TABLE_LUGGAGE+"."+COLUMN_LUGGAGE_CATEGORY_FK+
            " WHERE "+COLUMN_PACKING_LIST_ID+" = '"+ luggageListId+ "' "+
            " ORDER BY "+COLUMN_LUGGAGE_CATEGORY_FK+", "+COLUMN_LUGGAGE_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntity packingListEntity = new PackingListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setName(cursor.getString(1));
            packingListEntity.setDate(cursor.getString(2));
        } else {
            packingListEntity = null;
        }
        cursor.close();
        db.close();

        return packingListEntity;
    }

    public PackingListEntity findPackingListByDate(String packingListDate) {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST+" WHERE "+COLUMN_PACKING_LIST_DATE+" = '"+packingListDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntity packingListEntity = new PackingListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setName(cursor.getString(1));
            packingListEntity.setDate(cursor.getString(2));
        } else {
            packingListEntity = null;
        }
        cursor.close();
        db.close();

        return packingListEntity;
    }

    public PackingListEntity findCurrentPackingList() {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST+" WHERE "+COLUMN_PACKING_LIST_DATE+" >= date('now') ORDER BY "+COLUMN_PACKING_LIST_DATE+" LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntity packingListEntity = new PackingListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setName(cursor.getString(1));
            packingListEntity.setDate(cursor.getString(2));
        } else {
            packingListEntity = null;
        }
        cursor.close();
        db.close();

        return packingListEntity;
    }
}
