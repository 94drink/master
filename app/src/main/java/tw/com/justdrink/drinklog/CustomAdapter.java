package tw.com.justdrink.drinklog;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterBottlesData;
import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.drinkwater.DrinkWater;


/**
 * Created by Yuan on 9/17/2018.
 */
class CustomAdapter extends SimpleCursorAdapter {

    Cursor dataCursor;
    LayoutInflater mInflater;
    Context context;
    int sec_total;

    public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        dataCursor = c;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drink_log_header, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.one_day_log_bottle_time);
            holder.tvSize = (TextView) convertView.findViewById(R.id.one_day_log_bottle_size);
            holder.imageView = (ImageView) convertView.findViewById(R.id.one_day_log_bottle);
            holder.sec_total = (TextView) convertView.findViewById(R.id.one_day_log_total);
            holder.sec_hdr = (TextView) convertView.findViewById(R.id.one_day_log_bottle_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        dataCursor.moveToPosition(position);
        int time = dataCursor.getColumnIndex(WaterDBHelper.KEY_TIME);
        String task_time = dataCursor.getString(time);

        int title_date = dataCursor.getColumnIndex(WaterDBHelper.KEY_DATE);
        String task_day = dataCursor.getString(title_date);

        int description_index = dataCursor.getColumnIndex(WaterDBHelper.KEY_POS);
        int priority = dataCursor.getInt(description_index);

        sec_total = DrinkWater.getDrinkedByDate(task_day);

        holder.sec_hdr.setText(task_day);
        holder.tvSize.setText(WaterBottlesData.getData().get(priority).title + "ml");
        holder.imageView.setImageResource(WaterBottlesData.getData().get(priority).imageId);
        holder.tvTime.setText(task_time);
        holder.sec_total.setText(String.valueOf(sec_total) + "ml"); //總喝水量

        String prevDate = null;
        if (dataCursor.getPosition() > 0 && dataCursor.moveToPrevious()) {
            prevDate = dataCursor.getString(title_date);
            dataCursor.moveToNext();
        }

        if (task_day.equals(prevDate)) {
            holder.sec_hdr.setVisibility(View.GONE);      //日期(隱藏)
            holder.sec_total.setVisibility(View.GONE);    //總喝水量(隱藏)
        } else {
            holder.sec_hdr.setVisibility(View.VISIBLE);   //日期(顯示)
            holder.sec_total.setVisibility(View.VISIBLE); //總喝水量(顯示)
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvSize;
        public TextView tvTime;
        public TextView sec_hdr;
        public TextView sec_total;
        public ImageView imageView;
    }

}
