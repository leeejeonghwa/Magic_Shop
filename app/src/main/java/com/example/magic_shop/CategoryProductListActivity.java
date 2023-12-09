package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryProductListActivity extends AppCompatActivity {

    CategorySelection categorySelection = CategorySelection.getInstance();

    private Button productBtn;

    private int categoryTexts[] = {
            R.string.category_top,
            R.string.category_outer,
            R.string.category_pants,
            R.string.category_skirt_one_piece,
            R.string.category_shoes,
            R.string.category_bag,
    };

    private int detailedCategoryTexts[][] = {
            {R.string.category_top_1, R.string.category_top_2, R.string.category_top_3,R.string.category_top_4, R.string.category_top_5, R.string.category_top_6, R.string.category_top_7, R.string.category_top_8, R.string.category_top_9, R.string.category_top_10, R.string.category_top_11, R.string.category_top_12},
            {R.string.category_outer_1, R.string.category_outer_2, R.string.category_outer_3, R.string.category_outer_4, R.string.category_outer_5, R.string.category_outer_6, R.string.category_outer_7, R.string.category_outer_8, R.string.category_outer_9, R.string.category_outer_10, R.string.category_outer_11, R.string.category_outer_12},
            {R.string.category_pants_1, R.string.category_pants_2, R.string.category_pants_3, R.string.category_pants_4, R.string.category_pants_5, R.string.category_pants_6, R.string.category_pants_7, R.string.category_pants_8, R.string.category_pants_9, R.string.category_pants_10, R.string.category_pants_11, R.string.category_pants_12},
            {R.string.category_skirt_one_piece_1, R.string.category_skirt_one_piece_2, R.string.category_skirt_one_piece_3, R.string.category_skirt_one_piece_4, R.string.category_skirt_one_piece_5, R.string.category_skirt_one_piece_6, R.string.category_skirt_one_piece_7, R.string.category_skirt_one_piece_8, R.string.category_skirt_one_piece_9, R.string.category_skirt_one_piece_10, R.string.category_skirt_one_piece_11, R.string.category_skirt_one_piece_12},
            {R.string.category_shoes_1, R.string.category_shoes_2, R.string.category_shoes_3, R.string.category_shoes_4, R.string.category_shoes_5, R.string.category_shoes_6, R.string.category_shoes_7, R.string.category_shoes_8, R.string.category_shoes_9, R.string.category_shoes_10, R.string.category_shoes_11, R.string.category_shoes_12},
            {R.string.category_bag_1, R.string.category_bag_2, R.string.category_bag_3, R.string.category_bag_4, R.string.category_bag_5, R.string.category_bag_6, R.string.category_bag_7, R.string.category_bag_8, R.string.category_bag_9, R.string.category_bag_10, R.string.category_bag_11, R.string.category_bag_12}
    };

    private int detailedCategoryBtns[] = {
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.button10,
            R.id.button11,
            R.id.button12
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_product_list);

        categoryTextChange();
        detailedCategoryTextChange();

        init();

        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryProductListActivity.this, Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.category_back_id);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 현재 액티비티 종료
            }
        });

        findViewById(R.id.category_search_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryProductListActivity.this, CategorySelectionActivity.class);
                startActivity(intent); // Intent를 사용하여 SecondActivity 시작
            }
        });
    }

    public void init() {
        productBtn = findViewById(R.id.btn_product_id1);
    }




    public void categoryTextChange() {

        TextView categoryTextView = findViewById(R.id.category_text_id);

        for (int i=0; i<6; i++) {
            if (categorySelection.getSelectedCategory().ordinal() == i) {
                categoryTextView.setText(categoryTexts[i]);

                for (int j=0; j<12; j++) {
                    Button btn = findViewById(detailedCategoryBtns[j]);
                    btn.setText(detailedCategoryTexts[i][j]);
                }

                break;
            }

        }

    }

    public void detailedCategoryTextChange() {

        for (int i=0; i<12; i++) {
            if (categorySelection.getSelectedDetailedCategory() == i) {
                Button btn = findViewById(detailedCategoryBtns[i]);
                btn.setTextColor(getResources().getColor(R.color.category_main_color));

                break;
            }
        }
    }







}
