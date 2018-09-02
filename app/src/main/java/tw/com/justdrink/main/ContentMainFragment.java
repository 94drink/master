package tw.com.justdrink.main;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterBottlesData;
import tw.com.justdrink.database.WaterDatabase;
import tw.com.justdrink.database.WaterDbProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class ContentMainFragment extends Fragment {

    public FloatingActionButton floatingActionButtonAdd, floatingActionButtonSelect;
    String time, date;
    TextView drinked, goal;
    Button btn;
    public static MediaPlayer mp;

    static ProgressBar progressBar;

    public static String weight = "weight";
    public static String weight_flie = "weight_unit";
    SharedPreferences sharedDataWeightUnit, sharedDataWeight;
    String total_weight, prefs_unit;

    Cursor weight_cursor;
    int drink_target;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        time = sdf.format(c.getTime());
        date = df.format(c.getTime());

        View view = inflater.inflate(R.layout.content_main, container, false);

        mp = MediaPlayer.create(getActivity(), R.raw.button_click);

        drinked = (TextView) view.findViewById(R.id.drinked);
        goal = (TextView) view.findViewById(R.id.goal);
        btn = (Button) view.findViewById(R.id.water_settings);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new BottleGrid();
        fragmentTransaction.replace(R.id.bottle_container, fragment).commit();

//        Cursor c1 = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_WEIGHT_URI, null, null, null, null);
//        int weight_data = getWeight(c1);
//        Log.e("WEIGHT", weight_data + "");
//        drink_target= calTarget(weight_data);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterSettings waterSettings = new WaterSettings();
                waterSettings.show(fragmentManager, "Water");
            }
        });
        floatingActionButtonSelect = (FloatingActionButton) view.findViewById(R.id.fab_select);
        floatingActionButtonAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);
        floatingActionButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectGlass selectGlass = new SelectGlass();
                selectGlass.show(fragmentManager, "Glasses");
            }
        });
        floatingActionButtonSelect.setImageResource(WaterBottlesData.getData().get(SelectGlass.position).imageId);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
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

   /* public int getWeight(Cursor c1) {
        weight_cursor = c1;
        if (weight_cursor.getCount() > 0) {
            weight_array = new int[weight_cursor.getCount() + 1];
            weight_cursor.moveToFirst();
            int i = 0;
            while (!weight_cursor.isAfterLast()) {
                weight_array[i] = weight_cursor.getColumnIndex(WaterDatabase.KEY_WEIGHT);
                i++;
                weight_cursor.moveToNext();
            }
        }
        return weight_array[0];
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu);
    }
}