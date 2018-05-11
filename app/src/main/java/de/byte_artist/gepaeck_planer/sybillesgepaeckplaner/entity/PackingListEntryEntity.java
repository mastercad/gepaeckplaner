package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner.entity;

public class PackingListEntryEntity {
    private long id = 0;
    private long luggageListFk = 0;
    private PackingListEntity packingListEntity = null;
    private long luggageFk = 0;
    private LuggageEntity luggageEntity = null;
    private int count;

    public PackingListEntryEntity() {}

    public PackingListEntryEntity(long luggageListFk, long luggageFk, int count) {
        this.setLuggageFk(luggageFk)
            .setLuggageListFk(luggageListFk)
            .setCount(count);
    }

    public PackingListEntryEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return this.id;
    }

    public PackingListEntryEntity setLuggageListFk(long luggageListFk) {
        this.luggageListFk = luggageListFk;
        return this;
    }

    public long getLuggageListFk() {
        return this.luggageListFk;
    }

    public PackingListEntryEntity setLuggageFk(long luggageFk) {
        this.luggageFk = luggageFk;
        return this;
    }

    public long getLuggageFk() {
        return this.luggageFk;
    }

    public PackingListEntryEntity setCount(int count) {
        this.count = count;
        return this;
    }

    public int getCount() {
        return this.count;
    }

    public PackingListEntryEntity setPackingListEntity(PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
        return this;
    }

    public PackingListEntity getPackingListEntity() {
        return this.packingListEntity;
    }

    public PackingListEntryEntity setLuggageEntity(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
        return this;
    }

    public LuggageEntity getLuggageEntity() {
        return this.luggageEntity;
    }
}
