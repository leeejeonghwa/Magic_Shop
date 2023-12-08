package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

    public List<SearchResultItem> searchResultList;
    public SearchResultAdapter searchResultAdapter;
    public Context context;


    public List<SearchResultItem> getSearchResultList(String jsonResponse) throws JSONException {
        List<SearchResultItem > searchResultList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "deliveryAddresses" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("deliveryAddresses");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String userID = jsonObject.getString(("userID"));
            String addressID = jsonObject.getString(("addressID"));
            String deliveryAddressName = jsonObject.getString("deliveryAddressName");
            String recipient = jsonObject.getString("recipient");
            String phoneNumber = jsonObject.getString("phoneNumber");
            String address = jsonObject.getString("address");
            String addressDetail = jsonObject.getString("addressDetail");
            String deliveryRequest = jsonObject.getString("deliveryRequest");
            String defaultDeliveryAddress = jsonObject.getString("defaultDeliveryAddress");

            SearchResultItem searchResultItem = new SearchResultItem(userID, addressID, deliveryAddressName, recipient, phoneNumber, address,
                    addressDetail, deliveryRequest, defaultDeliveryAddress);

            searchResultList.add(searchResultItem);
        }

        return searchResultList;
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





        mypage_btn = (Button) findViewById(R.id.mypage_id);
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                if (sessionManager.isLoggedIn()) {
                    // 이미 로그인된 경우 마이페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인되지 않은 경우 로그인 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.search_list_re);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_DeliveryAddressManageActivity", "서버 응답: " + response);

                    List<SearchResultItem > searchResultList = getSearchResultList(response);

                    if (searchResultAdapter == null) {
                        Log.d("Mypage_DeliveryAddressManageActivity", "Adapter is null. Creating new adapter.");
                        searchResultAdapter = new searchResultAdapter(searchResultList, context);
                        recyclerView.setAdapter(searchResultAdapter);
                    } else {
                        Log.d("Mypage_DeliveryAddressManageActivity", "Adapter exists. Updating data.");
                        searchResultAdapter.setSearchResultList(searchResultList);
                        searchResultAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        SearchRequest searchRequest = new SearchRequest(this, searchTerm, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(searchRequest);
    }

    // AddressItem 클래스는 각 배송지 정보를 나타냅니다.
    public class SearchResultItem {
        String userID;
        String addressID;
        String deliveryAddressName;
        String recipient;
        String phoneNumber;
        String address;
        String addressDetail;
        String deliveryRequest;
        String defaultDeliveryAddress;

        public SearchResultItem(String userID, String addressID, String deliveryAddressName, String recipient, String phoneNumber, String address,
                                String addressDetail, String deliveryRequest, String defaultDeliveryAddress) {
            this.userID = userID;
            this.addressID = addressID;
            this.deliveryAddressName = deliveryAddressName;
            this.recipient = recipient;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.addressDetail = addressDetail;
            this.deliveryRequest = deliveryRequest;
            this.defaultDeliveryAddress = defaultDeliveryAddress;
        }
    }

    // AddressAdapter 클래스는 RecyclerView 데이터를 바인딩합니다.
    public class AddressAdapter extends RecyclerView.Adapter<.AddressAdapter.AddressViewHolder> {
        public List<.AddressItem> addressList;
        public Context context;

        AddressAdapter(List<.AddressItem> addressList, Context context) {
            this.addressList = sortAddresses(addressList);
            this.context = context;
        }

        public void setAddressList(List<.AddressItem> addressList) {
            this.addressList = sortAddresses(addressList);
            notifyDataSetChanged();
        }

        // 기본 배송지를 상단으로 이동시키는 정렬 메서드
        public List<.AddressItem> sortAddresses(List<.AddressItem> addresses) {
            List<.SearchResultItem > sortedAddresses = new ArrayList<>();
            List<.SearchResultItem > defaultAddresses = new ArrayList<>();
            List<.SearchResultItem > otherAddresses = new ArrayList<>();

            for (.SearchResultItem address : addresses) {
                if ("1".equals(address.defaultDeliveryAddress)) {
                    defaultAddresses.add(address);
                } else {
                    otherAddresses.add(address);
                }
            }

            sortedAddresses.addAll(defaultAddresses);
            sortedAddresses.addAll(otherAddresses);

            return sortedAddresses;
        }

        @NonNull
        @Override
        public .AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_address, parent, false);
            .AddressAdapter.AddressViewHolder viewHolder = new .AddressAdapter.AddressViewHolder(view, context);
            return new .AddressAdapter.AddressViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull .AddressAdapter.AddressViewHolder holder, int position) {
            .SearchResultItem searchResultItem = addressList.get(position);

            // 기본 배송지인 경우 삭제 버튼을 숨김
            if ("1".equals(searchResultItem.defaultDeliveryAddress)) {
                holder.addressDeleteButton.setVisibility(View.GONE);
            } else {
                holder.addressDeleteButton.setVisibility(View.VISIBLE);
            }
            holder.bind(searchResultItem);

            // 아이템의 위치가 변경된 경우 이동을 알림
            if (position != holder.getAdapterPosition()) {
                notifyItemMoved(holder.getAdapterPosition(), position);
            }
        }

        @Override
        public int getItemCount() {
            return addressList != null ? addressList.size() : 0;
        }

        public class AddressViewHolder extends RecyclerView.ViewHolder {
            private final TextView deliveryAddressNameTextView;
            private final TextView recipientTextView;
            private final TextView phoneNumberTextView;
            private final TextView addressTextView;
            private final TextView addressDetailTextView;
            private final TextView deliveryRequestTextView;
            private final TextView defaultDeliveryAddressTextView;
            public final Button addressEditButton;
            public final Button addressDeleteButton;
            private final Context context;

            public AddressViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                deliveryAddressNameTextView = itemView.findViewById(R.id.deliveryAddressName);
                recipientTextView = itemView.findViewById(R.id.recipient);
                phoneNumberTextView = itemView.findViewById(R.id.phoneNumber);
                addressTextView = itemView.findViewById(R.id.address);
                addressDetailTextView = itemView.findViewById(R.id.addressDetail);
                deliveryRequestTextView = itemView.findViewById(R.id.deliveryRequest);
                defaultDeliveryAddressTextView = itemView.findViewById(R.id.defaultDeliveryAddress);
                addressEditButton = itemView.findViewById(R.id.btn_address_edit);
                addressDeleteButton = itemView.findViewById(R.id.btn_address_delete);

                // 배송지 수정
                addressEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            .SearchResultItem searchResultItem = addressList.get(position);
                            Intent intent = new Intent(context, Mypage_DeliveryAddressEditActivity.class);
                            intent.putExtra("addressID", searchResultItem.addressID);
                            intent.putExtra("deliveryAddressName", searchResultItem.deliveryAddressName);
                            intent.putExtra("recipient", searchResultItem.recipient);
                            intent.putExtra("phoneNumber", searchResultItem.phoneNumber);
                            intent.putExtra("address", searchResultItem.address);
                            intent.putExtra("addressDetail", searchResultItem.addressDetail);
                            intent.putExtra("deliveryRequest", searchResultItem.deliveryRequest);
                            intent.putExtra("defaultDeliveryAddress", searchResultItem.defaultDeliveryAddress);
                            context.startActivity(intent);
                        }
                    }
                });

                // 배송지 삭제
                addressDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(.this)
                                .setMessage("배송지를 삭제 하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            .SearchResultItem searchResultItem = addressList.get(position);

                                            // 응답 리스너 정의
                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonResponse = new JSONObject(response);

                                                        boolean success = jsonResponse.getBoolean("success");

                                                        if (success) {
                                                            // 성공적으로 삭제된 경우
                                                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                                            // 리스트에서 삭제된 항목 제거
                                                            addressList.remove(position);
                                                            notifyItemRemoved(position);
                                                            notifyItemRangeChanged(position, addressList.size());
                                                        } else {
                                                            // 삭제 실패한 경우
                                                            Toast.makeText(context, "실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            // 에러 리스너 정의
                                            Response.ErrorListener errorListener = new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // 에러 처리
                                                    Log.e("Volley Error", error.toString());
                                                }
                                            };

                                            // DeliveryAddressDeleteRequest 객체 생성
                                            DeliveryAddressDeleteRequest deleteRequest = new DeliveryAddressDeleteRequest(
                                                    searchResultItem.userID,
                                                    searchResultItem.addressID,
                                                    responseListener,
                                                    errorListener
                                            );

                                            // 요청을 큐에 추가
                                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                                            requestQueue.add(deleteRequest);
                                        }
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        AlertDialog msgDlg = msgBuilder.create();
                        msgDlg.show();
                    }
                });
            }

            void bind(.SearchResultItem addressItem) {
                // 각 TextView에 해당하는 데이터를 설정
                deliveryAddressNameTextView.setText(addressItem.deliveryAddressName);
                recipientTextView.setText(addressItem.recipient);
                phoneNumberTextView.setText(addressItem.phoneNumber);
                addressTextView.setText(addressItem.address);
                addressDetailTextView.setText(addressItem.addressDetail);
                deliveryRequestTextView.setText(addressItem.deliveryRequest);

                if ("1".equals(addressItem.defaultDeliveryAddress)) {
                    defaultDeliveryAddressTextView.setText("기본배송지");
                } else {
                    defaultDeliveryAddressTextView.setText(null);
                }
            }
        }

    }
}
