package {package}.controller;

import {package}.api.dto.{entity}CreateDTO;
import {package}.api.dto.{entity}UpdateDTO;
import {package}.api.vo.{entity}VO;
import {package}.service.{entity}Service;
import com.gongxiang.zhenxuan.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * {entity} 管理控制器
 *
 * @author {author}
 */
@RestController
@RequestMapping("/{module}")
public class {entity}Controller {

    @Autowired
    private {entity}Service {entity_lower}Service;

    /**
     * 创建{entity_description}
     */
    @PostMapping
    public Result<{entity}VO> create(@Valid @RequestBody {entity}CreateDTO createDTO,
                                   @RequestHeader("X-User-Id") Long userId) {
        {entity}VO {entity_lower}VO = {entity_lower}Service.create(createDTO, userId);
        return Result.success({entity_lower}VO);
    }

    /**
     * 获取{entity_description}详情
     */
    @GetMapping("/{id}")
    public Result<{entity}VO> getById(@PathVariable("id") Long id,
                                    @RequestHeader("X-User-Id") Long userId) {
        {entity}VO {entity_lower}VO = {entity_lower}Service.getById(id, userId);
        return Result.success({entity_lower}VO);
    }

    /**
     * 更新{entity_description}
     */
    @PutMapping("/{id}")
    public Result<{entity}VO> update(@PathVariable("id") Long id,
                                   @Valid @RequestBody {entity}UpdateDTO updateDTO,
                                   @RequestHeader("X-User-Id") Long userId) {
        {entity}VO {entity_lower}VO = {entity_lower}Service.update(id, updateDTO, userId);
        return Result.success({entity_lower}VO);
    }

    /**
     * 删除{entity_description}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id,
                             @RequestHeader("X-User-Id") Long userId) {
        {entity_lower}Service.delete(id, userId);
        return Result.success();
    }
}
