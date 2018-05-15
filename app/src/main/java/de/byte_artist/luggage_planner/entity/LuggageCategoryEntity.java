package de.byte_artist.luggage_planner.entity;

public class LuggageCategoryEntity {
    private long id = 0;
    private String name = null;

    public LuggageCategoryEntity() {}

    public LuggageCategoryEntity(String name) {
        this.setName(name);
    }

    public void setName(String name) {
        this.name = name;
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

    public String toString() {
//        if (0 < this.id) {
//            return this.id + " " + this.name;
//        }
        return this.name;
    }
}
