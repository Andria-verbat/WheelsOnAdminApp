/*
 * Created by Accubits Technologies on 10/8/20 10:36 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 10/8/20 10:34 AM
 */

package com.app.wheelsonadminapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

// requires android.permission.ACCESS_NETWORK_STATE
public final class NetworkUtility
{
    private NetworkUtility() {}

    public static boolean isOnline(Context context)
    {
        if(context == null)  return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }
            else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("NetworkUtility", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("NetworkUtility", "" + e.getMessage());
                }
            }
        }
        Log.i("NetworkUtility","Network is available : FALSE ");
        return false;
    }
}
