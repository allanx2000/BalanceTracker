package com.innouvous.balancetracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditProviderActivity extends AppCompatActivity {

    private static final String PROVIDER_ID = "ProviderId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_provider);
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