package com.app.wheelsonadminapp.util;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static void getToken() {
        final String[] msg = {""};
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        msg[0] = token;
                        AppConstants.DEVICE_TOKEN = msg[0];

                    }
                });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("MSG", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("MSG", "Message data payload: " + remoteMessage.getData());
            Log.d("MSG", "Message data payload: " + remoteMessage.getData().get("Message"));
            if(remoteMessage.getData().containsKey("Message")){
               // Dialogs.showDisablePopup(getApplicationContext());

            }else {

            }

        }
        if (remoteMessage.getNotification() != null) {
            Log.d("MSG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


}
