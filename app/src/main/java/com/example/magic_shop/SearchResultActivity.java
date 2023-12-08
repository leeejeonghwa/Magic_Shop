package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

        // 제품을 위한 JSON 배열 파싱
        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String productName = jsonObject.getString("productName");
            String productPrice = jsonObject.getString("productPrice");
            String productImage = jsonObject.getString("productImage");
            String brandName = jsonObject.getString("brandName");

            SearchItem searchItem = new SearchItem(productName, productPrice, productImage, brandName);

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





        //검색 버튼
        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
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
                Intent intent = new Intent(getApplicationContext(),Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        //카테고리 검색 버튼
        categorySearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

        //상의 카테고리 선택 버튼
        topCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //하의 카테고리 선택 버튼
        pantsCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //스커트 카테고리 선택 버튼
        skirtCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //아우터 카테고리 선택 버튼
        outerCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //가방 카테고리 선택 버튼
        bagCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //신발 카테고리 선택 버튼
        shoesCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.search_list_re);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("SearchResultActivity", "서버 응답: " + response);

                    List<SearchItem> searchList = getSearchList(response);

                    if (searchAdapter == null) {
                        Log.d("SearchResultActivity", "Adapter is null. Creating new adapter.");
                        searchAdapter = new SearchAdapter(searchList, SearchResultActivity.this);
                        recyclerView.setAdapter(searchAdapter);
                    } else {
                        Log.d("SearchResultActivity", "Adapter exists. Updating data.");
                        searchAdapter.setSearchList(searchList);
                        searchAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("searchTerm")) {
            String searchTerm = intent.getStringExtra("searchTerm");

            // 서버에서 검색어에 해당하는 상품 가져오는 코드
            searchProducts(searchTerm, responseListener);
        }



//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.d("SearchResultActivity", "서버 응답: " + response);
//
//                    List<SearchItem> searchList = getSearchList(response);
//
//                    if (searchAdapter == null) {
//                        Log.d("Mypage_DeliveryAddressManageActivity", "Adapter is null. Creating new adapter.");
//                        searchAdapter = new SearchAdapter(searchList, context);
//                        recyclerView.setAdapter(searchAdapter);
//                    } else {
//                        Log.d("Mypage_DeliveryAddressManageActivity", "Adapter exists. Updating data.");
//                        searchAdapter.setSearchList(searchList);
//                        searchAdapter.notifyDataSetChanged();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        SearchRequest searchRequest = new SearchRequest(SearchResultActivity.this, searchTerm, responseListener);
//        RequestQueue queue = Volley.newRequestQueue(SearchResultActivity.this);
//        queue.add(searchRequest);
    }
    private void searchProducts(String searchTerm, Response.Listener<String> responseListener) {
        String url = "http://210.117.175.207:8976/search.php";

        SearchRequest searchRequest = new SearchRequest(SearchResultActivity.this, searchTerm, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SearchResultActivity.this);
        queue.add(searchRequest);
    }

    public class SearchItem {
        String productName;
        String productPrice;
        String productImage;
        String productBrand;
//        String defaultSearchResult;

        public SearchItem(String productName, String productPrice, String productImage, String productBrand) {
            this.productName = productName;
            this.productPrice = productPrice;
            this.productImage = productImage;
            this.productBrand = productBrand;
        }
    }

    // AddressAdapter 클래스는 RecyclerView 데이터를 바인딩합니다.
    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
        public List<SearchItem> searchList;
        public Context context;


        SearchAdapter(List<SearchItem> searchList, Context context) {
            this.searchList = searchList;  // 수정된 부분
            this.context = context;
        }

        public void setSearchList(List<SearchItem> searchList) {
//            this.searchList = sortSearchProducts(searchList);
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.search_list_re, parent, false);
            SearchViewHolder viewHolder = new SearchViewHolder(view, context);
            return new SearchViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            SearchItem searchItem = searchList.get(position);

            // 기본 배송지인 경우 삭제 버튼을 숨김
//            if ("1".equals(addressItem.defaultDeliveryAddress)) {
//                holder.addressDeleteButton.setVisibility(View.GONE);
//            } else {
//                holder.addressDeleteButton.setVisibility(View.VISIBLE);
//            }
//            holder.bind(addressItem);
//
//            // 아이템의 위치가 변경된 경우 이동을 알림
//            if (position != holder.getAdapterPosition()) {
//                notifyItemMoved(holder.getAdapterPosition(), position);
//            }
        }

        public int getItemCount() {
            return searchList != null ? searchList.size() : 0;
        }

        public class SearchViewHolder extends RecyclerView.ViewHolder {
            private final TextView productName;
            private final TextView productPrice;
            private final TextView productBrand;
            public final Button productImage;
            private final Context context;

            public SearchViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                productName = itemView.findViewById(R.id.product_name_text);
                productPrice = itemView.findViewById(R.id.product_price_text);
                productBrand = itemView.findViewById(R.id.product_brand_text);
                productImage = itemView.findViewById(R.id.btn_product);

                // 상품 사진 클릭
                productImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            SearchItem searchItem = searchList.get(position);
                            Intent intent = new Intent(context, Detailpage_MainActivity.class);
                            intent.putExtra("productName", searchItem.productName);
                            intent.putExtra("productPrice", searchItem.productPrice);
                            intent.putExtra("productBrand", searchItem.productBrand);
                            intent.putExtra("productImage", searchItem.productImage);
                            context.startActivity(intent);
                        }
                    }
                });
            }



//            void bind(SearchItem searchItem) {
//                // 각 TextView에 해당하는 데이터를 설정
//                productName.setText(searchItem.productName);
//                productPrice.setText(searchItem.productPrice);
//                productBrand.setText(searchItem.productBrand);
//                productImage.setText(searchItem.productImage);
//
//                if ("1".equals(searchItem.defaultSearchResult)) {
//                    default.setText("기본배송지");
//                } else {
//                    defaultDeliveryAddressTextView.setText(null);
//                }
//            }
        }


    }

}
