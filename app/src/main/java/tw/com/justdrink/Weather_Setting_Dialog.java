package tw.com.justdrink;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.dinrkreport.GetDates;

/**
 * A simple {@link Fragment} subclass.
 */
public class Weather_Setting_Dialog extends DialogFragment implements View.OnClickListener {
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4;
    TextView textView4, textView_tot;
    SeekBar seekBar;
    Button cancel, ok;
    View v;
    //--**資料庫相關類別宣告**--//
    Cursor cursor_single;
    GetDates getDates;
    int seml=0, wiml=0, totml=0, final_tot = 0, final_self = 0;

    private void init() {
        ok = (Button) v.findViewById(R.id.btn_cancel);
        cancel = (Button) v.findViewById(R.id.btn_ok);
        imageButton1 = (ImageButton)v.findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton)v.findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton)v.findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton)v.findViewById(R.id.imageButton4);
        textView4 = (TextView)v.findViewById(R.id.textView4);
        textView_tot = (TextView)v.findViewById(R.id.dialog_tot_textview);
        seekBar = (SeekBar)v.findViewById(R.id.seekBar);
        //取得今天日期
        getDates = new GetDates();
        String date_now = getDates.getDate();
        //**--從資料庫抓資料出來顯示--**//
        String d_now = "date) = '" + date_now + "' GROUP BY (date";
        cursor_single = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
        if(cursor_single.getCount() > 0) {
            cursor_single.moveToFirst();
            wiml = Integer.parseInt(cursor_single.getString(3));
            seml = Integer.parseInt(cursor_single.getString(5));
            totml = Integer.parseInt(cursor_single.getString(7));
            seekBar.setMax((int)(wiml * 0.5));
            seekBar.setProgress(seml);
            textView4.setText(seml + " ml");
            textView_tot.setText(totml + " ");
        }else{
            //enter_weight.setHint(getString(R.string.reinput));
        }
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                seekBar.setMax(wiml / 2);
                final_self = progresValue;
                final_tot = totml - seml + progresValue;
                textView4.setText(final_self + " ml");
                textView_tot.setText(final_tot + " ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {       }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_weather_setting_dialog, container);
        init();
        setCancelable(true);
        return v;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        WaterSettings waterSettings;
        int temp = 0;
        switch (view.getId()) {
            case R.id.imageButton1:
                seekBar.setMax((int)(wiml * 0.5));
                seekBar.setProgress(0);
                final_self = 0;
                final_tot = totml - seml;
                textView4.setText(final_self + " ml");
                textView_tot.setText(final_tot + " ");
                break;
            case R.id.imageButton2:
                seekBar.setMax((int)(wiml * 0.5));
                temp = (int)(wiml*0.1);
                seekBar.setProgress(temp);
                final_self = temp;
                final_tot = totml- seml + temp;
                textView4.setText(final_self + " ml");
                textView_tot.setText(final_tot + " ");
                break;
            case R.id.imageButton3:
                seekBar.setMax((int)(wiml * 0.5));
                temp = (int)(wiml*0.3);
                seekBar.setProgress(temp);
                final_self = temp;
                final_tot = totml- seml + temp;
                textView4.setText(final_self + " ml");
                textView_tot.setText(final_tot + " ");
                break;
            case R.id.imageButton4:
                seekBar.setMax((int)(wiml * 0.5));
                temp = (int)(wiml * 0.5);
                seekBar.setProgress(temp);
                final_self = temp;
                final_tot = totml- seml + temp;
                textView4.setText(final_self + " ml");
                textView_tot.setText(final_tot + " ");
                break;
            case R.id.btn_cancel:
                getDialog().dismiss();
                fm = getFragmentManager();
                waterSettings = new WaterSettings();
                waterSettings.show(fm, "Water");
                break;
            case R.id.btn_ok:
                //**--修改為寫入ＳＱＬＩＴＥ的程式段落--**//
                ContentValues values = new ContentValues();
                //**--從資料庫抓資料出來顯示--**//
                //取得今天日期
                getDates = new GetDates();
                String date_now = getDates.getDate();
                String d_now = "date) = '" + date_now + "' GROUP BY (date";
                cursor_single = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
                if(cursor_single.getCount() > 0){
                    cursor_single.moveToFirst();
                    String uid = "_id=" + cursor_single.getString(0);

                    values.put(WaterDBHelper.KEY_WEML, final_self);
                    values.put(WaterDBHelper.KEY_TOTML, final_tot);
                    int result = getActivity().getContentResolver().update(WaterDbProvider.CONTENT_URI_WEIGHT, values, uid, null);
                    //Toast.makeText(getActivity(), getString(R.string.set_ok) , Toast.LENGTH_SHORT).show();
                }else {

                }
                getDialog().dismiss();
                fm = getFragmentManager();
                waterSettings = new WaterSettings();
                waterSettings.show(fm, "Water");
                break;

        }
    }
}
