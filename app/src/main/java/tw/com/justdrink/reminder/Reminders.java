package tw.com.justdrink.reminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserServiceCompat;
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


import java.util.Calendar;

import tw.com.justdrink.R;

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
    AlarmService alarmService ;
    Intent intent ;
    TextView timeset ;
    //連接Service 的Function
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlarmService aService = new AlarmService();
            AlarmService.MyBinder mBinder  = aService.new MyBinder(); //建立AlarmService的Binder
            alarmService = mBinder.getService();//將Binder內的getService方法放進alarmService
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            alarmService = null ;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        reminderview = inflater.inflate(R.layout.fragment_reminders, container, false);
        listView = (ListView)reminderview.findViewById(R.id.reminderList_item);
        timeset = (TextView)reminderview.findViewById(R.id.timeText);

        String[] arr = getActivity().getResources().getStringArray(R.array.alarm);//帶入Array陣列
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,arr);
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
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> vg, View view, final int position, long id) {
                        //vg被點的母元件,也就是Listview//View 被點的item本身//position第幾個item被點到//item的id
                        //AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context,1);

                        switch (position){
                            case 0:

                                builder.setTitle(R.string.alarm_time)
                                        .setSingleChoiceItems(R.array.time, 0, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                calendar.setTimeInMillis(System.currentTimeMillis());
                                                return;
                                            }
                                        })
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                                int minute = calendar.get(Calendar.MINUTE);
                                                // 跳出TimePickerDialog來設定時間 */
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(context ,
                                                        new MyOnTimeSetListener(),hour,minute , true);
                                                timePickerDialog.show();

                                                intent = new Intent (Reminders.this.getContext(),AlarmService.class);
                                                //建立PendingIntent
                                                pi1 = PendingIntent.getService(context,100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                return;
                                            }
                                        })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                return;
                                            }
                                        });

                                builder.show();
                                Log.i("setting","setting");
                                break;
                            case 1 :
                                AlarmManager alarmManager =
                                        (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(pi);
                                // 以Toast提示已刪除設定，並更新顯示的鬧鐘時間
                                Toast.makeText(context , R.string.remove_alarm,
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 2 :
                                //建立內建的音樂選擇器 -- 可用於來電鈴聲.通知聲等
                                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "選擇提示聲:");//設置內建標題
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);//是否顯示沉默的項目 . 是
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);//是否顯示預設的項目 是
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALL); //選擇器內的鈴聲類型. 可選擇來電聲.或通知聲
                                startActivity(intent);//啟動
                                //Toast.makeText( getContext (),"設置成功！", Toast.LENGTH_SHORT ).show();
                                break;

                            case 3 :
                                Toast.makeText(context , "震動" ,
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 4 :
                                Toast.makeText(context , "靜音" ,
                                        Toast.LENGTH_SHORT).show();

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
            calendar.set(Calendar.HOUR_OF_DAY , hourOfDay);
            calendar.set(Calendar.MINUTE , minute );
            calendar.set(Calendar.SECOND , 0 );
            calendar.set(Calendar.MILLISECOND , 0 );
			/*
			 * AlarmManager.RTC_WAKEUP設定服務在系統休眠時同樣會執行
			 * 以set()設定的PendingIntent只會執行一次
			 */
            AlarmManager alarmManager =(AlarmManager)
                    context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() , pi);//設置首次呼叫


            //設置每小時呼叫一次
            //type：闹钟类型，startTime：闹钟首次执行时间，intervalTime：闹钟两次执行的间隔时间，pi：闹钟响应动作
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pi2);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR,pi1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR,pi);
            String tmpS = format(hourOfDay) + ":" + format(minute);
            // 以Toast提示設定已完成
            Toast.makeText(context , "設定鬧鐘時間為" + tmpS +"\n",
                    Toast.LENGTH_SHORT).show();
             //要將抓到的時間顯示在timeText
            timeset.setText( "現在時間為"+ tmpS +"\n" );
        }
    }

    // 日期時間顯示兩位數的method
    private String format(int x) {
        String s = String.valueOf(x);
        return (s.length() ==  1)?"0" + s:s;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

   //默認聲音選完會調用至onActivityResult方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult","onActivityResult");

    }


}
