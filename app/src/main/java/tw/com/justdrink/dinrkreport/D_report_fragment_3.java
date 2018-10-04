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

public class D_report_fragment_3 extends Fragment {
    Cursor chart_cursor, st_cursor;
    GetDates getDates;
    LineChart chart;
    LineData data;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList <String> yVals = new ArrayList<String>();
    private Button drep3_btn1;
    private Button drep3_btn2;
    private Button drep3_btn3;
    private Context context;
    private TextView drep3_date1,drep3_text2,drep3_text3,drep3_text5,drep3_text6;
    //private TextView drep3_text1,drep3_text4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d_report_fragment_3, container, false);
        context = view.getContext();
        chart = (LineChart) view.findViewById(R.id.yearchart);
        drep3_btn1 = (Button)view.findViewById(R.id.drep3_btn1);
        drep3_btn2 = (Button)view.findViewById(R.id.drep3_btn2);
        drep3_btn3 = (Button)view.findViewById(R.id.drep3_btn3);
        drep3_date1 = (TextView)view.findViewById(R.id.drep3_date1);
        //drep3_date2 = (TextView)view.findViewById(R.id.drep3_date2);
        //drep3_text1 = (TextView)view.findViewById(R.id.drep3_text1);
        drep3_text2 = (TextView)view.findViewById(R.id.drep3_text2);
        drep3_text3 = (TextView)view.findViewById(R.id.drep3_text3);
        //drep3_text4 = (TextView)view.findViewById(R.id.drep3_text4);
        drep3_text5 = (TextView)view.findViewById(R.id.drep3_text5);
        drep3_text6 = (TextView)view.findViewById(R.id.drep3_text6);
        getDates = new GetDates();

        //**--顯示當日飲水量--**//
        String date_today = getDates.getDate();
        String[] projection = new String[] {"date", "sum(ml) as suml"};
        String d_now = "date) = '" + date_today + "' GROUP BY (date";
        st_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, d_now, null, null);
        if(st_cursor.getCount() > 0) {
            st_cursor.moveToFirst();
            String st = st_cursor.getString(1) + "ml";
            drep3_text2.setText(st);
        }else{
            drep3_text2.setText("0ml");
        }

        //**--顯示當日目標達成率--**//
        Cursor single_weight = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
        if(single_weight.getCount() > 0) {
            single_weight.moveToFirst();
            st_cursor.moveToFirst();
            float st = Float.parseFloat(st_cursor.getString(1));
            float wt =  Float.parseFloat(single_weight.getString(7));
            int wa = (int) (st / wt * 100);
            drep3_text3.setText("達成率: " + wa + "%");
        }else{
        }

        //取得今年數字
        String date_now = getDate();

        //設定顯示今年年份
        drep3_date1.setText(date_now);

        //chart.setDescription("說明文字");
        chart.setDescription("");
        chart.fitScreen();
        //設置圖表資料
        chart.setData(getLineData(date_now));
        //預設不顯示右邊2顆按鈕
        drep3_btn2.setVisibility(View.INVISIBLE);
        drep3_btn3.setVisibility(View.INVISIBLE);

        //監聽最左邊按鈕動作
        drep3_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //案過按鈕之後顯示右邊2顆按鈕
                drep3_btn2.setVisibility(View.VISIBLE);
                drep3_btn3.setVisibility(View.VISIBLE);
                //取得目前顯示的起始日期放到date
                String date_now = drep3_date1.getText().toString();
                String tempdate = Integer.toString(Integer.parseInt(date_now)-1);
                //更新上方日期顯示
                drep3_date1.setText(tempdate);
                //drep3_date2.setText(date);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(tempdate));
            }
        });

        //監聽最中間按鈕動作
        drep3_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_now = getDate();
                drep3_date1.setText(date_now);
                //drep3_date2.setText(date);
                drep3_btn2.setVisibility(View.INVISIBLE);
                drep3_btn3.setVisibility(View.INVISIBLE);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_now));
            }
        });

        //監聽最右邊按鈕動作
        drep3_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drep3_btn2.setVisibility(View.VISIBLE);
                //取得目前顯示的結束日期放到date
                String date_now = drep3_date1.getText().toString();
                int tempdate = Integer.parseInt(date_now)+1;
                //Toast.makeText(context,"tempd=" + tempdate, Toast.LENGTH_SHORT).show();
                //更新上方日期顯示
                drep3_date1.setText(tempdate + "");
                 //判斷是否到今天，是則隱藏右方按鈕
                int enddate = Integer.parseInt(getDate());
                if (enddate == tempdate){
                    drep3_btn2.setVisibility(View.INVISIBLE);
                    drep3_btn3.setVisibility(View.INVISIBLE);
                }
                //重新呼叫刷新圖表
                String date3 = Integer.toString(tempdate);
                chart.clear();
                chart.setData(getLineData(date3));
            }
        });
        return view;
    }

    //取得現在年份
    private String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String date_now = df.format(new java.util.Date());
        return date_now;
    }

    //圖表建立函示
    private LineData getLineData(String date){
        //final int DATA_COUNT = 12;  //设置折线图横跨距离
        LineDataSet dataSetA = new LineDataSet( getChartAvg(date), getResources().getString(R.string.year_average));
        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识
        LineDataSet dataSetB = new LineDataSet( getChartData(date), getResources().getString(R.string.year_Drink));

        List<LineDataSet> dataSets = new ArrayList<>();
        //資料集A加入圖表
        dataSets.add(dataSetA);
        //資料集B加入圖表
        dataSets.add(dataSetB);
        //getLabels取得圖表下方的顯示文字(X坐標軸)
        String[] chartLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Juy", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LineData data = new LineData( chartLabels, dataSets);
        //設定主資料線條顏色
        dataSetA.setColor(Color.GREEN);
        dataSetB.setColor(Color.BLUE);
        //設定主資料節點顏色
        dataSetA.setCircleColor(Color.GREEN);
        dataSetB.setCircleColor(Color.BLUE);
        //設定主資料節點不包覆空心圓
        dataSetA.setDrawCircleHole(false);
        dataSetB.setDrawCircleHole(false);
        //設定資料節點大小
        dataSetA.setCircleSize(3f);
        dataSetB.setCircleSize(3f);
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
    private List<Entry> getChartData(String date){
        int count = 12;
        //**--抓取以日期為單位加總之飲水量總和**--//
        String[] projection = new String[] {"substr(date, 1, 7) as month,sum(ml) as total"};
        //chart_cursor.getString(1)，0代表日期"date"、1代表加總"suml"，此處只抓2個欄位
        String qureytxt = "substr(date, 1, 4)) = '" + date + "' group by (substr(date, 1, 7)";
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
        return chartData;
    }

    //建立圖表平均值資料集
    private List<Entry> getChartAvg(String date){
    int count = 12;
        //**--抓取以日期為單位加總之飲水量總和**--//
        String[] projection = new String[] {"substr(date, 1, 7) as month,sum(ml) as total"};
        //chart_cursor.getString(1)，0代表日期"date"、1代表加總"suml"，此處只抓2個欄位
        String qureytxt = "substr(date, 1, 4)) = '" + date + "' group by (substr(date, 1, 7)";
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
            drep3_text5.setText((int)(avg) + "ml");
        }else {
            drep3_text5.setText("0ml");
        }

        //**--顯示平均體重--**//
        String[] proj_avg = new String[] {"avg(totml) as total"};
        Cursor avg_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, proj_avg, qureytxt, null, null);
        if(avg_cursor.getCount() > 0){
            avg_cursor.moveToFirst();
            float wsum = Float.parseFloat(avg_cursor.getString(0));
            float wavg = wsum * 30;
            int wa = (int)(avg / wavg *100);
            drep3_text6.setText("年達成率: " + wa + "%");
        }
        return chartData;
    }

}
