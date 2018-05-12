package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.LuggageEntity;
import de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity.PackingListEntryEntity;

// https://dzone.com/articles/create-a-database-android-application-in-android-s

/**
 *
 */
public class PackingListEntryDbModel extends DbModel {

    private Context context = null;
    private SQLiteDatabase.CursorFactory cursorFactory = null;

    public PackingListEntryDbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        this.cursorFactory = factory;
    }

    public ArrayList<PackingListEntryEntity> load() {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST_ENTRY+" ORDER BY "+COLUMN_PACKING_LIST_FK+", "+COLUMN_LUGGAGE_FK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<PackingListEntryEntity> collection = new ArrayList<>();

        while (cursor.moveToNext()) {
            PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity();

            packingListEntryEntity.setId(cursor.getLong(0));
            packingListEntryEntity.setCount(cursor.getInt(3));

            long packingListFk = cursor.getLong(1);
            long luggageFk = cursor.getLong(2);

            packingListEntryEntity.setLuggageListFk(packingListFk);
            packingListEntryEntity.setLuggageFk(luggageFk);

            PackingListDbModel packingListDbModel = new PackingListDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntryEntity.setPackingListEntity(packingListDbModel.findPackingListById(packingListFk));

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntryEntity.setLuggageEntity(luggageDbModel.findLuggageById(luggageFk));

            collection.add(packingListEntryEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public void update(PackingListEntryEntity packingListEntryEntity) {
    }

    public void insert(PackingListEntryEntity packingListEntryEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PACKING_LIST_ENTRY_COUNT, packingListEntryEntity.getCount());
        values.put(COLUMN_LUGGAGE_FK, packingListEntryEntity.getLuggageFk());
        values.put(COLUMN_PACKING_LIST_FK, packingListEntryEntity.getLuggageListFk());

        SQLiteDatabase db = this.getWritableDatabase();

        packingListEntryEntity.setId(db.insertOrThrow(TABLE_PACKING_LIST_ENTRY, null, values));
        db.close();
    }

    public void delete(PackingListEntryEntity packingListEntryEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(packingListEntryEntity.getId())};
        db.delete(TABLE_PACKING_LIST_ENTRY,COLUMN_PACKING_LIST_ENTRY_ID + " = ?", whereArgs);
    }

    public ArrayList<PackingListEntryEntity> findPackingListById(long packingListId) {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST_ENTRY+" WHERE "+COLUMN_PACKING_LIST_FK+" = '"+packingListId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<PackingListEntryEntity> collection = new ArrayList<>();

        while (cursor.moveToNext()) {
            PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity();

            packingListEntryEntity.setId(cursor.getLong(0));
            packingListEntryEntity.setCount(cursor.getInt(3));
            long packingListFk = cursor.getLong(1);
            long luggageFk = cursor.getLong(2);
            packingListEntryEntity.setLuggageListFk(packingListFk);
            packingListEntryEntity.setLuggageFk(luggageFk);

            PackingListDbModel packingListDbModel = new PackingListDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntryEntity.setPackingListEntity(packingListDbModel.findPackingListById(packingListFk));

            LuggageDbModel luggageDbModel = new LuggageDbModel(this.context, DATABASE_NAME, this.cursorFactory, DATABASE_VERSION);
            packingListEntryEntity.setLuggageEntity(luggageDbModel.findLuggageById(luggageFk));

            collection.add(packingListEntryEntity);
        }
        cursor.close();
        db.close();

        return collection;
    }

    public PackingListEntryEntity findPackingListByDate(String packingListDate) {
        String query = "SELECT * FROM "+TABLE_PACKING_LIST+" WHERE "+COLUMN_PACKING_LIST_DATE+" = '"+packingListDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            packingListEntryEntity.setId(cursor.getLong(0));
            packingListEntryEntity.setLuggageListFk(cursor.getLong(1));
            packingListEntryEntity.setLuggageFk(cursor.getLong(2));
            packingListEntryEntity.setCount(cursor.getInt(3));
        } else {
            packingListEntryEntity = null;
        }
        cursor.close();
        db.close();

        return packingListEntryEntity;
    }

    public boolean checkLuggageUsed(LuggageEntity luggageEntity) {
        String query = "SELECT COUNT("+COLUMN_PACKING_LIST_ENTRY_ID+") FROM "+TABLE_PACKING_LIST_ENTRY+" WHERE "+COLUMN_LUGGAGE_FK+" = '"+luggageEntity.getId()+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PackingListEntryEntity packingListEntryEntity = new PackingListEntryEntity();
        int luggageCount = 0;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            luggageCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return luggageCount > 0;
    }
}
