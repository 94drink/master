package tw.com.justdrink;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import tw.com.justdrink.database.WaterBottlesData;
import tw.com.justdrink.database.WaterDatabase;
import tw.com.justdrink.database.WaterDbProvider;


public class DrinkWater extends Fragment {

    public FloatingActionButton fabAdd, fabSelect;
    String time, date, total_weight, prefs_unit;
    TextView drinked, goal;
    Button waterSetting;
    static ProgressBar progressBar;
    public static String weight_flie = "weight_unit", weight = "weight";
    SharedPreferences sharedDataWeightUnit, sharedDataWeight;
    int drink_target, is_drinked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        time = sdf.format(c.getTime());
        date = df.format(c.getTime());

        View view = inflater.inflate(R.layout.fragment_drinkwater, container, false);

        drinked = (TextView) view.findViewById(R.id.drinked);
        goal = (TextView) view.findViewById(R.id.goal);
        waterSetting = (Button) view.findViewById(R.id.water_setting);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new BottleGrid();
        ft.replace(R.id.bottle_container, fragment).commit();

        sharedDataWeight = getActivity().getSharedPreferences(weight, 0);
        String weight_prefs = sharedDataWeight.getString("weight", "70");
        int dummy = Integer.parseInt(weight_prefs);

        // 目標總水量
        drink_target = calTarget(dummy);
        goal.setText("/" + drink_target + "");
        progressBar.setMax(drink_target);

        // 目前已喝的水量
        is_drinked = 0;
        drinked.setText("" + is_drinked);
        progressBar.setProgress(is_drinked);

        // 修改喝水目標
        waterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterSettings waterSettings = new WaterSettings();
                waterSettings.show(fm, "Water");
            }
        });

        // 選擇水杯並新增
        fabSelect = (FloatingActionButton) view.findViewById(R.id.fab_select);
        fabSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectGlass selectGlass = new SelectGlass();
                selectGlass.show(fm, "Glasses");
            }
        });
        fabSelect.setImageResource(WaterBottlesData.getData().get(SelectGlass.position).imageId);

        // 新增水杯(直接使用上次選擇的杯子)
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.clear();
                int pos = SelectGlass.position;
                int ml = Integer.parseInt(WaterBottlesData.getData().get(SelectGlass.position).title);
                values.put(WaterDatabase.KEY_POS, pos);
                values.put(WaterDatabase.KEY_ML, ml);
                values.put(WaterDatabase.KEY_DATE, date);
                values.put(WaterDatabase.KEY_TIME, time);
                Uri uri = WaterDbProvider.CONTENT_URI;
                Uri newUri = getActivity().getContentResolver().insert(uri, values);
                progressBar.incrementProgressBy(ml);
                is_drinked += ml;
                drinked.setText("" + is_drinked);
                Fragment fragment = new BottleGrid();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.bottle_container, fragment).addToBackStack(null).commit();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    private int calTarget(int weight_data) {
        int target = weight_data * 33;
        //Log.e("TARGET", data_y + "");
        return target;

    }
}

