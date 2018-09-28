package tw.com.justdrink.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import tw.com.justdrink.R;



public class Reminders extends Fragment {
    //設定Adapter,RecyclerView 變數

    View reminderview;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 廣播連結
        reminderview = inflater.inflate(R.layout.fragment_reminders, container, false);
        listView = (ListView)reminderview.findViewById(R.id.reminderList_item);

        String[] arr = getActivity().getResources().getStringArray(R.array.alarm);
        ArrayAdapter<String> adapter =
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

//        listView.setOnItemClickListener(
//                new AdapterView.OnItemClickListener(){
//                    public void onItemClick(AdapterView<?> vg, View view, int position, long id) {
//                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
//                        dialog.setTitle("Title");
//                        if (position==0)
//                            dialog.setMessage("Book1 Selected");
//                        else if (position==1)
//                            dialog.setMessage("Book2 Selected");
//                        else
//                            dialog.setMessage("Book3 Selected");
//                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                            }
//                        });
//                        dialog.show();
//                    }
//                }
//        );

        return reminderview;
    }

}
