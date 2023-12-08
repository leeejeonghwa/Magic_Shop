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

                findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OrderFormActivity.class);
                        startActivity(intent);
                    }
                });

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                RecyclerView recyclerView = findViewById(R.id.basket_item1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                List<BasketItem> basketList = getBasketList();
                BasketAdapter adapter = new BasketAdapter(basketList, this, checkedCountTextView);

                // 초기화 시 전체 아이템 갯수, 결제 선택 상품 수, 전체 상품 가격 설정
                int totalItemCount = adapter.getItemCount();
                int initialTotalPrice = adapter.calculateTotalPrice();
                itemCountTextView.setText("전체 상품 수  " + totalItemCount);
                checkedCountTextView.setText("결제할 상품 총 " + totalItemCount + " 개"+
                        "\n상품 금액 " + initialTotalPrice +"원");


                recyclerView.setAdapter(adapter);
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }


        public List<BasketItem> getBasketList() {
            List<BasketItem> basketList = new ArrayList<>();
            basketList.add(new BasketItem("1", "1", "1", "1", true));
            basketList.add(new BasketItem("1", "1", "1", "1", true));

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
