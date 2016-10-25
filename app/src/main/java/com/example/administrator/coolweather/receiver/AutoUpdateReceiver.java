package com.example.administrator.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.coolweather.service.AutoUpdateService;

/**
 * Created by Administrator on 2016/10/25.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            context.startService(new Intent(context, AutoUpdateService.class));

    }
}
