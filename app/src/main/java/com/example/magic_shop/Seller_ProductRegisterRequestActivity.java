package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Seller_ProductRegisterRequestActivity extends AppCompatActivity{

    private RadioGroup subcategoryRadioGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_register_request);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        subcategoryRadioGroup = findViewById(R.id.small_category_id);

        RadioButton btn_topCat = findViewById(R.id.radio_top);

        btn_topCat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               setSubcategoryRadioBtns(getTopSubcategories());
               clearRadioGroupCheckedState(subcategoryRadioGroup);
            }
        });

        RadioButton btn_pantsCat = findViewById(R.id.radio_pants);
        btn_pantsCat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                setSubcategoryRadioBtns(getPantsCategories());
                clearRadioGroupCheckedState(subcategoryRadioGroup);
            }
        });

    }

    // 상위 카테고리에 대한 하위 카테고리를 설정하는 메서드
    private void setSubcategoryRadioBtns(String[] btn_topCat) {
        subcategoryRadioGroup.removeAllViews();

        for (String subcategory : btn_topCat) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(subcategory);
            subcategoryRadioGroup.addView(radioButton);
        }
    }

    private String[] getTopSubcategories() {
        return new String[]{"하위1", "하위2", "하위3", "하위4", "하위5", "하위6", "하위7", "하위8", "하위9", "하위10"};
    }

    private String[] getPantsCategories() {
        return new String[]{"다른1", "다른2", "다른3", "다른4", "다른5", "다른6", "다른7", "다른8", "다른9", "다른10"};
    }

    // 추가 메서드: 라디오 그룹의 선택 상태를 초기화하는 함수
    private void clearRadioGroupCheckedState(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
        }
        radioGroup.clearCheck();
    }
}

