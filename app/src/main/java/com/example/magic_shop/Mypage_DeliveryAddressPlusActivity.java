package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage_DeliveryAddressPlusActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_delivery_address_plus);
        getWindow().setWindowAnimations(0);

        EditText editTextAddressName = (EditText) findViewById(R.id.editTextAddressName);
        EditText editTextRecipient = (EditText) findViewById(R.id.editTextRecipient);
        EditText editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        EditText editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        EditText editTextAddressDetail = (EditText) findViewById(R.id.editTextAddressDetail);
        EditText editTextDeliveryAddressRequest = (EditText) findViewById(R.id.editTextDeliveryAddressRequest);

        CheckBox btn_delivery_address_default_check = (CheckBox) findViewById(R.id.btn_delivery_address_default_check);

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

        Button btn_address_plus_check = (Button) findViewById(R.id.btn_delivery_address_plus_check);
        btn_address_plus_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String deliveryAddressName = editTextAddressName.getText().toString();
                String recipient = editTextRecipient.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String address = editTextAddress.getText().toString();
                String addressDetail = editTextAddressDetail.getText().toString();
                String deliveryAddressRequest = editTextDeliveryAddressRequest.getText().toString();
                boolean isDefaultDeliveryAddress = btn_delivery_address_default_check.isChecked();

                // 기본배송지 여부 가져오기
                if (isDefaultDeliveryAddress){
                    String defaultDeliveryAddress = "Y";
                }
                else{
                    String defaultDeliveryAddress = "N";
                }


                Intent intent = new Intent(getApplicationContext(), Mypage_DeliveryAddressManageActivity.class);
                startActivity(intent);
            }
        });
    }
}
