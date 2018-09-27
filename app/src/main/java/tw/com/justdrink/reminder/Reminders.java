package tw.com.justdrink.reminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import tw.com.justdrink.R;



public class Reminders extends Fragment {
    //設定Adapter,RecyclerView 變數

    ListView reminderview;
    RecyclerViewAdpater adpater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        reminderview = (ListView)view.findViewById(R.id.reminderList_item);
        adpater = new RecyclerViewAdpater(getActivity());

        reminderview.setAdapter(adpater);

        return view;
    }

}
