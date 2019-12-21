package de.byte_artist.luggage_planner.listener;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.byte_artist.luggage_planner.AbstractActivity;
import de.byte_artist.luggage_planner.db.PreferencesDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.PreferencesEntity;
import de.byte_artist.luggage_planner.service.Preferences;

public class CategoryCollapseOnClickListener implements View.OnClickListener {

    private final AbstractActivity activity;
    private final String key;

    public CategoryCollapseOnClickListener(AbstractActivity activity, LuggageCategoryEntity luggageCategoryEntity, String key) {
        this.activity = activity;
        this.key = key;
    }

    @Override
    public void onClick(View view) {
        PreferencesEntity preferencesEntity = Preferences.get(key, activity);
        PreferencesDbModel preferencesDbModel = new PreferencesDbModel(activity);

        // preference already exists
        if (null != preferencesEntity) {
            boolean categoryVisible = preferencesEntity.getValue().equals("1");
            preferencesEntity.setValue(categoryVisible ? "0" : "1");
            preferencesDbModel.update(preferencesEntity);
        } else {
            preferencesEntity = new PreferencesEntity(key, "0");
            preferencesDbModel.insert(preferencesEntity);
        }
        activity.refresh();
    }
}
