package com.example.magic_shop;

import android.graphics.Bitmap;

public class ProductItem {

    String id;
    String date;
    String productName;
    String productSize;
    String productColor;
    Bitmap mainImage;

    String sellerId;

    public ProductItem(String date, String productName, String productSize, String productColor , Bitmap mainImage) {
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
    }

    public ProductItem(String id,String date, String productName, String productSize, String productColor , Bitmap mainImage) {
        this.id = id;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
    }

    public ProductItem(String id,String date, String productName, String productSize, String productColor , Bitmap mainImage, String sellerId) {
        this.id = id;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
        this.sellerId = sellerId;
    }
}
