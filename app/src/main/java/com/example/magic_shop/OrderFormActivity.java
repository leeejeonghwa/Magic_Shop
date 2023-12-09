    package com.example.magic_shop;

    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CheckBox;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
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

    public class OrderFormActivity extends AppCompatActivity {

        private TextView TextViewAddressName, TextViewRecipient, TextViewCallNumber, TextViewAddress,
                TextViewAddressDetail, TextViewDeliveryRequest;

        private CheckBox checkBoxCreditCard, checkBoxAccount, checkBoxBankDeposit;
        private String selectedPaymentMethod; // 사용자가 선택한 결제 수단을 저장하는 변수
        private Response.ErrorListener errorListener;
        private TextView totalPriceTextView;
        private int totalAmount; // totalAmount 변수를 선언

        private PurchaseAdapter adapter; // 어댑터 변수를 여기에 선언

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.order_form);
            totalPriceTextView = findViewById(R.id.purchase_all_cnt);

            SessionManager sessionManager = new SessionManager(getApplicationContext());
            String userID = sessionManager.getUserId();

            findViewById(R.id.back_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // 2. 체크박스 초기화
            checkBoxCreditCard = findViewById(R.id.check_card);
            checkBoxAccount = findViewById(R.id.check_account);
            checkBoxBankDeposit = findViewById(R.id.check_deposit);

            // 3. 체크박스 상태 변경 시 이벤트 처리
            checkBoxCreditCard.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckboxChecked(isChecked, "C"));
            checkBoxAccount.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckboxChecked(isChecked, "A"));
            checkBoxBankDeposit.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckboxChecked(isChecked, "B"));

            TextViewAddressName = findViewById(R.id.shipping_address_name);
            TextViewRecipient = findViewById(R.id.shipping_recipient);
            TextViewCallNumber = findViewById(R.id.call_number);
            TextViewAddress = findViewById(R.id.shipping_address);
            TextViewAddressDetail = findViewById(R.id.shipping_address_detail);
            TextViewDeliveryRequest = findViewById(R.id.delivery_request);

            getDefaultDeliveryAddress(userID);

            RecyclerView recyclerView = findViewById(R.id.purchase_recycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new PurchaseAdapter(new ArrayList<>(), this, totalPriceTextView);
            recyclerView.setAdapter(adapter);

            Intent intent = getIntent();

            ArrayList<String> selectedBrands = intent.getStringArrayListExtra("SELECTED_BRANDS");
            ArrayList<String> selectedProductNames = intent.getStringArrayListExtra("SELECTED_PRODUCT_NAMES");
            ArrayList<String> selectedOptions = intent.getStringArrayListExtra("SELECTED_OPTIONS");
            ArrayList<String> selectedPrice = intent.getStringArrayListExtra("SELECTED_PRICE");
            ArrayList<Integer> selectedProductID = intent.getIntegerArrayListExtra("SELECTED_PRODUCT_ID");
            ArrayList<Integer> selectedShoppingBasketID = intent.getIntegerArrayListExtra("SELECTED_BASKET_ID");




            if (intent != null) {
                int totalItemCount = intent.getIntExtra("TOTAL_ITEM_COUNT", 0);
                final int totalPrice = intent.getIntExtra("TOTAL_PRICE", 0);
                totalPriceTextView.setText("결제할 상품 총 " + totalItemCount + " 개" +
                        "\n상품 금액 " + totalPrice + "원");


                // PurchaseItem 생성 및 어댑터에 추가
                for (int i = 0; i < selectedBrands.size(); i++) {
                    PurchaseItem purchaseItem = new PurchaseItem(
                            selectedBrands.get(i),
                            selectedProductNames.get(i),
                            selectedOptions.get(i),
                            selectedPrice.get(i),
                            selectedProductID.get(i)

                    );
                    adapter.addPurchaseItem(purchaseItem);
                }
            }
            findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int totalPrice = intent.getIntExtra("TOTAL_PRICE", 0);
                    String selectedPaymentMethod = getSelectedPaymentMethod();

                    // 결제 수단이 선택되었을 때만 주문 처리를 수행
                    if (selectedPaymentMethod != null && !selectedPaymentMethod.isEmpty()) {
                        // 주문하기 버튼이 클릭되었을 때 실행되는 코드
                        sendOrder(userID, totalPrice, selectedPaymentMethod);

                        List<Integer> selectedBasketIDs = new ArrayList<>(selectedShoppingBasketID); // 새 목록 생성

                        BasketUpdateRequest basketUpdateRequest = new BasketUpdateRequest(
                                selectedBasketIDs,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("OrderFormActivity", "Response: " + response);

                                        // 바스켓 업데이트 후 응답 처리
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // 바스켓 업데이트 중 에러 처리
                                        Log.e("OrderFormActivity", "바스켓 업데이트 에러: " + error.getMessage());
                                    }
                                }
                        );



                        // Volley RequestQueue에 요청 추가
                        RequestQueue queue = Volley.newRequestQueue(OrderFormActivity.this);
                        queue.add(basketUpdateRequest);

                        // 주문 처리 후 페이지 이동 (예: 다음 화면으로 이동)
                        Intent intent = new Intent(OrderFormActivity.this, PaymentCompleteActivity.class);
                        intent.putExtra("ORDER_SUCCESSFUL", true); // You can pass additional data if needed
                        startActivity(intent);
                        finish(); // Optional: Finish the current activity if you don't want to go back to it
                    } else {
                        // 선택된 결제 수단이 없을 때 사용자에게 메시지 표시 또는 처리
                        Toast.makeText(OrderFormActivity.this, "결제 수단을 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void handleCheckboxChecked(boolean isChecked, String paymentMethod) {
            if (isChecked) {
                selectedPaymentMethod = paymentMethod;
                // 다른 체크박스 상태 초기화
                checkBoxAccount.setChecked(false);
                checkBoxBankDeposit.setChecked(false);
            }
        }

        // 선택된 결제 수단을 반환하는 메소드
        private String getSelectedPaymentMethod() {

            return selectedPaymentMethod;
        }


        private void getDefaultDeliveryAddress(String userID) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("OrderFormActivity", "서버 응답: " + response);

                        // 서버 응답 처리
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            // 기본 배송지 정보를 가져오기 성공했을 때
                            JSONArray defaultDeliveryAddresses = jsonResponse.getJSONArray("defaultDeliveryAddress");

                            if (defaultDeliveryAddresses.length() > 0) {
                                JSONObject defaultDeliveryAddress = defaultDeliveryAddresses.getJSONObject(0);

                                // TextView 업데이트
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String deliveryAddressName = defaultDeliveryAddress.getString("deliveryAddressName");
                                            String recipient = defaultDeliveryAddress.getString("recipient");
                                            String phoneNumber = defaultDeliveryAddress.getString("phoneNumber");
                                            String address = defaultDeliveryAddress.getString("address");
                                            String addressDetail = defaultDeliveryAddress.getString("addressDetail");
                                            String deliveryRequest = defaultDeliveryAddress.getString("deliveryRequest");

                                            // 각 TextView에 값 설정
                                            TextViewAddressName.setText(deliveryAddressName);
                                            TextViewRecipient.setText(recipient);
                                            TextViewCallNumber.setText(phoneNumber);
                                            TextViewAddress.setText(address);
                                            TextViewAddressDetail.setText(addressDetail);
                                            TextViewDeliveryRequest.setText(deliveryRequest);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                Log.d("OrderFormActivity", " 기본 배송지 가져오기 성공");
                            } else {
                                Log.e("OrderFormActivity", " 기본 배송지가 없습니다.");
                            }
                        } else {
                            Log.e("OrderFormActivity", " 기본 배송지 가져오기 실패");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("OrderFormActivity", " JSON 파싱 오류: " + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("OrderFormActivity", " 예외 발생: " + e.getMessage());
                    }
                }
            };

            // 서버 요청 클래스
            DefaultDeliveryAddressGetRequest defaultDeliveryAddressGetRequest = new DefaultDeliveryAddressGetRequest(userID, responseListener);
            RequestQueue queue = Volley.newRequestQueue(OrderFormActivity.this);
            queue.add(defaultDeliveryAddressGetRequest);
        }

        // OrderFormActivity 내에 새로 추가한 메소드
        private void sendOrder(String userID, int totalAmount, String selectedPaymentMethod) {
            // 사용자 ID를 세션에서 가져오거나 로그인 정보 등을 이용하여 설정

            // 결제 수단 선택 여부 확인
            if (selectedPaymentMethod == null) {
                // 사용자에게 결제 수단을 선택하도록 메시지 표시 또는 처리
                // 예: Toast 메시지 표시
                Toast.makeText(this, "결제 수단을 선택하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 주문 상품 목록
            List<Integer> selectedProductIDs = new ArrayList<>();
            for (PurchaseItem purchaseItem : adapter.getPurchaseList()) {
                selectedProductIDs.add(purchaseItem.productID);
            }

            // 서버에 주문 정보 전송
            SaveOrderRequest saveOrderRequest = new SaveOrderRequest(
                    userID,
                    selectedProductIDs,
                    totalAmount,  // 총 결제 금액 (이 값을 서버에서 사용)
                    selectedPaymentMethod,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버 응답 처리
                            try {
                                // 서버 응답 로그
                                Log.d("OrderFormActivity", "주문 서버 응답: " + response);

                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    // 주문 성공
                                    Toast.makeText(OrderFormActivity.this, "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    // 주문 완료 후 필요한 처리 수행
                                    // 예: 주문 완료 화면으로 이동 또는 다른 작업 수행
                                } else {
                                    // 주문 실패
                                    Toast.makeText(OrderFormActivity.this, "주문에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    // 실패 시 처리 수행
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    errorListener
            );

            // Volley RequestQueue에 요청 추가
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(saveOrderRequest);
        }

        public class PurchaseItem {
            String brandName;
            String productName;
            String option;
            String price;
            Integer productID;



            public PurchaseItem(String brandName, String productName, String option, String price, int productID) {
                this.productName = productName;
                this.brandName = brandName;
                this.option = option;
                this.price = price;
                this.productID = productID;

            }
        }

        public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
            private List<PurchaseItem> purchaseList;
            private Context context;
            private TextView checkedCountTextView;

            PurchaseAdapter(List<PurchaseItem> purchaseList, Context context, TextView checkedCountTextView) {
                this.purchaseList = purchaseList;
                this.context = context;
                this.checkedCountTextView = checkedCountTextView;
            }



            // PurchaseItem 추가하는 메서드
            public void addPurchaseItem(PurchaseItem purchaseItem) {
                purchaseList.add(purchaseItem);
                notifyDataSetChanged(); // 데이터 변경 알림
            }

            // PurchaseList 반환하는 메서드 추가
            public List<PurchaseItem> getPurchaseList() {
                return purchaseList;
            }

            @NonNull
            @Override
            public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
                return new PurchaseViewHolder(view, context);
            }

            @Override
            public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
                PurchaseItem purchaseItem = purchaseList.get(position);
                holder.bind(purchaseItem);
            }

            @Override
            public int getItemCount() {
                return purchaseList.size();
            }

            public class PurchaseViewHolder extends RecyclerView.ViewHolder {
                private final TextView productPrice, productOption, productBrand;
                private final TextView productNameTextView;

                public PurchaseViewHolder(View itemView, Context context) {
                    super(itemView);
                    productNameTextView = itemView.findViewById(R.id.product_name_textview);
                    productPrice = itemView.findViewById(R.id.product_price_textview);
                    productOption = itemView.findViewById(R.id.product_option_textview);
                    productBrand = itemView.findViewById(R.id.brand_name_textview);
                }

                void bind(PurchaseItem purchaseItem) {
                    productNameTextView.setText(purchaseItem.productName);
                    productPrice.setText(purchaseItem.price);
                    productBrand.setText(purchaseItem.brandName);
                    productOption.setText(purchaseItem.option);
                }
            }
        }
    }