package de.byte_artist.luggage_planner;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void convert() {
        Context context = InstrumentationRegistry.getTargetContext();
        TextView textView = new TextView(context);
        TextSize.convert(context, textView, TextSize.TEXT_TYPE_NORMAL);

        assertEquals(R.dimen.normal_text_size, textView.getTextSize(), 0);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("de.byte_artist.luggage_planner", appContext.getPackageName());
    }
}