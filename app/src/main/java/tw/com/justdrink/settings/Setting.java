package tw.com.justdrink.settings;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;

import tw.com.justdrink.R;

public class Setting extends Fragment{
    View v;
    ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container,false);
        lv = (ListView)v.findViewById(R.id.list_item);

        String[] arr = getActivity().getResources().getStringArray(R.array.setting);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,arr);
        lv.setAdapter(adapter);
        /******/
        lv.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }// onItemSelected

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                } //new OntemSelectedListener
        ); //lv.setOnItemSelectedListener

        lv.setOnItemClickListener(
                new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.setTitle("Title");
                        if (position==0)
                            dialog.setMessage("Book1 Selected");
                        else if (position==1)
                            dialog.setMessage("Book2 Selected");
                        else
                            dialog.setMessage("Book3 Selected");
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                        dialog.show();
                    }// onItemSelected
                } //new OnItemClickListener
        ); //lv.setOnItemClickListener
        /***********/
        return v;
    }
}



/*
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class Setting extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        TextView setting_tv = (TextView)rootView.findViewById(R.id.setting_text_view);
        ListView listView = (ListView)rootView.findViewById(R.id.list_item);
        //((MainActivity)getActivity()).getActionBar().setTitle(R.string.nav_settings);
        return rootView;
    }
}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        listView = (ListView)view.findViewById(R.id.list_item);

        /*ArrayAdapter<CharSequence> arrayAdapSetting =
                ArrayAdapter.createFromResource(Context context, R.array.setting,
                        android.R.layout.simple_list_item_1);
        setListAdapter(arrayAdapSetting);
        return view;
        }
    }*/

        /*@Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_setting);
            listView = (ListView) findViewById(R.id.list_item);
            //textView = (TextView) findViewById(R.id.textView);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            settingsAdapter = new SettingsAdapter(this);
            listView.setAdapter(settingsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Toast.makeText(getApplicationContext(), position + " selected", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getApplicationContext(), position + " selected", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(), position + " selected", Toast.LENGTH_LONG).show();
                            break;
                        case 3:
                            Toast.makeText(getApplicationContext(), position + " selected", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        }
    }
}*/
