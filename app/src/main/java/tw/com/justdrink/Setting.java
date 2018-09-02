package tw.com.justdrink;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Setting extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        TextView setting_tv = (TextView)rootView.findViewById(R.id.setting_text_view);
        //((MainActivity)getActivity()).getActionBar().setTitle(R.string.nav_settings);
        return rootView;
    }
}
