package tw.com.justdrink.drinkwater;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import tw.com.justdrink.R;
import tw.com.justdrink.settings.WaterSettings;
import tw.com.justdrink.settings.Weight;
import tw.com.justdrink.database.WaterBottlesData;
import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.database.WaterDbProvider;


/**
 * Created by Yuan on 9/17/2018.
 */
public class DrinkWater extends Fragment {

    public FloatingActionButton fabAdd, fabSelect;
    Button waterSetting;
    public static ProgressBar progressBar;
    public static TextView drinked, goal;
    public static int drink_target, is_drinked;
    static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drinkwater, container, false);

        drinked = (TextView) view.findViewById(R.id.drinked);
        goal = (TextView) view.findViewById(R.id.goal);
        waterSetting = (Button) view.findViewById(R.id.water_setting);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        context = getActivity();

        final FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new BottleGrid();
        ft.replace(R.id.bottle_container, fragment).commit();

        // 目標總水量
        drink_target = getWeightByDate(getTime("date"));
        if (drink_target == 0) {
            Weight weight = new Weight();
            Bundle bundle = new Bundle();
            bundle.putInt("Key01", 0);
            weight.setArguments(bundle);
            weight.show(fm, "Dialog");
        }
        goal.setText("/" + drink_target);
        progressBar.setMax(drink_target);

        // 單日已喝總水量
        is_drinked = getDrinkedByDate(getTime("date"));
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
                values.put(WaterDBHelper.KEY_POS, pos);
                values.put(WaterDBHelper.KEY_ML, ml);
                values.put(WaterDBHelper.KEY_DATE, getTime("date"));
                values.put(WaterDBHelper.KEY_TIME, getTime("time"));
                Uri uri = WaterDbProvider.CONTENT_URI_WATER;
                Uri newUri = getActivity().getContentResolver().insert(uri, values);
                progressBar.incrementProgressBy(ml);
                drinked.setText("" + getDrinkedByDate(getTime("date")));
                checkAchieveGoal();
                Fragment fragment = new BottleGrid();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.bottle_container, fragment).addToBackStack(null).commit();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    public static int getDrinkedByDate(String dateString) {
        String[] projection = new String[] {"sum(ml) as Total"};
        String where = "date) = '" + dateString + "' GROUP BY (date";
        Cursor drink_water_cursor = context.getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, projection, where, null, null);
        if (drink_water_cursor.getCount() > 0) {
            drink_water_cursor.moveToFirst();
            int is_drinked = drink_water_cursor.getInt(drink_water_cursor.getColumnIndex("Total"));
            //Log.e("cursor", "date:" + dateString + ", total:"  + drink_water_cursor.getString(drink_water_cursor.getColumnIndexOrThrow("Total")));
            return is_drinked;
        } else {
            return 0;
        }
    }

    public static int getWeightByDate(String dateString) {
        String where = "date = '" + dateString + "'";
        Cursor weight_cursor = context.getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, where, null, null);
        if (weight_cursor.getCount() > 0) {
            weight_cursor.moveToFirst();
            int drink_target = weight_cursor.getInt(weight_cursor.getColumnIndex("totml"));
            progressBar.setMax(drink_target);

            //Log.e("drink_target", "" + drink_target);
            return drink_target;
        } else {
            int tmp_weight = 0, tmp_total = 0;
            weight_cursor = context.getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, null, null, null);
            if (weight_cursor.getCount() > 0) {
                weight_cursor.moveToFirst();
                tmp_weight = weight_cursor.getInt(weight_cursor.getColumnIndex("weight"));
                tmp_total = tmp_weight * 33;
            }

            ContentValues values = new ContentValues();
            values.clear();
            values.put(WaterDBHelper.KEY_WEIGHT, "" + tmp_weight);
            values.put(WaterDBHelper.KEY_WDATE, dateString);
            values.put(WaterDBHelper.KEY_WIML, tmp_total);
            values.put(WaterDBHelper.KEY_SEML, 0);
            values.put(WaterDBHelper.KEY_WEML, 0);
            values.put(WaterDBHelper.KEY_SPML, 0);
            values.put(WaterDBHelper.KEY_TOTML, tmp_total);
            Uri uri = WaterDbProvider.CONTENT_URI_WEIGHT;
            Uri newUri = context.getContentResolver().insert(uri, values);
            progressBar.setMax(tmp_total);

            Log.e("tmp_weight", "" + tmp_weight + ", tmp_total: " + tmp_total);
            return tmp_total;
        }
    }

    private static String getTime(String type) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

        String res;
        if (type == "time") {
            res = sdf.format(c.getTime());
        } else {
            res = df.format(c.getTime());
        }
        return res;
    }

    //檢查是否已達成當日喝水目標
    public static void checkAchieveGoal() {
        String date = getTime("date");
        is_drinked = getDrinkedByDate(date);
        drink_target = getWeightByDate(date);
        //Log.e("checkAchieveGoal", "is_drinked:" + is_drinked + ", drink_target:"  + drink_target);

        if (is_drinked >= drink_target) {
            String achieveDate = "achieveDate", achieve_prefs;
            SharedPreferences sharedDataAchieve;

            sharedDataAchieve = context.getSharedPreferences(achieveDate, 0);
            achieve_prefs = sharedDataAchieve.getString("achieveDate", "");
            //Log.e("checkAchieveGoal", "achieve_prefs: " + achieve_prefs + ", date: "  + date);

            //檢查是否已寫入過SharedPreferences
            if (achieve_prefs == "" || !achieve_prefs.equals(date)) {
                //寫入SharedPreferences
                SharedPreferences.Editor editor = sharedDataAchieve.edit();
                editor.putString("achieveDate", date);
                editor.commit();

                //show Dialog
                new AlertDialog.Builder(context)
                        .setMessage("您已達成本日喝水目標！")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        }
    }
}

