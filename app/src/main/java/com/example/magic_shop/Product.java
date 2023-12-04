package com.example.magic_shop;

import android.graphics.Bitmap;

public class Product {

    private int id;
    private String productName;

    //1. top, 2. outer, 3. pants, 4. skirt_one_piece, 5. shoes, 6. bag
    private int categoryId;
    private int detailedCategoryId;
    private String sellerId;
    private int productPrice;

    private String allowance;

    private String  mainImage;

    private String detailedImage1;

    private String detailedImage2;

    private String detailedImage3;

    private String sizeImage;


    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getDetailedCategoryId() {
        return detailedCategoryId;
    }

    public void setDetailedCategoryId(int detailedCategoryId) {
        this.detailedCategoryId = detailedCategoryId;
    }
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDetailedImage1() {
        return detailedImage1;
    }

    public void setDetailedImage1(String detailedImage1) {
        this.detailedImage1 = detailedImage1;
    }

    public String getDetailedImage2() {
        return detailedImage2;
    }

    public void setDetailedImage2(String detailedImage2) {
        this.detailedImage2 = detailedImage2;
    }

    public String getDetailedImage3() {
        return detailedImage3;
    }

    public void setDetailedImage3(String detailedImage3) {
        this.detailedImage3 = detailedImage3;
    }

    public String getSizeImage() {
        return sizeImage;
    }

    public void setSizeImage(String sizeImage) {
        this.sizeImage = sizeImage;
    }
}