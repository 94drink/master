package tw.com.justdrink;

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
import android.widget.Toast;


public class WaterSettings extends DialogFragment {

    ListView listView;
    Button cancel, save;
    WaterSettingsAdapter adapter;

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
                        showDialog("other");
                        break;
                    case 3:
                        showDialog("weather");
                        break;
                    case 4:
                        showDialog("sport");
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
                    Toast.makeText(getContext(), "功能尚未完成!!", Toast.LENGTH_SHORT).show();
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
        }else if (tag.equals("other")){
            Other_Setting_Dialog dialog=new Other_Setting_Dialog();
            dialog.show(getFragmentManager(), tag);
        }else if (tag.equals("weather")){
            Other_Setting_Dialog dialog=new Other_Setting_Dialog();
            dialog.show(getFragmentManager(), tag);
        }else if (tag.equals("sport")){
            Other_Setting_Dialog dialog=new Other_Setting_Dialog();
            dialog.show(getFragmentManager(), tag);
        }
    }

}

