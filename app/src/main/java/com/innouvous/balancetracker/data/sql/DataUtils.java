package com.innouvous.balancetracker.data.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.innouvous.balancetracker.data.Provider;
import com.innouvous.utils.SQLiteUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Allan on 8/7/2016.
 */
public class DataUtils {

    public static Provider parseProvider(Cursor c) throws ParseException {

        Long id = c.getLong(c.getColumnIndex(SQLConst.Providers.ID));
        String name = c.getString(c.getColumnIndex(SQLConst.Providers.NAME));
        Double balance = c.getDouble(c.getColumnIndex(SQLConst.Providers.BALANCE));
        Double fare = c.getDouble(c.getColumnIndex(SQLConst.Providers.FARE));
        String unit = c.getString(c.getColumnIndex(SQLConst.Providers.UNIT));
        Date lastUsed = SQLiteUtils.toDate(c.getString(c.getColumnIndex(SQLConst.Providers.LAST_USED)));

        Provider provider = new Provider(name, balance, fare);
        provider.setUnit(unit);
        provider.setLastUsed(lastUsed);
        provider.setId(id);

        return provider;
    }

    public static List<Provider> getProviders(Cursor c, SQLiteDatabase db) throws ParseException {
        ArrayList<Provider> providers = new ArrayList<>();

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
                Provider provider = parseProvider(c);
                providers.add(provider);
            } while (c.moveToNext());
        }

        return providers;
    }
}
