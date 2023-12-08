package com.example.magic_shop;

import android.graphics.Bitmap;

public class ProductItem {
    String date;
    String productName;
    String productSize;
    String productColor;

    Bitmap mainImage;

    public ProductItem(String date, String productName, String productSize, String productColor , Bitmap mainImage) {
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
    }
}
