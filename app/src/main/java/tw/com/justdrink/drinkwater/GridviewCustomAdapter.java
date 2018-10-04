package tw.com.justdrink.drinkwater;

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


/**
 * Created by Yuan on 9/17/2018.
 */
public class GridviewCustomAdapter extends SimpleCursorAdapter {

    Cursor dataCursor;
    LayoutInflater mInflater;
    Context context;

    public GridviewCustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        dataCursor = c;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CustomViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_grid_view, null);
            holder = new CustomViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.bottle_time);
            holder.tvSize = (TextView) convertView.findViewById(R.id.bottle_quantity);
            holder.imageView = (ImageView) convertView.findViewById(R.id.bottle);
            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        dataCursor.moveToPosition(position);
        int title = dataCursor.getColumnIndex(WaterDBHelper.KEY_TIME);
        String task_title = dataCursor.getString(title);

        int description_index = dataCursor.getColumnIndex(WaterDBHelper.KEY_POS);
        int priority = dataCursor.getInt(description_index);

        holder.tvTime.setText(task_title);
        holder.tvSize.setText(WaterBottlesData.getData().get(priority).title + " ml");
        holder.imageView.setImageResource(WaterBottlesData.getData().get(priority).imageId);

        return convertView;
    }

    public static class CustomViewHolder {
        public TextView tvSize;
        public TextView tvTime;
        public ImageView imageView;
    }
}

