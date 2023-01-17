package com.example.mvignetteappv2;

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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mvignetteappv2.databinding.FragmentFirstBinding;
import com.example.mvignetteappv2.databinding.FragmentFleetDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FleetDetails extends Fragment {

    private final String user = "Bob";
    private FragmentFleetDetailsBinding binding;
    protected RecyclerView mRecyclerView;
    protected VehicleAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
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

        binding = FragmentFleetDetailsBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();

        mRecyclerView = rootView.findViewById(R.id.idRVVehicles);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        String fleetId = FleetDetailsArgs.fromBundle(getArguments()).getFleetID();
        Log.d("ARG", fleetId);
        String url = "https://mvignette.azurewebsites.net/api/v1/Fleet/" + fleetId;
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
                NavHostFragment.findNavController(FleetDetails.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
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
        ArrayList<VehicleModel> vehicles = new ArrayList<>();
        String fleetName;
        JSONArray vehiclesJSON;
        try {
            fleetName = response.getString("name");
            vehiclesJSON = response.getJSONArray("vehicles");
            //vehiclesJSON = new JSONArray(response.get("vehicles"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        /*
         */
        for (int i = 0; i < vehiclesJSON.length(); i++){
            try {
                JSONObject object = vehiclesJSON.getJSONObject(i);
                vehicles.add(new VehicleModel(object.getString("id"), object.getString("name")));
            } catch (JSONException e){
                e.printStackTrace();
                return;
            }
        }
        Log.d("REST event", "parsed data: " + vehicles.size());
        mAdapter = new VehicleAdapter(getContext(), vehicles, FleetDetails.this);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("REST", response.toString());
        TextView title =  binding.getRoot().findViewById(R.id.idTVTitle);
        title.setText(fleetName);
    };

    private final Response.ErrorListener errorListener = error -> Log.d("REST error", error.getMessage());
}