package tw.com.justdrink.reminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        reminderview = inflater.inflate(R.layout.fragment_reminders, container, false);
        listView = (ListView)reminderview.findViewById(R.id.reminderList_item);

        String[] arr = getActivity().getResources().getStringArray(R.array.alarm);
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
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.setTitle("Title");

                        switch (position){
                            case 0:
                                //Intent i = new Intent(context , AlarmService.class);
//                                //建立PendingIntent pi
//                                pi = PendingIntent.getService(context,100,i , PendingIntent.FLAG_CANCEL_CURRENT);
//                                calendar.setTimeInMillis(System.currentTimeMillis());//抓取時間
//                                int hour = calendar.get(Calendar.DAY_OF_MONTH);//小時傳入
//                                int minute = calendar.get(Calendar.MINUTE);//分鐘傳入
//
//                                TimePickerDialog timePickerDialog = new TimePickerDialog(context ,
//                                        new MyOnTimeSetListener(),hour,minute , true);
//                                timePickerDialog.show();
                                break;
                            case 1 :
                                Log.i("setting","setting");
                                break;
                            case 2 :
                                Log.i("music","music");
                                break;
                            case 3 :
                                Log.i("music","music");

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

}
