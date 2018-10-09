package tw.com.justdrink.settings;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import tw.com.justdrink.R;
import tw.com.justdrink.dinrkreport.GetDates;
import tw.com.justdrink.drinkwater.DrinkWater;


public class WaterSettings extends DialogFragment {

    ListView listView;
    Button cancel, save;
    WaterSettingsAdapter adapter;
    GetDates getDates;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.water_settings, container, false);
        listView = (ListView) view.findViewById(R.id.water_settings_list);
        adapter = new WaterSettingsAdapter(getActivity());
        TextView tvValue = (TextView)listView.findViewById(R.id.tvValue);
        listView.setAdapter(adapter);
        cancel = (Button) view.findViewById(R.id.cancel);
        save = (Button) view.findViewById(R.id.save);
        cancel.setOnClickListener(new ButtonHandler());
        save.setOnClickListener(new ButtonHandler());
        setCancelable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //int aa = listView.getId();
                int itemId = (int)adapter.getItemId(i);
                switch (itemId){
                    case 1:
                        showDialog("weight");
                        getDialog().dismiss();
                        break;
                    case 2:
                        showDialog("self");
                        getDialog().dismiss();
                        break;
                    case 3:
                        showDialog("weather");
                        getDialog().dismiss();
                        break;
                    case 4:
                        showDialog("sport");
                        getDialog().dismiss();
                        break;
                }
            }
        });
        return view;
    }

    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    getDialog().dismiss();
                    break;
                case R.id.save:
                    getDates = new GetDates();
                    String date = getDates.getDate();
                    //Toast.makeText(getContext(), "功能尚未完成!!", Toast.LENGTH_SHORT).show();
                    int drink_target = DrinkWater.getWeightByDate(date);
                    DrinkWater.goal.setText("/" + drink_target + "");
                    getDialog().dismiss();
                    break;
            }
        }
    }

    public void showDialog(String tag){
        if (tag.equals("weight")){
            Weight dialog=new Weight();
            Bundle bundle = new Bundle();
            bundle.putInt("Key01", 1);
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), tag);
        }else if (tag.equals("self")){
            Self_Setting_Dialog dialog=new Self_Setting_Dialog();
            dialog.show(getFragmentManager(), tag);
        }else if (tag.equals("weather")){
            Weather_Setting_Dialog dialog=new Weather_Setting_Dialog();
            dialog.show(getFragmentManager(), tag);
        }else if (tag.equals("sport")){
            Sport_Setting_Dialog dialog=new Sport_Setting_Dialog();
            dialog.show(getFragmentManager(), tag);
        }
    }
}

