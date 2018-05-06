package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

class LuggageListEntity {
    private long id = 0;
    private String name = null;
    private String date = null;

    LuggageListEntity() {}

    LuggageListEntity(String name, String date) {
        this.setName(name)
            .setDate(date);
    }

    public LuggageListEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LuggageListEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return this.id;
    }

    public LuggageListEntity setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDate() {
        return this.date;
    }
}
