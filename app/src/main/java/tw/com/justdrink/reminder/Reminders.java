package tw.com.justdrink.reminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import tw.com.justdrink.R;

public class Reminders extends Fragment {
    //設定Adapter,RecyclerView 變數
    Context context;
    View reminderview;
    ListView listView;
    Calendar calendar = Calendar.getInstance();
    PendingIntent pi;
    AlarmService alarmService ;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            AlarmService aService = new AlarmService();
            AlarmService.MyBinder mBinder  = aService.new MyBinder();
            alarmService = mBinder.getService();
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
                    public void onItemClick(AdapterView<?> vg, View view, int position, long id) {
                        //vg被點的母元件,也就是Listview//View 被點的item本身//position第幾個item被點到//item的id
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.setTitle("Title");

                        switch (position){
                            case 0:
//                                Intent intent = new Intent(vg.getContext() , AlarmService.class);
//                                startActivity(intent);

//                                Intent intent = new Intent (Reminders.this.getContext(),AlarmService.class);
//                                startActivity(intent);
//
//                                //建立PendingIntent
//                               pi = PendingIntent.getService(context,100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                //取得按下按鈕的時間為TimePickerDialog的預設值
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);
//
//                                // 跳出TimePickerDialog來設定時間 */
                                TimePickerDialog timePickerDialog = new TimePickerDialog(context ,
                                        new MyOnTimeSetListener(),hour,minute , true);
                                timePickerDialog.show();

                                Log.i("setting","setting");
                                break;
                            case 1 :
                                AlarmManager alarmManager =(AlarmManager)
                                        context.getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(pi);

                                Log.i("取消","setting");
                                break;
                            case 2 :
                                Log.i("music","music");
                                break;
                            case 3 :
                                Log.i("music","music");
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
                    calendar.getTimeInMillis() , pi);

            String tmpS = format(hourOfDay) + ":" + format(minute);
            // 以Toast提示設定已完成
            Toast.makeText(context , "設定鬧鐘時間為" + tmpS ,
                    Toast.LENGTH_SHORT).show();
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


}
