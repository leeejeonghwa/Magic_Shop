package com.example.magic_shop;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductRegisterRequestManager {

    private static ProductRegisterRequestManager instance;
    private RequestQueue requestQueue;
    private List<ProductItem> productRegisterRequestList;
    private Context context;
    private String userId;

    private boolean isManager;


    private ProductRegisterRequestManager(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
        this.productRegisterRequestList = new ArrayList<>();
    }

    public static synchronized ProductRegisterRequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductRegisterRequestManager(context);
        }
        return instance;
    }

    public List<ProductItem> getProductRegisterRequestList() {
        return productRegisterRequestList;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void fetchDataFromServer(OnDataReceivedListener listener) {
        String url = "http://210.117.175.207:8976/product_resigter_request_accept.php";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("상품등록요청 리스트", "서버 응답");
                        parseJsonData(response, listener);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("상품등록요청 리스트", "fail" );
                        //listener.onDataReceived();

                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    public void checkUserId(String userID) {
        this.userId = userID;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public interface OnDataReceivedListener {
        void onDataReceived();
    }



    private void parseJsonData(JSONArray jsonArray, OnDataReceivedListener listener) {
        productRegisterRequestList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject productObject = jsonArray.getJSONObject(i);

                String id = productObject.getString("seller_id");

                String product_id = productObject.getString("id");

                if (!isManager && id.equals(userId)) {
                    Log.d("로그인한 유저의 상품", id);
                    String date = productObject.getString("created_at");
                    String productName = productObject.getString("product_name");
                    String productSizeS = productObject.getString("size_s");
                    if (productSizeS.equals("Y")) {
                        productSizeS = "S";
                    }
                    else {
                        productSizeS = "";

                    }

                    String productSizeM = productObject.getString("size_m");
                    if (productSizeM.equals("Y")) {
                        productSizeM = "M";
                    }
                    else {
                        productSizeM = "";

                    }
                    String productSizeL = productObject.getString("size_l");
                    if (productSizeL.equals("Y")) {
                        productSizeL = "L";
                    }
                    else {
                        productSizeL = "";

                    }
                    // 문자열이 "N"이 아니면 "/"를 추가하도록 조건문 사용
                    String productSize = productSizeS + " " + productSizeM + " " + productSizeL;
                    String productColor1 = productObject.getString("color_id1");
                    String productColor2 = productObject.getString("color_id2");

                    String productColor;

                    if (productColor2.equals("N")) {
                        productColor = productColor1;
                    } else {
                        productColor = productColor1 + " " + productColor2;
                    }

                    // main_image를 base64로 인코딩한 값
                    String mainImageBase64 = productObject.getString("main_image");

                    // base64 디코딩
                    // base64 디코딩
                    byte[] imageBytes = Base64.decode(mainImageBase64, Base64.DEFAULT);
                    Bitmap mainImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    imageBytes = null;

                    // 여기서 mainImageBytes를 사용하여 이미지 처리를 수행할 수 있음

                    Log.d("로그인한 유저의 상품", "id: " + id + ", date: " + date + ", name: " + productName + ", size: " + productSize + ", color: " + productColor);

                    ProductItem item = new ProductItem(product_id, date, productName, productSize, productColor, mainImage, id);
                    productRegisterRequestList.add(item);
                    String numStr = Integer.toString(productRegisterRequestList.size());
                    Log.d("삽입된 상품 수", numStr);


                } else if (isManager) {
                    Log.d("관리자의 상품리스트", product_id);
                    String date = productObject.getString("created_at");
                    String productName = productObject.getString("product_name");
                    String productSizeS = productObject.getString("size_s");
                    if (productSizeS.equals("Y")) {
                        productSizeS = "S";
                    }
                    else {
                        productSizeS = "";

                    }

                    String productSizeM = productObject.getString("size_m");
                    if (productSizeM.equals("Y")) {
                        productSizeM = "M";
                    }
                    else {
                        productSizeM = "";

                    }
                    String productSizeL = productObject.getString("size_l");
                    if (productSizeL.equals("Y")) {
                        productSizeL = "L";
                    }
                    else {
                        productSizeL = "";

                    }
                    // 문자열이 "N"이 아니면 "/"를 추가하도록 조건문 사용
                    String productSize = productSizeS + " " + productSizeM + " " + productSizeL;
                    String productColor1 = productObject.getString("color_id1");
                    String productColor2 = productObject.getString("color_id2");

                    String productColor;

                    if (productColor2.equals("N")) {
                        productColor = productColor1;
                    } else {
                        productColor = productColor1 + " " + productColor2;
                    }

                    // main_image를 base64로 인코딩한 값
                    String mainImageBase64 = productObject.getString("main_image");

                    // base64 디코딩
                    // base64 디코딩
                    byte[] imageBytes = Base64.decode(mainImageBase64, Base64.DEFAULT);
                    Bitmap mainImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    imageBytes = null;

                    // 여기서 mainImageBytes를 사용하여 이미지 처리를 수행할 수 있음

                    Log.d("관리자 상품", "id: " + id + ", date: " + date + ", name: " + productName + ", size: " + productSize + ", color: " + productColor);

                    ProductItem item = new ProductItem(product_id, date, productName, productSize, productColor, mainImage, id);
                    productRegisterRequestList.add(item);
                    String numStr = Integer.toString(productRegisterRequestList.size());
                    Log.d("삽입된 상품 수", numStr);
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
