package com.beebeom.a20_service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {

    public static final String TAG = "서비스";
    private boolean stop;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: 호출");
        stop = false;
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (stop) {
                Log.d(TAG, "인텐트 서비스 종료됨");
                break;
            }
            Log.d(TAG, "인텐트 서비스 실행중: " + i);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: 호출");
        if (!stop) {
            stop = true;
        }
        super.onDestroy();
    }
}
