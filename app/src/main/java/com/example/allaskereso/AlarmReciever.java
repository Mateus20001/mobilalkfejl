package com.example.allaskereso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Értesítés létrehozása és megjelenítése
        NotificationUtils.showNotification(context, "Rendszeres értesítés", "Üzenet a rendszeres értesítésről");
    }
}
