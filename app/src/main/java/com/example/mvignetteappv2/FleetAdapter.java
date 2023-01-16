package com.example.mvignetteappv2;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class FleetAdapter extends RecyclerView.Adapter<FleetAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<FleetModel> fleetModelArrayList;
    private final Fragment fragment;

    // Constructor
    public FleetAdapter(Context context, ArrayList<FleetModel> fleetModelArrayList, Fragment fragment) {
        this.context = context;
        this.fleetModelArrayList = fleetModelArrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public FleetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fleet, parent, false);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String fleetId = view.getTag().toString();
                Log.i("EVENT", "card clicked: " + fleetId);
                MyFleetsDirections.ViewFleetDetails action = MyFleetsDirections.viewFleetDetails(fleetId);
                NavHostFragment.findNavController(fragment)
                        .navigate(action);
                /*

                 */
            }

        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FleetAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        FleetModel model = fleetModelArrayList.get(position);
        holder.itemView.setTag(model.getFleet_id());
        holder.fleetNameTV.setText(model.getFleet_name());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return fleetModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fleetNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fleetNameTV = itemView.findViewById(R.id.idTVFleetName);
        }
    }
}
