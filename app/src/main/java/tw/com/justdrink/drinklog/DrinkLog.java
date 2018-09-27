package tw.com.justdrink.drinklog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tw.com.justdrink.R;
import tw.com.justdrink.database.WaterDBHelper;
import tw.com.justdrink.database.WaterDbProvider;


public class DrinkLog extends Fragment {

    ListView recyclerView;
    CustomAdapter customAdapter;
    Cursor drink_log_cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drinklog, container, false);
        String sortOrder = WaterDBHelper.KEY_DATE + " DESC, " + WaterDBHelper.KEY_ID;
        drink_log_cursor = getActivity().getContentResolver().query(WaterDbProvider.CONTENT_URI_WATER, null, null, null, sortOrder);
        String[] from = {WaterDBHelper.KEY_POS, WaterDBHelper.KEY_TIME, WaterDBHelper.KEY_DATE};
        int[] to = {R.id.one_day_log_bottle_size, R.id.one_day_log_bottle_time, R.id.one_day_log_bottle_date};
        customAdapter = new CustomAdapter(getActivity(), R.layout.one_day_drink_log, drink_log_cursor, from, to, 0);

        recyclerView = (ListView) view.findViewById(R.id.drink_log_view);
        recyclerView.setAdapter(customAdapter);
        return view;
    }
}
