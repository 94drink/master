package tw.com.justdrink.dinrkreport;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterDbProvider;

public class D_report_fragment_2 extends Fragment {

    LineChart chart;
    LineData data;
    Cursor chart_cursor, st_cursor;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList <String> yVals = new ArrayList<String>();
    private GetDates getDates;
    private Button drep2_btn1;
    private Button drep2_btn2;
    private Button drep2_btn3;
    private Context context;
    private TextView drep2_date1,drep2_date2,drep2_text2,drep2_text3,drep2_text5,drep2_text6;
    //private TextView drep2_text1,drep2_text4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d_report_fragment_2, container, false);
        context = view.getContext();
        chart = (LineChart) view.findViewById(R.id.monthchart);
        drep2_btn1 = (Button)view.findViewById(R.id.drep2_btn1);
        drep2_btn2 = (Button)view.findViewById(R.id.drep2_btn2);
        drep2_btn3 = (Button)view.findViewById(R.id.drep2_btn3);
        drep2_date1 = (TextView)view.findViewById(R.id.drep2_date1);
        drep2_date2 = (TextView)view.findViewById(R.id.drep2_date2);
        //drep2_text1 = (TextView)view.findViewById(R.id.drep2_text1);
        drep2_text2 = (TextView)view.findViewById(R.id.drep2_text2);
        drep2_text3 = (TextView)view.findViewById(R.id.drep2_text3);
        //drep2_text4 = (TextView)view.findViewById(R.id.drep2_text4);
        drep2_text5 = (TextView)view.findViewById(R.id.drep2_text5);
        drep2_text6 = (TextView)view.findViewById(R.id.drep2_text6);

        getDates = new GetDates();
        //取得今天日期
        String date_now = getDates.getDate();
        //取得7天前日期
        String date_lw = getDates.getDateafter(date_now, 30);
        //設定顯示今天日期
        drep2_date1.setText(date_lw);
        drep2_date2.setText(date_now);

        //**--顯示當日飲水量--**//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String d_now = "date) = '" + date_now + "' GROUP BY (date";
        st_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, d_now, null, null);
        if(st_cursor.getCount() > 0) {
            st_cursor.moveToFirst();
            String st = st_cursor.getString(1) + "ml";
            drep2_text2.setText(st);
        }else{
            drep2_text2.setText("0ml");
        }

        //**--顯示當日目標達成率--**//
        Cursor single_weight = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
        if(single_weight.getCount() > 0 && st_cursor.getCount() > 0) {
            single_weight.moveToFirst();
            st_cursor.moveToFirst();
            float st = Float.parseFloat(st_cursor.getString(1));
            float wt =  Float.parseFloat(single_weight.getString(7));
            int wa = (int) (st / wt * 100);
            drep2_text3.setText("達成率: " + wa + "%");
        }else{
            drep2_text3.setText("達成率: 0%");
        }

        //chart.setDescription("說明文字");
        chart.setDescription("");
        chart.fitScreen();
        //設置圖表資料
        chart.setData(getLineData(date_now));
        //預設不顯示右邊2顆按鈕
        drep2_btn2.setVisibility(View.INVISIBLE);
        drep2_btn3.setVisibility(View.INVISIBLE);

        //監聽最左邊按鈕動作
        drep2_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //案過按鈕之後顯示右邊2顆按鈕
                drep2_btn2.setVisibility(View.VISIBLE);
                drep2_btn3.setVisibility(View.VISIBLE);
                //取得目前顯示的起始日期放到date
                String date_now = drep2_date1.getText().toString();
                //把date丟到getDateafter函式計算7天前日期
                String date_lw = getDates.getDateafter(date_now, 30);
                //更新上方日期顯示
                drep2_date1.setText(date_lw);
                drep2_date2.setText(date_now);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_now));
            }
        });

        //監聽最中間按鈕動作
        drep2_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_now = getDates.getDate();
                String date_lw = getDates.getDateafter(date_now, 30);
                drep2_date1.setText(date_lw);
                drep2_date2.setText(date_now);
                drep2_btn2.setVisibility(View.INVISIBLE);
                drep2_btn3.setVisibility(View.INVISIBLE);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_now));
            }
        });

        //監聽最右邊按鈕動作
        drep2_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drep2_btn2.setVisibility(View.VISIBLE);
                //取得目前顯示的結束日期放到date
                String date_now = drep2_date2.getText().toString();
                //把date丟到getDatebefore函式計算7天後日期
                String date_lw = getDates.getDatebefore(date_now, 30);
                //更新上方日期顯示
                drep2_date1.setText(date_now);
                drep2_date2.setText(date_lw);
                //判斷是否到今天，是則隱藏右方按鈕
                String tempdate = getDates.getDate();
                if (tempdate.equals(date_lw)){
                    drep2_btn2.setVisibility(View.INVISIBLE);
                    drep2_btn3.setVisibility(View.INVISIBLE);
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
        String date_now =date;
        final int DATA_COUNT = 30;  //设置折线图横跨距离
        LineDataSet dataSetA = new LineDataSet( getChartAvg(DATA_COUNT, date_now), getResources().getString(R.string.monthly_average));
        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识
        LineDataSet dataSetB = new LineDataSet( getChartData(DATA_COUNT, date_now), getResources().getString(R.string.monthly_Drink));

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
        //設定不顯示資料文字
        dataSetA.setDrawValues(false);
        dataSetB.setDrawValues(false);
        //設定資料節點大小
        dataSetA.setCircleSize(2f);
        dataSetB.setCircleSize(2f);
        //設定被選中時，節點顯示顏色
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
        //取得今天日期
        String date_now = date;
        //取得count天前日期
        String date_lw = getDates.getDateafter(date_now, count);

        //**--抓取DATE GROUP 飲水量總和**--//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String qureytxt = "date) BETWEEN '" + date_lw + "' AND '" + date_now + "' GROUP BY (date";
        String d_now = "date) = '" + date_now + "' GROUP BY (date";
        chart_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, qureytxt, null, "date ASC");
        //**--抓取DATE GROUP 飲水量總和**--//

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
        return chartData;
    }

    //建立圖表平均值資料集
    private List<Entry> getChartAvg(int count, String date){
        //取得今天日期
        String date_now = date;
        //取得count天前日期
        String date_lw = getDates.getDateafter(date_now, count);

        //**--抓取DATE GROUP 飲水量總和**--//
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String qureytxt = "date) BETWEEN '" + date_lw + "' AND '" + date_now + "' GROUP BY (date";
        chart_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, qureytxt, null, "date ASC");
        //**--抓取DATE GROUP 飲水量總和**--//
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

        //**--建立圖表資料--**//
        List<Entry> chartData = new ArrayList<>();
        float avg = 0;
        if(chart_cursor.getCount() > 0){
            for (int i=0;i<count;i++){
                avg = sum / chart_cursor.getCount();
                chartData.add(new Entry( avg, i));
            }
            drep2_text5.setText((int)(avg) + "ml");
        }else {
            drep2_text5.setText("0ml");
        }

        //**--顯示平均體重--**//
        String[] proj_avg = new String[] {"avg(totml) as total"};
        Cursor avg_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, proj_avg, qureytxt, null, null);
        if(avg_cursor.getCount() > 0){
            avg_cursor.moveToFirst();
            String wavg = avg_cursor.getString(0);
            int wa = (int)(avg / Float.parseFloat(wavg) *100);
            drep2_text6.setText("月達成率: " + wa + "%");
        }

        return chartData;
    }

    private List<String> getLabels(int count){
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String day = df.format(new java.util.Date());
        int dayOfWeek = Integer.parseInt(day);

        List<String> chartLabels = new ArrayList<>();
        for(int i=1;i<=count;i++) {
            switch ((dayOfWeek+i)%30){
                case 0:
                    chartLabels.add("30");
                    break;
                case 1:
                    chartLabels.add("01");
                    break;
                case 2:
                    chartLabels.add("02");
                    break;
                case 3:
                    chartLabels.add("03");
                    break;
                case 4:
                    chartLabels.add("04");
                    break;
                case 5:
                    chartLabels.add("05");
                    break;
                case 6:
                    chartLabels.add("06");
                    break;
                case 7:
                    chartLabels.add("07");
                    break;
                case 8:
                    chartLabels.add("08");
                    break;
                case 9:
                    chartLabels.add("09");
                    break;
                case 10:
                    chartLabels.add("10");
                    break;
                case 11:
                    chartLabels.add("11");
                    break;
                case 12:
                    chartLabels.add("12");
                    break;
                case 13:
                    chartLabels.add("13");
                    break;
                case 14:
                    chartLabels.add("14");
                    break;
                case 15:
                    chartLabels.add("15");
                    break;
                case 16:
                    chartLabels.add("16");
                    break;
                case 17:
                    chartLabels.add("17");
                    break;
                case 18:
                    chartLabels.add("18");
                    break;
                case 19:
                    chartLabels.add("19");
                    break;
                case 20:
                    chartLabels.add("20");
                    break;
                case 21:
                    chartLabels.add("21");
                    break;
                case 22:
                    chartLabels.add("22");
                    break;
                case 23:
                    chartLabels.add("23");
                    break;
                case 24:
                    chartLabels.add("24");
                    break;
                case 25:
                    chartLabels.add("25");
                    break;
                case 26:
                    chartLabels.add("26");
                    break;
                case 27:
                    chartLabels.add("27");
                    break;
                case 28:
                    chartLabels.add("28");
                    break;
                case 29:
                    chartLabels.add("29");
                    break;
            }
        }
        return chartLabels;
    }

}
