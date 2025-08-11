package com.gongxiang.zhenxuan.goods.controller;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.goods.api.dto.ProductCreateDTO;
import com.gongxiang.zhenxuan.goods.api.dto.StockUpdateDTO;
import com.gongxiang.zhenxuan.goods.api.vo.ProductVO;
import com.gongxiang.zhenxuan.goods.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品管理控制器
 *
 * @author gongxiang-zhenxuan
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 创建商品
     */
    @PostMapping
    public Result<ProductVO> createProduct(@Valid @RequestBody ProductCreateDTO createDTO) {
        ProductVO productVO = productService.createProduct(createDTO);
        return Result.success(productVO);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/{productId}")
    public Result<ProductVO> getProduct(@PathVariable Long productId) {
        ProductVO productVO = productService.getProductById(productId);
        return Result.success(productVO);
    }

    /**
     * 更新商品信息
     */
    @PutMapping("/{productId}")
    public Result<ProductVO> updateProduct(@PathVariable Long productId,
                                         @Valid @RequestBody ProductCreateDTO updateDTO) {
        ProductVO productVO = productService.updateProduct(productId, updateDTO);
        return Result.success(productVO);
    }

    /**
     * 删除商品（下架）
     */
    @DeleteMapping("/{productId}")
    public Result<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return Result.success();
    }

    /**
     * 更新商品状态
     */
    @PutMapping("/{productId}/status")
    public Result<ProductVO> updateProductStatus(@PathVariable Long productId,
                                               @RequestParam Integer status) {
        ProductVO productVO = productService.updateProductStatus(productId, status);
        return Result.success(productVO);
    }

    /**
     * 更新库存
     */
    @PutMapping("/stock")
    public Result<ProductVO> updateStock(@Valid @RequestBody StockUpdateDTO stockUpdateDTO) {
        ProductVO productVO = productService.updateStock(stockUpdateDTO);
        return Result.success(productVO);
    }

    /**
     * 获取商户商品列表（外卖菜品）
     */
    @GetMapping("/merchant/{merchantId}")
    public Result<List<ProductVO>> getMerchantProducts(@PathVariable Long merchantId,
                                                     @RequestParam(required = false) Integer status) {
        List<ProductVO> products = productService.getMerchantProducts(merchantId, status);
        return Result.success(products);
    }

    /**
     * 获取平台自营商品列表
     */
    @GetMapping("/platform")
    public Result<List<ProductVO>> getPlatformProducts(@RequestParam(required = false) Long categoryId,
                                                     @RequestParam(required = false) String keyword) {
        List<ProductVO> products = productService.getPlatformProducts(categoryId, keyword);
        return Result.success(products);
    }

    /**
     * 获取团购商品列表
     */
    @GetMapping("/groupbuy")
    public Result<List<ProductVO>> getGroupBuyProducts(@RequestParam(required = false) String keyword) {
        List<ProductVO> products = productService.getGroupBuyProducts(keyword);
        return Result.success(products);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result<List<ProductVO>> searchProducts(@RequestParam String keyword,
                                                @RequestParam(required = false) Integer productType,
                                                @RequestParam(required = false) Long merchantId) {
        List<ProductVO> products = productService.searchProducts(keyword, productType, merchantId);
        return Result.success(products);
    }

    /**
     * 获取商品列表（管理后台）
     */
    @GetMapping("/list")
    public Result<List<ProductVO>> getProductList(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(required = false) Integer productType,
                                                @RequestParam(required = false) Integer status) {
        List<ProductVO> products = productService.getProductList(page, size, productType, status);
        return Result.success(products);
    }
}