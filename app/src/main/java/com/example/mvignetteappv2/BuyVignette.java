package com.example.mvignetteappv2;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
import com.example.mvignetteappv2.databinding.FragmentBuyVignetteBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class BuyVignette extends Fragment {

    private final String user = "Bob";
    private String vehicleId;
    private FragmentBuyVignetteBinding binding;
    private LocalDate expireDate;
    private TextView buyInfo;
    private RequestQueue requestQueue;
    String url = "https://mvignette.azurewebsites.net/api/v1/Vignette";

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

        binding = FragmentBuyVignetteBinding.inflate(inflater, container, false);
        buyInfo = binding.getRoot().findViewById(R.id.idTVBuyInfo);
        vehicleId = BuyVignetteArgs.fromBundle(getArguments()).getVehicleID();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.spinnerVignetteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = parent.getAdapter().getItem(position).toString();
                expireDate = LocalDate.now();
                switch (type){
                    case "Weekly":
                        expireDate = expireDate.plusWeeks(1);
                        buyInfo.setText(getString(R.string.buy_info, expireDate, "10EUR"));
                        break;
                    case "Monthly":
                        expireDate = expireDate.plusMonths(1);
                        buyInfo.setText(getString(R.string.buy_info, expireDate, "30EUR"));
                        break;
                    case "Yearly":
                        expireDate = expireDate.plusYears(1);
                        buyInfo.setText(getString(R.string.buy_info, expireDate, "110EUR"));
                        break;
                    default:

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyVignette(view);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void buyVignette(View view){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("expireDate", expireDate);
            jsonBody.put("ownerName", user);
            jsonBody.put("vehicleId", vehicleId);

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

            BuyVignetteDirections.ActionBackToVehicle action = BuyVignetteDirections.actionBackToVehicle(vehicleId);
            NavHostFragment.findNavController(BuyVignette.this)
                    .navigate(action);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}