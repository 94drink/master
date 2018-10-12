package tw.com.justdrink.drinkwater;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.database.WaterDbProvider;


/**
 * Created by Yuan on 9/17/2018.
 */
public class BottleGrid extends Fragment {

    String time, date;
    static int pos;
    private GridView gridView;
    GridviewCustomAdapter gridviewCustomAdapter;
    Uri uri = WaterDbProvider.CONTENT_URI_WATER;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottle_grid, container, false);
        gridView = (GridView) view.findViewById(R.id.scroll_view_content);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = sdf.format(c.getTime());
        date = df.format(c.getTime());

        String[] projection = {WaterDBHelper.KEY_ID, WaterDBHelper.KEY_POS, WaterDBHelper.KEY_ML, WaterDBHelper.KEY_DATE, WaterDBHelper.KEY_TIME};
        String selection = WaterDBHelper.KEY_DATE + " = ?";
        String[] selectionArgs = { "" };
        selectionArgs[0] = date;
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);

        String[] from = { WaterDBHelper.KEY_TIME };
        int[] to = { R.id.bottle_time };
        gridviewCustomAdapter = new GridviewCustomAdapter(getActivity(), R.layout.custom_grid_view, cursor, from, to, 0);
        gridviewCustomAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridviewCustomAdapter);
        gridView.invalidateViews();
        gridView.setSelection(gridView.getAdapter().getCount() - 1);

        final FragmentManager fm = getFragmentManager();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                Toast.makeText(getActivity() , "longclick" ,
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }

}
