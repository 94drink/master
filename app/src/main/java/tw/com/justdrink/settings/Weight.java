package tw.com.justdrink.settings;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.dinrkreport.GetDates;
import tw.com.justdrink.drinkwater.DrinkWater;


public class Weight extends DialogFragment implements OnClickListener {

    EditText enter_weight;
    TextView unit;
    Button cancel, ok;
    View v;
    private int dig_bun;
    public static String weight_flie = "weight_unit";
    public static String weight = "weight";
    SharedPreferences sharedDataWeightUnit, sharedDataWeight;

    //--**資料庫相關類別宣告**--//
    Cursor cursor_single, cursor_muti;
    GetDates getDates;


    private void init() {
        enter_weight = (EditText) v.findViewById(R.id.weight);
        //unit = (TextView) v.findViewById(R.id.unit);
        ok = (Button) v.findViewById(R.id.ok);
        cancel = (Button) v.findViewById(R.id.cancel);
        enter_weight.setTextColor(getResources().getColor(R.color.black));

        //取得今天日期
        getDates = new GetDates();
        String date_now = getDates.getDate();
        //**--從資料庫抓資料出來顯示--**//
        String d_now = "date) = '" + date_now + "' GROUP BY (date";
        cursor_single = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
        if(cursor_single.getCount() > 0) {
            cursor_single.moveToFirst();
            String st = cursor_single.getString(1);
            enter_weight.setHint(st);
        }else{
            enter_weight.setHint(getString(R.string.reinput));
        }
        //**--顯示當日Weight Table--**//
        //Toast.makeText(getActivity(), "enter_weight=" + enter_weight.getText() , Toast.LENGTH_SHORT).show();
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle bundle = getArguments();
        dig_bun = bundle.getInt("Key01");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.firstweight, container, false);
        init();
        setCancelable(true);
        //Toast.makeText(getActivity(), dig_bun + "", Toast.LENGTH_LONG).show();
        return v;
    }

    @Override
    public void onClick(View arg0) {
        FragmentManager fm = getFragmentManager();
        WaterSettings waterSettings;
        switch (arg0.getId()) {
            case R.id.cancel:
                if (dig_bun == 1){
                    getDialog().dismiss();
                    fm = getFragmentManager();
                    waterSettings = new WaterSettings();
                    waterSettings.show(fm, "Water");
                    break;
                }else{
                    getDialog().dismiss();
                    break;
                }

            case R.id.ok:
                //**--修改為寫入ＳＱＬＩＴＥ的程式段落--**//
                ContentValues values = new ContentValues();
                String uid, weight;
                //**--從資料庫抓資料出來顯示--**//
                //取得今天日期
                getDates = new GetDates();
                String date_now = getDates.getDate();
                weight = enter_weight.getText().toString();
                //**--顯示當日Weight Table--**//
                if (weight.equals(null) || weight.equals("")){
                    Toast.makeText(getActivity(), getString(R.string.reinput) , Toast.LENGTH_SHORT).show();
                }else{
                    String d_now = "date) = '" + date_now + "' GROUP BY (date";
                    cursor_single = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
                    if(cursor_single.getCount() > 0) {
                        int weight_ml = Integer.parseInt(weight) * 33;
                        cursor_single.moveToFirst();
                        uid = "_id=" + cursor_single.getString(0);
                        int weight_total = weight_ml + Integer.parseInt(cursor_single.getString(4)) + Integer.parseInt(cursor_single.getString(5)) + Integer.parseInt(cursor_single.getString(6));
                        values.put(WaterDBHelper.KEY_WEIGHT, weight);
                        values.put(WaterDBHelper.KEY_WIML, weight_ml);
                        values.put(WaterDBHelper.KEY_TOTML, weight_total);
                        DrinkWater.goal.setText("/" + weight_total);
                        DrinkWater.progressBar.setMax(weight_total);
                        int result = getActivity().getContentResolver().update(WaterDbProvider.CONTENT_URI_WEIGHT, values, uid, null);
                        Toast.makeText(getActivity(), getString(R.string.set_ok) , Toast.LENGTH_SHORT).show();
                    }else{
                        int weight_ml = Integer.parseInt(weight) * 33;
                        values = new ContentValues();
                        values.clear();
                        values.put(WaterDBHelper.KEY_WEIGHT, weight);
                        values.put(WaterDBHelper.KEY_WDATE, date_now);
                        values.put(WaterDBHelper.KEY_WIML, weight_ml);
                        values.put(WaterDBHelper.KEY_SEML, 0);
                        values.put(WaterDBHelper.KEY_WEML, 0);
                        values.put(WaterDBHelper.KEY_SPML, 0);
                        values.put(WaterDBHelper.KEY_TOTML, weight_ml);
                        //Uri uri = WaterDbProvider.CONTENT_URI_WEIGHT;
                        Uri newUri = getActivity().getContentResolver().insert(WaterDbProvider.CONTENT_URI_WEIGHT, values);
                        Toast.makeText(getActivity(), getString(R.string.set_ok) , Toast.LENGTH_SHORT).show();
                    }
                    //**--修改為寫入ＳＱＬＩＴＥ的程式段落--**//

                    if (dig_bun == 1){
                        getDialog().dismiss();
                        fm = getFragmentManager();
                        waterSettings = new WaterSettings();
                        waterSettings.show(fm, "Water");
                        break;
                    }else {
                        getDates = new GetDates();
                        String date = getDates.getDate();
                        //Toast.makeText(getContext(), "功能尚未完成!!", Toast.LENGTH_SHORT).show();
                        int drink_target = DrinkWater.getWeightByDate(date);
                        DrinkWater.goal.setText("/" + drink_target + "");
                        getDialog().dismiss();
                        break;
                    }
                }
        }
    }
}
