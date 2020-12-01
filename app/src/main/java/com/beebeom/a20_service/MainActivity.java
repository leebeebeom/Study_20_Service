package com.beebeom.a20_service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private NormalService mNormalService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Normal Service 시작
        findViewById(R.id.btn_startService).setOnClickListener(v -> {
            Intent intent = new Intent(this, NormalService.class);
            //서비스는 startService로 시작해야함
            startService(intent);
        });

        //Normal Service 중지
        findViewById(R.id.btn_stopService).setOnClickListener(v -> {
            Intent intent = new Intent(this, NormalService.class);
            //마찬가지로 stopService로 중지해야함.
            stopService(intent);
        });

        //인텐트 서비스의 차이점.
        //1. 자체 비동기로 실행됨.
        //2. 서비스 두번 실행시 순차적으로 실행됨.
        //3. 알아서 selfStop 으로 onDestroy 호출함
        //따라서 사용자가 서비스를 종료하거나 해야할 떈 일반 서비스를 이용하고,
        //알아서 종료되게 하고 싶을땐 인텐트 서비스를 사용하면 됨.

        //Intent Service 시작
        findViewById(R.id.btn_startIntentService).setOnClickListener(v -> {
            Intent intent = new Intent(this, MyIntentService.class);
            startService(intent);
        });

        //물론 인텐트 서비스도 중간에 중지할 수 있음
        //Intent Service 중지
        findViewById(R.id.btn_stopIntentService).setOnClickListener(v -> {
            Intent intent = new Intent(this, MyIntentService.class);
            stopService(intent);
        });

        //포그라운드 서비스.
        //포그라운드 서비스는 알림이 사라지기 전까지는 절대 종료되지 않음.
        //노멀 서비스 종료 버튼으로 종료 할 수 있음,
        findViewById(R.id.btn_startForeGroundService).setOnClickListener(v -> {
            Intent intent = new Intent(this, NormalService.class);
            intent.setAction("foreground");
            startService(intent);
        });

        //포그라운드 서비스 2
        //무슨 차이?
        //startForegroundService 로 포그라운드 시작시
        //앱이 포그라운드 상태가 아니어도 서비스를 시작 할 수 있음.
        findViewById(R.id.btn_startForeGroundService2).setOnClickListener(v -> {
            Intent intent = new Intent(this, NormalService.class);
            intent.setAction("foreground");
            startForegroundService(intent);
        });

        //바인드
        //바인드는 서비스와 다른 컴포넌트들과 서로 소통할 수 있게 해줌.
        //바인드는 일반 서비스 종료로는 해제할 수 없음
        findViewById(R.id.btn_bind).setOnClickListener(v -> {
            int count = mNormalService.getCount();
            Toast.makeText(mNormalService, "카운트 : "+ count, Toast.LENGTH_SHORT).show();
        });


    }

    //바인드
    //일반적으로는 onStart 에서 연결하고 onStop 에서 해제해줌
    @Override
    protected void onStart() {
        Intent intent = new Intent(this, NormalService.class);
        bindService(intent, mConnetionc, BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unbindService(mConnetionc);
        mBound = false;
        super.onStop();
    }

    //서비스 커넥션 객체 생성
    private ServiceConnection mConnetionc = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //바인더 객체가 인자로 들어옴.
            NormalService.MyBinder binder = (NormalService.MyBinder) service;
            //내가 만든 서비스 객체
            mNormalService = binder.getService();
            //boolean 객체로 연결 확인
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}