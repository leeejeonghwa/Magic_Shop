package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBasketActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private Button btnBack;
    private TextView checkedCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            setContentView(R.layout.shopping_basket);

            checkedCountTextView = findViewById(R.id.purchase_cnt);
            TextView itemCountTextView = findViewById(R.id.all_cnt);

            btnBack = findViewById(R.id.back_id);

            // RecyclerView 초기화
            RecyclerView recyclerView = findViewById(R.id.basket_item1);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            // ... (기타 코드)

            findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BasketAdapter adapter = (BasketAdapter) recyclerView.getAdapter();
                    int totalItemCount = adapter.getItemCount();
                    int totalPrice = adapter.calculateTotalPrice();

                    // OrderFormActivity로 데이터 전송
                    Intent intent = new Intent(getApplicationContext(), OrderFormActivity.class);
                    intent.putExtra("TOTAL_ITEM_COUNT", totalItemCount);
                    intent.putExtra("TOTAL_PRICE", totalPrice);
                    startActivity(intent);
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            SessionManager sessionManager = new SessionManager(getApplicationContext());

            String userID = sessionManager.getUserId();

            GetBasketItemsRequest getBasketItemsRequest = new GetBasketItemsRequest(
                    userID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                List<BasketItem> basketList = getBasketList(jsonArray);
                                BasketAdapter adapter = new BasketAdapter(basketList, ShoppingBasketActivity.this, checkedCountTextView);
                                recyclerView.setAdapter(adapter);

                                // 초기화 시 전체 아이템 갯수, 결제 선택 상품 수, 전체 상품 가격 설정
                                int totalItemCount = adapter.getItemCount();
                                int initialTotalPrice = adapter.calculateTotalPrice();
                                itemCountTextView.setText("전체 상품 수  " + totalItemCount);
                                checkedCountTextView.setText("결제할 상품 총 " + totalItemCount + " 개" +
                                        "\n상품 금액 " + initialTotalPrice + "원");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("Volley", "JSON Parsing Error: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // 오류 처리
                            Log.e("Volley", "Get Basket Items Request Error: " + error.getMessage());
                        }
                    });

            // Volley 큐에 장바구니 상품 목록 요청 추가
            Volley.newRequestQueue(ShoppingBasketActivity.this).add(getBasketItemsRequest);
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // 기존의 getBasketList 메서드를 수정
    public List<BasketItem> getBasketList(JSONArray jsonArray) {
        List<BasketItem> basketList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // JSON 객체에서 데이터 추출
                String brandName = jsonObject.getString("seller_id");
                String productName = jsonObject.getString("product_name");
                String option = jsonObject.getString("product_option");
                String price = jsonObject.getString("product_price");
                boolean isChecked = true;

                // BasketItem 생성 및 목록에 추가
                BasketItem basketItem = new BasketItem(brandName, productName, option, price, isChecked);
                basketList.add(basketItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return basketList;
    }

    public class BasketItem {
        String brandName;
        String productName;
        String option;
        String price;
        boolean isChecked;

        public BasketItem(String brandName, String productName, String option, String price, boolean isChecked) {
            this.productName = productName;
            this.brandName = brandName;
            this.option = option;
            this.price = price;
            this.isChecked = isChecked;
        }
    }

    public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {
        private List<BasketItem> basketList;
        private Context context;
        private TextView checkedCountTextView;

        BasketAdapter(List<BasketItem> basketList, Context context, TextView checkedCountTextView) {
            this.basketList = basketList;
            this.context = context;
            this.checkedCountTextView = checkedCountTextView;
        }

        @NonNull
        @Override
        public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.basket_item, parent, false);
            return new BasketViewHolder(view, context);
        }

        public int calculateTotalPrice() {
            int totalPrice = 0;
            for (BasketItem item : basketList) {
                if (item.isChecked) {
                    // price가 double 값이라고 가정합니다. 타입을 필요에 따라 조정하세요.
                    totalPrice += Integer.parseInt(item.price);
                }
            }
            return totalPrice;
        }

        public void toggleAllItems(boolean isChecked) {
            for (BasketItem item : basketList) {
                item.isChecked = isChecked;
            }
            notifyDataSetChanged();
            Log.d("BasketAdapter", "toggleAllItems - isChecked: " + isChecked);
            updateCheckedCountText();
        }

        // 체크된 항목 수를 업데이트하는 메서드
        private void updateCheckedCountText() {
            int checkedCount = countCheckedItems();
            int totalPrice = calculateTotalPrice();
            Log.d("BasketAdapter", "updateCheckedCountText - checkedCount: " + checkedCount);

            // 체크된 항목 수와 총 가격을 함께 표시합니다.
            checkedCountTextView.setText("결제할 상품 총 " + checkedCount + " 개" +
                    "\n상품 금액 " + totalPrice + "원");
        }

        @Override
        public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
            BasketItem basketItem = basketList.get(position);
            holder.bind(basketItem);

            // 내부 체크박스 클릭 처리
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basketItem.isChecked = holder.checkBox.isChecked();
                    updateCheckedCountText();
                }
            });
        }

        private int countCheckedItems() {
            int count = 0;
            for (BasketItem item : basketList) {
                if (item.isChecked) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public int getItemCount() {
            return basketList.size();
        }

        public class BasketViewHolder extends RecyclerView.ViewHolder {
            private final TextView productPrice, productOption, productBrand;
            private final TextView productNameTextView;
            private final CheckBox checkBox;

            public BasketViewHolder(View itemView, Context context) {
                super(itemView);
                productNameTextView = itemView.findViewById(R.id.product_name_textview);
                productPrice = itemView.findViewById(R.id.product_price_textview);
                productOption = itemView.findViewById(R.id.product_option_textview);
                productBrand = itemView.findViewById(R.id.brand_name_textview);
                checkBox = itemView.findViewById(R.id.basket_check);

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            basketList.get(position).isChecked = checkBox.isChecked();
                        }
                    }
                });
            }

            void bind(BasketItem basketItem) {
                productNameTextView.setText(basketItem.productName);
                productPrice.setText(basketItem.price);
                productBrand.setText(basketItem.brandName);
                productOption.setText(basketItem.option);
                checkBox.setChecked(basketItem.isChecked);
            }
        }
    }
}
