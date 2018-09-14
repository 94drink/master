package tw.com.justdrink;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

public class Reminders extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_reminders, container, false);
        TextView setting_tv = (TextView)rootView.findViewById(R.id.reminders_text_view);
        //((MainActivity)getActivity()).getActionBar().setTitle(R.string.nav_settings);
        return rootView;
    }
}
