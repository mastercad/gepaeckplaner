package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity;

public class PackingListEntity {
    private long id = 0;
    private String name = null;
    private String date = null;

    public PackingListEntity() {}

    public PackingListEntity(String name, String date) {
        this.setName(name)
            .setDate(date);
    }

    public PackingListEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public PackingListEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return this.id;
    }

    public PackingListEntity setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDate() {
        return this.date;
    }

    public String toString() {
        if (0 < this.id) {
            return this.id + " " + this.name + " ("+this.date+")";
        }
        return this.name;
    }
}
