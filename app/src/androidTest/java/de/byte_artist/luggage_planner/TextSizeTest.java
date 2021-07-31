package de.byte_artist.luggage_planner;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.byte_artist.luggage_planner.service.TextSize;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TextSizeTest {

    static private final String DB_NAME = "test_luggage.db";

    @Test
    public void convert() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);

        TextView textView = new TextView(context);
        TextSize.convert(context, textView, TextSize.TEXT_TYPE_NORMAL);

//        assertEquals(R.dimen.normal_text_size, textView.getTextSize(), 0);
        assertEquals(24.0, textView.getTextSize(), 0);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("de.byte_artist.luggage_planner", appContext.getPackageName());
    }
}