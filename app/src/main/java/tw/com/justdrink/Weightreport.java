package tw.com.justdrink;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.dinrkreport.GetDates;

public class Weightreport extends Fragment {

    Cursor cursor_single, cursor_muti;
    TextView weightreport_text_view;
    private String result = "",date_now,date_lw ;
    WaterDbProvider waterDbProvider;
    GetDates getDates;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weightreport, container, false);
        TextView weightreport_text_view = (TextView)rootView.findViewById(R.id.weightreport_text_view);
        TextView datestart = (TextView)rootView.findViewById(R.id.datestart);
        TextView dateend = (TextView)rootView.findViewById(R.id.dateend);
        waterDbProvider = new WaterDbProvider();

        //取得今天日期
        getDates = new GetDates();
        date_now = getDates.getDate();

        //取得7天前日期
        date_lw = getDates.getDateafter(date_now, 7);

        datestart.setText(date_lw);
        dateend.setText(date_now);

//        //**--顯示當日飲水量--**//
//        String[] projection = new String[] {"date", "sum(ml) as suml"};
//        String d_now = "date) = '" + date_now + "' GROUP BY (date";
//        cursor_single = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, d_now, null, null);
//        if(cursor_single.getCount() > 0) {
//            cursor_single.moveToFirst();
//            int st = Integer.parseInt(cursor_single.getString(1));
//            weightreport_text_view.setText(st + "");
//        }else{
//            weightreport_text_view.setText("0ml");
//        }
//        //**--顯示當日飲水量--**//

        //**--顯示多日飲水量--**//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String qureytxt = "date) BETWEEN '" + date_lw + "' AND '" + date_now + "' GROUP BY (date";
        cursor_muti = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, qureytxt, null, "date ASC");
        int[] value = new int[cursor_muti.getCount()];
        String st = "";
        if(cursor_muti.getCount() > 0) {
            cursor_muti.moveToFirst();
            for (int i=0; i<cursor_muti.getCount(); i++){
                value[i] = Integer.parseInt(cursor_muti.getString(1));
                st = st +cursor_muti.getString(1) +"\n";
                weightreport_text_view.setText(st);
                cursor_muti.moveToNext();
            }
        }else{
            weightreport_text_view.setText("0ml");
        }
        //**--顯示多日飲水量--**//

        return rootView;
    }
}
