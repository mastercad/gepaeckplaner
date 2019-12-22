package de.byte_artist.luggage_planner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import de.byte_artist.luggage_planner.service.Database;
// import de.byte_artist.luggage_planner.test.R;

import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("UnusedAssignment")
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    static private final String DB_NAME = "test_luggage.db";
//    static private final String DB_NAME = R.;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void createV1() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V1:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 1);
        String query = "SELECT luggage_active FROM luggage";
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("no such column: luggage_active (code 1 SQLITE_ERROR): , while compiling: SELECT luggage_active FROM luggage");

        sqLiteDatabase.execSQL(query);
    }

    @Test
    public void migrateV1V2() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try (DatabaseHelper databaseHelper = new DatabaseHelper(context)) {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V2:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        String query = "SELECT luggage_active FROM luggage";

        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation for foreign keys of luggage
     */
    public void migrateV1V3() throws SQLiteException{
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V3:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)");

        // it is not possible anymore to insert luggage without matching luggage category
        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage 2', 2, 5, 2);";

        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation for preferences
     */
    public void migrateV1V4() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V4:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 4);
        ArrayList<String> tables = database.showTables();

        boolean found = false;
        for (String table: tables) {
            if (table.equals("preferences")) {
                found = true;
            }
        }

        assertTrue(found);
        String query = "SELECT luggage_active FROM luggage";
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique category names
     */
    public void migrateV1V5EnsureCategoryNameIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V5EnsureCategoryNameIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage_category.luggage_category_name (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique luggage entries
     */
    public void migrateV1V5EnsureLuggageEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V5EnsureLuggageEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage.luggage_name, luggage.luggage_category_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        // it is not possible anymore to insert luggage without matching luggage category
        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 5, 2);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing lists
     */
    public void migrateV1V5EnsurePackingListIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V5EnsurePackingListIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list.packing_list_name, packing_list.packing_list_date (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing list entries
     */
    public void migrateV1V5EnsurePackingListEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV1V5EnsurePackingListEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
            "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
            "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list_entry.packing_list_fk, packing_list_entry.luggage_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation for foreign keys of luggage
     */
    public void migrateV2V3() throws SQLiteException{
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV2V3:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)");

        // it is not possible anymore to insert luggage without matching luggage category
        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage 2', 2, 5, 2);";

        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation for preferences
     */
    public void migrateV2V4() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV2V4:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 4);
        sqLiteDatabase = database.getReadableDatabase();

        ArrayList<String> tables = database.showTables();

        boolean found = false;
        for (String table: tables) {
            if (table.equals("preferences")) {
                found = true;
            }
        }

        assertTrue(found);

        String query = "SELECT luggage_active FROM luggage";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique category names
     */
    public void migrateV2V5EnsureCategoryNameIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV2V5EnsureCategoryNameIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage_category.luggage_category_name (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique luggage entries
     */
    public void migrateV2V5EnsureLuggageEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV2V5EnsureLuggageEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage.luggage_name, luggage.luggage_category_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        // it is not possible anymore to insert luggage without matching luggage category
        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 5, 2);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing lists
     */
    public void migrateV2V5EnsurePackingListIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV2V5EnsurePackingListIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list.packing_list_name, packing_list.packing_list_date (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing list entries
     */
    public void migrateV2V5EnsurePackingListEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV2V5EnsurePackingListEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 2);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
            "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
            "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list_entry.packing_list_fk, packing_list_entry.luggage_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation for preferences
     */
    public void migrateV3V4() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV3V4:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 4);
        sqLiteDatabase = database.getReadableDatabase();

        ArrayList<String> tables = database.showTables();

        boolean found = false;
        for (String table: tables) {
            if (table.equals("preferences")) {
                found = true;
            }
        }

        assertTrue(found);
        String query = "SELECT luggage_active FROM luggage";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique category names
     */
    public void migrateV3V5EnsureCategoryNameIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV3V5EnsureCategoryNameIsUnique:", Objects.requireNonNull(exception.getMessage()), exception);
        }

        de.byte_artist.luggage_planner.Database database;
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage_category.luggage_category_name (code 2067 SQLITE_CONSTRAINT_UNIQUE)");
//        thrown.expectMessage("UNIQUE constraint failed: luggage_category.luggage_category_name (Sqlite code 2067 SQLITE_CONSTRAINT_UNIQUE), (OS error - 2:No such file or directory)");

        query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique luggage entries
     */
    public void migrateV3V5EnsureLuggageEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV3V5EnsureLuggageEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
//        thrown.expectMessage("UNIQUE constraint failed: luggage.luggage_name, luggage.luggage_category_fk (code 2067)");
        thrown.expectMessage("UNIQUE constraint failed: luggage.luggage_name, luggage.luggage_category_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        // it is not possible anymore to insert luggage without matching luggage category
        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 5, 2);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing lists
     */
    public void migrateV3V5EnsurePackingListIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV3V5EnsurePackingListIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list.packing_list_name, packing_list.packing_list_date (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing list entries
     */
    public void migrateV3V5EnsurePackingListEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV3V5EnsurePackingListEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
            "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
            "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list_entry.packing_list_fk, packing_list_entry.luggage_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique category names
     */
    public void migrateV4V5EnsureCategoryNameIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV4V5EnsureCategoryNameIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 4);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage_category.luggage_category_name (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique luggage entries
     */
    public void migrateV4V5EnsureLuggageEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV4V5EnsureLuggageEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: luggage.luggage_name, luggage.luggage_category_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        // it is not possible anymore to insert luggage without matching luggage category
        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 5, 2);";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing lists
     */
    public void migrateV4V5EnsurePackingListIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV4V5EnsurePackingListIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
                "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list.packing_list_name, packing_list.packing_list_date (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
                "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);
    }

    @Test
    /*
     * implementation unique packing list entries
     */
    public void migrateV4V5EnsurePackingListEntryIsUnique() throws SQLiteException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Database databaseService = new Database(context);
        databaseService.resetDatabase(DB_NAME);

        try {
            InputStream inputStream = context.getAssets().open("database_v1.sqlite");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.execute(inputStream);
        } catch (IOException exception) {
            Log.e("DatabaseTest migrateV4V5EnsurePackingListEntryIsUnique:", Objects.requireNonNull(exception.getMessage()));
        }

        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 3);
        SQLiteDatabase sqLiteDatabase;

        database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);
        sqLiteDatabase = database.getWritableDatabase();

        String query = "INSERT INTO `luggage_category` (`luggage_category_name`) VALUES('test category');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `luggage` (`luggage_name`, `luggage_category_fk`, `luggage_weigh`, `luggage_count`) "+
            "VALUES('test luggage', 1, 20, 1);";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list` (`packing_list_name`, `packing_list_date`) "+
            "VALUES('test packing list', '2018-10-06');";
        sqLiteDatabase.execSQL(query);

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);

        thrown.expect(SQLiteException.class);
        thrown.expectMessage("UNIQUE constraint failed: packing_list_entry.packing_list_fk, packing_list_entry.luggage_fk (code 2067 SQLITE_CONSTRAINT_UNIQUE)");

        query = "INSERT INTO `packing_list_entry` (`luggage_fk`, `packing_list_fk`, `packing_list_entry_count`) "+
            "VALUES(1, 1, 5);";
        sqLiteDatabase.execSQL(query);
    }
}
