package com.innouvous.balancetracker.adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innouvous.balancetracker.AppStateService;
import com.innouvous.balancetracker.R;
import com.innouvous.balancetracker.data.Provider;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Allan on 8/7/2016.
 */
public class ProviderAdapter extends ArrayAdapter<Provider> {
    private final List<Provider> providers;
    private final Context context;
    private final int layoutId;
    private final Locale locale;

    public ProviderAdapter(Context context, int layoutId, List<Provider> providers, Locale locale) {
        super(context, layoutId, providers);

        this.providers = providers;
        this.context = context;
        this.layoutId = layoutId;
        this.locale = locale;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Provider provider = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.lblProviderName);
        name.setText(provider.getName());

        TextView fare = (TextView) convertView.findViewById(R.id.lblFare);
        fare.setText(formatUnit(provider.getFare()));

        TextView balance = (TextView) convertView.findViewById(R.id.lblRemaining);
        balance.setText(formatUnit(provider.getBalance()));

        TextView lastUsed = (TextView) convertView.findViewById(R.id.lblLastUsed);
        lastUsed.setText(formatDate(provider.getLastUsed()));

        convertView.setTag(provider);

        return convertView;
    }

    //TODO: Localize?
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy H:mm a");
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
