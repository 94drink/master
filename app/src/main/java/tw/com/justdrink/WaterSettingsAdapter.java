package tw.com.justdrink;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.justdrink.database.WaterDbProvider;
import tw.com.justdrink.dinrkreport.GetDates;


public class WaterSettingsAdapter extends BaseAdapter {
    //--**資料庫相關類別宣告**--//
    Cursor cursor_single;
    WaterDbProvider waterDbProvider;
    GetDates getDates;
    String weight;

    private Context context;
    String[] mWaterSettinsTitles;
    String[] mWaterSettinsVvalue;//={"", "1000ml", "1200ml", "1500ml", "1300ml", "5000ml", ""};

    public WaterSettingsAdapter(Context context){
        this.context = context;
        mWaterSettinsTitles=context.getResources().getStringArray(R.array.water_settings_array);
    }
    @Override
    public int getCount() {
        return mWaterSettinsTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mWaterSettinsTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //**--從資料庫抓資料出來顯示--**//
        //取得今天日期
        getDates = new GetDates();
        String date_now = getDates.getDate();
        //**--顯示當日Weight Table--**//
        String d_now = "date) = '" + date_now + "' GROUP BY (date";

        cursor_single = context.getContentResolver().query(WaterDbProvider.CONTENT_URI_WEIGHT, null, d_now, null, null);
        if(cursor_single.getCount() > 0) {
            cursor_single.moveToFirst();
            weight = cursor_single.getString(1);
            mWaterSettinsVvalue = new String[6];
            mWaterSettinsVvalue[0] = "";
            mWaterSettinsVvalue[1] = cursor_single.getString(3);//體重增量
            mWaterSettinsVvalue[2] = cursor_single.getString(4);//自訂增量
            mWaterSettinsVvalue[3] = cursor_single.getString(5);//天氣增量
            mWaterSettinsVvalue[4] = cursor_single.getString(6);//運動增量
            mWaterSettinsVvalue[5] = cursor_single.getString(7);//總計增量
            //Toast.makeText(context, "weight=" + weight +"\n" + "總計增量=" +  cursor_single.getString(7), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, context.getString(R.string.no_data) , Toast.LENGTH_SHORT).show();
        }
        //**--從資料庫抓資料出來顯示--**//

        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.water_settings_view, parent, false);
        } else {
            row = convertView;
        }
        LinearLayout linearLayout=(LinearLayout)row.findViewById(R.id.settings_layout);

        TextView tvTitle = (TextView) row.findViewById(R.id.tvTitle);
        TextView tvValue = (TextView) row.findViewById(R.id.tvValue);
        ImageView tvImage = (ImageView)row.findViewById(R.id.tvImage);

        if (position==0 || position==5){
            tvTitle.setText(mWaterSettinsTitles[position]);
            tvValue.setText(mWaterSettinsVvalue[position]);
            tvImage.setVisibility(View.INVISIBLE);
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            tvTitle.setTextColor(Color.parseColor("#FFFFFF"));
            tvValue.setTextColor(Color.parseColor("#FFFFFF"));
        }else if (position==1){
            tvTitle.setText(mWaterSettinsTitles[position] + "  " + weight + "Kg");
            tvValue.setText(mWaterSettinsVvalue[position]);
        }

        else {
            tvTitle.setText(mWaterSettinsTitles[position]);
            tvValue.setText(mWaterSettinsVvalue[position]);
        }

        return row;
    }
}
