package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class Seller_ProductRegisterRequestActivity extends AppCompatActivity {

    int [] superCatRadio = {
            R.id.radio_top,
            R.id.radio_outer,
            R.id.radio_pants,
            R.id.radio_skirt,
            R.id.radio_shoes,
            R.id.radio_bag
    };

    int [] subCatRadio = {
            R.id.radioButton1,
            R.id.radioButton2,
            R.id.radioButton3,
            R.id.radioButton4,
            R.id.radioButton5,
            R.id.radioButton6,
            R.id.radioButton7,
            R.id.radioButton8,
            R.id.radioButton9,
            R.id.radioButton10
    };

    private int subCatRadio_name[][] = {
            {R.string.category_top_3,R.string.category_top_4, R.string.category_top_5, R.string.category_top_6, R.string.category_top_7, R.string.category_top_8, R.string.category_top_9, R.string.category_top_10, R.string.category_top_11, R.string.category_top_12},
            {R.string.category_outer_3, R.string.category_outer_4, R.string.category_outer_5, R.string.category_outer_6, R.string.category_outer_7, R.string.category_outer_8, R.string.category_outer_9, R.string.category_outer_10, R.string.category_outer_11, R.string.category_outer_12},
            { R.string.category_pants_3, R.string.category_pants_4, R.string.category_pants_5, R.string.category_pants_6, R.string.category_pants_7, R.string.category_pants_8, R.string.category_pants_9, R.string.category_pants_10, R.string.category_pants_11, R.string.category_pants_12},
            { R.string.category_skirt_one_piece_3, R.string.category_skirt_one_piece_4, R.string.category_skirt_one_piece_5, R.string.category_skirt_one_piece_6, R.string.category_skirt_one_piece_9, R.string.category_skirt_one_piece_10, R.string.category_skirt_one_piece_11, R.string.category_skirt_one_piece_12,  },
            {R.string.category_shoes_3, R.string.category_shoes_4, R.string.category_shoes_5, R.string.category_shoes_6, R.string.category_shoes_7, R.string.category_shoes_8, R.string.category_shoes_9, R.string.category_shoes_10, R.string.category_shoes_11, R.string.category_shoes_12},
            { R.string.category_bag_3, R.string.category_bag_4, R.string.category_bag_5, R.string.category_bag_6, R.string.category_bag_7, R.string.category_bag_8, R.string.category_bag_9, R.string.category_bag_10, R.string.category_bag_11, R.string.category_bag_12}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_register_request);
        getWindow().setWindowAnimations(0);

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        for (int i : superCatRadio) {
            findViewById(i).setOnClickListener(new superCatRadioClickListener());
        }

        for (int i: subCatRadio) {
            findViewById(i).setOnClickListener(new subCatRadioClickListener());
        }

    }

    private class superCatRadioClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int clickedId = v.getId();
            onCheckedChange(clickedId);

            if (clickedId == superCatRadio[3]){
                //스커트 하위 카테고리 나타나기
                RadioButton button9 = findViewById(R.id.radioButton9);
                button9.setVisibility(View.INVISIBLE);
                RadioButton button10 = findViewById(R.id.radioButton10);
                button10.setVisibility(View.INVISIBLE);
                for (int i=0; i<8; i++) {
                    RadioButton radioButton = findViewById(subCatRadio[i]);
                    radioButton.setText(subCatRadio_name[3][i]);
                }
            }
            else {
                RadioButton button9 = findViewById(R.id.radioButton9);
                button9.setVisibility(View.VISIBLE);
                RadioButton button10 = findViewById(R.id.radioButton10);
                button10.setVisibility(View.VISIBLE);
                if (clickedId == superCatRadio[0]){

                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[0][i]);
                    }
                }
                else if (clickedId == superCatRadio[1]){
                    //아우터 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[1][i]);
                    }
                }
                else if (clickedId == superCatRadio[2]){
                    //바지 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[2][i]);
                    }
                }

                else if (clickedId == superCatRadio[4]){
                    //신발 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[4][i]);
                    }
                }
                else if (clickedId == superCatRadio[5]){
                    //가방 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[5][i]);
                    }
                }
            }


        }

    }

    public void onCheckedChange(int id) {
        for (int i = 0; i < 6; i++) {
            RadioButton radioButton = findViewById(superCatRadio[i]);
            radioButton.setChecked(id == superCatRadio[i]);
        }
    }

    private class subCatRadioClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int clickedId = v.getId();

            onSubCheckedChange(clickedId);
        }
    }

    public void onSubCheckedChange(int id) {
        for (int i = 0; i < 10; i++) {
            RadioButton radioButton = findViewById(subCatRadio[i]);
            radioButton.setChecked(id == subCatRadio[i]);
        }
    }
}
