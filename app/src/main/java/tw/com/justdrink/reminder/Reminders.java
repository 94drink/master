package tw.com.justdrink.reminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import tw.com.justdrink.R;



public class Reminders extends Fragment {
    //設定Adapter,RecyclerView 變數
//    String[] contactArray = {};
//    ListView listView;
//    ArrayAdapter<String> contactAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
//        contactArray = new String[]{"設置","早安","午安","晚安","你好","Hellow","Hola?","JustDrink"};
//        listView = (ListView)findViewById(R.id.reminders);
//
//        contactAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactArray);
//        listView.setAdapter(contactAdapter);
        return view;
    }

}
