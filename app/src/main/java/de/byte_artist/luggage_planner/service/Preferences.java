package de.byte_artist.luggage_planner.service;

import android.content.Context;

import java.util.ArrayList;

import de.byte_artist.luggage_planner.db.PreferencesDbModel;
import de.byte_artist.luggage_planner.entity.PreferencesEntity;

public class Preferences {

    private static ArrayList<PreferencesEntity> preferencesEntities = null;
    public final static String FONT_SIZE = "font_size";

    public static PreferencesEntity get(String preferenceName, Context context) {
        PreferencesEntity preferencesEntity = null;

        if (null == preferencesEntities) {
            preparePreferences(context);
        }

        for (PreferencesEntity currentPreferencesEntity : preferencesEntities) {
            if (preferenceName.equals(currentPreferencesEntity.getName())) {
                preferencesEntity = currentPreferencesEntity;
            }
        }

        return preferencesEntity;
    }

    public static void refresh(Context context) {
        preparePreferences(context);
    }

    private static void preparePreferences(Context context) {
        PreferencesDbModel preferencesDbModel = new PreferencesDbModel(context);
        preferencesEntities = preferencesDbModel.load();
    }
}
