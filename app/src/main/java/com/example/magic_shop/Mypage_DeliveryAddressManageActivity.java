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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mypage_DeliveryAddressManageActivity extends AppCompatActivity {

    public List<AddressItem> addressList;
    public Context context;

    public List<AddressItem> getAddressList(String jsonResponse) throws JSONException {
        List<AddressItem> addressList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "deliveryAddresses" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("deliveryAddresses");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String addressID = jsonObject.getString(("addressID"));
            String deliveryAddressName = jsonObject.getString("deliveryAddressName");
            String recipient = jsonObject.getString("recipient");
            String phoneNumber = jsonObject.getString("phoneNumber");
            String address = jsonObject.getString("address");
            String addressDetail = jsonObject.getString("addressDetail");
            String deliveryRequest = jsonObject.getString("deliveryRequest");
            String defaultDeliveryAddress = jsonObject.getString("defaultDeliveryAddress");

            AddressItem addressItem = new AddressItem(addressID, deliveryAddressName, recipient, phoneNumber, address,
                    addressDetail, deliveryRequest, defaultDeliveryAddress);

            addressList.add(addressItem);
        }

        return addressList;
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Mypage_DeliveryAddressManageActivity.this)
//                .setTitle("배송지 삭제")
                .setMessage("배송지를 삭제 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_delivery_address_manage);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserId();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_home = (Button) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.address_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Mypage_DeliveryAddressManageActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        Button btn_delivery_address_plus = (Button) findViewById(R.id.btn_delivery_address_plus);
        btn_delivery_address_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int itemCount = recyclerView.getAdapter().getItemCount();
                if(itemCount>=5){
                    Toast.makeText(getApplicationContext(), "배송지는 5개가 최대입니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Mypage_DeliveryAddressPlusActivity.class);
                    startActivity(intent);
                }
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_DeliveryAddressManageActivity", "서버 응답: " + response);

                    List<AddressItem> addressList = getAddressList(response);
                    AddressAdapter addressAdapter = new AddressAdapter(addressList, Mypage_DeliveryAddressManageActivity.this);
                    recyclerView.setAdapter(addressAdapter);  // 어댑터 설정
                    addressAdapter.notifyDataSetChanged(); // 어댑터 갱신

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DeliveryAddressGetRequest deliveryAddressGetRequest = new DeliveryAddressGetRequest(Mypage_DeliveryAddressManageActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_DeliveryAddressManageActivity.this);
        queue.add(deliveryAddressGetRequest);
    }

    // AddressItem 클래스는 각 배송지 정보를 나타냅니다.
    public class AddressItem {
        String addressID;
        String deliveryAddressName;
        String recipient;
        String phoneNumber;
        String address;
        String addressDetail;
        String deliveryRequest;
        String defaultDeliveryAddress;

        public AddressItem(String addressID, String deliveryAddressName, String recipient,String phoneNumber, String address,
                           String addressDetail, String deliveryRequest, String defaultDeliveryAddress) {
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
    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
        public List<AddressItem> addressList;
        public Context context;

        AddressAdapter(List<AddressItem> addressList, Context context) {
            this.addressList = addressList;
            this.context = context;
        }

        @NonNull
        @Override
        public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_address, parent, false);
            return new AddressViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
            AddressItem addressItem = addressList.get(position);
            holder.bind(addressItem);
        }

        @Override
        public int getItemCount() {
            return addressList.size();
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
                            AddressItem addressItem = addressList.get(position);
                            Intent intent = new Intent(context, Mypage_DeliveryAddressEditActivity.class);
                            intent.putExtra("addressID", addressItem.addressID);
                            intent.putExtra("deliveryAddressName", addressItem.deliveryAddressName);
                            intent.putExtra("recipient", addressItem.recipient);
                            intent.putExtra("phoneNumber", addressItem.phoneNumber);
                            intent.putExtra("address", addressItem.address);
                            intent.putExtra("addressDetail", addressItem.addressDetail);
                            intent.putExtra("deliveryRequest", addressItem.deliveryRequest);
                            intent.putExtra("defaultDeliveryAddress", addressItem.defaultDeliveryAddress);
                            context.startActivity(intent);
                        }
                    }
                });

                // 배송지 삭제
                addressDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            AddressItem addressItem = addressList.get(position);
                        }
                    }
                });
            }

            void bind(AddressItem addressItem) {
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
