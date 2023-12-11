package com.example.magic_shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorySelectionManager {

    private static CategorySelectionManager instance;

    private RequestQueue requestQueue;

    private List<ProductItem> productList;

    private Category selectedCategory;

    private int selectedDetailedCategory;

    private Context context;

    public enum Category {
        TOP, OUTER, PANTS, SKIRT_ONE_PIECE, SHOES, BAG
    }

    public int getIntegerCategory() {
        if (selectedCategory == Category.TOP) return 1;
        else if (selectedCategory ==Category.OUTER) return 2;
        else if (selectedCategory == Category.PANTS) return 3;
        else if (selectedCategory == Category.SKIRT_ONE_PIECE) return 4;
        else if (selectedCategory == Category.SHOES) return 5;
        else if (selectedCategory == Category.BAG) return 6;
        return 0;
    }

    private CategorySelectionManager(Context context) {
        selectedCategory = Category.TOP;
        this.context = context;
        this.requestQueue = getRequestQueue();
        this.productList = new ArrayList<>();
    }

    public static synchronized CategorySelectionManager getInstance(Context context) {
        if (instance == null) {
            instance = new CategorySelectionManager(context);
        }
        return instance;
    }

    public List<ProductItem> getProductList() {
        return productList;

    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public interface OnDataReceivedListener {
        void onDataReceived();
    }

    public void fetchDataFromServer(int categoryID, int detailedCategoryID, OnDataReceivedListener listener) {
        String url = "http://210.117.175.207:8976/category_list.php";

        // 요청에 필요한 매개변수 설정
        HashMap<String, String> params = new HashMap<>();
        params.put("categoryID", String.valueOf(categoryID));
        params.put("detailedCategoryID", String.valueOf(detailedCategoryID));
        productList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("카테고리 리스트", "서버 응답");
                        // 여기에서 JSON 문자열을 구문 분석합니다.
                        try {
                            parseJsonData(new JSONArray(response), listener);
                        } catch (JSONException e) {
                            //throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("카테고리 리스트", "fail" );
                        Log.d("카테고리 리스트", "에러: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // 요청에 매개변수 추가
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void parseJsonData(JSONArray jsonArray, OnDataReceivedListener listener) {
        productList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {

                JSONObject productObject = jsonArray.getJSONObject(i);

                String id = productObject.getString("seller_id");

                String product_id = productObject.getString("id");
                String productPrice = productObject.getString("product_price");

                String date = productObject.getString("created_at");
                String productName = productObject.getString("product_name");
                String productSizeS = productObject.getString("size_s");
                String productSizeM = productObject.getString("size_m");
                String productSizeL = productObject.getString("size_l");
                // 문자열이 "N"이 아니면 "/"를 추가하도록 조건문 사용
                String productSize = (!productSizeS.equals("N") ? productSizeS + " / " : "")
                        + (!productSizeM.equals("N") ? productSizeM + " / " : "")
                        + (!productSizeL.equals("N") ? productSizeL : "");

                String productColor1= productObject.getString("color_id1");
                String productColor2 = productObject.getString("color_id2");

                String productColor;

                if (productColor2.equals("N")) {
                    productColor = productColor1;
                }
                else {
                    productColor = productColor1 + " / " + productColor2;
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

                ProductItem item = new ProductItem(product_id, date, productName, productSize, productColor, mainImage, id, productPrice);
                productList.add(item);
                String numStr = Integer.toString(productList.size());
                Log.d("삽입된 상품 수", numStr);



            } catch (JSONException e) {
                e.printStackTrace();

                Log.e("Volley Error", "Server response is not a valid JSON array");

            }

            // Move the listener call here, after all products are added to the list
            listener.onDataReceived();

        }
    }


    public void changeCategory(int selectedCategory) {
        for (Category category : Category.values()) {
            if (selectedCategory == category.ordinal()) {
                this.selectedCategory = category;
                Log.d("category", category.toString());
            }
        }
    }

    public void setSelectedDetailedCategory(int detailedCategory) {
        this.selectedDetailedCategory = detailedCategory;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public int getSelectedDetailedCategory() {
        return selectedDetailedCategory;
    }

}
