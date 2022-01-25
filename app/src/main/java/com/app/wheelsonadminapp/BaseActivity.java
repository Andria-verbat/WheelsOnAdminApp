package com.app.wheelsonadminapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {
    public void replaceFragment(Fragment newFragment, boolean isAddtoStack, Bundle bundle){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(bundle != null){
            newFragment.setArguments(bundle);
        }
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, newFragment);
        if (isAddtoStack)
            fragmentTransaction.addToBackStack(newFragment.getTag());
        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment newFragment, boolean isAddtoStack, Bundle bundle){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(bundle != null){
            newFragment.setArguments(bundle);
        }
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.add(R.id.frame_container, newFragment);
        if (isAddtoStack)
            fragmentTransaction.addToBackStack(newFragment.getTag());
        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void showErrorToast(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_error_layout,
                (ViewGroup) findViewById(R.id.toast_layout));
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showSuccessToast(String msg) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_sucess_layout,
                (ViewGroup) findViewById(R.id.toast_layout));
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
