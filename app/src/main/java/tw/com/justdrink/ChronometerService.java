package tw.com.justdrink;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import tw.com.justdrink.drinkwater.DrinkWater;

public class ChronometerService extends Service {

    Context context;

    private Handler handler = new Handler();
    private Runnable showTime = new Runnable() {
        @Override
        public void run() {
            // 目前時間
            Date now_date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String date = df.format(now_date.getTime());
            String time = sdf.format(now_date.getTime());

            //Log.i("service", "date: " + date + ", time: " + time);

            // 每天晚上12點新增一筆體重資料
            if (time.equals("00:00:00")) {
                context = getApplicationContext();
                int res = DrinkWater.getWeightByDate(date);
                Log.i("service", "return: " + res);
            }

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("mylog", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("mylog", "onStartCommand()");
        handler.post(showTime);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("mylog", "onDestroy()");
        handler.removeCallbacks(showTime);
        super.onDestroy();
    }
}
