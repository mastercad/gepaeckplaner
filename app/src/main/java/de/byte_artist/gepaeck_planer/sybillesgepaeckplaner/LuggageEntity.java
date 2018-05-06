package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

class LuggageEntity {
    private long id = 0;
    private String name = null;
    private long categoryId = 0;
    private double weight = 0;

    LuggageEntity() {}

    LuggageEntity(String name, long categoryId, double weight) {
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

    public LuggageEntity setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public double getWeight() {
        return this.weight;
    }
}
