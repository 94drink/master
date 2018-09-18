package tw.com.justdrink.dinrkreport;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tw.com.justdrink.R;

public class D_report_fragment_3 extends Fragment {

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        //取得今年數字
        String date = getDate();
        //取得7天前日期
        //final String date_lw = getDateafter(date, 365);
        //設定顯示今天日期
        drep3_date1.setText(date);
        //drep3_date2.setText(date);

        //chart.setDescription("說明文字");
        chart.setDescription("");
        chart.fitScreen();
        //設置圖表資料
        chart.setData(getLineData());
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
                String date = drep3_date1.getText().toString();
                int tempdate = Integer.parseInt(date)-1;
                //更新上方日期顯示
                drep3_date1.setText(tempdate + "");
                //drep3_date2.setText(date);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData());
            }
        });

        //監聽最中間按鈕動作
        drep3_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = getDate();
                drep3_date1.setText(date);
                //drep3_date2.setText(date);
                drep3_btn2.setVisibility(View.INVISIBLE);
                drep3_btn3.setVisibility(View.INVISIBLE);
                //重新呼叫刷新圖表
                chart.clear();
                chart.setData(getLineData());
            }
        });

        //監聽最右邊按鈕動作
        drep3_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drep3_btn2.setVisibility(View.VISIBLE);
                //取得目前顯示的結束日期放到date
                String date = drep3_date1.getText().toString();
                int tempdate = Integer.parseInt(date)+1;
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
                chart.clear();
                chart.setData(getLineData());
            }
        });
        return view;
    }

    //取得現在月份
    private String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String date_now = df.format(new java.util.Date());
        return date_now;
    }

    //圖表建立函示
    private LineData getLineData(){
        final int DATA_COUNT = 12;  //设置折线图横跨距离
        LineDataSet dataSetA = new LineDataSet( getChartAvg(DATA_COUNT, 1), getResources().getString(R.string.year_average));
        //设置折线数据 getChartData返回一个List<Entry>键值对集合标识 折线点的横纵坐标，"A"代表折线标识
        LineDataSet dataSetB = new LineDataSet( getChartData(DATA_COUNT, 2), getResources().getString(R.string.year_Drink));

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
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String day = df.format(new java.util.Date());
        int dayOfWeek = Integer.parseInt(day);

        List<String> chartLabels = new ArrayList<>();
        for(int i=0;i<count;i++) {
            switch ((dayOfWeek+i)%12){
                case 0:
                    chartLabels.add("Dec");
                    break;
                case 1:
                    chartLabels.add("Jan");
                    break;
                case 2:
                    chartLabels.add("Feb");
                    break;
                case 3:
                    chartLabels.add("Mar");
                    break;
                case 4:
                    chartLabels.add("Apr");
                    break;
                case 5:
                    chartLabels.add("May");
                    break;
                case 6:
                    chartLabels.add("Jun");
                    break;
                case 7:
                    chartLabels.add("Jul");
                    break;
                case 8:
                    chartLabels.add("Aug");
                    break;
                case 9:
                    chartLabels.add("Sep");
                    break;
                case 10:
                    chartLabels.add("Oct");
                    break;
                case 11:
                    chartLabels.add("Nov");
                    break;
            }
        }
        return chartLabels;
    }
}
