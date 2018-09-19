package tw.com.justdrink.reminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import tw.com.justdrink.R;



public class Reminders extends Fragment {
    //設定Adapter,RecyclerView 變數

    ListView recyclerViewonAlarm;
    RecyclerViewAdpater recyclerViewAdpater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);



//        recyclerViewonAlarm = (ListView) view.findViewById(R.id.list_view);
//        recyclerViewonAlarm.setAdapter(recyclerViewAdpater);
        return view;
    }

}
