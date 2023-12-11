package com.example.magic_shop;

public class Product {


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

    private String color1;

    private String color2;

    private String sizeS;

    private String sizeM;

    private String sizeL;




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

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getSizeS() {
        return sizeS;
    }

    public void setSizeS(String sizeS) {
        this.sizeS = sizeS;
    }

    public String getSizeM() {
        return sizeM;
    }

    public void setSizeM(String sizeM) {
        this.sizeM = sizeM;
    }

    public String getSizeL() {
        return sizeL;
    }

    public void setSizeL(String sizeL) {
        this.sizeL = sizeL;
    }

}