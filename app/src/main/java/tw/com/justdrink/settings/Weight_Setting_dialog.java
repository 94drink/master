package tw.com.justdrink.settings;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.com.justdrink.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Weight_Setting_dialog extends DialogFragment {


    public Weight_Setting_dialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_weight__setting_dialog, container, false);
        View view=inflater.inflate(R.layout.fragment_weight__setting_dialog, container);
        return view;
    }

}
