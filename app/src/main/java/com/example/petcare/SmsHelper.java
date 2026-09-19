package com.example.petcare;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

public class SmsHelper {
    private final Context context;

    public SmsHelper(Context context) {
        this.context = context;
    }

    public void sendSms(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                != androidx.core.content.PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (androidx.appcompat.app.AppCompatActivity) context,
                    new String[]{Manifest.permission.SEND_SMS}, 101);
            return;
        }
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(context, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
    }
}