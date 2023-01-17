package com.example.mvignetteappv2;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mvignetteappv2.FleetDetailsDirections;
import com.example.mvignetteappv2.databinding.FragmentFirstBinding;
import com.example.mvignetteappv2.databinding.FragmentFleetDetailsBinding;
import com.example.mvignetteappv2.databinding.FragmentVehicleDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VehicleDetails extends Fragment {

    private final String user = "Bob";
    private String vehicleId;
    private FragmentVehicleDetailsBinding binding;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();

        vehicleId = VehicleDetailsArgs.fromBundle(getArguments()).getVehicleID();
        Log.d("ARG", vehicleId);
        String url = "https://mvignette.azurewebsites.net/api/v1/Vehicle/" + vehicleId;
        JsonObjectRequest request = new JsonObjectRequest(url, jsonObjectListener, errorListener);
        requestQueue.add(request);

        return rootView;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FleetDetailsDirections.AddVehicle action = FleetDetailsDirections.addVehicle(fleetId);
                NavHostFragment.findNavController(VehicleDetails.this)
                        .navigate(action);
            }
        });
         */

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final Response.Listener<JSONObject> jsonObjectListener = response -> {
        Log.d("REST event", "got response: " + response.length());
        View rootView = binding.getRoot();
        String vehicleName;
        String vehicleRegPlate;
        String vehicleType;
        JSONObject vignette = null;
        try {
            vehicleName = response.getString("name");
            vehicleRegPlate = response.getString("regPlate");
            vehicleType = response.getString("type");
            try {
                vignette = response.getJSONObject("vignette");
            } catch (JSONException ignored) {}
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Log.d("REST event", "parsed data: " + vehicles.size());
        TextView title =  rootView.findViewById(R.id.idTVVehicleTitle);
        title.setText(vehicleName);

        TextView regPlate =  rootView.findViewById(R.id.idTVVehicleRegPlate);
        regPlate.setText(vehicleRegPlate);

        TextView type =  rootView.findViewById(R.id.idTVVehicleType);
        type.setText(vehicleType);

        if (vignette == null) {
            Button buyV = (Button) LayoutInflater.from(getContext()).inflate(R.layout.button_buy, binding.getRoot(), false);

            buyV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("EVENT", "BUY button clicked");
                    /*
                    FleetDetailsDirections.AddVehicle action = FleetDetailsDirections.addVehicle(fleetId);
                    NavHostFragment.findNavController(VehicleDetails.this)
                            .navigate(action);

                     */

                }
            });

            binding.getRoot().addView(buyV);
        } else {
            TextView vignetteInfo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.vignette_info, binding.getRoot(), false);
            try {
                String text = getString(R.string.vignette_valid_until) + vignette.getString("expireDate").substring(0, 10);
                vignetteInfo.setText(text);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            binding.getRoot().addView(vignetteInfo);
        }
    };

    private final Response.ErrorListener errorListener = error -> Log.d("REST error", error.getMessage());
}