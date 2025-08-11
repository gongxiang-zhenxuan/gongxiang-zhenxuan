package com.gongxiang.zhenxuan.goods.service;

import com.gongxiang.zhenxuan.goods.api.dto.ProductCreateDTO;
import com.gongxiang.zhenxuan.goods.api.dto.StockUpdateDTO;
import com.gongxiang.zhenxuan.goods.api.vo.ProductVO;

import java.util.List;

/**
 * 商品服务接口
 *
 * @author gongxiang-zhenxuan
 */
public interface ProductService {

    /**
     * 创建商品
     */
    ProductVO createProduct(ProductCreateDTO createDTO);

    /**
     * 根据商品ID获取商品信息
     */
    ProductVO getProductById(Long productId);

    /**
     * 更新商品信息
     */
    ProductVO updateProduct(Long productId, ProductCreateDTO updateDTO);

    /**
     * 删除商品（软删除，设置为下架状态）
     */
    void deleteProduct(Long productId);

    /**
     * 更新商品状态
     */
    ProductVO updateProductStatus(Long productId, Integer status);

    /**
     * 更新库存
     */
    ProductVO updateStock(StockUpdateDTO stockUpdateDTO);

    /**
     * 获取商户商品列表（外卖菜品）
     */
    List<ProductVO> getMerchantProducts(Long merchantId, Integer status);

    /**
     * 获取平台自营商品列表
     */
    List<ProductVO> getPlatformProducts(Long categoryId, String keyword);

    /**
     * 获取团购商品列表
     */
    List<ProductVO> getGroupBuyProducts(String keyword);

    /**
     * 搜索商品
     */
    List<ProductVO> searchProducts(String keyword, Integer productType, Long merchantId);

    /**
     * 获取商品列表（分页）
     */
    List<ProductVO> getProductList(Integer page, Integer size, Integer productType, Integer status);

    /**
     * 检查库存是否充足
     */
    boolean checkStock(Long productId, Integer quantity);

    /**
     * 锁定库存（下单时）
     */
    boolean lockStock(Long productId, Integer quantity);

    /**
     * 释放库存（取消订单时）
     */
    void releaseStock(Long productId, Integer quantity);

    /**
     * 扣减库存（支付成功时）
     */
    boolean reduceStock(Long productId, Integer quantity);

    /**
     * 验证商品是否可购买
     */
    boolean isProductAvailable(Long productId);
}