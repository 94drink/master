package tw.com.justdrink;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import tw.com.justdrink.database.WaterBottlesData;
import tw.com.justdrink.database.WaterDatabase;
import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.main.BottleGrid;
import tw.com.justdrink.main.WaterSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class Drinklog extends Fragment {

    public FloatingActionButton fabAdd, fabSelect;
    String time, date;
    TextView drinked, goal;
    Button waterSetting;
    Context context;
    public static MediaPlayer mp;

    static ProgressBar progressBar;

    public static String weight = "weight";
    public static String weight_flie = "weight_unit";
    SharedPreferences sharedDataWeightUnit, sharedDataWeight;
    String total_weight, prefs_unit;
    int drink_target;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        time = sdf.format(c.getTime());
        date = df.format(c.getTime());

        View view = inflater.inflate(R.layout.fragment_drinklog, container, false);

        mp = MediaPlayer.create(getActivity(), R.raw.button_click);

        drinked = (TextView) view.findViewById(R.id.drinked);
        goal = (TextView) view.findViewById(R.id.goal);
        waterSetting = (Button) view.findViewById(R.id.water_setting);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new BottleGrid();
        fragmentTransaction.replace(R.id.bottle_container, fragment).commit();

        sharedDataWeight = getActivity().getSharedPreferences(weight, 0);
        String weight_prefs = sharedDataWeight.getString("weight", 0 + "");
        int dummy = Integer.parseInt(weight_prefs);
        drink_target = calTarget(dummy);
        goal.setText("/" + drink_target + "");

        progressBar.setMax(drink_target);

        sharedDataWeightUnit = getActivity().getSharedPreferences(weight_flie, 0);
        sharedDataWeight = getActivity().getSharedPreferences(weight, 0);
        prefs_unit = sharedDataWeightUnit.getString("weight_unit", "Kg");
        sharedDataWeight = getActivity().getSharedPreferences(weight, 0);
        total_weight = sharedDataWeight.getString("weight", "00");

        // 修改喝水目標
        waterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterSettings waterSettings = new WaterSettings();
                waterSettings.show(fragmentManager, "Water");
            }
        });

        // 選擇水杯
        fabSelect = (FloatingActionButton) view.findViewById(R.id.fab_select);
        fabSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectGlass selectGlass = new SelectGlass();
                selectGlass.show(fragmentManager, "Glasses");
            }
        });
        fabSelect.setImageResource(WaterBottlesData.getData().get(SelectGlass.position).imageId);

        // 新增水杯(直接使用上次選擇的杯子)
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                ContentValues values = new ContentValues();
                values.clear();
                int pos = SelectGlass.position;
                values.put(WaterDatabase.KEY_POS, pos);
                values.put(WaterDatabase.KEY_DATE, date);
                values.put(WaterDatabase.KEY_TIME, time);
                Uri uri = WaterDbProvider.CONTENT_URI;
                Uri newUri = getActivity().getContentResolver().insert(uri, values);
                updateProgress(pos);
                Fragment fragment = new BottleGrid();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.bottle_container, fragment).addToBackStack(null).commit();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    public static void updateProgress(int position) {
        switch (position) {
            case 0:
                progressBar.incrementProgressBy(100);
                break;
            case 1:
                progressBar.incrementProgressBy(150);
                break;
            case 2:
                progressBar.incrementProgressBy(200);
                break;
            case 3:
                progressBar.incrementProgressBy(400);
                break;
            case 4:
                progressBar.incrementProgressBy(500);
                break;
            case 5:
                progressBar.incrementProgressBy(600);
                break;
            case 6:
                progressBar.incrementProgressBy(700);
                break;
            case 7:
                progressBar.incrementProgressBy(800);
                break;
        }
    }

    private int calTarget(int weight_data) {
        int data_y = weight_data * 33;
        Log.e("TARGET", data_y + "");
        return data_y;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu);
    }
}
