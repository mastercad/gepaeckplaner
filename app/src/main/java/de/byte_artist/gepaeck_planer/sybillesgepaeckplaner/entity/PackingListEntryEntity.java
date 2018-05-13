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

    public void setId(long id) {
        this.id = id;
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

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setPackingListEntity(PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
    }

    public PackingListEntity getPackingListEntity() {
        return this.packingListEntity;
    }

    public void setLuggageEntity(LuggageEntity luggageEntity) {
        this.luggageEntity = luggageEntity;
    }

    public LuggageEntity getLuggageEntity() {
        return this.luggageEntity;
    }
}
