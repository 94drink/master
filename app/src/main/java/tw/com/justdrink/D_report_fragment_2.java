package tw.com.justdrink;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class D_report_fragment_2 extends Fragment {

    LineChart chart;
    LineData data;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList <String> yVals = new ArrayList<String>();
    private Button drep2_btn1;
    private Button drep2_btn2;
    private Button drep2_btn3;
    private Context context;
    private TextView drep2_date1,drep2_date2,drep2_text2,drep2_text3,drep2_text5,drep2_text6;
    //private TextView drep2_text1,drep2_text4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        //取得今天日期
        String date = getDate();
        //取得7天前日期
        String date_lw = getDateafter(date, 30);
        //設定顯示今天日期
        drep2_date1.setText(date_lw);
        drep2_date2.setText(date);

        //chart.setDescription("說明文字");
        chart.setDescription("");
        chart.fitScreen();
        //設置圖表資料
        chart.setData(getLineData());
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
                String date = drep2_date1.getText().toString();
                //把date丟到getDateafter函式計算7天前日期
                String date_lw = getDateafter(date, 30);
                //更新上方日期顯示
                drep2_date1.setText(date_lw);
                drep2_date2.setText(date);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData());
            }
        });

        //監聽最中間按鈕動作
        drep2_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = getDate();
                String date_lw = getDateafter(date, 30);
                drep2_date1.setText(date_lw);
                drep2_date2.setText(date);
                drep2_btn2.setVisibility(View.INVISIBLE);
                drep2_btn3.setVisibility(View.INVISIBLE);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData());
            }
        });

        //監聽最右邊按鈕動作
        drep2_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drep2_btn2.setVisibility(View.VISIBLE);
                //取得目前顯示的結束日期放到date
                String date = drep2_date2.getText().toString();
                //把date丟到getDatebefore函式計算7天後日期
                String date_lw = getDatebefore(date, 30);
                //更新上方日期顯示
                drep2_date1.setText(date);
                drep2_date2.setText(date_lw);
                //判斷是否到今天，是則隱藏右方按鈕
                String tempdate = getDate();
                if (tempdate.equals(date)){
                    drep2_btn2.setVisibility(View.INVISIBLE);
                    drep2_btn3.setVisibility(View.INVISIBLE);
                }
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData());
            }
        });

        return view;
    }

    //取得現在日期
    private String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date_now = df.format(new java.util.Date());
        return date_now;
    }

    //計算7天前日期(7天為參數Num)
    public static String getDateafter(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //計算7天後日期(7天為參數Num)
    public static String getDatebefore(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 +改为-
        Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //圖表建立函示
    private LineData getLineData(){
        final int DATA_COUNT = 30;  //设置折线图横跨距离
        LineDataSet dataSetA = new LineDataSet( getChartAvg(DATA_COUNT, 1), getResources().getString(R.string.monthly_average));
        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识
        LineDataSet dataSetB = new LineDataSet( getChartData(DATA_COUNT, 2), getResources().getString(R.string.monthly_Drink));

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
    private List<Entry> getChartData(int count, int ratio){
        List<Entry> chartData = new ArrayList<>();
        for(int i=0;i<count;i++){
            float val = (float) (Math.random() * 49) + 50;
            chartData.add(new Entry( val, i));
        }
        return chartData;
    }

    //建立圖表平均值資料集
    private List<Entry> getChartAvg(int count, int ratio){
        List<Entry> chartData = new ArrayList<>();
        float sum = 0;
        //計算期間總和
        for(int i=0;i<count;i++){
            float val = (float) (Math.random() * 49) + 50;

            sum += val;
        }
        //填入每天平均值
        for (int i=0;i<count;i++){
            float avg = sum / count;
            chartData.add(new Entry( avg, i));
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
