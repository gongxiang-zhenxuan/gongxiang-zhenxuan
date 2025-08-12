package {package}.service.impl;

import {package}.api.dto.{entity}CreateDTO;
import {package}.api.vo.{entity}VO;
import {package}.entity.{entity}Entity;
import {package}.mapper.{entity}Mapper;
import {package}.service.{entity}Service;
import com.gongxiang.zhenxuan.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {entity}ServiceImpl 单元测试
 *
 * @author {author}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("{entity}Service 单元测试")
class {entity}ServiceImplTest {

    @Mock
    private {entity}Mapper {entity_lower}Mapper;

    @InjectMocks
    private {entity}ServiceImpl {entity_lower}Service;

    private {entity}CreateDTO validCreateDTO;
    private {entity}Entity mockEntity;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        
        // 准备测试数据
        validCreateDTO = new {entity}CreateDTO();
        // TODO: 设置测试数据属性
        
        mockEntity = new {entity}Entity();
        mockEntity.setId(1L);
        // TODO: 设置 mock 实体属性
    }

    @Test
    @DisplayName("应该成功创建{entity_description}")
    void shouldCreate{entity}Successfully() {
        // Given
        when({entity_lower}Mapper.insert(any({entity}Entity.class))).thenReturn(1);

        // When
        {entity}VO result = {entity_lower}Service.create(validCreateDTO, testUserId);

        // Then
        assertNotNull(result);
        verify({entity_lower}Mapper).insert(any({entity}Entity.class));
    }

    @Test
    @DisplayName("当创建参数为空时应该抛出异常")
    void shouldThrowExceptionWhenCreateDTOIsNull() {
        // When & Then
        assertThrows(BusinessException.class, () -> {
            {entity_lower}Service.create(null, testUserId);
        });
        
        verify({entity_lower}Mapper, never()).insert(any());
    }

    @Test
    @DisplayName("应该成功根据ID获取{entity_description}")
    void shouldGetByIdSuccessfully() {
        // Given
        Long entityId = 1L;
        when({entity_lower}Mapper.selectById(entityId)).thenReturn(mockEntity);

        // When
        {entity}VO result = {entity_lower}Service.getById(entityId, testUserId);

        // Then
        assertNotNull(result);
        assertEquals(entityId, result.getId());
        verify({entity_lower}Mapper).selectById(entityId);
    }

    @Test
    @DisplayName("当ID为空时应该抛出异常")
    void shouldThrowExceptionWhenIdIsNull() {
        // When & Then
        assertThrows(BusinessException.class, () -> {
            {entity_lower}Service.getById(null, testUserId);
        });
        
        verify({entity_lower}Mapper, never()).selectById(any());
    }

    @Test
    @DisplayName("当{entity_description}不存在时应该抛出异常")
    void shouldThrowExceptionWhen{entity}NotFound() {
        // Given
        Long entityId = 999L;
        when({entity_lower}Mapper.selectById(entityId)).thenReturn(null);

        // When & Then
        assertThrows(BusinessException.class, () -> {
            {entity_lower}Service.getById(entityId, testUserId);
        });
        
        verify({entity_lower}Mapper).selectById(entityId);
    }

    @Test
    @DisplayName("应该成功删除{entity_description}")
    void shouldDeleteSuccessfully() {
        // Given
        Long entityId = 1L;
        when({entity_lower}Mapper.selectById(entityId)).thenReturn(mockEntity);

        // When
        {entity_lower}Service.delete(entityId, testUserId);

        // Then
        verify({entity_lower}Mapper).selectById(entityId);
        verify({entity_lower}Mapper).deleteById(entityId);
    }
}
