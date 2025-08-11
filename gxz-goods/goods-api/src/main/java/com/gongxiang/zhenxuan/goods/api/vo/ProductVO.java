package com.gongxiang.zhenxuan.goods.api.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息VO
 *
 * @author gongxiang-zhenxuan
 */
public class ProductVO {

    private Long id;

    private Integer productType;

    private String productTypeDesc;

    private Long merchantId;

    private String merchantName;

    private Long platformCategoryId;

    private String categoryName;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private String description;

    private String specifications;

    private Integer status;

    private String statusDesc;

    private Integer minOrderQty;

    private String unit;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductTypeDesc() {
        return productTypeDesc;
    }

    public void setProductTypeDesc(String productTypeDesc) {
        this.productTypeDesc = productTypeDesc;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getPlatformCategoryId() {
        return platformCategoryId;
    }

    public void setPlatformCategoryId(Long platformCategoryId) {
        this.platformCategoryId = platformCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Integer getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(Integer minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "ProductVO{" +
                "id=" + id +
                ", productType=" + productType +
                ", productTypeDesc='" + productTypeDesc + '\'' +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", platformCategoryId=" + platformCategoryId +
                ", categoryName='" + categoryName + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", specifications='" + specifications + '\'' +
                ", status=" + status +
                ", statusDesc='" + statusDesc + '\'' +
                ", minOrderQty=" + minOrderQty +
                ", unit='" + unit + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}