package tw.com.justdrink.reminder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tw.com.justdrink.R;

public class Thanks extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_thank_you, container, false);
        TextView setting_tv = (TextView)rootView.findViewById(R.id.thanks_you);
        return rootView;
    }
}
