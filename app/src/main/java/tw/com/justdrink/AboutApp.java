package tw.com.justdrink;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import tw.com.justdrink.R;

//Author Yi-Chun,Kuo(2018/10/4)
public class AboutApp extends Fragment {

    private TextView textView1, textView2;
    private ImageView imageView1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about_app, container, false);

        textView1 = (TextView) rootView.findViewById(R.id.about_view1);
        textView2 = (TextView) rootView.findViewById(R.id.about_view2);

        imageView1 = (ImageView) rootView.findViewById(R.id.imageView);

        return rootView;
    }
}
