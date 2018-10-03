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

public class Weightreport extends Fragment {
    Cursor chart_cursor, st_cursor;
    GetDates getDates;
    LineChart chart;
    LineData data;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList <String> yVals = new ArrayList<String>();
    private Button d_weight_btn1;
    private Button d_weight_btn2;
    private Button d_weight_btn3;
    private Context context;
    private TextView d_weight_date1,d_weight_text2,d_weight_text3,d_weight_text1,d_weight_text4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weightreport, container, false);
        context = view.getContext();
        chart = (LineChart) view.findViewById(R.id.yearchart);
        d_weight_btn1 = (Button)view.findViewById(R.id.d_weight_btn1);
        d_weight_btn2 = (Button)view.findViewById(R.id.d_weight_btn2);
        d_weight_btn3 = (Button)view.findViewById(R.id.d_weight_btn3);
        d_weight_date1 = (TextView)view.findViewById(R.id.d_weight_date1);
        //drep3_date2 = (TextView)view.findViewById(R.id.drep3_date2);
        d_weight_text1 = (TextView)view.findViewById(R.id.d_weight_text1);
        d_weight_text2 = (TextView)view.findViewById(R.id.d_weight_text2);
        d_weight_text3 = (TextView)view.findViewById(R.id.d_weight_text3);
        d_weight_text4 = (TextView)view.findViewById(R.id.d_weight_text4);
        getDates = new GetDates();

        //**--顯示當日體重--**//
        String date_today = getDates.getDate();
        String[] projection = new String[] {"date", "sum(weight) as suml"};
        String d_now = "date) = '" + date_today + "' GROUP BY (date";
        st_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, projection, d_now, null, null);
        if(st_cursor.getCount() > 0) {
            st_cursor.moveToFirst();
            String st = st_cursor.getString(1) + "kg";
            d_weight_text1.setText(st);
        }else{
            d_weight_text1.setText("No Data!!");
        }

        //**--顯示平均體重--**//
        String[] proj_avg = new String[] {"avg(weight) as total"};
        Cursor avg_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, proj_avg, null, null, null);
        if(avg_cursor.getCount() > 0) {
            avg_cursor.moveToFirst();
            String st = avg_cursor.getString(0) + "kg";
            d_weight_text2.setText(st);
        }else{
            d_weight_text2.setText("No Data!!");
        }

        //**--顯示MIN體重--**//
        String[] proj_min = new String[] {"min(weight) as total"};
        Cursor min_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, proj_min, null, null, null);
        if(min_cursor.getCount() > 0) {
            min_cursor.moveToFirst();
            String st = min_cursor.getString(0) + "kg";
            d_weight_text3.setText(st);
        }else{
            d_weight_text3.setText("No Data!!");
        }

        //**--顯示MAX體重--**//
        String[] proj_max = new String[] {"max(weight) as total"};
        Cursor max_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, proj_max, null, null, null);
        if(max_cursor.getCount() > 0) {
            max_cursor.moveToFirst();
            String st = max_cursor.getString(0) + "kg";
            d_weight_text4.setText(st);
        }else{
            d_weight_text4.setText("No Data!!");
        }

        //取得今年數字
        String date_now = getDate();

        //設定顯示今年年份
        d_weight_date1.setText(date_now);

        //chart.setDescription("說明文字");
        chart.setDescription("");
        chart.fitScreen();
        //設置圖表資料
        chart.setData(getLineData(date_now));
        //預設不顯示右邊2顆按鈕
        d_weight_btn2.setVisibility(View.INVISIBLE);
        d_weight_btn3.setVisibility(View.INVISIBLE);

        //監聽最左邊按鈕動作
        d_weight_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //案過按鈕之後顯示右邊2顆按鈕
                d_weight_btn2.setVisibility(View.VISIBLE);
                d_weight_btn3.setVisibility(View.VISIBLE);
                //取得目前顯示的起始日期放到date
                String date_now = d_weight_date1.getText().toString();
                String tempdate = Integer.toString(Integer.parseInt(date_now)-1);
                //更新上方日期顯示
                d_weight_date1.setText(tempdate);
                //drep3_date2.setText(date);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(tempdate));
            }
        });

        //監聽最中間按鈕動作
        d_weight_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_now = getDate();
                d_weight_date1.setText(date_now);
                //drep3_date2.setText(date);
                d_weight_btn2.setVisibility(View.INVISIBLE);
                d_weight_btn3.setVisibility(View.INVISIBLE);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData(date_now));
            }
        });

        //監聽最右邊按鈕動作
        d_weight_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //d_weight_btn2.setVisibility(View.VISIBLE);
                //取得目前顯示的結束日期放到date
                String date_now = d_weight_date1.getText().toString();
                int tempdate = Integer.parseInt(date_now)+1;
                //Toast.makeText(context,"tempd=" + tempdate, Toast.LENGTH_SHORT).show();
                //更新上方日期顯示
                d_weight_date1.setText(tempdate + "");
                //判斷是否到今天，是則隱藏右方按鈕
                int enddate = Integer.parseInt(getDate());
                if (enddate == tempdate){
                    d_weight_btn2.setVisibility(View.INVISIBLE);
                    d_weight_btn3.setVisibility(View.INVISIBLE);
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
        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识
        LineDataSet dataSetB = new LineDataSet( getChartData(date), getResources().getString(R.string.year_Drink));

        List<LineDataSet> dataSets = new ArrayList<>();

        //資料集B加入圖表
        dataSets.add(dataSetB);
        //getLabels取得圖表下方的顯示文字(X坐標軸)
        String[] chartLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Juy", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LineData data = new LineData( chartLabels, dataSets);
        //設定主資料線條顏色
        dataSetB.setColor(Color.BLUE);
        //設定主資料節點顏色
        dataSetB.setCircleColor(Color.BLUE);
        //設定主資料節點不包覆空心圓
        dataSetB.setDrawCircleHole(false);
        //設定資料節點大小
        dataSetB.setCircleSize(3f);
        //設定被選中時，顯示Hilight顏色
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
        String[] projection = new String[] {"substr(date, 1, 7) as month,avg(weight) as total"};
        //chart_cursor.getString(1)，0代表日期"date"、1代表加總"suml"，此處只抓2個欄位
        String qureytxt = "substr(date, 1, 4)) = '" + date + "' group by (substr(date, 1, 7)";
        chart_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, projection, qureytxt, null, "date ASC");
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

}
