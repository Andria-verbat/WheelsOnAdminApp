package com.app.wheelsonadminapp.ui.home.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.driver.DriverItem;

import java.util.ArrayList;

public class SpinnerDriverAdapter extends ArrayAdapter<DriverItem> {

    public SpinnerDriverAdapter(Context context,
                                ArrayList<DriverItem> driverItemArrayList)
    {
        super(context, 0, driverItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_driver, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_view);
        DriverItem currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }
        return convertView;
    }
}
