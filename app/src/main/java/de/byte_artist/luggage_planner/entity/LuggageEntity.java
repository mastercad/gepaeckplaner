package de.byte_artist.luggage_planner.entity;

public class LuggageEntity {
    private long id = 0;
    private String name = null;
    private long categoryId = 0;
    private int weight = 0;
    private int count = 0;
    private LuggageCategoryEntity categoryEntity = null;

    public LuggageEntity() {}

    public LuggageEntity(String name, long categoryId, int weight) {
        this.setName(name)
            .setCategoryId(categoryId)
            .setWeight(weight);
    }

    public LuggageEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public LuggageEntity setCategoryId(long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public long getCategoryId() {
        return this.categoryId;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setCategoryEntity(LuggageCategoryEntity luggageCategoryEntity) {
        this.categoryEntity = luggageCategoryEntity;
    }

    public LuggageCategoryEntity getCategoryEntity() {
        return this.categoryEntity;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public String toString() {
//        if (0 < this.id) {
//            return this.id + " " + this.name;
//        }
        return this.name;
    }
}
