package com.beebeom.a20_service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NormalService extends Service {
    public static final String TAG = "서비스";
    private Thread mThread;
    private int mCount;
    private IBinder mBinder = new MyBinder();

    //바인드 객체로 서비스 넘거주기
    public class MyBinder extends Binder {
        public NormalService getService() {
            return NormalService.this;
        }
    }


    public NormalService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: 호출");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("foreground".equals(intent.getAction())) {
            onForeGroundService_my();
        }
        Log.d(TAG, "onStartCommand: 호출");
        //서비스는 다중실행이 가능하기때문에 처리를 헤줘야함
        if (mThread == null) {
            mThread = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "서비스 종료됨");
                        break;
                    }
                    mCount++;
                    Log.d(TAG, "서비스 실행중: " + mCount);
                }
            });
            mThread.start();
        }
        //리턴 값
        //START_STICKY
        //- 서비스가 장제 종료되었을때 다시 시작함. 다만 Intent를 전달하지 않음.
        //START_NOT_STICKY
        //다시 시작하지 않음.
        //START_REDELIVER_INTENT
        //- 다시 시작시 인텐트를 전달해줌
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: 호출");
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
            mCount = 0;
        }
        super.onDestroy();
    }

    //포그라운드 서비스로 승격
    public void onForeGroundService_my() {
        //오레오 이후 부터는 노티피케이션 채널을 등록해줘야 함.
        //채널은 설정에서 알림 이름으로 보임
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "포그라운드 서비스");
        //스몰 아이콘, 타이틀, 텍스트는 필수요소
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행중");
        //인텐트는 클릭시 실행할 컴포넌트
        Intent intent = new Intent(this, MainActivity.class);
        //플래그는 플래그마다 기능이 있는데 실제 사용할 때 찾아볼 것.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        //빌더에서는 여러가지 설정을 해줄 수 있다.(라지 아이콘, 컬러, 진동 패턴, 알림 사운드 등)\

        //채널 만들기
        //노티피케이션 매니져 객체 가져오기
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("포그라운드 서비스", "채널 설명", NotificationManager.IMPORTANCE_DEFAULT);
        //채널에서도 여러가지 설정을 해줄 수 있는데,
        //빌더 설정과 차이점은 이 채널을 통하는 알림들의 설정을 일괄적으로 해줄 수 있다는 점.
        manager.createNotificationChannel(channel);

        //포그라운드 서비스 시작
        startForeground(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getCount() {
        return mCount;
    }
}