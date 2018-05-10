package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DbModel extends SQLiteOpenHelper {

    protected static final Integer DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "luggage.db";

    protected static final String TABLE_LUGGAGE = "luggage";
    protected static final String TABLE_LUGGAGE_CATEGORY = "luggage_category";
    protected static final String TABLE_LUGGAGE_LIST = "luggage_list";
    protected static final String TABLE_LUGGAGE_LIST_ENTRY = "luggage_list_entry";

    protected static final String COLUMN_LUGGAGE_ID = "luggage_id";
    protected static final String COLUMN_LUGGAGE_NAME = "luggage_name";
    protected static final String COLUMN_LUGGAGE_WEIGHT = "luggage_weigh";
    protected static final String COLUMN_LUGGAGE_CATEGORY_FK = "luggage_category_fk";

    protected static final String COLUMN_LUGGAGE_CATEGORY_ID = "luggage_category_id";
    protected static final String COLUMN_LUGGAGE_CATEGORY_NAME = "luggage_category_name";

    protected static final String COLUMN_LUGGAGE_LIST_ID = "luggage_list_id";
    protected static final String COLUMN_LUGGAGE_LIST_DATE = "luggage_list_date";
    protected static final String COLUMN_LUGGAGE_LIST_NAME = "luggage_list_name";

    protected static final String COLUMN_LUGGAGE_LIST_ENTRY_ID = "luggage_list_entry_id";
    protected static final String COLUMN_LUGGAGE_LIST_FK = "luggage_list_fk";
    protected static final String COLUMN_LUGGAGE_FK = "luggage_fk";
    protected static final String COLUMN_LUGGAGE_LIST_ENTRY_COUNT = "luggage_list_entry_count";

    public DbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createLuggageCategoryTable(db)
            .createLuggageTable(db)
            .createLuggageListTable(db)
            .createLuggageListEntriesTable(db);
    }

    private DbModel createLuggageTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE+" ("+
            COLUMN_LUGGAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_NAME+" TEXT NOT NULL, "+
            COLUMN_LUGGAGE_CATEGORY_FK+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_WEIGHT+" INTEGER NOT NULL, "+
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

    private DbModel createLuggageListTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE_LIST+" ("+
            COLUMN_LUGGAGE_LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_LIST_NAME+" TEXT NOT NULL, "+
            COLUMN_LUGGAGE_LIST_DATE+" REAL NOT NULL "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createLuggageListEntriesTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE_LIST_ENTRY+" ("+
            COLUMN_LUGGAGE_LIST_ENTRY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_LIST_FK+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_FK+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_LIST_ENTRY_COUNT+" REAL NOT NULL, "+
            "FOREIGN KEY ("+COLUMN_LUGGAGE_LIST_FK+") REFERENCES "+TABLE_LUGGAGE_LIST+"("+COLUMN_LUGGAGE_LIST_ID+"), "+
            "FOREIGN KEY ("+COLUMN_LUGGAGE_FK+") REFERENCES "+TABLE_LUGGAGE+"("+COLUMN_LUGGAGE_ID+") "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
