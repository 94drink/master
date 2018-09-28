package tw.com.justdrink.dinrkreport;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterDbProvider;

public class D_report_fragment_1 extends Fragment {

    LineChart chart;
    LineData data;
    GetDates getDates;
    Cursor chart_cursor, st_cursor;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList <String> yVals = new ArrayList<String>();
    private Button drep1_btn1;
    private Button drep1_btn2;
    private Button drep1_btn3;
    private Context context;
    private TextView drep1_date1,drep1_date2,drep1_text2,drep1_text3,drep1_text5,drep1_text6;
    //private TextView drep1_text1,drep1_text4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_d_report_fragment_1, container, false);
        final int count=6;
        context = view.getContext();
        getDates = new GetDates();
        chart = (LineChart) view.findViewById(R.id.weekchart);
        drep1_btn1 = (Button)view.findViewById(R.id.drep1_btn1);
        drep1_btn2 = (Button)view.findViewById(R.id.drep1_btn2);
        drep1_btn3 = (Button)view.findViewById(R.id.drep1_btn3);
        drep1_date1 = (TextView)view.findViewById(R.id.drep1_date1);
        drep1_date2 = (TextView)view.findViewById(R.id.drep1_date2);
        //drep1_text1 = (TextView)view.findViewById(R.id.drep1_text1);
        drep1_text2 = (TextView)view.findViewById(R.id.drep1_text2);
        drep1_text3 = (TextView)view.findViewById(R.id.drep1_text3);
        //drep1_text4 = (TextView)view.findViewById(R.id.drep1_text4);
        drep1_text5 = (TextView)view.findViewById(R.id.drep1_text5);
        drep1_text6 = (TextView)view.findViewById(R.id.drep1_text6);

        //取得今天日期
        String date_now = getDates.getDate();
        //取得7天前日期
        String date_lw = getDates.getDateafter(date_now, count);
        //設定顯示今天日期
        drep1_date1.setText(date_lw);
        drep1_date2.setText(date_now);

        //**--顯示當日飲水量--**//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String d_now = "date) = '" + date_now + "' GROUP BY (date";
        st_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, d_now, null, null);
        if(st_cursor.getCount() > 0) {
            st_cursor.moveToFirst();
            String st = st_cursor.getString(1) + "ml";
            drep1_text2.setText(st);
        }else{
            drep1_text2.setText("0ml");
        }
        //**--顯示當日飲水量--**//

        //chart.setDescription("說明文字");
        chart.setDescription("");
        chart.fitScreen();
        //設置圖表資料
        chart.setData(getLineData(date_now));
        //預設不顯示右邊2顆按鈕
        drep1_btn2.setVisibility(View.INVISIBLE);
        drep1_btn3.setVisibility(View.INVISIBLE);

        //監聽最左邊按鈕動作
        drep1_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //案過按鈕之後顯示右邊2顆按鈕
                drep1_btn2.setVisibility(View.VISIBLE);
                drep1_btn3.setVisibility(View.VISIBLE);
                //取得目前顯示的起始日期放到date
                getDates = new GetDates();
                String date_now = drep1_date1.getText().toString();
                //把date丟到getDateafter函式計算7天前日期
                String date_lw = getDates.getDateafter(date_now, count);
                //更新上方日期顯示
                drep1_date1.setText(date_lw);
                drep1_date2.setText(date_now);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_now));

                //----***開發中測試***----//

                //----***開發中測試***----//
            }
        });

        //監聽最中間按鈕動作
        drep1_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDates = new GetDates();
                String date_now = getDates.getDate();
                String date_lw = getDates.getDateafter(date_now, count);
                drep1_date1.setText(date_lw);
                drep1_date2.setText(date_now);
                drep1_btn2.setVisibility(View.INVISIBLE);
                drep1_btn3.setVisibility(View.INVISIBLE);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_now));
            }
        });

        //監聽最右邊按鈕動作
        drep1_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drep1_btn2.setVisibility(View.VISIBLE);
                //取得目前顯示的結束日期放到date
                getDates = new GetDates();
                String date_now = drep1_date2.getText().toString();
                //把date丟到getDatebefore函式計算7天後日期
                String date_lw = getDates.getDatebefore(date_now, count);
                //更新上方日期顯示
                drep1_date1.setText(date_now);
                drep1_date2.setText(date_lw);
                //判斷是否到今天，是則隱藏右方按鈕
                String tempdate = getDates.getDate();
                if (tempdate.equals(date_lw)){
                    drep1_btn2.setVisibility(View.INVISIBLE);
                    drep1_btn3.setVisibility(View.INVISIBLE);
                }
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_lw));
            }
        });

        return view;
    }

    //圖表建立函示
    private LineData getLineData(String date){
        final int DATA_COUNT = 7;  //设置折线图横跨距离
        LineDataSet dataSetA = new LineDataSet( getChartAvg(DATA_COUNT, date), getResources().getString(R.string.weekly_average));
        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识
        LineDataSet dataSetB = new LineDataSet( getChartData(DATA_COUNT, date), getResources().getString(R.string.weekly_Drink));

        List<LineDataSet> dataSets = new ArrayList<>();
        //資料集A加入圖表
        dataSets.add(dataSetA);
        //資料集B加入圖表
        dataSets.add(dataSetB);
        //getLabels取得圖表下方的顯示文字(X坐標軸)
        LineData data = new LineData( getLabels(DATA_COUNT), dataSets);
        //設定主資料線條顏色
        dataSetA.setColor(Color.GREEN);
        dataSetB.setColor(Color.BLUE);
        //設定主資料節點顏色
        dataSetA.setCircleColor(Color.GREEN);
        dataSetB.setCircleColor(Color.BLUE);
        //設定主資料節點不包覆空心圓
        dataSetA.setDrawCircleHole(false);
        dataSetB.setDrawCircleHole(false);

        //設定被選中時，顯示Hilight顏色
        dataSetA.setHighLightColor(Color.RED);
        dataSetB.setHighLightColor(Color.RED);
        //將X軸標題顯示在圖表下方
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 返回LineData类型数据，该类型由标识X轴单位 List<String>的 集合和一个标识折线数据的List<ILineDataSet>组成
        return data;
    }

    //建立圖表主資料集
    private List<Entry> getChartData(int count, String date){

        //取得傳入日期
        String date_now = date;
        //取得count天前日期
        String date_lw = getDates.getDateafter(date_now, count-1);

        //**--抓取以日期為單位加總之飲水量總和**--//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String qureytxt = "date) BETWEEN '" + date_lw + "' AND '" + date_now + "' GROUP BY (date";
        chart_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, qureytxt, null, "date ASC");
        //**--抓取以日期為單位加總之飲水量總和**--//

        //**--建立圖表資料--**//
        List<Entry> chartData = new ArrayList<>();
        int j = 0;
        chart_cursor.moveToFirst();
        for (int i=0; i<count; i++){
            if ((count-j-chart_cursor.getCount()) <= 0){
                //圖表塞入DB資料
                float val = Float.valueOf(chart_cursor.getString(1));
                chartData.add(new Entry( val, i));
                chart_cursor.moveToNext();
            }else{
                //圖表塞入0
                chartData.add(new Entry( 0, i));
            }
            j++;
        }
        //**--建立圖表資料--**//

        return chartData;
    }

    //建立圖表平均值資料集
    private List<Entry> getChartAvg(int count, String date){
        //取得今天日期
        String date_now = date;
        //取得count天前日期
        String date_lw = getDates.getDateafter(date_now, count);

        //**--抓取以日期為單位加總之飲水量總和**--//
        //String[] projection = new String[] {"date", "sum(ml) as suml"};
        String[] projection = new String[] {"date, sum(ml) as suml"};
        //chart_cursor.getString(1)，0代表日期"date"、1代表加總"suml"，此處只抓2個欄位
        String qureytxt = "date) BETWEEN '" + date_lw + "' AND '" + date_now + "' GROUP BY (date";
        chart_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, qureytxt, null, "date ASC");
        //**--抓取以日期為單位加總之飲水量總和**--//

        //**--計算期間飲水量總和**--//
        float sum = 0;
        if(chart_cursor.getCount() > 0) {
            chart_cursor.moveToFirst();
            do{
                sum += Float.valueOf(chart_cursor.getString(1));
            }while (chart_cursor.moveToNext());
        }else{
            Toast.makeText(context, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
        }
        //**--計算期間飲水量總和**--//

        //**--建立圖表資料--**//
        List<Entry> chartData = new ArrayList<>();
        float avg = 0;
        if(chart_cursor.getCount() > 0){
            for (int i=0;i<count;i++){
                avg = sum / chart_cursor.getCount();
                chartData.add(new Entry( avg, i));
            }
            drep1_text5.setText(avg + "ml");
        }else {
            drep1_text5.setText("0ml");
        }
        //**--建立圖表資料--**//
        return chartData;
    }

    //**--建立圖表下方(X軸)顯示單位--**//
    private List<String> getLabels(int count){
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        List<String> chartLabels = new ArrayList<>();
        for(int i=0;i<count;i++) {
            switch ((dayOfWeek+i)%7){
                case 0:
                    chartLabels.add("Sun");
                    break;
                case 1:
                    chartLabels.add("Mon");
                    break;
                case 2:
                    chartLabels.add("Tue");
                    break;
                case 3:
                    chartLabels.add("Wed");
                    break;
                case 4:
                    chartLabels.add("Thu");
                    break;
                case 5:
                    chartLabels.add("Fri");
                    break;
                case 6:
                    chartLabels.add("Sat");
                    break;
            }
        }
        return chartLabels;
    }
}
