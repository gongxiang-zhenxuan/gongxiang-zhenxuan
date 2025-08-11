package com.gongxiang.zhenxuan.goods.api.dto;

import javax.validation.constraints.NotNull;

/**
 * 库存更新DTO
 *
 * @author gongxiang-zhenxuan
 */
public class StockUpdateDTO {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "库存变化量不能为空")
    private Integer stockChange;

    private String reason;

    public StockUpdateDTO() {
    }

    public StockUpdateDTO(Long productId, Integer stockChange, String reason) {
        this.productId = productId;
        this.stockChange = stockChange;
        this.reason = reason;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getStockChange() {
        return stockChange;
    }

    public void setStockChange(Integer stockChange) {
        this.stockChange = stockChange;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "StockUpdateDTO{" +
                "productId=" + productId +
                ", stockChange=" + stockChange +
                ", reason='" + reason + '\'' +
                '}';
    }
}