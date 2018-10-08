package tw.com.justdrink.reminder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.justdrink.R;


public class ReminderAdpater extends BaseAdapter {

    private Context context;
    String[] mReminderSettingTitles;
    int []images = {R.mipmap.ic_action_notification};

    public ReminderAdpater(Context context){
        this.context = context ;
        mReminderSettingTitles = context.getResources().getStringArray(R.array.alarm);

    }

    @Override
    public int getCount() {
        return mReminderSettingTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mReminderSettingTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup vg) {
        View row = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.fragment_reminderadapter,vg,false);
        }else{
            row = view ;
        }
        TextView tl = (TextView)row.findViewById(R.id.label2);
        tl.setTextColor(Color.BLACK);
        ImageView IV = (ImageView)row.findViewById(R.id.imageView2);

        tl.setText(mReminderSettingTitles[i]);
        IV.setImageResource(images[i]);

        return row;
    }
}

