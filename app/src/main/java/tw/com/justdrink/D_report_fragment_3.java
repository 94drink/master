package tw.com.justdrink;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class D_report_fragment_3 extends Fragment {
    LineChart chart;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList <String> yVals = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_d_report_fragment_3, container, false);
        View view = inflater.inflate(R.layout.fragment_d_report_fragment_3, container, false);
                chart = (LineChart) view.findViewById(R.id.chart);
        chart.setDescription("說明文字");
        chart.fitScreen();
        LineData data = getData(12, 100);
        chart.setData(data);
        return view;
    }

    protected String[] mMonths = new String[] {
            "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
    };

    private LineData getData(int count, float range) {
        // TODO Auto-generated method stub
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setLineWidth(5);
        set1.setCircleSize(3f);
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set1.setHighLightColor(Color.BLUE);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        return data;
    }
}
