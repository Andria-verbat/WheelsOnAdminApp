/*
 * Created by Accubits Technologies on 10/8/20 10:36 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 10/8/20 10:34 AM
 */

package com.app.wheelsonadminapp.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Window;


import com.app.wheelsonadminapp.R;

import java.util.Objects;

public class MessageProgressDialog {
    private Dialog dialog;
    private static MessageProgressDialog mInstance;

    public static synchronized MessageProgressDialog getInstance() {
        if (mInstance == null) {
            mInstance = new MessageProgressDialog();
        }
        return mInstance;
    }

    public void show(Context context) {
        if (dialog != null && dialog.isShowing()) {
            Log.i("MAINACTIVITY", "showing already: ");
            return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_view);
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
