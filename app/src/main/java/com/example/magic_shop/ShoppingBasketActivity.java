package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

    private List<BasketItem> basketList;
    private BasketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            setContentView(R.layout.shopping_basket);

            checkedCountTextView = findViewById(R.id.purchase_cnt);
            TextView itemCountTextView = findViewById(R.id.all_cnt);

            btnBack = findViewById(R.id.home_id);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            RecyclerView recyclerView = findViewById(R.id.basket_item1);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            String userID = sessionManager.getUserId();

            basketList = new ArrayList<>();
            adapter = new BasketAdapter(basketList, this, checkedCountTextView);
            recyclerView.setAdapter(adapter);

            GetBasketItemsRequest getBasketItemsRequest = new GetBasketItemsRequest(
                    userID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                basketList.clear();
                                basketList.addAll(getBasketList(jsonArray));
                                adapter.notifyDataSetChanged();

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
                            Log.e("Volley", "Get Basket Items Request Error: " + error.getMessage());
                        }
                    });

            Volley.newRequestQueue(ShoppingBasketActivity.this).add(getBasketItemsRequest);

            findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<BasketItem> selectedItems = new ArrayList<>();

                    // 선택된 항목들을 수집
                    for (BasketItem item : basketList) {
                        if (item.isChecked) {
                            selectedItems.add(item);
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), OrderFormActivity.class);

                    int TotalItemCount = adapter.countCheckedItems();
                    int TotalPrice = adapter.calculateTotalPrice();

                    intent.putExtra("TOTAL_ITEM_COUNT", TotalItemCount);
                    intent.putExtra("TOTAL_PRICE", TotalPrice);

                    ArrayList<String> selectedBrands = new ArrayList<>();
                    ArrayList<String> selectedProductNames = new ArrayList<>();
                    ArrayList<String> selectedOptions = new ArrayList<>();
                    ArrayList<String> selectedPrice = new ArrayList<>();
                    ArrayList<Integer> selectedProductID = new ArrayList<>();
                    ArrayList<Integer> selectedShoppingBasketID = new ArrayList<>();
                    ArrayList<String> selectedProductImage = new ArrayList<>();


                    for (BasketItem selectedItem : selectedItems) {
                        selectedBrands.add(selectedItem.brandName);
                        selectedProductNames.add(selectedItem.productName);
                        selectedOptions.add(selectedItem.option);
                        selectedPrice.add(selectedItem.price);
                        selectedProductID.add(selectedItem.productID);
                        selectedShoppingBasketID.add(selectedItem.shoppingBasketID);
                        selectedProductImage.add(selectedItem.productImage);

                    }

                    intent.putStringArrayListExtra("SELECTED_BRANDS", selectedBrands);
                    intent.putStringArrayListExtra("SELECTED_PRODUCT_NAMES", selectedProductNames);
                    intent.putStringArrayListExtra("SELECTED_OPTIONS", selectedOptions);
                    intent.putStringArrayListExtra("SELECTED_PRICE", selectedPrice);
                    intent.putIntegerArrayListExtra("SELECTED_PRODUCT_ID", selectedProductID);
                    intent.putIntegerArrayListExtra("SELECTED_BASKET_ID", selectedShoppingBasketID);
                    intent.putStringArrayListExtra("SELECTED_PRODUCT_IMAGE", selectedProductImage);

                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public List<BasketItem> getBasketList(JSONArray jsonArray) {
        List<BasketItem> basketList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String brandName = jsonObject.getString("seller_id");
                String productName = jsonObject.getString("product_name");
                String option = jsonObject.getString("product_option");
                String price = jsonObject.getString("product_price");
                String productImage = jsonObject.getString("main_image");
                int productID = Integer.parseInt(jsonObject.getString("productID"));
                int shoppingBasketID  = Integer.parseInt(jsonObject.getString("id"));
                boolean isChecked = true;

                BasketItem basketItem = new BasketItem(brandName,productID, productName, option, price, shoppingBasketID, productImage, isChecked);
                basketList.add(basketItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return basketList;
    }

    public class BasketItem {
        int productID;

        int shoppingBasketID;
        String brandName;

        String productImage;
        String productName;
        String option;
        String price;
        boolean isChecked;

        public BasketItem(String brandName, int productID, String productName, String option, String price, int shoppingBasketID, String productImage, boolean isChecked) {
            this.productName = productName;
            this.productID = productID;
            this.brandName = brandName;
            this.option = option;
            this.price = price;
            this.productImage = productImage;
            this.shoppingBasketID = shoppingBasketID;
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
            updateCheckedCountText();
        }

        private void updateCheckedCountText() {
            int checkedCount = countCheckedItems();
            int totalPrice = calculateTotalPrice();

            checkedCountTextView.setText("결제할 상품 총 " + checkedCount + " 개" +
                    "\n상품 금액 " + totalPrice + "원");
        }

        @Override
        public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
            BasketItem basketItem = basketList.get(position);
            holder.bind(basketItem);

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

            private final ImageView imageView;
            private final CheckBox checkBox;

            public BasketViewHolder(View itemView, Context context) {
                super(itemView);
                productNameTextView = itemView.findViewById(R.id.product_name_textview);
                productPrice = itemView.findViewById(R.id.product_price_textview);
                productOption = itemView.findViewById(R.id.product_option_textview);
                productBrand = itemView.findViewById(R.id.brand_name_textview);
                checkBox = itemView.findViewById(R.id.basket_check);
                imageView= itemView.findViewById(R.id.btn_product_id1);

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

                byte[] decodedString = Base64.decode(basketItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);



            }
        }
    }
}
