package com.app.wheelsonadminapp.ui.home.vehicle;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;


public class VehicleActivity extends BaseActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        replaceFragment(new ListVehicleFragment(),false,null);
    }
}