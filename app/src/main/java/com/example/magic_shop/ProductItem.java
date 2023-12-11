package com.example.magic_shop;

import android.graphics.Bitmap;

public class ProductItem {

    String productID;
    String date;
    String productName;
    String productSize;
    String productColor;
    Bitmap mainImage;
    String productPrice;
    String brandName;

    public ProductItem(String date, String productName, String productSize, String productColor , Bitmap mainImage, String brandName) {
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
    }

    public ProductItem(String productID, String date, String productName, String productSize, String productColor , Bitmap mainImage) {
        this.productID = productID;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
    }

    public ProductItem(String productID, String date, String productName, String productSize, String productColor , Bitmap mainImage, String brandName) {
        this.productID = productID;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
        this.brandName = brandName;
    }

    public ProductItem(String productID, String date, String productName, String productSize, String productColor , Bitmap mainImage, String brandName, String productPrice) {
        this.productID = productID;
        this.date = date;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.mainImage = mainImage;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }
}
