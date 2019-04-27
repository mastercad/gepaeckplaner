package de.byte_artist.luggage_planner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.byte_artist.luggage_planner.db.DbModel;

class Database extends DbModel {

    Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.setVersion(version);
    }

    public void setVersion(int version) {
        DATABASE_VERSION = version;
    }

    public ArrayList<String> showTables() {
        String query = "SELECT name FROM sqlite_master WHERE type='table'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> tables = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                tables.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return tables;
    }
}
