package com.innouvous.balancetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.innouvous.balancetracker.adapters.ProviderAdapter;
import com.innouvous.balancetracker.data.IDataStore;
import com.innouvous.balancetracker.data.MockDataStore;
import com.innouvous.balancetracker.data.Provider;
import com.innouvous.balancetracker.data.SQLiteDataStore;
import com.innouvous.utils.ToastHelper;

import java.util.List;

public class ProvidersActivity extends AppCompatActivity implements ProviderAdapter.Callback {

    private static final int REQUEST_NEW_PROVIDER = 110;
    private static final int REQUEST_EDIT_PROVIDER = 120;

    private Toolbar toolbar;
    private FloatingActionButton fab;

    private ListView providersList;

    private final Context context = this;
    private List<Provider> providers;
    private Provider selectedProvider;
    private IDataStore ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppStateService.setAppContext(this.getApplicationContext());
        ToastHelper.initializeHelper(this);

        ds = new SQLiteDataStore(this.getApplicationContext());
        //ds = new MockDataStore();
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
        registerForContextMenu(providersList);
        providersList.setOnCreateContextMenuListener(this);
        providersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProvider = providers.get(position);
                openContextMenu(providersList);
            }
        });

        loadProviders();
    }

    private void loadProviders()  {
        try {
            providers = ds.getProviders();
            ProviderAdapter adapter = new ProviderAdapter(this, R.layout.item_provider,
                    providers, getResources().getConfiguration().locale,
                    this);

            providersList.setAdapter(adapter);

        } catch (Exception e) {
            ToastHelper.showShortToast("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listProviders)
        {
            menu.add(0, v.getId(), 1, "Edit");
            menu.add(0, v.getId(), 2, "Delete");
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item){

        if (selectedProvider == null)
            return true;
        try {
            switch (item.getOrder()) {
                case 1: //Show Locations
                    Intent intent = EditProviderActivity.createIntent(this, selectedProvider.getId());
                    startActivityForResult(intent, REQUEST_EDIT_PROVIDER);
                    break;
                case 2: //Delete
                    final Long providerId = selectedProvider.getId();
                    AppStateService.getAlertBuilder().createDefaultConfirm("Confirm Delete",
                            "Are you sure you want to delete the selected provider?",
                            this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        AppStateService.getDataStore().deleteProvider(providerId);
                                        loadProviders();
                                    } catch (Exception e) {}
                                }
                            }).show();
                    break;
            }
        }
        catch (Exception e)
        {
        }
        finally {
            selectedProvider = null;
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            loadProviders();
        }
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

    @Override
    public void useClicked(Provider provider) {

        final Provider p = provider;

        AppStateService.getAlertBuilder().createDefaultConfirm("Use Provider", "Use the provider?", this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        p.use();
                        ds.updateProvider(p);
                        ToastHelper.showShortToast("Recorded. Left: " + p.getBalance());

                        loadProviders();
                    }
                    catch (Exception e)
                    {
                        ToastHelper.showShortToast("Error: " + e.getMessage());
                    }
                }
            }).show();
    }
}
