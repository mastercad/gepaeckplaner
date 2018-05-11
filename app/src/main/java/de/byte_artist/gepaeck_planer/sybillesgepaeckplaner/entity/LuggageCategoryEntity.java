package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity;

public class LuggageCategoryEntity {
    private long id = 0;
    private String name = null;

    public LuggageCategoryEntity() {}

    public LuggageCategoryEntity(String name) {
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

    public String toString() {
        if (0 < this.id) {
            return this.id + " " + this.name;
        }
        return this.name;
    }
}
