package de.byte_artist.luggage_planner.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {

    public static Locale investigateLocale(Context context) {
        Configuration config = context.getResources().getConfiguration();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            return getSystemLocale(config);
        }
        return getSystemLocaleLegacy(config);
    }

    private static java.util.Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static java.util.Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }
}
