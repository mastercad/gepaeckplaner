package de.byte_artist.luggage_planner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.byte_artist.luggage_planner.entity.PreferencesEntity;
import de.byte_artist.luggage_planner.service.Preferences;

public class PreferencesDbModel extends DbModel {

    public PreferencesDbModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PreferencesDbModel(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public ArrayList<PreferencesEntity> load() {
        ArrayList<PreferencesEntity> preferencesEntities = new ArrayList<>();

        String query = "SELECT * FROM "+TABLE_PREFERENCES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            PreferencesEntity preferencesEntity = new PreferencesEntity();

            preferencesEntity.setId(Long.parseLong(cursor.getString(0)));
            preferencesEntity.setName(cursor.getString(1));
            preferencesEntity.setValue(cursor.getString(2));

            preferencesEntities.add(preferencesEntity);
        }
        cursor.close();
        db.close();

        return preferencesEntities;
    }

    public void update(PreferencesEntity preferencesEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PREFERENCES_NAME, preferencesEntity.getName());
        values.put(COLUMN_PREFERENCES_VALUE, preferencesEntity.getValue());

        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(preferencesEntity.getId())};
        db.update(TABLE_PREFERENCES, values, COLUMN_PREFERENCES_ID + " = ?", whereArgs);

        db.close();
        Preferences.refresh(context);
    }

    public void insert(PreferencesEntity preferencesEntity) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PREFERENCES_NAME, preferencesEntity.getName());
        values.put(COLUMN_PREFERENCES_VALUE, preferencesEntity.getValue());

        SQLiteDatabase db = this.getWritableDatabase();

        preferencesEntity.setId(db.insertOrThrow(TABLE_PREFERENCES, null, values));
        db.close();
        Preferences.refresh(context);
    }

    public void delete(PreferencesEntity preferencesEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String[] whereArgs = {Long.toString(preferencesEntity.getId())};
        db.delete(TABLE_PREFERENCES,COLUMN_PREFERENCES_ID + " = ?", whereArgs);
    }

    public PreferencesEntity findPreference(String preferenceName) {
        String query = "SELECT * FROM "+TABLE_PREFERENCES+" WHERE "+COLUMN_PREFERENCES_NAME+" LIKE('"+preferenceName+"');";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        PreferencesEntity preferencesEntity = new PreferencesEntity();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            preferencesEntity.setId(Long.parseLong(cursor.getString(0)));
            preferencesEntity.setName(cursor.getString(1));
            preferencesEntity.setValue(cursor.getString(2));

        } else {
            preferencesEntity = null;
        }
        cursor.close();
        db.close();
        Preferences.refresh(context);

        return preferencesEntity;
    }
}
