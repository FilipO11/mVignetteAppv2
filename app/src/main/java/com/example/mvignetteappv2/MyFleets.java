package com.example.mvignetteappv2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mvignetteappv2.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MyFleets extends Fragment {

    private String user = "Bob";
    private FragmentFirstBinding binding;
    protected RecyclerView mRecyclerView;
    protected FleetAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    //String url = String.format("https://mvignette.azurewebsites.net/api/v1/Fleet?user=%s", user);
    private String url = "https://mvignette.azurewebsites.net/api/v1/Fleet?user=" + user;

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

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();
        rootView.setTag("My fleets");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.idRVFleets);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
        requestQueue.add(request);

        return rootView;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MyFleets.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final Response.Listener<JSONArray> jsonArrayListener = response -> {
        Log.d("REST event", "got response: " + response.length());
        ArrayList<FleetModel> fleets = new ArrayList<>();
        for (int i = 0; i < response.length(); i++){
            try {
                JSONObject object = response.getJSONObject(i);
                fleets.add(new FleetModel(object.getString("id"), object.getString("name")));
            } catch (JSONException e){
                e.printStackTrace();
                return;

            }
        }
        Log.d("REST event", "parsed data: " + fleets.size());
        mAdapter = new FleetAdapter(getContext(), fleets, MyFleets.this);
        mRecyclerView.setAdapter(mAdapter);
    };

    private final Response.ErrorListener errorListener = error -> Log.d("REST error", error.getMessage());

}