package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryProductListActivity extends AppCompatActivity {

    CategorySelectionManager categorySelectionManager;

    public Context context;
    private Button btnProduct;

    private ProductAdapter adapter;

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
        getWindow().setWindowAnimations(0);

        context = this; // context 변수를 초기화
        init();


        categorySelectionManager = CategorySelectionManager.getInstance(context);
        adapter = new ProductAdapter(categorySelectionManager.getProductList(), this);

        categoryTextChange();
        detailedCategoryTextChange();

        Button btnBack = findViewById(R.id.category_back_id);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 현재 액티비티 종료
            }
        });


        RecyclerView recyclerView = findViewById(R.id.product_category_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fetchDataFromServer();
    }

    public void init() {
        btnProduct = findViewById(R.id.btn_product_id1);
    }




    public void categoryTextChange() {

        TextView categoryTextView = findViewById(R.id.category_text_id);

        for (int i=0; i<6; i++) {
            if (categorySelectionManager.getSelectedCategory().ordinal() == i) {
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
            if (categorySelectionManager.getSelectedDetailedCategory() == i) {
                Button btn = findViewById(detailedCategoryBtns[i]);
                btn.setTextColor(getResources().getColor(R.color.category_main_color));

                break;
            }
        }
    }

    private void fetchDataFromServer() {
        categorySelectionManager.fetchDataFromServer(categorySelectionManager.getIntegerCategory(), categorySelectionManager.getSelectedDetailedCategory(), new CategorySelectionManager.OnDataReceivedListener(){
            @Override
            public void onDataReceived() {
                String str = Integer.toString(categorySelectionManager.getProductList().size());
                Log.d("fetch", str);
                updateUI();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private List<ProductItem> productList;
        private Context context;

        ProductAdapter(List<ProductItem> productList, Context context) {
            this.productList = productList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.category_item_product_list, parent, false);
            return new ProductViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            ProductItem productItem = productList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            private final TextView productNameTextView;
            private final TextView productPriceTextView;
            private final TextView brandNameTextView;

            private final ImageButton productMainImageButton;
            private final Context context;

            public ProductViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                productNameTextView = itemView.findViewById(R.id.product_name_text_id1);
                productPriceTextView = itemView.findViewById(R.id.product_price_text_id1);
                productMainImageButton = itemView.findViewById(R.id.btn_product_id1);
                brandNameTextView = itemView.findViewById(R.id.product_brand_text_id1);

            }

            void bind(ProductItem productItem) {
                productNameTextView.setText(productItem.productName);
                productPriceTextView.setText(productItem.productPrice);
                brandNameTextView.setText(productItem.brandName);
                if (productItem.mainImage == null) {
                    Log.d("이미지", "null");
                }
                else {
                    productMainImageButton.setImageBitmap(productItem.mainImage);
                }

            }
        }
    }







}
