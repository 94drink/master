package tw.com.justdrink;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import tw.com.justdrink.R;

//s1005
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
//1005e

//Author Yi-Chun,Kuo(2018/10/4)
public class AboutApp extends Fragment {

    private TextView textView1, textView2;
    private ImageView imageView1;

    //s1005
    private Vibrator vib;
    private SensorManager sensor_manager;
    private MySensorEventListener listener;
    //1005e

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about_app, container, false);

        textView1 = (TextView) rootView.findViewById(R.id.about_view1);
        textView2 = (TextView) rootView.findViewById(R.id.about_view2);

        //s1005
        //sensor_manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //vib = (Vibrator) getActivity().getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        sensor_manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        vib = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        //1005e
        imageView1 = (ImageView) rootView.findViewById(R.id.about_view3);

        //s1005
        // 接近傳感器
        Sensor sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        listener = new MySensorEventListener();
        sensor_manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //1005e
        return rootView;
    }

    //s1005
    // 感應器事件監聽器
    private class MySensorEventListener implements SensorEventListener {

        // 感應器有了改變的回呼函示
        @Override
        public void onSensorChanged(SensorEvent event) {
            final float proxyValue = event.values[0];


                    if (proxyValue < 1) {
                        //vib.vibrate(3000);
                        imageView1.setImageResource(R.drawable.group_pic2);
                    } else {
                        //vib.vibrate(1000);
                        imageView1.setImageResource(R.drawable.group_pic);
                    }
                }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }


    };
}
