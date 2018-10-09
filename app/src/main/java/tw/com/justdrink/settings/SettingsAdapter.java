package tw.com.justdrink.settings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.justdrink.R;


public class SettingsAdapter extends BaseAdapter {

    private Context context;
    String[] mSettinsTitles;
    int[] images = {R.mipmap.ic_drawer_notification, R.mipmap.ic_drink_water, R.mipmap.ic_weight_goal, R.mipmap.ic_unit_format};

    public SettingsAdapter(Context context) {
        this.context = context;
        mSettinsTitles = context.getResources().getStringArray(R.array.setting);
    }

    @Override
    public int getCount() {
        return mSettinsTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mSettinsTitles[position];
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
            row = inflater.inflate(R.layout.fragment_settingadapter, parent, false);
        } else {
            row = convertView;
        }
        TextView titletv = (TextView) row.findViewById(R.id.label);
        titletv.setTextColor(Color.BLACK);
        ImageView titleiv = (ImageView) row.findViewById(R.id.tvimage);
        titletv.setText(mSettinsTitles[position]);
        titleiv.setImageResource(images[position]);
        return row;
    }
}
