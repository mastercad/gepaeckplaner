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
public class LuggageListEntryDbModel extends DbModel {

    private Context context = null;
    private SQLiteDatabase.CursorFactory cursorFactory = null;

    LuggageListEntryDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        this.cursorFactory = factory;
    }

    public ArrayList<LuggageListEntryEntity> load() {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST_ENTRY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<LuggageListEntryEntity> collection = new ArrayList<>();
        
        while (cursor.moveToNext()) {
            LuggageListEntryEntity luggageListEntryEntity = new LuggageListEntryEntity();

            luggageListEntryEntity.setId(cursor.getLong(0));
            luggageListEntryEntity.setCount(cursor.getDouble(3));

            long luggageListFk = cursor.getLong(1);
            long luggageFk = cursor.getLong(2);

            luggageListEntryEntity.setLuggageListFk(luggageListFk);
            luggageListEntryEntity.setLuggageFk(luggageFk);

            LuggageListDbModel luggageListDbModel = new LuggageListDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            luggageListEntryEntity.setLuggageListEntity(luggageListDbModel.findLuggageById(luggageListFk));

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            luggageListEntryEntity.setLuggageEntity(luggageDbModel.findLuggageById(luggageFk));

            collection.add(luggageListEntryEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void insert(LuggageListEntryEntity luggageListEntryEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LUGGAGE_LIST_ENTRY_COUNT, luggageListEntryEntity.getCount());
        values.put(COLUMN_LUGGAGE_FK, luggageListEntryEntity.getLuggageFk());
        values.put(COLUMN_LUGGAGE_LIST_FK, luggageListEntryEntity.getLuggageListFk());

        SQLiteDatabase db = this.getWritableDatabase();

        luggageListEntryEntity.setId(db.insertOrThrow(TABLE_LUGGAGE_LIST_ENTRY, null, values));
        db.close();
    }

    public LuggageListEntryEntity findLuggageListEntryById(int luggageListEntryId) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST+" WHERE "+COLUMN_LUGGAGE_LIST_ENTRY_ID+" = '"+luggageListEntryId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageListEntryEntity luggageListEntryEntity = new LuggageListEntryEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageListEntryEntity.setId(cursor.getLong(0));
            luggageListEntryEntity.setCount(cursor.getDouble(3));
            long luggageListFk = cursor.getLong(1);
            long luggageFk = cursor.getLong(2);
            luggageListEntryEntity.setLuggageListFk(luggageListFk);
            luggageListEntryEntity.setLuggageFk(luggageFk);

            LuggageListDbModel luggageListDbModel = new LuggageListDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            luggageListEntryEntity.setLuggageListEntity(luggageListDbModel.findLuggageById(luggageListFk));

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            luggageListEntryEntity.setLuggageEntity(luggageDbModel.findLuggageById(luggageFk));
        } else {
            luggageListEntryEntity = null;
        }
        cursor.close();
        db.close();

        return luggageListEntryEntity;
    }

    public LuggageListEntryEntity findLuggageListByDate(String luggageListDate) {
        String query = "SELECT * FROM "+TABLE_LUGGAGE_LIST+" WHERE "+COLUMN_LUGGAGE_LIST_DATE+" = '"+luggageListDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LuggageListEntryEntity luggageListEntryEntity = new LuggageListEntryEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageListEntryEntity.setId(cursor.getLong(0));
            luggageListEntryEntity.setLuggageListFk(cursor.getLong(1));
            luggageListEntryEntity.setLuggageFk(cursor.getLong(2));
            luggageListEntryEntity.setCount(cursor.getDouble(3));
        } else {
            luggageListEntryEntity = null;
        }
        cursor.close();
        db.close();

        return luggageListEntryEntity;
    }
}
