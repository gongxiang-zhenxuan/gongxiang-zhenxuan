package {package}.service.impl;

import {package}.api.dto.{entity}CreateDTO;
import {package}.api.dto.{entity}UpdateDTO;
import {package}.api.vo.{entity}VO;
import {package}.entity.{entity}Entity;
import {package}.mapper.{entity}Mapper;
import {package}.service.{entity}Service;
import com.gongxiang.zhenxuan.common.exception.BusinessException;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * {entity} 服务实现类
 *
 * @author {author}
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class {entity}ServiceImpl implements {entity}Service {

    @Autowired
    private {entity}Mapper {entity_lower}Mapper;

    @Override
    public {entity}VO create({entity}CreateDTO createDTO, Long userId) {
        // 参数验证
        if (createDTO == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "创建参数不能为空");
        }

        // 创建实体
        {entity}Entity entity = new {entity}Entity();
        // TODO: 设置实体属性
        entity.setCreatedBy(userId);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());

        // 保存到数据库
        {entity_lower}Mapper.insert(entity);

        // 转换为 VO 并返回
        return convertToVO(entity);
    }

    @Override
    public {entity}VO getById(Long id, Long userId) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "ID不能为空");
        }

        {entity}Entity entity = {entity_lower}Mapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "{entity_description}不存在");
        }

        // TODO: 检查权限
        // checkPermission(entity, userId);

        return convertToVO(entity);
    }

    @Override
    public {entity}VO update(Long id, {entity}UpdateDTO updateDTO, Long userId) {
        // 参数验证
        if (id == null || updateDTO == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "更新参数不能为空");
        }

        // 查询现有实体
        {entity}Entity entity = {entity_lower}Mapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "{entity_description}不存在");
        }

        // TODO: 检查权限
        // checkPermission(entity, userId);

        // 更新实体属性
        // TODO: 设置更新的属性
        entity.setUpdatedBy(userId);
        entity.setUpdatedTime(LocalDateTime.now());

        // 更新数据库
        {entity_lower}Mapper.updateById(entity);

        return convertToVO(entity);
    }

    @Override
    public void delete(Long id, Long userId) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "ID不能为空");
        }

        {entity}Entity entity = {entity_lower}Mapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "{entity_description}不存在");
        }

        // TODO: 检查权限
        // checkPermission(entity, userId);

        // 软删除或硬删除
        {entity_lower}Mapper.deleteById(id);
    }

    /**
     * 实体转换为 VO
     */
    private {entity}VO convertToVO({entity}Entity entity) {
        if (entity == null) {
            return null;
        }

        {entity}VO vo = new {entity}VO();
        // TODO: 设置 VO 属性
        vo.setId(entity.getId());
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedTime(entity.getUpdatedTime());

        return vo;
    }

    /**
     * 检查操作权限
     */
    private void checkPermission({entity}Entity entity, Long userId) {
        // TODO: 实现权限检查逻辑
        if (!hasPermission(entity, userId)) {
            throw new BusinessException(ResultCode.PERMISSION_DENIED, "没有操作权限");
        }
    }

    private boolean hasPermission({entity}Entity entity, Long userId) {
        // TODO: 实现具体的权限检查逻辑
        return true;
    }
}
