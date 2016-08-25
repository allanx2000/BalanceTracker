package com.innouvous.balancetracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.innouvous.balancetracker.data.IDataStore;
import com.innouvous.utils.Alerts;

public class AppStateService {

    private static IDataStore ds;
    private static Context appContext;
    private static Alerts alert;
    private static boolean initialized = false;

    public static IDataStore getDataStore() {
        return ds;
    }

    public static void setDataStore(IDataStore dataStore)
    {
        ds = dataStore;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        AppStateService.appContext = appContext;
    }

    /*
    public static SettingsEditor getSettingsEditor() {
        return appContext == null? null : SettingsEditor.getInstance(appContext);
    }

    public static SettingsEditor getSettingsEditor(Context appContext) {
        return SettingsEditor.getInstance(appContext);
    }
    */

    public static void hideKeyboard(View view)
    {
        try {
            InputMethodManager imm = (InputMethodManager) appContext.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (view != null && imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception e)
        {
        }
    }

    public static Alerts getAlertBuilder() {
        if (alert == null) {
            alert = new Alerts(R.style.myDialog);
        }

        return alert;
    }

    /*
    public static DBLogger getDBLogger() {
        return DBLogger.getInstance(appContext);
    }
    */
}
