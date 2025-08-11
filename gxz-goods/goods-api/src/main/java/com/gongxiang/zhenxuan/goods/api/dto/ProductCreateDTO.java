package com.gongxiang.zhenxuan.goods.api.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品创建DTO
 *
 * @author gongxiang-zhenxuan
 */
public class ProductCreateDTO {

    @NotNull(message = "商品类型不能为空")
    private Integer productType;

    private Long merchantId;

    private Long platformCategoryId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    private String imageUrl;

    private String description;

    private String specifications;

    @Min(value = 1, message = "起订量不能小于1")
    private Integer minOrderQty = 1;

    private String unit = "份";

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getPlatformCategoryId() {
        return platformCategoryId;
    }

    public void setPlatformCategoryId(Long platformCategoryId) {
        this.platformCategoryId = platformCategoryId;
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

    @Override
    public String toString() {
        return "ProductCreateDTO{" +
                "productType=" + productType +
                ", merchantId=" + merchantId +
                ", platformCategoryId=" + platformCategoryId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", specifications='" + specifications + '\'' +
                ", minOrderQty=" + minOrderQty +
                ", unit='" + unit + '\'' +
                '}';
    }
}