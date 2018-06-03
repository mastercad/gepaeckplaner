package de.byte_artist.luggage_planner.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.service.Preferences;

class DbModel extends SQLiteOpenHelper {

    static final Integer DATABASE_VERSION = 5;
    static final String DATABASE_NAME = "luggage.db";

    static final String TABLE_LUGGAGE = "luggage";
    static final String TABLE_LUGGAGE_CATEGORY = "luggage_category";
    static final String TABLE_PACKING_LIST = "packing_list";
    static final String TABLE_PACKING_LIST_ENTRY = "packing_list_entry";
    static final String TABLE_PREFERENCES = "preferences";

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

    static final String COLUMN_PREFERENCES_ID = "preferences_id";
    static final String COLUMN_PREFERENCES_NAME = "preferences_name";
    static final String COLUMN_PREFERENCES_VALUE = "preferences_value";

    final Context context;

    DbModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
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
            .createPackingListEntriesTable(db)
            .createPreferencesTable(db);
    }

    private DbModel createLuggageTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE+" ("+
            COLUMN_LUGGAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_NAME+" TEXT NOT NULL, "+
            COLUMN_LUGGAGE_CATEGORY_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE_CATEGORY+" ("+COLUMN_LUGGAGE_CATEGORY_ID+"), "+
            COLUMN_LUGGAGE_WEIGHT+" REAL NOT NULL, "+
            COLUMN_LUGGAGE_COUNT+" INTEGER NOT NULL, "+
            COLUMN_LUGGAGE_ACTIVE+" INTEGER NOT NULL DEFAULT 1, "+
            "UNIQUE ("+COLUMN_LUGGAGE_NAME+", "+COLUMN_LUGGAGE_CATEGORY_FK+") "+
        ");";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createLuggageCategoryTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_LUGGAGE_CATEGORY+" ("+
            COLUMN_LUGGAGE_CATEGORY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_LUGGAGE_CATEGORY_NAME+" TEXT NOT NULL UNIQUE "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createPackingListTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_PACKING_LIST+" ("+
            COLUMN_PACKING_LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_PACKING_LIST_NAME+" TEXT NOT NULL, "+
            COLUMN_PACKING_LIST_DATE+" REAL NOT NULL, "+
            "UNIQUE ("+COLUMN_PACKING_LIST_NAME+", "+COLUMN_PACKING_LIST_DATE+") "+
        ")";

        db.execSQL(createTable);

        return this;
    }

    private DbModel createPackingListEntriesTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_PACKING_LIST_ENTRY+" ("+
            COLUMN_PACKING_LIST_ENTRY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_PACKING_LIST_FK+" INTEGER NOT NULL REFERENCES "+TABLE_PACKING_LIST+" ("+COLUMN_PACKING_LIST_ID+"), "+
            COLUMN_LUGGAGE_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE+" ("+COLUMN_LUGGAGE_ID+"), "+
            COLUMN_PACKING_LIST_ENTRY_COUNT+" REAL NOT NULL, "+
            "UNIQUE ("+COLUMN_PACKING_LIST_FK+", "+COLUMN_LUGGAGE_FK+") "+
        ")";

        db.execSQL(createTable);

        return this;

    }

    private DbModel createPreferencesTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_PREFERENCES+" ("+
            COLUMN_PREFERENCES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            COLUMN_PREFERENCES_NAME+" TEXT NOT NULL UNIQUE, "+
            COLUMN_PREFERENCES_VALUE+" REAL NOT NULL "+
        ");";

        db.execSQL(createTable);

        float currentFontSize = context.getResources().getDimension(R.dimen.normal_text_size);

        String query = "INSERT INTO "+TABLE_PREFERENCES+" ("+COLUMN_PREFERENCES_NAME+", "+COLUMN_PREFERENCES_VALUE+") VALUES ('"+ Preferences.FONT_SIZE+"', '"+currentFontSize+"');";

        db.execSQL(query);

        return this;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTable;

        switch (newVersion) {
            case 1:
                createTable = "ALTER TABLE "+TABLE_LUGGAGE+" ADD "+COLUMN_LUGGAGE_ACTIVE+" INTEGER NOT NULL DEFAULT 1;";

                db.execSQL(createTable);
            case 2:
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
            case 4:
                createPreferencesTable(db);
                break;
            case 5:
                String mySql = " SELECT name FROM sqlite_master " + " WHERE type='table'";

                Cursor cursor = db.rawQuery(mySql, null);
                if (cursor.moveToFirst())
                {
                    do{
                        String content = cursor.getString(0);
                        Log.i("Show Tables", content);
                    }while (cursor.moveToNext());
                }
                cursor.close();

                if (db.inTransaction()) {
                    db.endTransaction();
                }

                db.setForeignKeyConstraintsEnabled(false);

                createTable = "CREATE TABLE luggage_category_temp AS SELECT * FROM "+TABLE_LUGGAGE_CATEGORY+";";

                db.execSQL(createTable);

                createTable = "DROP TABLE "+TABLE_LUGGAGE_CATEGORY+";";

                db.execSQL(createTable);

                createTable = "CREATE TABLE "+TABLE_LUGGAGE_CATEGORY+" ("+
                    COLUMN_LUGGAGE_CATEGORY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    COLUMN_LUGGAGE_CATEGORY_NAME+" TEXT NOT NULL UNIQUE"+
                ");";

                db.execSQL(createTable);

                createTable = "INSERT INTO "+TABLE_LUGGAGE_CATEGORY+" SELECT * FROM luggage_category_temp;";

                db.execSQL(createTable);

                createTable = "DROP TABLE luggage_category_temp;";

                db.execSQL(createTable);

                if (db.inTransaction()) {
                    db.endTransaction();
                }

                createTable = "CREATE TABLE packing_list_entry_tmp AS SELECT * FROM "+TABLE_PACKING_LIST_ENTRY+";";

                db.execSQL(createTable);

                createTable = "DROP TABLE "+TABLE_PACKING_LIST_ENTRY+";";

                db.execSQL(createTable);

                createTable = "CREATE TABLE "+TABLE_PACKING_LIST_ENTRY+" ("+
                    COLUMN_PACKING_LIST_ENTRY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    COLUMN_PACKING_LIST_FK+" INTEGER NOT NULL REFERENCES "+TABLE_PACKING_LIST+" ("+COLUMN_PACKING_LIST_ID+"), "+
                    COLUMN_LUGGAGE_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE+" ("+COLUMN_LUGGAGE_ID+"), "+
                    COLUMN_PACKING_LIST_ENTRY_COUNT+" REAL NOT NULL, "+
                    "UNIQUE ("+COLUMN_PACKING_LIST_FK+", "+COLUMN_LUGGAGE_FK+") "+
                ")";

                db.execSQL(createTable);

                createTable = "INSERT INTO "+TABLE_PACKING_LIST_ENTRY+" SELECT * FROM packing_list_entry_tmp;";

                db.execSQL(createTable);

                createTable = "DROP TABLE packing_list_entry_tmp;";

                db.execSQL(createTable);

                createTable = "CREATE TABLE luggage_temp AS SELECT * FROM "+TABLE_LUGGAGE+";";

                db.execSQL(createTable);

                createTable = "DROP TABLE "+TABLE_LUGGAGE+";";

                db.execSQL(createTable);

                createTable = "CREATE TABLE "+TABLE_LUGGAGE+" ("+
                    COLUMN_LUGGAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    COLUMN_LUGGAGE_NAME+" TEXT NOT NULL, "+
                    COLUMN_LUGGAGE_CATEGORY_FK+" INTEGER NOT NULL REFERENCES "+TABLE_LUGGAGE_CATEGORY+" ("+COLUMN_LUGGAGE_CATEGORY_ID+"), "+
                    COLUMN_LUGGAGE_WEIGHT+" REAL NOT NULL, "+
                    COLUMN_LUGGAGE_COUNT+" INTEGER NOT NULL, "+
                    COLUMN_LUGGAGE_ACTIVE+" INTEGER NOT NULL DEFAULT 1, "+
                    "UNIQUE ("+COLUMN_LUGGAGE_NAME+", "+COLUMN_LUGGAGE_CATEGORY_FK+") "+
                ");";

                db.execSQL(createTable);

                createTable = "INSERT INTO "+TABLE_LUGGAGE+" SELECT * FROM luggage_temp;";

                db.execSQL(createTable);

                createTable = "DROP TABLE luggage_temp;";

                db.execSQL(createTable);

                if (db.inTransaction()) {
                    db.endTransaction();
                }

                createTable = "CREATE TABLE packing_list_tmp AS SELECT * FROM "+TABLE_PACKING_LIST+";";

                db.execSQL(createTable);

                createTable = "DROP TABLE "+TABLE_PACKING_LIST+";";

                db.execSQL(createTable);

                createTable = "CREATE TABLE "+TABLE_PACKING_LIST+" ("+
                    COLUMN_PACKING_LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    COLUMN_PACKING_LIST_NAME+" TEXT NOT NULL, "+
                    COLUMN_PACKING_LIST_DATE+" REAL NOT NULL, "+
                    "UNIQUE ("+COLUMN_PACKING_LIST_NAME+", "+COLUMN_PACKING_LIST_DATE+") "+
                ")";

                db.execSQL(createTable);

                db.setForeignKeyConstraintsEnabled(false);

                createTable = "INSERT INTO "+TABLE_PACKING_LIST+" SELECT * FROM packing_list_tmp;";

                db.execSQL(createTable);

                createTable = "DROP TABLE packing_list_tmp;";

                db.execSQL(createTable);

                if (db.inTransaction()) {
                    db.endTransaction();
                }

                db.setForeignKeyConstraintsEnabled(false);

                db.beginTransaction();

                if (db.inTransaction()) {
                    db.endTransaction();
                }

                createTable = "CREATE TABLE preferences_tmp AS SELECT * FROM "+TABLE_PREFERENCES+";";

                db.execSQL(createTable);

                createTable = "DROP TABLE "+TABLE_PACKING_LIST_ENTRY+";";

                db.execSQL(createTable);

                createTable = "CREATE TABLE "+TABLE_PREFERENCES+" ("+
                    COLUMN_PREFERENCES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    COLUMN_PREFERENCES_NAME+" TEXT NOT NULL UNIQUE, "+
                    COLUMN_PREFERENCES_VALUE+" REAL NOT NULL "+
                ");";

                db.execSQL(createTable);

                createTable = "INSERT INTO "+TABLE_PREFERENCES+" SELECT * FROM preferences_tmp;";

                db.execSQL(createTable);

                createTable = "DROP TABLE preferences_tmp;";

                db.execSQL(createTable);

                if (db.inTransaction()) {
                    db.endTransaction();
                }

                db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
