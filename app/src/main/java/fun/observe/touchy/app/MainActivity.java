package fun.observe.touchy.app;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fun.observe.touchy.MotionEventBroadcaster;
import fun.observe.touchy.MotionEventReceiver;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private MotionEventReceiver motionEventReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fun.observe.touchy.app.R.layout.activity_main);

        motionEventReceiver = new MotionEventReceiver() {
            @Override
            protected void onReceive(MotionEvent motionEvent) {
                Log.d(TAG, motionEvent.toString());
            }
        };

        MotionEventBroadcaster.registerReceiver(this, motionEventReceiver);
        String cName = getWindow().getDecorView().getClass().getName();

        Log.d(TAG,cName);


        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"ON TOUCH_"+event.toString());
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(motionEventReceiver != null){
            MotionEventBroadcaster.removeReceiver(motionEventReceiver);
        }
        super.onDestroy();
    }
}
