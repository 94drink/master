package tw.com.justdrink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.justdrink.drinkwater.DrinkWater;


public class Weight extends DialogFragment implements OnClickListener {

    EditText enter_weight;
    TextView unit;
    Button cancel, ok;
    View v;
    String prefs_unit,weight_prefs;
    public static String weight_flie = "weight_unit";
    public static String weight = "weight";
    SharedPreferences sharedDataWeightUnit, sharedDataWeight;


    private void init() {
        enter_weight = (EditText) v.findViewById(R.id.weight);
        unit = (TextView) v.findViewById(R.id.unit);
        ok = (Button) v.findViewById(R.id.ok);
        cancel = (Button) v.findViewById(R.id.cancel);
        enter_weight.setTextColor(getResources().getColor(R.color.black));


        sharedDataWeightUnit = getActivity().getSharedPreferences(weight_flie, 0);
        sharedDataWeight = getActivity().getSharedPreferences(weight, 0);
        prefs_unit = sharedDataWeightUnit.getString("weight_unit", "Kg");
        weight_prefs = sharedDataWeight.getString("weight", 0 + "");
        unit.setText(prefs_unit);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.firstweight, container, false);
        TextView title = (TextView) v.findViewById(R.id.dialog_tite);
        title.setText("請輸入您的體重");
        init();
        enter_weight.setContentDescription(weight_prefs);
        setCancelable(true);
        return v;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.cancel:
                getDialog().dismiss();
                break;

            case R.id.ok:
                String data = enter_weight.getText().toString();
                int todaydate=Integer.parseInt(data);
                SharedPreferences.Editor editor = sharedDataWeight.edit();
                editor.putString("weight", data);
                editor.commit();


                Fragment fragment = new DrinkWater();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                getDialog().dismiss();

                Toast.makeText(getActivity(), "weight: " +  enter_weight.getText().toString(),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
