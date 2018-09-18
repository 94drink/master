package tw.com.justdrink;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class Reminders extends Fragment {

    String[] strings = {"1", "2", "3", "4", "5", "6", "7"};

    public Reminders(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //http://www.codedata.com.tw/mobile/android-tutorial-the-5th-class-1-broadcastreceiver-alarmmanager/ 09/18
        View rootView = inflater.inflate(R.layout.fragment_reminders, container, false);
        TextView setting_tv = (TextView)rootView.findViewById(R.id.my_recycler_view);
        //((MainActivity)getActivity()).getActionBar().setTitle(R.string.nav_settings);

        RecyclerView rc = new RecyclerView(getContext());
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        rc.setAdapter(new SimpleRVAdapter(strings));

        return rootView;
    }

    public class SimpleRVAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
        private String[] dataSource;
        public SimpleRVAdapter(String[] dataArgs){
            dataSource = dataArgs;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new TextView(parent.getContext());
            SimpleViewHolder viewHolder = new SimpleViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            holder.textView.setText(dataSource[position]);
        }

        @Override
        public int getItemCount() {
            return dataSource.length;
        }
    }

    /**
     * A Simple ViewHolder for the RecyclerView
     */
    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

}
