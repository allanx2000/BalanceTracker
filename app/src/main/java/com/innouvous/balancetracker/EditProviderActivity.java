package com.innouvous.balancetracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.innouvous.balancetracker.data.IDataStore;
import com.innouvous.balancetracker.data.Provider;
import com.innouvous.utils.ToastHelper;

import java.text.DecimalFormat;

public class EditProviderActivity extends AppCompatActivity {

    private static final String PROVIDER_ID = "ProviderId";
    private ActionBar toolbar;

    private final long NA_LONG = -1L;
    private Long providerId;

    private EditText txtName;
    private EditText txtBalance;
    private EditText txtFare;
    private IDataStore ds;
    private Provider existingProvider;

    private boolean existing() { return  providerId != null; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_provider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.actionCancel:
                finish();
                break;
            case R.id.actionSave:
                saveProvider();
                break;
        }

        return true;
    }

    private void saveProvider() {
        try
        {
            String name = txtName.getText().toString();

            Double fare, balance;

            String tmp;
            tmp = txtFare.getText().toString();
            fare = Double.parseDouble(tmp);

            tmp = txtBalance.getText().toString();
            balance = Double.parseDouble(tmp);

            //TODO: Add unit

            Provider provider;
            if (existing()) {
                provider = existingProvider;
                provider.setName(name);
                provider.setBalance(balance);
                provider.setFare(fare);

                Provider.validateProvider(provider);
                ds.updateProvider(provider);
            }
            else {
                provider = new Provider(name, balance, fare);
                Provider.validateProvider(provider);

                Long id = ds.insertProvider(provider);
                provider.setId(id);
            }

            setResult(RESULT_OK);
            finish();
        }
        catch (Exception e)
        {
            ToastHelper.showShortToast(e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_provider);
        ds = AppStateService.getDataStore();

        toolbar = getSupportActionBar();

        Intent bundle = getIntent();

        providerId = bundle.getLongExtra(PROVIDER_ID, NA_LONG);
        if (providerId == NA_LONG)
            providerId = null;

        toolbar.setTitle((existing()? "Edit" : "Add") + " Provider");

        wireControls();

        if (providerId != null)
        {
            try {
                existingProvider = ds.getProvider(providerId);
                txtName.setText(existingProvider.getName());
                txtBalance.setText(format(existingProvider.getBalance()));
                txtFare.setText(format(existingProvider.getFare()));
            } catch (Exception e) {
                finish();
                e.printStackTrace();
            }
        }
    }

    private static DecimalFormat df2 = new DecimalFormat("0.00");

    private String format(double amount) {
        return df2.format(amount);
    }

    private void wireControls() {
        txtName = (EditText) findViewById(R.id.txtName);
        txtBalance = (EditText) findViewById(R.id.txtBalance);
        txtFare = (EditText) findViewById(R.id.txtFare);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, EditProviderActivity.class);

        return intent;
    }

    public static Intent createIntent(Context context, Long providerId) {
        Intent intent = createIntent(context);
        intent.putExtra(PROVIDER_ID, providerId);

        return intent;
    }
}