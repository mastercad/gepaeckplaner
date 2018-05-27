package de.byte_artist.luggage_planner.service;

import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class TextSize {

    public static final String TEXT_TYPE_NORMAL = "normal_text_size";
    public static final String TEXT_TYPE_HEADER = "header_text_size";
    public static final String TEXT_TYPE_FOOTER = "footer_text_size";
    public static final String TEXT_TYPE_MESSAGE = "message_text_size";
    public static final String TEXT_TYPE_BUTTON = "button_text_size";
    public static final String TEXT_TYPE_TITLE = "title_text_size";

    public static TextView convert(FragmentActivity activity, TextView textView, String type) {

        Locale locale = activity.getResources().getConfiguration().locale;

        // font size of needed type
        int textSize = activity.getResources().getDimensionPixelSize(
            activity.getResources().getIdentifier(
                type,
                "dimen",
                activity.getPackageName()
            )
        );

        // font size of default type
        int defaultTextSize = activity.getResources().getDimensionPixelSize(
            activity.getResources().getIdentifier(
                TEXT_TYPE_NORMAL,
                "dimen",
                activity.getPackageName()
            )
        );

        int currentDefaultTextSize = Integer.parseInt(
            String.format(
                locale,
                "%.0f",
                Float.parseFloat(Preferences.get(Preferences.FONT_SIZE, activity.getApplicationContext()).getValue())
            )
        );

        float factor = (float)currentDefaultTextSize / defaultTextSize;

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float)textSize * factor);

        return textView;
    }

    public static Spinner convert(FragmentActivity activity, Spinner spinner, String type) {

        Locale locale = activity.getResources().getConfiguration().locale;

        // font size of needed type
        int textSize = activity.getResources().getDimensionPixelSize(
            activity.getResources().getIdentifier(
                type,
                "dimen",
                activity.getPackageName()
            )
        );

        // font size of default type
        int defaultTextSize = activity.getResources().getDimensionPixelSize(
            activity.getResources().getIdentifier(
                TEXT_TYPE_NORMAL,
                "dimen",
                activity.getPackageName()
            )
        );

        int currentDefaultTextSize = Integer.parseInt(
            String.format(
                locale,
                "%.0f",
                Float.parseFloat(Preferences.get(Preferences.FONT_SIZE, activity.getApplicationContext()).getValue())
            )
        );

        float factor = (float)currentDefaultTextSize / defaultTextSize;

        if (0 < spinner.getChildCount()) {
            for (int i = 0; i < spinner.getChildCount(); i++) {
                ((TextView) spinner.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) textSize * factor);
            }
        }

        return spinner;
    }
}
