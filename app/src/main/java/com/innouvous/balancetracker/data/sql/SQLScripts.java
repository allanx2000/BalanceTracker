package com.innouvous.balancetracker.data.sql;

import com.innouvous.utils.LineStringBuilder;
import com.innouvous.utils.SQLiteUtils;

public class SQLScripts {
    public static String SQL_CREATE_BALANCES;

    static { //TODOL Put in constrains
        LineStringBuilder sb;

        sb = new LineStringBuilder();
        sb.appendLine("CREATE TABLE " + SQLConst.Providers.TABLE + " (");
        sb.appendLine(SQLConst.Providers.ID + "    integer NOT NULL PRIMARY KEY AUTOINCREMENT,");
        sb.appendLine(SQLConst.Providers.NAME + "  VARCHAR(50) NOT NULL,");
        sb.appendLine(SQLConst.Providers.BALANCE + "  DECIMAL(4,2) NOT NULL,");
        sb.appendLine(SQLConst.Providers.FARE + " DECIMAL(4,2) NOT NULL,");
        sb.appendLine(SQLConst.Providers.UNIT + "  VARCHAR(20) NOT NULL,");
        sb.appendLine(SQLConst.Providers.LAST_USED + "  TEXT");
        sb.appendLine(");");

        SQL_CREATE_BALANCES = sb.toString();
    }
}