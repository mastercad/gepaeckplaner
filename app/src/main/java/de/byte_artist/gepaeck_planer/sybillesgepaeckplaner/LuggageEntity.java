package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

class LuggageEntity {
    private long id = 0;
    private String name = null;
    private long categoryId = 0;
    private int weight = 0;
    private LuggageCategoryEntity categoryEntity = null;

    LuggageEntity() {}

    LuggageEntity(String name, long categoryId, int weight) {
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

    public LuggageEntity setId(long id) {
        this.id = id;
        return this;
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

    public LuggageEntity setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getWeight() {
        return this.weight;
    }

    public LuggageEntity setCategory(LuggageCategoryEntity luggageCategoryEntity) {
        this.categoryEntity = luggageCategoryEntity;
        return this;
    }

    public LuggageCategoryEntity getCategoryEntity() {
        return this.categoryEntity;
    }
}
