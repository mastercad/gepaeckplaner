package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class PackingListDbModel extends DbModel {

    private Context context = null;
    private SQLiteDatabase.CursorFactory cursorFactory = null;

    PackingListDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        this.cursorFactory = factory;
    }

    public ArrayList<PackingListEntity> load() {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST_ENTRY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<PackingListEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            PackingListEntity packingListEntity = new PackingListEntity();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setCount(cursor.getDouble(3));

            long luggageListFk = cursor.getLong(1);
            long luggageFk = cursor.getLong(2);

            packingListEntity.setLuggageListFk(luggageListFk);
            packingListEntity.setLuggageFk(luggageFk);

            LuggageListDbModel luggageListDbModel = new LuggageListDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntity.setLuggageListEntity(luggageListDbModel.findLuggageById(luggageListFk));

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntity.setLuggageEntity(luggageDbModel.findLuggageById(luggageFk));

            collection.add(packingListEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void insert(PackingListEntity packingListEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_LIST_ENTRY_COUNT, packingListEntity.getCount());
        values.put(COLUMN_LUGGAGE_FK, packingListEntity.getLuggageFk());
        values.put(COLUMN_LUGGAGE_LIST_FK, packingListEntity.getLuggageListFk());

        SQLiteDatabase db = this.getWritableDatabase();

        packingListEntity.setId(db.insertOrThrow(TABLE_LUGGAGE_LIST_ENTRY, null, values));
        db.close();
    }

    public PackingListEntity findLuggageListEntryById(int luggageListEntryId) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST+" WHERE "+COLUMN_LUGGAGE_LIST_ENTRY_ID+" = '"+luggageListEntryId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntity packingListEntity = new PackingListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setCount(cursor.getDouble(3));
            long luggageListFk = cursor.getLong(1);
            long luggageFk = cursor.getLong(2);
            packingListEntity.setLuggageListFk(luggageListFk);
            packingListEntity.setLuggageFk(luggageFk);

            LuggageListDbModel luggageListDbModel = new LuggageListDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntity.setLuggageListEntity(luggageListDbModel.findLuggageById(luggageListFk));

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntity.setLuggageEntity(luggageDbModel.findLuggageById(luggageFk));
        } else {
            packingListEntity = null;
        }
        cursor.close();
        db.close();

        return packingListEntity;
    }

    public PackingListEntity findLuggageListByDate(String luggageListDate) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST+" WHERE "+COLUMN_LUGGAGE_LIST_DATE+" = '"+luggageListDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntity packingListEntity = new PackingListEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            packingListEntity.setId(cursor.getLong(0));
            packingListEntity.setLuggageListFk(cursor.getLong(1));
            packingListEntity.setLuggageFk(cursor.getLong(2));
            packingListEntity.setCount(cursor.getDouble(3));
        } else {
            packingListEntity = null;
        }
        cursor.close();
        db.close();

        return packingListEntity;
    }
}
