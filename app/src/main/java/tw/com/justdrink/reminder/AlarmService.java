package tw.com.justdrink.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import tw.com.justdrink.MainActivity;
import tw.com.justdrink.R;


public class AlarmService extends Service  {

    public Binder mBinder = new Binder(); //透過Binder來做連接Class
    TextView timechange;
    public class MyBinder extends Binder{
        public AlarmService getService(){
            return AlarmService.this;
        }//建構抓取服務的Function
    }

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


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("mylog","onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 抓取
        Log.i("mylog","onStartCommand()");
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
                builder.setVisibility(Notification.VISIBILITY_SECRET);
                builder.setPriority(Notification.PRIORITY_HIGH);// 亦可帶入Notification.PRIORITY_MAX參數(2,1,0,-1,-2)優先順序從高至低
                Notification notification = builder.build();

//                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(alarmSound);
//                RingtoneManager.getRingtone(getActivity(), Uri.parse("uri")).play();
                builder.setSmallIcon(R.mipmap.ic_drink_water) //通知服務圖片連結icon  statusbar上的icon
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))//LargIcon大圖示//顯示列圖示
                        .setContentTitle("該喝水囉!俊男美女")// 標題MainText
                        .setContentText("立即點下即刻補充.....水份") //內文/副標題
                        .setContentInfo("目前時間")             //顯示欄右下文字
                        .setColor((ContextCompat.getColor(getApplicationContext(),R.color.lightblue)))//小圈圈顏色
                        .setWhen(System.currentTimeMillis())// 設置時間發生時間 .setDefaults(Notification.DEFAULT_ALL) // 使用所有默認值，比如聲音，震動，閃屏等等

                        .setAutoCancel(true);

                // 為您的應用中的活動創建明確的意圖 點下通知列會連結至歡迎畫面
                Intent resultIntent = new Intent(AlarmService.this,tw.com.justdrink.WelcomeActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(AlarmService.this); //建立TaskStackBuilder方法,藉此與提醒服務連接
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(tw.com.justdrink.WelcomeActivity.class); //stackBuilder下的addParentStack方法連接Manifest name需要引導到的畫面
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent); //加入點擊之後的連接畫面
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                notificationManager.notify(nid , builder.build());// 把指定id到狀態條上(發送通知) 原為notification　修正為 builder.build()
                try {
                    Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ring_uri);
                    r.play();
                } catch (Exception e) {
                    // Error playing sound
                }
            }
        }.start();

        // 關閉服務
        stopSelf();
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT ,startId);
    }

    @Override
    public void onDestroy() {
        Log.i("mylog","onDestroy()");
        super.onDestroy();
    }


}

