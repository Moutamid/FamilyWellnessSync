package com.moutimid.familywellness.user.model;

public class CategoryProductInfo {
    private String ProductImage;
    private String ProductName;
    private String ProductPrice;
    private String ProductExpiryDate;
    private String ProductCategory;
    private String ProductQuatinty;
    private boolean IsFavorite;
    public CategoryProductInfo(){

    }

    public CategoryProductInfo(String ProductImage, String ProductName, String ProductPrice, String ProductExpiryDate, boolean IsFavorite, String ProductCategory, String ProductQuatinty ){
        this.ProductImage = ProductImage;
        this.ProductName = ProductName;
        this.ProductPrice = ProductPrice;
        this.ProductExpiryDate = ProductExpiryDate;
        this.IsFavorite = IsFavorite;
        this.ProductCategory = ProductCategory;
        this.ProductQuatinty = ProductQuatinty;
    }  public CategoryProductInfo(String ProductImage, String ProductName, String ProductPrice, String ProductExpiryDate, boolean IsFavorite){
        this.ProductImage = ProductImage;
        this.ProductName = ProductName;
        this.ProductPrice = ProductPrice;
        this.ProductExpiryDate = ProductExpiryDate;
        this.IsFavorite = IsFavorite;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductExpiryDate() {
        return ProductExpiryDate;
    }

    public void setProductExpiryDate(String productExpiryDate) {
        ProductExpiryDate = productExpiryDate;
    }

    public boolean getIsFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductQuatinty() {
        return ProductQuatinty;
    }

    public void setProductQuatinty(String productQuatinty) {
        ProductQuatinty = productQuatinty;
    }
}
