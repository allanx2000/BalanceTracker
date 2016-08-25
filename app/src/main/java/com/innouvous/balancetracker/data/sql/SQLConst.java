package com.innouvous.balancetracker.data.sql;

/**
 * Created by Allan on 7/16/2016.
 */
public class SQLConst {

    private static final String mID = "ID";

    public class Providers {
        public static final String TABLE = "Providers";

        public static final String ID = mID;
        public static final String NAME = "ProviderName";
        public static final String BALANCE = "Balance";
        public static final String FARE = "Fare";
        public static final String UNIT = "ProviderUnit";
        public static final String LAST_USED = "LastUsed";
    }
}
