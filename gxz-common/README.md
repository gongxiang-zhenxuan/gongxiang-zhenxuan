# GXZ Common - 共享臻选公共模块

## 概述

`gxz-common` 是共享臻选微服务架构中的公共基础模块，提供了各个微服务共同使用的基础组件、工具类、常量定义、异常处理等通用功能。

## 主要功能

### 🔧 基础组件
- **统一响应格式**: 标准化的 API 响应结构
- **异常处理**: 全局异常处理和自定义异常类
- **工具类**: 常用的工具方法集合
- **常量定义**: 系统级常量和枚举

### 📦 核心特性
- **配置管理**: 统一的配置类和属性定义
- **数据验证**: 通用的数据校验注解和规则
- **日期处理**: 日期时间相关的工具方法
- **字符串处理**: 字符串操作的常用方法
- **加密解密**: 安全相关的加密工具

## 技术栈

- **Java**: 8
- **Spring Boot**: 2.7.x
- **Spring Cloud**: 2021.x
- **Maven**: 项目构建工具
- **Lombok**: 简化代码编写
- **Jackson**: JSON 序列化/反序列化
- **Hutool**: Java 工具类库

## 项目结构

```
gxz-common/
├── src/main/java/com/gongxiang/zhenxuan/common/
│   ├── annotation/          # 自定义注解
│   ├── config/             # 配置类
│   ├── constant/           # 常量定义
│   ├── enums/              # 枚举类
│   ├── exception/          # 异常类
│   ├── model/              # 通用模型
│   │   ├── dto/           # 数据传输对象
│   │   ├── entity/        # 实体类基类
│   │   └── vo/            # 视图对象
│   ├── response/           # 响应格式
│   └── utils/              # 工具类
├── src/main/resources/
│   └── META-INF/
│       └── spring.factories # Spring 自动配置
└── pom.xml
```

## 核心组件说明

### 统一响应格式
```java
// 成功响应
Result.success(data);

// 失败响应
Result.error("错误信息");

// 分页响应
PageResult.success(list, total);
```

### 异常处理
```java
// 业务异常
throw new BusinessException("业务处理失败");

// 参数异常
throw new ParamException("参数不能为空");
```

### 工具类使用
```java
// 字符串工具
StringUtils.isEmpty(str);

// 日期工具
DateUtils.format(date, "yyyy-MM-dd");

// 加密工具
EncryptUtils.md5(password);
```

## 使用方式

### Maven 依赖
```xml
<dependency>
    <groupId>com.gongxiang.zhenxuan</groupId>
    <artifactId>gxz-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 自动配置
该模块支持 Spring Boot 自动配置，引入依赖后即可直接使用。

## 开发指南

### 添加新的工具类
1. 在 `utils` 包下创建工具类
2. 使用静态方法提供功能
3. 添加完整的 JavaDoc 注释
4. 编写单元测试

### 添加新的异常类
1. 继承 `BaseException` 基类
2. 定义错误码和错误信息
3. 提供多种构造方法

### 添加新的常量
1. 在 `constant` 包下创建常量类
2. 使用 `public static final` 定义
3. 按功能模块分组

## 注意事项

- ⚠️ 该模块被所有其他模块依赖，修改时需谨慎
- ⚠️ 不要在此模块中引入业务相关的依赖
- ⚠️ 保持向后兼容性，避免破坏性变更
- ⚠️ 新增功能需要充分测试

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年