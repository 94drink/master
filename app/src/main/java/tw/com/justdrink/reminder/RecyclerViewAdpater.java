package tw.com.justdrink.reminder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import tw.com.justdrink.R;


public class RecyclerViewAdpater extends BaseAdapter {

    private Context context;
    String[] mReminderSettingTitles;

    public RecyclerViewAdpater(Context context){
        this.context = context ;
        mReminderSettingTitles = context.getResources().getStringArray(R.array.alarm);

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
    public int getCount() {
        return mReminderSettingTitles.length;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        View view = null;

        return view;
    }
}

