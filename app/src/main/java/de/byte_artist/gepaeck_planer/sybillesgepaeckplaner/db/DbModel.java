package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbModel extends SQLiteOpenHelper {

    protected static final Integer DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "luggage.db";

    protected static final String TABLE_LUGGAGE = "luggage";
    protected static final String TABLE_LUGGAGE_CATEGORY = "luggage_category";
    protected static final String TABLE_PACKING_LIST = "packing_list";
    protected static final String TABLE_PACKING_LIST_ENTRY = "packing_list_entry";

    protected static final String COLUMN_LUGGAGE_ID = "luggage_id";
    protected static final String COLUMN_LUGGAGE_NAME = "luggage_name";
    protected static final String COLUMN_LUGGAGE_COUNT = "luggage_count";
    protected static final String COLUMN_LUGGAGE_WEIGHT = "luggage_weigh";
    protected static final String COLUMN_LUGGAGE_CATEGORY_FK = "luggage_category_fk";

    protected static final String COLUMN_LUGGAGE_CATEGORY_ID = "luggage_category_id";
    protected static final String COLUMN_LUGGAGE_CATEGORY_NAME = "luggage_category_name";

    protected static final String COLUMN_PACKING_LIST_ID = "packing_list_id";
    protected static final String COLUMN_PACKING_LIST_DATE = "packing_list_date";
    protected static final String COLUMN_PACKING_LIST_NAME = "packing_list_name";

    protected static final String COLUMN_PACKING_LIST_ENTRY_ID = "packing_list_entry_id";
    protected static final String COLUMN_PACKING_LIST_FK = "packing_list_fk";
    protected static final String COLUMN_LUGGAGE_FK = "luggage_fk";
    protected static final String COLUMN_PACKING_LIST_ENTRY_COUNT = "packing_list_entry_count";

    public DbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createLuggageCategoryTable(db)
            .createLuggageTable(db)
            .createPackingListTable(db)
            .createPackingListEntriesTable(db);
    }

    private DbModel createLuggageTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE+" ("+
            COLUMN_LUGGAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_NAME+" TEXT NOT NULL, "+
            COLUMN_LUGGAGE_CATEGORY_FK+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_WEIGHT+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_COUNT+" INTEGER NOT NULL, "+
//            "PRIMARY KEY ("+COLUMN_LUGGAGE_ID+", "+COLUMN_LUGGAGE_CATEGORY_FK+"),"+
            "FOREIGN KEY ("+COLUMN_LUGGAGE_CATEGORY_FK+") REFERENCES "+TABLE_LUGGAGE_CATEGORY+"("+COLUMN_LUGGAGE_CATEGORY_ID+") "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createLuggageCategoryTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE_CATEGORY+" ("+
            COLUMN_LUGGAGE_CATEGORY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_CATEGORY_NAME+" TEXT NOT NULL "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createPackingListTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_PACKING_LIST+" ("+
            COLUMN_PACKING_LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_PACKING_LIST_NAME+" TEXT NOT NULL, "+
            COLUMN_PACKING_LIST_DATE+" REAL NOT NULL "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createPackingListEntriesTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_PACKING_LIST_ENTRY+" ("+
            COLUMN_PACKING_LIST_ENTRY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_PACKING_LIST_FK+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_FK+" INTEGER NOT NULL, "+
            COLUMN_PACKING_LIST_ENTRY_COUNT+" REAL NOT NULL, "+
            "FOREIGN KEY ("+COLUMN_PACKING_LIST_FK+") REFERENCES "+TABLE_PACKING_LIST+"("+COLUMN_PACKING_LIST_ID+"), "+
            "FOREIGN KEY ("+COLUMN_LUGGAGE_FK+") REFERENCES "+TABLE_LUGGAGE+"("+COLUMN_LUGGAGE_ID+") "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
