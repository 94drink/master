package tw.com.justdrink.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import tw.com.justdrink.R;


public class AlarmService extends Service  {

    public Binder mBinder = new Binder(); //透過Binder來做連接Class

    @Override
    public IBinder onBind(Intent intent) {
       // 由於是啟動Service, 因此onBind會是return null, 由於Service是運作在UI Thread上面
        return null; //原本為回傳null 創建一個binder回傳  mBinder
    }
    @Override
    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }
    public String getAlarmServiceName(){
        return AlarmService.class.getName();
    }
    public class MyBinder extends Binder{
        public AlarmService getService(){
            return AlarmService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //final String connect = intent.getExtras().getString("connect");
        // 抓取

        new Thread() {
            @Override
            public void run() {

                // step1.建立 Notification id <有通知全縣高低的優先權 以ID分別>
                int nid = 1 ;
                //step2.初始化Notifaction
                NotificationManager notificationManager =
                        (NotificationManager)getApplicationContext()
                                .getSystemService(NOTIFICATION_SERVICE);
                //step3.透過Notifaction Builder來建構　notifaction
                Notification.Builder builder =
                        new Notification.Builder(getApplicationContext());
                builder.setSmallIcon(R.mipmap.ic_launcher) //通知服務圖片連結icon
                        .setContentTitle("該喝水囉朋友~")// 標題
                        .setContentText("再忙!也要喝杯水吧?立即點下即刻補出水份") //內文
                        .setContentInfo("目前時間")
                        .setWhen(System.currentTimeMillis())// 設置時間發生時間
                        .setDefaults(Notification.DEFAULT_ALL) // 使用所有默認值，比如聲音，震動，閃屏等等
                        .setAutoCancel(true);
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);


                builder.setPriority(Notification.PRIORITY_HIGH);// 亦可帶入Notification.PRIORITY_MAX參數
                Notification notification = builder.build();
                notificationManager.notify(nid , notification);// 把指定id發送到狀態條上
            }

        }.start();

        // 關閉服務
        stopSelf();
        return Service.START_REDELIVER_INTENT;
       // return super.onStartCommand(intent, flags ,startId);
    }

    @Override
    public void onDestroy() {
        Log.i("mylog","onDestroy()");
        super.onDestroy();
    }
}

