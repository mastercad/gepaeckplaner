package de.byte_artist.luggage_planner;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.widget.TextView;

import com.google.android.material.resources.TextAppearance;

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

    // @Test
    public void convert() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // only needed to prevent reset database to 1 from 5 for loading preferences.
        de.byte_artist.luggage_planner.Database database = new de.byte_artist.luggage_planner.Database(context, DB_NAME, null, 5);

        TextView textView = new TextView(context);
        TextSize.convert(context, textView, TextSize.TEXT_TYPE_NORMAL);

        assertEquals(dpToPx(R.style.TextAppearance_AppCompat_Large, context), textView.getTextSize(), 0);
//        assertEquals(24.0, textView.getTextSize(), 0);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("de.byte_artist.luggage_planner", appContext.getPackageName());
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int dpToSp(float dp, Context context) {
        return (int) (dpToPx(dp, context) / context.getResources().getDisplayMetrics().scaledDensity);
    }
}