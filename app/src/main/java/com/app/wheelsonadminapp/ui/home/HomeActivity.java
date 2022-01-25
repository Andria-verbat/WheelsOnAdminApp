package com.app.wheelsonadminapp.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.ui.auth.SignUpActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeActivity extends BaseActivity {

    private RelativeLayout layout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        layout = (RelativeLayout) findViewById(R.id.layout);
        configureNavigationDrawer();
        configureToolbar();
        replaceFragment(new HomeFragment(),false,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }
    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }
    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment f = null;
                int itemId = menuItem.getItemId();
                switch (itemId){
                    case R.id.logout:
                        showLogoutDialog();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.dashboard:
                        replaceFragment(new HomeFragment(),false,null);
                        drawerLayout.closeDrawers();
                        break;
                }
//                if (itemId == R.id.refresh) {
//                    f = new RefreshFragment();
//                } else if (itemId == R.id.stop) {
//                    f = new StopFragment();
//                }
                if (f != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, f);
                    transaction.commit();
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            // Android home
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            // manage other entries if you have it ...
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showLogoutDialog(){
        Dialog dialog = new Dialog(this,R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.logout_dialog);
        Button btOkay = dialog.findViewById(R.id.btOkay);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppRepository appRepository= new AppRepository(HomeActivity.this);
                appRepository.deleteAllUser();
                startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
                finish();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }
}