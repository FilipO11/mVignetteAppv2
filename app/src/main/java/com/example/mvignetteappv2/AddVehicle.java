package com.example.mvignetteappv2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mvignetteappv2.databinding.FragmentAddVehicleBinding;
import com.example.mvignetteappv2.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddVehicle extends Fragment {

    protected String user = "Bob";
    protected FragmentAddVehicleBinding binding;
    protected EditText name;
    protected EditText regPlate;
    protected String type;
    protected String fleetId;
    protected RequestQueue requestQueue;
    String url = "https://mvignette.azurewebsites.net/api/v1/Vehicle";

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

        binding = FragmentAddVehicleBinding.inflate(inflater, container, false);
        name = binding.getRoot().findViewById(R.id.editTextVehicleName);
        regPlate = binding.getRoot().findViewById(R.id.editTextRegPlate);
        fleetId = AddVehicleArgs.fromBundle(getArguments()).getFleetID();
        Log.d("ARG", fleetId);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getAdapter().getItem(position).toString().substring(0, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVehicle(view);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addVehicle(View view){
        if (TextUtils.isEmpty(name.getText())){
            Snackbar.make(view, "Please enter the name of your new vehicle", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if (TextUtils.isEmpty(regPlate.getText())){
            Snackbar.make(view, "Please enter the registration plate of your new vehicle", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name.getText());
            jsonBody.put("regPlate", regPlate.getText());
            jsonBody.put("type", type);
            jsonBody.put("ownerName", user);
            jsonBody.put("fleetId", fleetId);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };

            requestQueue.add(stringRequest);

            Log.d("ARG", "fleetId: " + fleetId);
            AddVehicleDirections.ActionBackToFleet action = AddVehicleDirections.actionBackToFleet(fleetId);
            NavHostFragment.findNavController(AddVehicle.this)
                    .navigate(action);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}