package tw.com.justdrink;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.dinrkreport.GetDates;


public class Weight extends DialogFragment implements OnClickListener {

    EditText enter_weight;
    TextView unit;
    Button cancel, ok;
    View v;
    String prefs_unit,weight_prefs;
    private int dig_bun;
    public static String weight_flie = "weight_unit";
    public static String weight = "weight";
    SharedPreferences sharedDataWeightUnit, sharedDataWeight;

    //--**資料庫相關類別宣告**--//
    Cursor cursor_single, cursor_muti;
    //WaterDbProvider waterDbProvider;
    GetDates getDates;


    private void init() {
        enter_weight = (EditText) v.findViewById(R.id.weight);
        //unit = (TextView) v.findViewById(R.id.unit);
        ok = (Button) v.findViewById(R.id.ok);
        cancel = (Button) v.findViewById(R.id.cancel);
        enter_weight.setTextColor(getResources().getColor(R.color.black));

        //**--從SharedPreferences抓資料出來顯示--**//
//        sharedDataWeightUnit = getActivity().getSharedPreferences(weight_flie, 0);
//        sharedDataWeight = getActivity().getSharedPreferences(weight, 0);
//        prefs_unit = sharedDataWeightUnit.getString("weight_unit", "Kg");
//        weight_prefs = sharedDataWeight.getString("weight", 0 + "");
//        unit.setText(prefs_unit);

        //**--從資料庫抓資料出來顯示--**//
        //取得今天日期
        getDates = new GetDates();
        String date_now = getDates.getDate();
        //**--顯示當日Weight Table--**//
        //String[] projection = new String[] {"date", "sum(weight) as suml"};
        String d_now = "date) = '" + date_now + "' GROUP BY (date";
        cursor_single = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
        if(cursor_single.getCount() > 0) {
            cursor_single.moveToFirst();
            String st = cursor_single.getString(1);
            enter_weight.setHint(st);
        }else{
            enter_weight.setText(getString(R.string.no_data));
        }
        //**--顯示當日Weight Table--**//

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
        //TextView title = (TextView) v.findViewById(R.id.dialog_tite);
        init();
        enter_weight.setContentDescription(weight_prefs);
        setCancelable(true);
        //Toast.makeText(getActivity(), dig_bun + "", Toast.LENGTH_LONG).show();
        return v;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.cancel:
                getDialog().dismiss();
                break;

            case R.id.ok:
                //**--原本寫入SharedPreferences的程式段落--**//
//                String data = enter_weight.getText().toString();
//                int todaydate=Integer.parseInt(data);
//                SharedPreferences.Editor editor = sharedDataWeight.edit();
//                editor.putString("weight", data);
//                editor.commit();
//
//                Fragment fragment = new DrinkWater();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                //**--原本寫入SharedPreferences的程式段落--**//

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
                        cursor_single.moveToFirst();
                        uid = "_id=" + cursor_single.getString(0);
                        int weight_ml = Integer.parseInt(weight) * 33;
                        int weight_total = weight_ml + Integer.parseInt(cursor_single.getString(4)) + Integer.parseInt(cursor_single.getString(5)) + Integer.parseInt(cursor_single.getString(6));
                        values.put(WaterDBHelper.KEY_WEIGHT, weight);
                        values.put(WaterDBHelper.KEY_WIML, weight_ml);
                        values.put(WaterDBHelper.KEY_TOTML, weight_total);
                        int result = getActivity().getContentResolver().update(WaterDbProvider.CONTENT_URI_WEIGHT, values, uid, null);
                        Toast.makeText(getActivity(), getString(R.string.set_ok) , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), getString(R.string.no_data) , Toast.LENGTH_SHORT).show();
                    }
                    //**--修改為寫入ＳＱＬＩＴＥ的程式段落--**//

                    if (dig_bun == 1){
                        getDialog().dismiss();
                        FragmentManager fm = getFragmentManager();
                        WaterSettings waterSettings = new WaterSettings();
                        waterSettings.show(fm, "Water");
                        break;
                    }else {
                        getDialog().dismiss();
                        break;
                    }
                }
        }
    }
}
