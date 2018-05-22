package de.byte_artist.luggage_planner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DbModel extends SQLiteOpenHelper {

    static final Integer DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "luggage.db";

    static final String TABLE_LUGGAGE = "luggage";
    static final String TABLE_LUGGAGE_CATEGORY = "luggage_category";
    static final String TABLE_PACKING_LIST = "packing_list";
    static final String TABLE_PACKING_LIST_ENTRY = "packing_list_entry";

    static final String COLUMN_LUGGAGE_ID = "luggage_id";
    static final String COLUMN_LUGGAGE_NAME = "luggage_name";
    static final String COLUMN_LUGGAGE_COUNT = "luggage_count";
    static final String COLUMN_LUGGAGE_WEIGHT = "luggage_weigh";
    static final String COLUMN_LUGGAGE_ACTIVE = "luggage_active";
    static final String COLUMN_LUGGAGE_CATEGORY_FK = "luggage_category_fk";

    static final String COLUMN_LUGGAGE_CATEGORY_ID = "luggage_category_id";
    static final String COLUMN_LUGGAGE_CATEGORY_NAME = "luggage_category_name";

    static final String COLUMN_PACKING_LIST_ID = "packing_list_id";
    static final String COLUMN_PACKING_LIST_DATE = "packing_list_date";
    static final String COLUMN_PACKING_LIST_NAME = "packing_list_name";

    static final String COLUMN_PACKING_LIST_ENTRY_ID = "packing_list_entry_id";
    static final String COLUMN_PACKING_LIST_FK = "packing_list_fk";
    static final String COLUMN_LUGGAGE_FK = "luggage_fk";
    static final String COLUMN_PACKING_LIST_ENTRY_COUNT = "packing_list_entry_count";

    final Context context;
    final SQLiteDatabase.CursorFactory factory;

    DbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        this.factory = factory;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
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
            COLUMN_LUGGAGE_CATEGORY_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE_CATEGORY+" ("+COLUMN_LUGGAGE_CATEGORY_ID+"), "+
            COLUMN_LUGGAGE_WEIGHT+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_COUNT+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_ACTIVE+" INTEGER NOT NULL DEFAULT 1 "+
        ");";

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

    private void createPackingListEntriesTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_PACKING_LIST_ENTRY+" ("+
            COLUMN_PACKING_LIST_ENTRY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_PACKING_LIST_FK+" INTEGER NOT NULL REFERENCES "+TABLE_PACKING_LIST+" ("+COLUMN_PACKING_LIST_ID+"), "+
            COLUMN_LUGGAGE_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE+" ("+COLUMN_LUGGAGE_ID+"), "+
            COLUMN_PACKING_LIST_ENTRY_COUNT+" REAL NOT NULL "+
        ")";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTable = "";

        if (1 == oldVersion
            && 2 == newVersion
        ) {
            createTable = "ALTER TABLE "+TABLE_LUGGAGE+" ADD "+COLUMN_LUGGAGE_ACTIVE+" INTEGER NOT NULL DEFAULT 1;";

            db.execSQL(createTable);
        } else if (2 == oldVersion
            && 3 == newVersion
        ) {
            if (db.inTransaction()) {
                db.endTransaction();
            }

            db.setForeignKeyConstraintsEnabled(false);

            createTable = "CREATE TABLE luggage_temp AS SELECT * FROM "+TABLE_LUGGAGE+";";

            db.execSQL(createTable);

            createTable = "DROP TABLE "+TABLE_LUGGAGE;

            db.execSQL(createTable);

            createTable = "CREATE TABLE "+TABLE_LUGGAGE+" ("+
                COLUMN_LUGGAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                COLUMN_LUGGAGE_NAME+" TEXT NOT NULL, "+
                COLUMN_LUGGAGE_CATEGORY_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE_CATEGORY+" ("+COLUMN_LUGGAGE_CATEGORY_ID+"), "+
                COLUMN_LUGGAGE_WEIGHT+" REAL NOT NULL DEFAULT 0, "+
                COLUMN_LUGGAGE_COUNT+" INTEGER NOT NULL, "+
                COLUMN_LUGGAGE_ACTIVE+" INTEGER NOT NULL DEFAULT 1 "+
            ");";

            db.execSQL(createTable);

            createTable = "INSERT INTO "+TABLE_LUGGAGE+" SELECT * FROM luggage_temp;";

            db.execSQL(createTable);

            createTable = "DROP TABLE luggage_temp";

            db.execSQL(createTable);

            if (db.inTransaction()) {
                db.endTransaction();
            }

            db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
