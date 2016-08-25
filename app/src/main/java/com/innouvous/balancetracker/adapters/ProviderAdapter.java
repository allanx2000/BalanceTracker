package com.innouvous.balancetracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.innouvous.balancetracker.AppStateService;
import com.innouvous.balancetracker.R;
import com.innouvous.balancetracker.data.Provider;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Allan on 8/7/2016.
 */
public class ProviderAdapter extends ArrayAdapter<Provider> {
    private static final int LOW_TRIPS = 5;

    private final List<Provider> providers;
    private final Context context;
    private final int layoutId;
    private final Locale locale;
    private final Callback callback;

    public interface Callback
    {
        void useClicked(Provider p);
    }

    public ProviderAdapter(Context context, int layoutId, List<Provider> providers, Locale locale, Callback callback) {
        super(context, layoutId, providers);

        this.callback = callback;
        this.providers = providers;
        this.context = context;
        this.layoutId = layoutId;
        this.locale = locale;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Provider provider = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.lblProviderName);
        name.setText(provider.getName());

        TextView fare = (TextView) convertView.findViewById(R.id.lblFare);
        fare.setText(formatUnit(provider.getFare()));

        TextView balance = (TextView) convertView.findViewById(R.id.lblRemaining);
        balance.setText(formatUnit(provider.getBalance()));

        if (hasLowBalance(provider))
        {
            balance.setTextColor(Color.RED);
        }

        TextView lastUsed = (TextView) convertView.findViewById(R.id.lblLastUsed);
        lastUsed.setText(formatDate(provider.getLastUsed()));

        Button btnUse = (Button) convertView.findViewById(R.id.btnUse);
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.useClicked(provider);
            }
        });

        convertView.setTag(provider);

        return convertView;
    }

    private boolean hasLowBalance(Provider provider) {
        return provider.getBalance() < LOW_TRIPS*provider.getFare();
    }

    //TODO: Localize?
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:mm a");
    private String formatDate(Date date) {

        if (date == null)
            return "NA";
        else
            return dateFormat.format(date);
    }

    private String formatUnit(double amount) {

        //TODO: Add unit type, add switch
        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance(locale);

        String formatted = currencyFormatter.format(amount);
        return formatted;
    }
}
