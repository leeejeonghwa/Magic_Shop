package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    public List<SearchItem> searchList;
    public SearchAdapter searchAdapter;
    public Context context;

    public List<SearchItem> getSearchList(String jsonResponse) throws JSONException {
        List<SearchItem> searchList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "deliveryAddresses" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("searchTerm");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String productName = jsonObject.getString(("product_name"));
            String productPrice = jsonObject.getString(("product_price"));
            String brandName = jsonObject.getString("seller_id");
            String base64MainImage = jsonObject.getString("main_image");

            SearchItem searchItem = new SearchItem(productName, productPrice, brandName, base64MainImage);

            searchList.add(searchItem);
        }

        return searchList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        getWindow().setWindowAnimations(0);


        Button search_btn = (Button) findViewById(R.id.search_btn);
        Button home_btn = (Button) findViewById(R.id.go_home_id);
        Button mypage_btn = (Button) findViewById(R.id.mypage_id);
        Button categorySearch = (Button) findViewById(R.id.category_search_id);
        Button topCat_btn = (Button) findViewById(R.id.btn_top_id);
        Button pantsCat_btn = (Button) findViewById(R.id.btn_pants_id);
        Button skirtCat_btn = (Button) findViewById(R.id.btn_skirt_one_piece_id);
        Button outerCat_btn = (Button) findViewById(R.id.btn_outer_id);
        Button bagCat_btn = (Button) findViewById(R.id.btn_bag_id);
        Button shoesCat_btn = (Button) findViewById(R.id.btn_shoes_id);


        Intent intent = getIntent();

        String searchTerm = intent.getStringExtra("searchTerm");

        Log.d("searTerm은 이거에요", searchTerm);


        //검색 버튼
        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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

        RecyclerView recyclerView = findViewById(R.id.search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("SearchResultActivity", "서버 응답: " + response);

                    List<SearchItem> searchList = getSearchList(response);

                    if (searchAdapter == null) {
                        Log.d("SearchResultActivity", "Adapter is null. Creating new adapter.");
                        searchAdapter = new SearchAdapter(searchList, context);
                        recyclerView.setAdapter(searchAdapter);
                    } else {
                        Log.d("SearchResultActivity", "Adapter exists. Updating data.");
                        searchAdapter.setSearchList(searchList);
                        searchAdapter.notifyDataSetChanged();
                    }

                    // 여기에 검색 결과가 없을 때 메시지를 표시하는 코드 추가
                    if (searchList.isEmpty()) {
                        findViewById(R.id.no_items_layout).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.no_items_layout).setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SearchRequest searchRequest = new SearchRequest(SearchResultActivity.this, searchTerm, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SearchResultActivity.this);
        queue.add(searchRequest);

    }

    public class SearchItem {
        String productName;
        String productPrice;
        String productBrand;
        String productImage;

        public SearchItem(String productName, String productPrice, String productBrand, String productImage) {
            this.productName = productName;
            this.productPrice = productPrice;
            this.productBrand = productBrand;
            this.productImage = productImage;
        }
    }
    // AddressAdapter 클래스는 RecyclerView 데이터를 바인딩합니다.
    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
        public List<SearchItem> searchList;
        public Context context;

        SearchAdapter(List<SearchItem> searchList, Context context) {
            this.searchList = searchList;
            this.context = context;
        }

        public void setSearchList(List<SearchItem> searchList) {
            this.searchList = searchList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.search_list_re_2, parent, false);
            SearchAdapter.SearchViewHolder viewHolder = new SearchAdapter.SearchViewHolder(view, context);
            return new SearchAdapter.SearchViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
            SearchItem searchItem = searchList.get(position);
            holder.bind(searchItem);

            // 아이템 뷰에 클릭 리스너 설정
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 여기에서 아이템 클릭 처리, 예를 들어 새로운 활동 시작
                    startActivityForProduct(searchItem.productName, searchItem.productBrand, searchItem.productPrice);
                }
                private void startActivityForProduct(String productName, String productBrand, String productPrice) {
                    Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
                    // 상품 정보 및 이미지를 Intent에 추가하여 Detail 페이지로 전달
                    intent.putExtra("product_name", productName);
                    intent.putExtra("seller_id", productBrand);
                    intent.putExtra("product_price", productPrice);

                    startActivity(intent);
                }


            });

        }



        @Override
        public int getItemCount() {
            return searchList != null ? searchList.size() : 0;
        }
        public class SearchViewHolder extends RecyclerView.ViewHolder {
            private final TextView productNameTextView;
            private final TextView productBrandTextView;
            private final TextView productPriceTextView;
            private final ImageView productImageView;

            private final Context context;

            public SearchViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                productNameTextView = itemView.findViewById(R.id.product_name_text);
                productBrandTextView = itemView.findViewById(R.id.product_brand_text);
                productPriceTextView = itemView.findViewById(R.id.product_price_text);
                productImageView = itemView.findViewById(R.id.product_image);
            }
            void bind(SearchItem searchItem) {
                // 각 TextView에 해당하는 데이터를 설정
                productNameTextView.setText(searchItem.productName);
                productBrandTextView.setText(searchItem.productBrand);
                productPriceTextView.setText(searchItem.productPrice);
                setBase64Image(productImageView, searchItem.productImage);
            }
        }
    }

    private void setBase64Image(ImageView imageView, String base64Image) {
        // Base64로 인코딩된 이미지를 디코딩하여 버튼의 이미지뷰에 설정
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setBackground(new BitmapDrawable(getResources(), decodedByte));
    }


//
}
