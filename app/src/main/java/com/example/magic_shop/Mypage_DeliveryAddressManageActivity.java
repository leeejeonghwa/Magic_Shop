package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Mypage_DeliveryAddressManageActivity extends AppCompatActivity {

    public List<AddressItem> getAddressList() {
        List<AddressItem> addressList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        addressList.add(new AddressItem("집", "010-1234-5678", "서울시 강남구"));
        addressList.add(new AddressItem("회사", "010-9876-5432", "경기도 수원시"));
        addressList.add(new AddressItem("친구집", "010-9876-5432", "경기도 수원시"));
        addressList.add(new AddressItem("회사2", "010-9876-5432", "경기도 수원시"));
        addressList.add(new AddressItem("회사3", "010-9876-5432", "경기도 수원시"));
        // ... 추가적인 데이터

        return addressList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_delivery_address_manage);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_SettingActivity.class);
                startActivity(intent);
            }
        });

        Button btn_delivery_address_plus = (Button) findViewById(R.id.btn_delivery_address_plus);
        btn_delivery_address_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_DeliveryAddressPlusActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.address_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<AddressItem> addressList = getAddressList(); // 여러 배송지 정보를 가져오는 메서드
        AddressAdapter adapter = new AddressAdapter(addressList, this);
        recyclerView.setAdapter(adapter);
    }

    // AddressItem 클래스는 각 배송지 정보를 나타냅니다.
    public class AddressItem {
        String name;
        String phoneNumber;
        String address;

        public AddressItem(String name, String phoneNumber, String address) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.address = address;
        }
    }

    // AddressAdapter 클래스는 RecyclerView 데이터를 바인딩합니다.
    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
        private List<AddressItem> addressList;
        private Context context;

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

        // AddressViewHolder 클래스는 각 아이템의 뷰를 관리합니다.
        public class AddressViewHolder extends RecyclerView.ViewHolder {
            private final TextView nameTextView;
            private final TextView phoneNumberTextView;
            private final TextView addressTextView;
            public final Button addressEditButton;
            private final Context context;

            public AddressViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                nameTextView = itemView.findViewById(R.id.address_name);
                phoneNumberTextView = itemView.findViewById(R.id.address_phone_number);
                addressTextView = itemView.findViewById(R.id.address);
                addressEditButton = itemView.findViewById(R.id.btn_address_edit);

                addressEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            AddressItem addressItem = addressList.get(position);
                            Intent intent = new Intent(context, Mypage_DeliveryAddressEditActivity.class);
                            intent.putExtra("name", addressItem.name);
                            intent.putExtra("phoneNumber", addressItem.phoneNumber);
                            intent.putExtra("address", addressItem.address);
                            context.startActivity(intent);
                        }
                    }
                });
            }
            void bind(AddressItem addressItem) {
                nameTextView.setText(addressItem.name);
                phoneNumberTextView.setText(addressItem.phoneNumber);
                addressTextView.setText(addressItem.address);
            }
        }
    }
}
