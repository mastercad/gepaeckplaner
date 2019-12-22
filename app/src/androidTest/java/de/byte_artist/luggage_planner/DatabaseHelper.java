package de.byte_artist.luggage_planner;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * basic class to execute needed database operations to prepare the database
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private Context context = null;
    static final private String DB_NAME = "test_luggage.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME,null, 1);
        this.context = context;
    }

    public DatabaseHelper(Context context, String name) {
        super(context, name.isEmpty() ? DB_NAME : name, null, 1);
        this.context = context;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void execute(InputStream stream) {
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[16384];  // read 16k blocks
        int len; // how much content was read?

        try {
            while (0 < (len = reader.read(buffer))) {
                stringBuilder.append(buffer, 0, len);
            }
            reader.close();
            this.execute(stringBuilder.toString());
        } catch (IOException exception) {
            Toast.makeText(context, R.string.error_import_file, Toast.LENGTH_SHORT).show();
        }
    }

    private void execute(String statement) {
        SQLiteDatabase db = this.getWritableDatabase();

        //noinspection TryFinallyCanBeTryWithResources
        try {
            String[] queries = statement.split(";");
            for (String query : queries) {
                query = query.trim();
                if (!query.isEmpty()) {
                    db.execSQL(query);
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, R.string.error_create_tables, Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}
