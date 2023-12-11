package com.example.magic_shop;

import android.graphics.Bitmap;

public class ProductItem {

    String id;
    String date;
    String productName;
    String productSize;
    String productColor;
    Bitmap mainImage;

    String productPrice;

    String brandName;

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

    public ProductItem(String id,String date, String productName, String productSize, String productColor , Bitmap mainImage, String brandName) {
        this.id = id;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
        this.brandName = brandName;
    }

    public ProductItem(String id, String date, String productName, String productSize, String productColor , Bitmap mainImage, String brandName, String productPrice) {
        this.id = id;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }
}
