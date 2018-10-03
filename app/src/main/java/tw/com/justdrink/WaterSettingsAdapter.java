package tw.com.justdrink;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WaterSettingsAdapter extends BaseAdapter {

    private Context context;
    String[] mWaterSettinsTitles;
    String[] mWaterSettinsVvalue={"", "1000ml", "1200ml", "1500ml", "1300ml", "5000ml", ""};

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
            tvTitle.setText(mWaterSettinsTitles[position] + "  70Kg");
            tvValue.setText(mWaterSettinsVvalue[position]);
        }
//        else if (position==6){
//            tvTitle.setText(mWaterSettinsTitles[position]);
//            tvValue.setVisibility(View.INVISIBLE);
//        }
        else {
            tvTitle.setText(mWaterSettinsTitles[position]);
            tvValue.setText(mWaterSettinsVvalue[position]);
        }

        return row;
    }
}
