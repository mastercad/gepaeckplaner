package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

class LuggageCategoryEntity {
    private long id = 0;
    private String name = null;

    LuggageCategoryEntity() {}

    LuggageCategoryEntity(String name) {
        this.setName(name);
    }

    public LuggageCategoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LuggageCategoryEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return this.id;
    }
}
