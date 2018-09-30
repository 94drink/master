package tw.com.justdrink.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import tw.com.justdrink.R;


public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String connect = intent.getExtras().getString("connect");
        // 抓取
        new Thread() {
            @Override
            public void run() {


                // 建立 Notification
                int nid = 1 ;
                NotificationManager notificationManager =
                        (NotificationManager)getApplicationContext()
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder builder =
                        new Notification.Builder(getApplicationContext());
                builder.setSmallIcon(R.mipmap.ic_launcher) //通知服務圖片連結icon
                        .setContentTitle("該喝水囉朋友~")// 標題
                        .setContentText("再忙!也要喝杯水吧?立即點下即刻補出水份") //內文
                        .setContentInfo("目前時間")
                        .setDefaults(Notification.DEFAULT_ALL) // 信息
                        .setAutoCancel(true);

                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                // 抬頭顯示儀

                builder.setPriority(Notification.PRIORITY_HIGH);// 亦可帶入Notification.PRIORITY_MAX參數
                Notification notification = builder.build();
                notificationManager.notify(nid , notification);// 發佈Notification
            }

        }.start();

        // 關閉服務
        stopSelf();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i("mylog","onDestroy()");
        super.onDestroy();
    }
}

