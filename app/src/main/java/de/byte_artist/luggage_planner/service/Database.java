package de.byte_artist.luggage_planner.service;

import android.content.Context;

import java.io.File;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.LuggageCategoryDbModel;
import de.byte_artist.luggage_planner.db.LuggageDbModel;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.db.PackingListEntryDbModel;
import de.byte_artist.luggage_planner.entity.LuggageCategoryEntity;
import de.byte_artist.luggage_planner.entity.LuggageEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.entity.PackingListEntryEntity;

public class Database {

    private final Context context;

    public Database(Context context) {
        this.context = context;
    }

    public void recreateDatabase() {
        this.resetDatabase();

        LuggageCategoryDbModel luggageCategoryDbModel = new LuggageCategoryDbModel(this.context);
        LuggageCategoryEntity luggageCategoryEntity = new LuggageCategoryEntity(
            this.context.getResources().getString(R.string.category1)
        );

        luggageCategoryDbModel.insert(luggageCategoryEntity);

        PackingListDbModel luggageListDbModel = new PackingListDbModel(this.context);
        PackingListEntity luggageListEntity = new PackingListEntity(
            this.context.getResources().getString(R.string.packing_list1),
            "2018-08-04"
        );
        luggageListDbModel.insert(luggageListEntity);

        LuggageDbModel luggageDbModel = new LuggageDbModel(this.context);
        LuggageEntity luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage1_category1),
            luggageCategoryEntity.getId(),
            65
        );
        luggageDbModel.insert(luggageEntity);

        PackingListEntryDbModel packingListDbModel = new PackingListEntryDbModel(this.context);
        PackingListEntryEntity packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
                2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage2_category1),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageCategoryEntity = new LuggageCategoryEntity(
            this.context.getResources().getString(R.string.category2)
        );
        luggageCategoryDbModel.insert(luggageCategoryEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage1_category2),
            luggageCategoryEntity.getId(),
            65
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage2_category2),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage3_category2),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageCategoryEntity = new LuggageCategoryEntity(
            this.context.getResources().getString(R.string.category3)
        );
        luggageCategoryDbModel.insert(luggageCategoryEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage1_category3),
            luggageCategoryEntity.getId(),
            65
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage2_category3),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);

        luggageEntity = new LuggageEntity(
            this.context.getResources().getString(R.string.luggage3_category3),
            luggageCategoryEntity.getId(),
            120
        );
        luggageDbModel.insert(luggageEntity);

        packingListEntity = new PackingListEntryEntity(
            luggageListEntity.getId(),
            luggageEntity.getId(),
            2
        );
        packingListDbModel.insert(packingListEntity);
    }

    public void resetDatabase() {
        resetDatabase("luggage.db");
    }

    public void resetDatabase(String name) {
        String filePathName = this.context.getDatabasePath(name).toString();
        File file = new File(filePathName);

        if (file.exists()) {
            file.delete();
        }
    }
}