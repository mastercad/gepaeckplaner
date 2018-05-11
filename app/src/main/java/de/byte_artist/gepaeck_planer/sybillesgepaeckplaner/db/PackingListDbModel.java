package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntity;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class PackingListDbModel extends DbModel {

    private Context context = null;
    private SQLiteDatabase.CursorFactory cursorFactory = null;

    public PackingListDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        this.cursorFactory = factory;
    }

    public ArrayList<PackingListEntity> load() {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST;

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

    public PackingListEntity findPackingListById(long luggageListId) {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST+" WHERE "+COLUMN_PACKING_LIST_ID+" = '"+luggageListId+"'";

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
}
