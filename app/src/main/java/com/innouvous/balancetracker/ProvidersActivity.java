package com.innouvous.balancetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.innouvous.balancetracker.adapters.ProviderAdapter;
import com.innouvous.balancetracker.data.IDataStore;
import com.innouvous.balancetracker.data.MockDataStore;
import com.innouvous.balancetracker.data.Provider;

import java.util.List;

public class ProvidersActivity extends AppCompatActivity {

    private static final int REQUEST_NEW_PROVIDER = 110;
    private static final int REQUEST_EDIT_PROVIDER = 120;

    private Toolbar toolbar;
    private FloatingActionButton fab;

    private ListView providersList;

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppStateService.setAppContext(this.getApplicationContext());

        IDataStore ds = new MockDataStore();
        AppStateService.setDataStore(ds);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditProviderActivity.createIntent(context);
                startActivityForResult(intent, REQUEST_NEW_PROVIDER);
            }
        });

        providersList = (ListView) findViewById(R.id.listProviders);

        try {
            loadProviders(ds);

        } catch (Exception e) {
            //TODO: Toast
        }
    }

    private void loadProviders(IDataStore ds) throws Exception {
        List<Provider> providers = ds.getProviders();
        ProviderAdapter adapter = new ProviderAdapter(this, R.layout.item_provider, providers, getResources().getConfiguration().locale);
        providersList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
