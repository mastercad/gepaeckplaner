package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;

class PackingListEntity {
    private long id = 0;
    private long luggageListFk = 0;
    private LuggageListEntity luggageListEntity = null;
    private long luggageFk = 0;
    private LuggageEntity luggageEntity = null;
    private double count;

    PackingListEntity() {}

    PackingListEntity(long luggageListFk, long luggageFk, long count) {
        this.setLuggageFk(luggageFk)
            .setLuggageListFk(luggageListFk)
            .setCount(count);
    }

    public PackingListEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return this.id;
    }

    public PackingListEntity setLuggageListFk(long luggageListFk) {
        this.luggageListFk = luggageListFk;
        return this;
    }

    public long getLuggageListFk() {
        return this.luggageListFk;
    }

    public PackingListEntity setLuggageFk(long luggageFk) {
        this.luggageFk = luggageFk;
        return this;
    }

    public long getLuggageFk() {
        return this.luggageFk;
    }

    public PackingListEntity setCount(double count) {
        this.count = count;
        return this;
    }

    public double getCount() {
        return this.count;
    }

    public PackingListEntity setLuggageListEntity(LuggageListEntity luggageListEntity) {
        this.luggageListEntity = luggageListEntity;
        return this;
    }

    public LuggageListEntity getLuggageListEntity() {
        return this.luggageListEntity;
    }

    public PackingListEntity setLuggageEntity(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
        return this;
    }

    public LuggageEntity getLuggageEntity() {
        return this.luggageEntity;
    }
}
