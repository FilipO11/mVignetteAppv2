package com.example.mvignetteappv2;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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

import com.example.mvignetteappv2.FleetDetailsDirections;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<VehicleModel> vehicleModelArrayList;
    private final Fragment fragment;

    // Constructor
    public VehicleAdapter(Context context, ArrayList<VehicleModel> vehicleModelArrayList, Fragment fragment) {
        this.context = context;
        this.vehicleModelArrayList = vehicleModelArrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public VehicleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vehicle, parent, false);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String vehicleId = view.getTag().toString();
                Log.i("EVENT", "card clicked: " + vehicleId);
                /*
                 */
                FleetDetailsDirections.ViewVehicleDetails action = FleetDetailsDirections.viewVehicleDetails(vehicleId);
                NavHostFragment.findNavController(fragment)
                        .navigate(action);

            }

        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.ViewHolder holder, int position) {
        // to set each card layout
        VehicleModel model = vehicleModelArrayList.get(position);
        holder.itemView.setTag(model.getVehicle_id());
        if (model.getVignette() != null) {
            holder.card.setCardBackgroundColor(context.getColor(R.color.green));
        }
        holder.vehicleNameTV.setText(model.getVehicle_name());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return vehicleModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView vehicleNameTV;
        private final CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.idCVCard);
            vehicleNameTV = itemView.findViewById(R.id.idTVVehicleName);
        }
    }
}
