package de.byte_artist.luggage_planner.entity;

public class PreferencesEntity {
    private long id = 0;
    private String name = null;
    private String value = null;

    public PreferencesEntity() {}

    public PreferencesEntity(String name, String value) {
        this.setName(name)
            .setValue(value);
    }

    public PreferencesEntity setName(String name) {
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

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        if (0 < this.id) {
            return this.name + " => "+this.getValue();
        }
        return this.name;
    }
}
