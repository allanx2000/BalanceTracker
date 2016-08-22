package com.innouvous.balancetracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.List;

public class SQLiteDataStore  { //implements IDataStore {
/*
    private static final String TAG = "SQLiteDataStore";
    private Context context;
    private SQLiteDatabase db;
    private DataHelper helper;

    private DBLogger logger;

    public SQLiteDataStore(Context cxt) {
        this.context = cxt;
        this.helper = new DataHelper(cxt);
        this.db = this.helper.getWritableDatabase();
        this.logger = DBLogger.getInstance(cxt);
    }

    public SQLiteDataStore(SQLiteDatabase database) {
        this.db = database;
    }

    @Override
    public Long insertSet(LocationsSet set) throws Exception {
        logger.log(TAG, "Insert set:" + set.toString());
        return insertSet(set, false);
    }


    @Override
    public void updateSet(LocationsSet set, List<Location> locationsToImport) throws Exception {
        db.beginTransaction();

        try {
            updateSet(set);

            if (locationsToImport != null && locationsToImport.size() > 0) {
                for (Location l : locationsToImport) {
                    l.setSetId(set.getId());
                    if (!locationExists(l.getSetId(), l.getAddress(), null)) {
                        insertLocation(l);
                    }
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Long insertSet(LocationsSet set, List<Location> locationsToImport) throws Exception {
        db.beginTransaction();

        try {
            Long id = insertSet(set, false);

            if (locationsToImport != null && locationsToImport.size() > 0) {
                for (Location l : locationsToImport) {
                    l.setSetId(id);
                    insertLocation(l);
                }
            }

            db.setTransactionSuccessful();
            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    private Long insertSet(LocationsSet set, boolean startTxn) throws Exception {
        ContentValues values = new ContentValues();

        try {

            if (startTxn)
                db.beginTransaction();

            if (Utils.isNullOrEmpty(set.getName()))
                throw new Exception("Name cannot be empty.");

            values.put(SQLConst.Sets.NAME, set.getName());

            SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Sets.TABLE);
            Long id = sb.insert(db, values);

            if (startTxn)
                db.setTransactionSuccessful();

            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            if (startTxn)
                db.endTransaction();
        }
    }

    @Override
    public void updateSet(LocationsSet set) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Sets.TABLE);

        ContentValues values = new ContentValues();
        values.put(SQLConst.Sets.NAME, set.getName());

        sb.update(db, SQLConst.Sets.ID, set.getId(), values);
    }

    @Override
    public void deleteSet(long id) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Sets.TABLE);
        sb.setWhere(SQLConst.Sets.ID + "= ?", id);

        sb.delete(db);
    }

    @Override
    public Long insertLocation(Location location) throws Exception {
        return insertLocation(location, false);
    }

    private Long insertLocation(Location location, boolean startTxn) throws Exception {
        ContentValues values = new ContentValues();

        try {

            if (startTxn)
                db.beginTransaction();

            if (Utils.isNullOrEmpty(location.getName()))
                throw new Exception("Name cannot be empty.");
            else if (Utils.isNullOrEmpty(location.getAddress()))
                throw new Exception("Address cannot be empty.");
            else if (location.getSetId() == null)
                throw new Exception("SetId is not set");

            values.put(SQLConst.Locations.SET_ID, location.getSetId());
            values.put(SQLConst.Locations.NAME, location.getName());
            values.put(SQLConst.Locations.ADDRESS, location.getAddress());

            SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Locations.TABLE);
            Long id = sb.insert(db, values);

            if (startTxn)
                db.setTransactionSuccessful();
            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            if (startTxn)
                db.endTransaction();
        }
    }

    @Override
    public void updateLocation(Location location) throws Exception {

        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Locations.TABLE);

        ContentValues values = new ContentValues();
        values.put(SQLConst.Locations.NAME, location.getName());
        values.put(SQLConst.Locations.ADDRESS, location.getAddress());

        sb.update(db, SQLConst.Locations.ID, location.getId(), values);

        //TODO: Add hours? Make separate table
    }

    @Override
    public void deleteLocation(long id) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Locations.TABLE);
        sb.setWhere(SQLConst.Locations.ID + "= ?", id);

        sb.delete(db);
    }

    @Override
    public List<LocationsSet> getLocationSets() throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Sets.TABLE);
        Cursor c = sb.queryAll(db);

        List<LocationsSet> sets = DataUtils.getLocationSets(c, db);

        return sets;
    }

    private HashMap<Long, Integer> getLocationCounts() {
        LineStringBuilder sb = new LineStringBuilder();
        sb.appendLine("SELECT " + SQLConst.Locations.SET_ID + ", count(*) AS Cnt");
        sb.appendLine("FROM " + SQLConst.Locations.TABLE);
        sb.appendLine("GROUP BY " + SQLConst.Locations.SET_ID);

        String query = sb.toString();
        Cursor c = db.rawQuery(query, new String[]{});

        HashMap<Long, Integer> map = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
                Long id = c.getLong(0);
                Integer count = c.getInt(1);

                map.put(id, count);
            } while (c.moveToNext());
        }

        return map;
    }

    @Override
    public List<Location> getLocations(Long setId) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Locations.TABLE);
        sb.setWhere(SQLConst.Locations.SET_ID + "=?", setId);
        Cursor c = sb.query(db);

        List<Location> locations = DataUtils.getLocations(c);

        return locations;
    }

    @Override
    public Location getLocation(Long setId, Long locationId) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Locations.TABLE);
        sb.setWhere(SQLConst.Locations.SET_ID + "= ? AND " + SQLConst.Locations.ID + "= ?",
                setId, locationId);

        Cursor c = sb.query(db);

        return DataUtils.getLocation(c);
    }

    @Override
    public LocationsSet getLocationSet(Long setId) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Sets.TABLE);
        sb.setWhere(SQLConst.Sets.ID + "= ?", setId);

        Cursor c = sb.query(db);

        return DataUtils.getLocationSet(c);
    }

    @Override
    public boolean locationExists(Long setId, String address, Long locationId) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Locations.TABLE);

        SQLiteBuilder.WhereBuilder wb = new SQLiteBuilder.WhereBuilder(" AND ");
        wb.addEquals(SQLConst.Locations.SET_ID, setId);
        wb.addEquals(SQLConst.Locations.ADDRESS, address);

        if (locationId != null)
            wb.addStatement(SQLConst.Locations.ID + "<> ?", locationId);

        sb.setWhere(wb);

        int count = sb.queryCount(db);
        return count > 0;
    }

    @Override
    public boolean setExists(String name, Long setId) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Sets.TABLE);

        SQLiteBuilder.WhereBuilder wb = new SQLiteBuilder.WhereBuilder(" AND ");
        wb.addEquals(SQLConst.Sets.NAME, name);

        if (setId != null)
            wb.addStatement(SQLConst.Sets.ID + "<> ?", setId);
        sb.setWhere(wb);

        int count = sb.queryCount(db);
        return count > 0;
    }

    */
}
