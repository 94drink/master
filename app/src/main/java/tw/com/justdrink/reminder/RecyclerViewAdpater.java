package tw.com.justdrink.reminder;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;


public class RecyclerViewAdpater extends RecyclerView.Adapter {

    ArrayList<String> recyclerView; //放在recycler內

    RecyclerViewAdpater(){
        recyclerView =  new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return 0;
    }
}