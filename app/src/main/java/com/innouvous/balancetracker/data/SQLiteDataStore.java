package com.innouvous.balancetracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.innouvous.balancetracker.data.sql.DataHelper;
import com.innouvous.balancetracker.data.sql.DataUtils;
import com.innouvous.balancetracker.data.sql.SQLConst;
import com.innouvous.utils.SQLiteBuilder;
import com.innouvous.utils.SQLiteUtils;

import java.util.List;

public class SQLiteDataStore  implements IDataStore {
    private static final String TAG = "SQLiteDataStore";
    private Context context;
    private SQLiteDatabase db;
    private DataHelper helper;

    public SQLiteDataStore(Context cxt) {
        this.context = cxt;
        this.helper = new DataHelper(cxt);
        this.db = this.helper.getWritableDatabase();
    }

    public SQLiteDataStore(SQLiteDatabase database) {
        this.db = database;
    }

    @Override
    public Long insertProvider(Provider provider) throws Exception {
        ContentValues values = new ContentValues();

        try {

            Provider.validateProvider(provider);

            values.put(SQLConst.Providers.NAME, provider.getName());
            values.put(SQLConst.Providers.BALANCE, provider.getBalance());
            values.put(SQLConst.Providers.FARE, provider.getFare());
            values.put(SQLConst.Providers.UNIT, "DUMMY"); //TODO: Change

            SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Providers.TABLE);
            Long id = sb.insert(db, values);

            return id;
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Override
    public void updateProvider(Provider provider) throws Exception {
        ContentValues values = new ContentValues();

        try {
            Provider.validateProvider(provider);

            values.put(SQLConst.Providers.NAME, provider.getName());
            values.put(SQLConst.Providers.BALANCE, provider.getBalance());
            values.put(SQLConst.Providers.FARE, provider.getFare());
            values.put(SQLConst.Providers.UNIT, "DUMMY"); //TODO: Change

            if (provider.getLastUsed() == null)
                values.putNull(SQLConst.Providers.LAST_USED);
            else
            {
                String strLastUsed = SQLiteUtils.fromDate(provider.getLastUsed());
                values.put(SQLConst.Providers.LAST_USED, strLastUsed);
            }

            SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Providers.TABLE);
            sb.update(db, SQLConst.Providers.ID, provider.getId(), values);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Override
    public void deleteProvider(long id) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Providers.TABLE);
        sb.setWhere(SQLConst.Providers.ID + "= ?", id);

        sb.delete(db);
    }

    @Override
    public List<Provider> getProviders() throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Providers.TABLE);
        Cursor c = sb.queryAll(db);

        List<Provider> providers = DataUtils.getProviders(c, db);

        return providers;
    }

    @Override
    public Provider getProvider(Long id) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Providers.TABLE);
        sb.setWhere(SQLConst.Providers.ID + "= ?", id);

        Cursor c = sb.query(db);
        c.moveToFirst();

        return DataUtils.parseProvider(c);
    }

    @Override
    public boolean providerExists(String name, Long ignoredId) throws Exception {
        SQLiteBuilder sb = new SQLiteBuilder(SQLConst.Providers.TABLE);

        SQLiteBuilder.WhereBuilder wb = new SQLiteBuilder.WhereBuilder(" AND ");
        wb.addEquals(SQLConst.Providers.NAME, name);

        if (ignoredId != null)
            wb.addStatement(SQLConst.Providers.ID + "<> ?", ignoredId);

        sb.setWhere(wb);

        int count = sb.queryCount(db);
        return count > 0;
    }
}
