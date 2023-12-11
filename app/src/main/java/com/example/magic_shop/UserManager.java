package com.example.magic_shop;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;

    private RequestQueue requestQueue;
    private List<User> userList;
    private Context context;

    private UserManager(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
        this.userList = new ArrayList<>();
    }

    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
        }
        return instance;
    }

    public List<User> getProductList() {
        return userList;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void fetchSellerDataFromServer(OnDataReceivedListener listener) {

        String url = "http://210.117.175.207:8976/manager_seller_list.php";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("seller 리스트", "서버 응답");
                        parseJsonData(response, listener);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("seller 리스트", "fail" );
                        //listener.onDataReceived();

                    }
                });

        requestQueue.add(jsonArrayRequest);
    }





    public interface OnDataReceivedListener {
        void onDataReceived();
    }

    private void parseJsonData(JSONArray jsonArray, UserManager.OnDataReceivedListener listener) {

        userList.clear();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject productObject = jsonArray.getJSONObject(i);


                String userType = productObject.getString("userType");

                if (userType.equals("B")) {
                    String userName = productObject.getString("userName");

                    String userID = productObject.getString("userID");

                    User user = new User(userName, userID, userType);

                    userList.add(user);
                    String numStr = Integer.toString(userList.size());
                    Log.d("셀러 수", numStr);
                }




            } catch (JSONException e) {
                e.printStackTrace();

                Log.e("Volley Error", "Server response is not a valid JSON array");

            }

            // Move the listener call here, after all products are added to the list
            listener.onDataReceived();

        }
    }



}
