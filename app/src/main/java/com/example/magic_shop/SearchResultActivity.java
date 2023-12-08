package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends AppCompatActivity {

    public SearchAdapter searchAdapter;
    public Context context;

    public List<SearchItem> getSearchList(String jsonResponse) throws JSONException {
        List<SearchItem> searchList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "deliveryAddresses" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("deliveryAddresses");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String productName = jsonObject.getString(("productName"));
            String productPrice = jsonObject.getString(("productPrice"));


            SearchItem searchItem = new SearchItem(productName, productPrice);

            searchList.add(searchItem);
        }

        return searchList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        getWindow().setWindowAnimations(0);


        Button search_btn = (Button) findViewById(R.id.search_btn);
        Button mypage_btn = (Button) findViewById(R.id.mypage_id);
        Button categorySearch = (Button) findViewById(R.id.category_search_id);
        Button topCat_btn = (Button) findViewById(R.id.btn_top_id);
        Button pantsCat_btn = (Button) findViewById(R.id.btn_pants_id);
        Button skirtCat_btn = (Button) findViewById(R.id.btn_skirt_one_piece_id);
        Button outerCat_btn = (Button) findViewById(R.id.btn_outer_id);
        Button bagCat_btn = (Button) findViewById(R.id.btn_bag_id);
        Button shoesCat_btn = (Button) findViewById(R.id.btn_shoes_id);




        RecyclerView recyclerView = findViewById(R.id.search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_DeliveryAddressManageActivity", "서버 응답: " + response);

                    List<SearchItem> searchList = getSearchList(response);

                    if (searchList.isEmpty()) {
                        // 더미
                        searchList.add(new SearchItem("No Results", ""));
                    }


                    if (searchAdapter == null) {
                        Log.d("SearchResultActivity", "Adapter is null. Creating new adapter.");
//                        searchAdapter = new SearchAdapter(searchList, context);
                        recyclerView.setAdapter(searchAdapter);
                    } else {
                        Log.d("SearchResultActivity", "Adapter exists. Updating data.");
//                        searchAdapter.setSearchList(searchList);
                        searchAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SearchRequest searchRequest = new SearchRequest(SearchResultActivity.this, searchTerm, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SearchResultActivity.this);
        queue.add(searchRequest);





        //검색 버튼
        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //쇼핑카트 버튼
        Button shoppingcart_btn = (Button) findViewById(R.id.shoppingcart_btn);
        shoppingcart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        //마이페이지 버튼
        mypage_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        //카테고리 검색 버튼
        categorySearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

        //상의 카테고리 선택 버튼
        topCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //하의 카테고리 선택 버튼
        pantsCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //스커트 카테고리 선택 버튼
        skirtCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //아우터 카테고리 선택 버튼
        outerCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //가방 카테고리 선택 버튼
        bagCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //신발 카테고리 선택 버튼
        shoesCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryProductListActivity.class);
                startActivity(intent);
            }
        });






    }


    // AddressItem 클래스는 각 배송지 정보를 나타냅니다.
    public class SearchItem {
        String productName;
        String productPrice;


        public SearchItem(String productName, String productPrice) {
            this.productName = productName;
            this.productPrice = productPrice;

        }
    }

    // SearchAdapter 클래스는 RecyclerView 데이터를 바인딩합니다.
    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
        public List<SearchItem> searchList;
        public Context context;


        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.search_list_re, parent, false);
            SearchViewHolder viewHolder = new SearchViewHolder(view, context);
            return new SearchViewHolder(view, context);
        }

        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            SearchItem searchItem = searchList.get(position);


            holder.bind(searchItem);


        }



        @Override
        public int getItemCount() {
            return searchList != null ? searchList.size() : 0;
        }

        public class SearchViewHolder extends RecyclerView.ViewHolder {
            private final TextView productNameText;
            private final TextView productPriceText;
            private final Context context;

            public SearchViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                productNameText = itemView.findViewById(R.id.product_name_text);
                productPriceText = itemView.findViewById(R.id.product_price_text);
            }

            void bind(SearchItem searchItem) {
                // 각 TextView에 해당하는 데이터를 설정
                productNameText.setText(searchItem.productName);
                productPriceText.setText(searchItem.productPrice);


//                if ("1".equals(searchItem.defaultDeliveryAddress)) {
//                    defaultDeliveryAddressTextView.setText("기본배송지");
//                } else {
//                    defaultDeliveryAddressTextView.setText(null);
//                }
            }
        }
    }



}
