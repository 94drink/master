package tw.com.justdrink;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.dinrkreport.GetDates;

public class Weightreport extends Fragment {

    Cursor weight_cursor;
    //TextView weightreport_text_view;
    private String result = "",date_now,date_lw ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weightreport, container, false);
        TextView weightreport_text_view = (TextView)rootView.findViewById(R.id.weightreport_text_view);
        TextView datestart = (TextView)rootView.findViewById(R.id.datestart);
        TextView dateend = (TextView)rootView.findViewById(R.id.dateend);

        //取得今天日期
        GetDates getDates = new GetDates();
        date_now = getDates.getDate();

        //取得7天前日期
        date_lw = getDates.getDateafter(date_now, 7);

        datestart.setText(date_lw);
        dateend.setText(date_now);

        //**--抓取DATE GROUP 飲水量總和**--//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
//        String qureytxt = "date) BETWEEN '2018-09-20' AND '2018-09-24' GROUP BY (date";
        String qureytxt = "date) BETWEEN '" + date_lw + "' AND '" + date_now + "' GROUP BY (date";
        weight_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI, projection, qureytxt, null, null);
        //**--抓取DATE GROUP 飲水量總和**--//

        if(weight_cursor.getCount() > 0) {
            weight_cursor.moveToFirst();
            do{
                result += weight_cursor.getString(0) + ","
                        + weight_cursor.getString(1) + "\n";
            }while (weight_cursor.moveToNext());
            weightreport_text_view.setText(result);
        }else{
            weightreport_text_view.setText("NO DATA!!");
        }

        return rootView;
    }
}
