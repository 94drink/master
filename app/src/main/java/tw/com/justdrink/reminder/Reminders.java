package tw.com.justdrink.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.UriMatcher;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.Calendar;

import tw.com.justdrink.R;
import tw.com.justdrink.settings.Setting;

/**
 * Created by Ｍark on 10/08/2018.
 */

public class Reminders extends Fragment {
    //設定Adapter,RecyclerView 變數
    Context context;
    View reminderview;
    ListView listView;
    Calendar calendar = Calendar.getInstance();
    PendingIntent pi;
    PendingIntent pi1;
    PendingIntent pi2;
    AlarmService alarmService;
    Intent intent;
    TextView timeset;
    int timeNumber = 0 ;
    int voiceType = 0 ;
    Vibrator vib;
    String path ;
    private static final int Ringtone = 0;
    AudioManager audioManager ;

    //連接Service 的Function
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlarmService aService = new AlarmService();
            AlarmService.MyBinder mBinder = aService.new MyBinder(); //建立AlarmService的Binder
            alarmService = mBinder.getService();//將Binder內的getService方法放進alarmService
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            alarmService = null;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        reminderview = inflater.inflate(R.layout.fragment_reminders, container, false);
        listView = (ListView) reminderview.findViewById(R.id.reminderList_item);
        timeset = (TextView) reminderview.findViewById(R.id.timeText);
        vib = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);


        String[] arr = getActivity().getResources().getStringArray(R.array.alarm);//帶入Array陣列
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_expandable_list_item_1, arr);
        listView.setAdapter(adapter);

        listView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> vg, View view, int position, long id) {
                    }// onItemSelected

                    @Override
                    public void onNothingSelected(AdapterView<?> vg) {
                    }
                } //new OntemSelectedListener
        );


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> vg, View view, final int position, long id) {
                        //vg被點的母元件,也就是Listview//View 被點的item本身//position第幾個item被點到//item的id
                        //AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context, 1);

                        switch (position) {
                            case 0:

                                builder.setTitle(R.string.alarm_time)
                                        .setSingleChoiceItems(R.array.time, 0, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                switch (i){
                                                    case 0 :
                                                         Toast.makeText(context, "15 Min Once",
                                                                Toast.LENGTH_SHORT).show();
                                                        timeNumber = 0 ;
                                                        return ;
                                                    case 1:
                                                        Toast.makeText(context, "30 Min Once",
                                                                Toast.LENGTH_SHORT).show();
                                                        timeNumber=1;
                                                        return;
                                                    case 2:
                                                        Toast.makeText(context, "One Hour Once",
                                                                Toast.LENGTH_SHORT).show();
                                                        timeNumber=2;
                                                        return;
                                                }
                                                return;
                                            }
                                        })
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//按下確定後判斷timeNumber 0 , 1 , 2 分別提醒每15分,30分,1小時一次
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                calendar.setTimeInMillis(System.currentTimeMillis());
                                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                                int minute = calendar.get(Calendar.MINUTE);

                                                if (timeNumber == 0 ){
                                                    timeNumber = 0 ;
                                                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                                            new MyOnTimeSetListener(), hour, minute, true);
                                                    timePickerDialog.show();

                                                    intent = new Intent(Reminders.this.getContext(), AlarmService.class);
                                                    //建立PendingIntent
                                                    pi2 = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                }else if(timeNumber  == 1 ) {
                                                    timeNumber = 1 ;
                                                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(context,
                                                            new MyOnTimeSetListener(), hour, minute, true);
                                                    timePickerDialog1.show();

                                                    intent = new Intent(Reminders.this.getContext(), AlarmService.class);
                                                    //建立PendingIntent
                                                    pi1 = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                }else if(timeNumber == 2) {
                                                    // 跳出TimePickerDialog來設定時間 */
                                                    timeNumber = 2 ;
                                                    TimePickerDialog timePickerDialog2 = new TimePickerDialog(context,
                                                            new MyOnTimeSetListener(), hour, minute, true);
                                                    timePickerDialog2.show();

                                                    intent = new Intent(Reminders.this.getContext(), AlarmService.class);
                                                    //建立PendingIntent
                                                    pi = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                }
                                                return;
                                            }
                                        })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { //如果都沒有選
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(context, "取消",
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                                builder.show();
                                Log.i("setting", "setting");
                                break;
                            case 1:
                                AlarmManager alarmManager =
                                        (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(pi);
                                // 以Toast提示已刪除設定，並更新顯示的鬧鐘時間
                                Toast.makeText(context, R.string.remove_alarm,
                                        Toast.LENGTH_SHORT).show();
                                timeset.setText("");
                                break;
                            case 2:
                                //建立內建的音樂選擇器 -- 可用於來電鈴聲.通知聲等
                                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);//內建系統鈴聲設置
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "選擇提示聲:");//設置內建標題
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);//是否顯示沉默的項目 .否
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);//是否顯示預設的項目 是
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_INCLUDE_DRM, false);//靜音選項 否
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION); //選擇器內的鈴聲類型. 可選擇來電聲.或通知聲
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,RingtoneManager.getActualDefaultRingtoneUri(getActivity(),RingtoneManager.TYPE_RINGTONE));
                                //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                startActivityForResult(intent, Ringtone);//這邊採startActivityForResult來跳轉，０代表一個根據，可寫其他的值　但一定要>=0

                                break;

                            case 3:
                                builder.setTitle(R.string.voiceTitle)
                                        .setSingleChoiceItems(R.array.voiceSetting, 3, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                switch (i){
                                                    case 0 :
                                                        voiceType =0 ; //normal正常模式
                                                        return ;
                                                    case 1:
                                                        voiceType =1 ; //vribrate震動
                                                        return;
                                                    case 2:
                                                        voiceType =2 ; //  靜音模式
                                                        return;
                                                }
                                                return;
                                            }
                                        })
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//按下確定後判斷timeNumber 0 , 1 , 2 分別提醒每15分,30分,1小時一次
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                    if(voiceType==0){
                                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                                        Toast.makeText(context, "Normal/正常模式",
                                                                Toast.LENGTH_SHORT).show();
                                                        //增加音樂提示聲音
                                                    }else if(voiceType == 1){
                                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                                        vib.vibrate(500);
                                                        Toast.makeText(context, "Vrbrate/震動開啟",
                                                                Toast.LENGTH_SHORT).show();
                                                    }else if(voiceType == 2){
                                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                                        Toast.makeText(context, "Silent/靜音開啟",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                return;
                                            }
                                        })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { //如果都沒有選
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                return;
                                            }
                                        });
                                builder.show();
                                break;
                        }
                    }
                }
        );

        return reminderview;
    }

    private class MyOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // 取得設定後的時間，秒跟毫秒設為 0
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
			/*
			 * AlarmManager.RTC_WAKEUP設定服務在系統休眠時同樣會執行
			 * 以set()設定的PendingIntent只會執行一次
			 */
            AlarmManager alarmManager = (AlarmManager)
                    context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pi);//設置首次呼叫

            //設置每小時呼叫一次
            //type：鬧鐘類型，startTime：鬧鐘首次執行時間，intervalTime：鬧鐘執行的間隔時間，pi：闹钟响应动作
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi2);//每15min 1 次
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pi1);     //30min   1 次
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pi);            //1hour   1次
            String tmpS = format(hourOfDay) + ":" + format(minute);
            // 以Toast提示設定已完成
//            Toast.makeText(context, "設定鬧鐘時間為" + tmpS ,
//                    Toast.LENGTH_SHORT).show();
            //要將抓到的時間顯示在timeText
            if(timeNumber==0) {
                timeset.setText("現在時間為" + tmpS + "\n" +"15分鐘提醒一次");
            }else if (timeNumber ==1 ){
                timeset.setText("現在時間為" + tmpS + "\n"+ "30分鐘提醒一次");
            }else if (timeNumber==2){
                timeset.setText("現在時間為" + tmpS + "\n"+ "1 hour提醒一次");
            }
        }
    }

    // 日期時間顯示兩位數的method
    private String format(int x) {
        String s = String.valueOf(x);
        return (s.length() == 1) ? "0" + s : s;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //默認聲音選完會調用至onActivityResult方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//系統內建聲音
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);//聲音的URI回傳值 至uri
        RingtoneManager.getActualDefaultRingtoneUri(context,RingtoneManager.getDefaultType(uri));

        //判斷按下取消以及確認之後
        if (resultCode != Activity.RESULT_OK) {//按下取消
            Toast.makeText(context, "取消",Toast.LENGTH_SHORT).show();

            return;
        } else if (notification != null)
        {
           switch (requestCode)
           {
                    case Ringtone:
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,voiceType,0);
//                        MediaPlayer player = MediaPlayer.create(getContext(),uri);
//                        player.start();
//                        Toast.makeText(context, notification + "",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, uri +"",Toast.LENGTH_SHORT).show();
                        Log.i("String","String"+ path);
                        
                        return;
                    default:
                        break;
                 }
                }
            }



        }



