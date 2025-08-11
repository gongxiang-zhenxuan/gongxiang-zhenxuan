# GXZ Content - 共享臻选内容管理服务

## 概述

`gxz-content` 是共享臻选微服务架构中的内容管理服务，负责处理文章管理、广告投放、轮播图管理、富文本编辑、媒体资源管理、内容审核等核心内容相关功能。

## 主要功能

### 📝 文章管理
- **文章发布**: 支持富文本编辑和多媒体内容
- **分类管理**: 文章分类和标签体系
- **内容审核**: 自动和人工内容审核机制
- **版本控制**: 文章版本管理和历史记录

### 🎯 广告管理
- **广告位管理**: 多种广告位类型和尺寸
- **广告投放**: 精准投放和定向推送
- **效果统计**: 广告点击率和转化率统计
- **A/B测试**: 广告创意A/B测试功能

### 🖼️ 媒体管理
- **图片上传**: 支持多种格式图片上传
- **视频管理**: 视频上传、转码和播放
- **文件存储**: 分布式文件存储和CDN加速
- **水印处理**: 图片和视频水印添加

### 🔍 内容搜索
- **全文搜索**: 基于Elasticsearch的全文搜索
- **智能推荐**: 基于用户行为的内容推荐
- **热门内容**: 热门文章和话题统计
- **相关推荐**: 相关内容智能推荐

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和会话存储
- **Elasticsearch**: 搜索引擎
- **MinIO**: 对象存储
- **FFmpeg**: 视频处理
- **RocketMQ**: 消息队列
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-content/
├── content-api/                 # 内容服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/content/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── content-provider/            # 内容服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/content/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── search/         # 搜索相关
│   │       ├── upload/         # 文件上传
│   │       ├── audit/          # 内容审核
│   │       ├── recommend/      # 推荐算法
│   │       └── ContentApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 文章管理
```java
// 创建文章
ArticleCreateDTO articleDTO = new ArticleCreateDTO();
articleDTO.setTitle("共享臻选平台介绍");
articleDTO.setContent("<p>这是一篇关于共享臻选平台的介绍文章...</p>");
articleDTO.setSummary("平台介绍摘要");
articleDTO.setCategoryId(1L);
articleDTO.setTags(Arrays.asList("平台", "介绍"));
articleDTO.setCoverImage("https://example.com/cover.jpg");
Result<ArticleVO> result = articleService.createArticle(articleDTO);

// 发布文章
ArticlePublishDTO publishDTO = new ArticlePublishDTO();
publishDTO.setArticleId(1L);
publishDTO.setPublishTime(LocalDateTime.now());
Result<Void> publishResult = articleService.publishArticle(publishDTO);
```

### 广告管理
```java
// 创建广告
AdvertisementCreateDTO adDTO = new AdvertisementCreateDTO();
adDTO.setAdName("首页轮播广告");
adDTO.setAdType(AdType.BANNER);
adDTO.setAdPosition("home_banner");
adDTO.setImageUrl("https://example.com/ad.jpg");
adDTO.setLinkUrl("https://example.com/product/123");
adDTO.setStartTime(LocalDateTime.now());
adDTO.setEndTime(LocalDateTime.now().plusDays(30));
Result<AdvertisementVO> adResult = advertisementService.createAd(adDTO);

// 记录广告点击
AdClickDTO clickDTO = new AdClickDTO();
clickDTO.setAdId(1L);
clickDTO.setUserId(1L);
clickDTO.setClickTime(LocalDateTime.now());
clickDTO.setUserAgent("Mozilla/5.0...");
clickDTO.setIpAddress("192.168.1.1");
Result<Void> clickResult = advertisementService.recordClick(clickDTO);
```

### 媒体上传
```java
// 上传图片
@PostMapping("/upload/image")
public Result<MediaVO> uploadImage(@RequestParam("file") MultipartFile file) {
    MediaUploadDTO uploadDTO = new MediaUploadDTO();
    uploadDTO.setFile(file);
    uploadDTO.setMediaType(MediaType.IMAGE);
    uploadDTO.setUserId(getCurrentUserId());
    
    return mediaService.uploadMedia(uploadDTO);
}

// 上传视频
@PostMapping("/upload/video")
public Result<MediaVO> uploadVideo(@RequestParam("file") MultipartFile file) {
    MediaUploadDTO uploadDTO = new MediaUploadDTO();
    uploadDTO.setFile(file);
    uploadDTO.setMediaType(MediaType.VIDEO);
    uploadDTO.setUserId(getCurrentUserId());
    
    return mediaService.uploadMedia(uploadDTO);
}
```

## API 接口

### 文章管理接口
- `POST /api/content/article/create` - 创建文章
- `GET /api/content/article/{id}` - 获取文章详情
- `PUT /api/content/article/{id}` - 更新文章
- `DELETE /api/content/article/{id}` - 删除文章
- `POST /api/content/article/{id}/publish` - 发布文章
- `GET /api/content/article/list` - 获取文章列表
- `GET /api/content/article/search` - 搜索文章

### 分类管理接口
- `POST /api/content/category/create` - 创建分类
- `GET /api/content/category/{id}` - 获取分类详情
- `PUT /api/content/category/{id}` - 更新分类
- `DELETE /api/content/category/{id}` - 删除分类
- `GET /api/content/category/tree` - 获取分类树

### 广告管理接口
- `POST /api/content/ad/create` - 创建广告
- `GET /api/content/ad/{id}` - 获取广告详情
- `PUT /api/content/ad/{id}` - 更新广告
- `DELETE /api/content/ad/{id}` - 删除广告
- `GET /api/content/ad/position/{position}` - 获取指定位置广告
- `POST /api/content/ad/{id}/click` - 记录广告点击

### 媒体管理接口
- `POST /api/content/media/upload/image` - 上传图片
- `POST /api/content/media/upload/video` - 上传视频
- `GET /api/content/media/{id}` - 获取媒体信息
- `DELETE /api/content/media/{id}` - 删除媒体文件
- `GET /api/content/media/list` - 获取媒体列表

### 内容搜索接口
- `GET /api/content/search` - 全文搜索
- `GET /api/content/recommend/{userId}` - 个性化推荐
- `GET /api/content/hot` - 热门内容
- `GET /api/content/related/{articleId}` - 相关推荐

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_content?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis 配置
```yaml
spring:
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: 7
```

### Elasticsearch 配置
```yaml
spring:
  elasticsearch:
    uris: ${ES_URIS:http://localhost:9200}
    username: ${ES_USERNAME:}
    password: ${ES_PASSWORD:}
```

### MinIO 配置
```yaml
minio:
  endpoint: ${MINIO_ENDPOINT:http://localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin}
  bucket-name: ${MINIO_BUCKET:gxz-content}
```

### 内容配置
```yaml
content:
  upload:
    max-file-size: 100MB  # 最大文件大小
    allowed-image-types: jpg,jpeg,png,gif,webp  # 允许的图片类型
    allowed-video-types: mp4,avi,mov,wmv  # 允许的视频类型
  audit:
    auto-audit: true  # 自动审核
    sensitive-words-check: true  # 敏感词检查
  search:
    index-name: gxz_content  # ES索引名称
    max-result-size: 1000  # 最大搜索结果数
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis elasticsearch minio

# 启动内容服务
cd content-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar content-provider/target/content-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 文章表 (t_article)
```sql
CREATE TABLE t_article (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    article_code VARCHAR(32) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    summary VARCHAR(500),
    content LONGTEXT,
    cover_image VARCHAR(500),
    category_id BIGINT,
    author_id BIGINT NOT NULL,
    author_name VARCHAR(50),
    tags VARCHAR(200),
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    share_count INT DEFAULT 0,
    status TINYINT DEFAULT 0,
    is_top TINYINT DEFAULT 0,
    is_hot TINYINT DEFAULT 0,
    publish_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 分类表 (t_category)
```sql
CREATE TABLE t_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    category_code VARCHAR(32) UNIQUE NOT NULL,
    parent_id BIGINT DEFAULT 0,
    level TINYINT DEFAULT 1,
    sort_order INT DEFAULT 0,
    icon VARCHAR(255),
    description VARCHAR(200),
    article_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 广告表 (t_advertisement)
```sql
CREATE TABLE t_advertisement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ad_code VARCHAR(32) UNIQUE NOT NULL,
    ad_name VARCHAR(100) NOT NULL,
    ad_type TINYINT NOT NULL,
    ad_position VARCHAR(50) NOT NULL,
    title VARCHAR(100),
    description VARCHAR(200),
    image_url VARCHAR(500),
    link_url VARCHAR(500),
    target_type TINYINT DEFAULT 1,
    sort_order INT DEFAULT 0,
    click_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    start_time DATETIME,
    end_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 媒体文件表 (t_media)
```sql
CREATE TABLE t_media (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    media_code VARCHAR(32) UNIQUE NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    media_type TINYINT NOT NULL,
    mime_type VARCHAR(100),
    width INT,
    height INT,
    duration INT,
    thumbnail_url VARCHAR(500),
    upload_user_id BIGINT,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 内容审核表 (t_content_audit)
```sql
CREATE TABLE t_content_audit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content_id BIGINT NOT NULL,
    content_type TINYINT NOT NULL,
    audit_type TINYINT NOT NULL,
    audit_status TINYINT DEFAULT 0,
    audit_result TEXT,
    audit_score DECIMAL(5,2),
    auditor_id BIGINT,
    auditor_name VARCHAR(50),
    audit_time DATETIME,
    reject_reason VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 业务流程

### 文章发布流程
1. **内容创建**: 用户创建文章内容
2. **内容审核**: 自动或人工审核内容
3. **审核通过**: 审核通过后可发布
4. **内容发布**: 发布文章到平台
5. **索引更新**: 更新搜索引擎索引
6. **缓存刷新**: 刷新相关缓存

### 媒体上传流程
1. **文件上传**: 用户上传媒体文件
2. **格式检查**: 检查文件格式和大小
3. **病毒扫描**: 扫描文件安全性
4. **文件存储**: 存储到对象存储
5. **缩略图生成**: 生成缩略图
6. **CDN分发**: 分发到CDN节点

### 内容审核流程
1. **内容提交**: 提交待审核内容
2. **自动审核**: AI自动审核
3. **人工审核**: 人工复审
4. **审核结果**: 通过/拒绝/修改
5. **结果通知**: 通知内容创建者

## 搜索引擎集成

### Elasticsearch 配置
```java
@Configuration
public class ElasticsearchConfig {
    
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return ElasticsearchClients.createSimple(
            HttpHost.create(elasticsearchUrl)
        );
    }
}
```

### 文章索引映射
```json
{
  "mappings": {
    "properties": {
      "id": {"type": "long"},
      "title": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart"
      },
      "content": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart"
      },
      "summary": {
        "type": "text",
        "analyzer": "ik_max_word"
      },
      "tags": {"type": "keyword"},
      "categoryId": {"type": "long"},
      "authorId": {"type": "long"},
      "publishTime": {"type": "date"},
      "viewCount": {"type": "integer"},
      "likeCount": {"type": "integer"}
    }
  }
}
```

### 搜索服务实现
```java
@Service
public class ContentSearchService {
    
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    
    public SearchResult<ArticleVO> searchArticles(SearchRequest request) {
        try {
            SearchResponse<ArticleDocument> response = elasticsearchClient.search(
                SearchRequest.of(s -> s
                    .index("gxz_content")
                    .query(q -> q
                        .multiMatch(m -> m
                            .query(request.getKeyword())
                            .fields("title^2", "content", "summary")
                        )
                    )
                    .from(request.getOffset())
                    .size(request.getSize())
                    .sort(SortOptions.of(so -> so
                        .field(FieldSort.of(f -> f
                            .field("publishTime")
                            .order(SortOrder.Desc)
                        ))
                    ))
                ),
                ArticleDocument.class
            );
            
            return convertToSearchResult(response);
        } catch (Exception e) {
            log.error("搜索文章失败", e);
            throw new BusinessException("搜索失败");
        }
    }
}
```

## 内容推荐算法

### 协同过滤推荐
```java
@Service
public class ContentRecommendService {
    
    public List<ArticleVO> recommendByCollaborativeFiltering(Long userId) {
        // 获取用户行为数据
        List<UserBehavior> userBehaviors = getUserBehaviors(userId);
        
        // 计算用户相似度
        Map<Long, Double> userSimilarities = calculateUserSimilarities(userId, userBehaviors);
        
        // 获取相似用户喜欢的内容
        List<Long> recommendArticleIds = getSimilarUserPreferences(userSimilarities);
        
        // 过滤已读内容
        recommendArticleIds = filterReadArticles(userId, recommendArticleIds);
        
        // 返回推荐文章
        return articleService.getArticlesByIds(recommendArticleIds);
    }
    
    public List<ArticleVO> recommendByContentBased(Long userId) {
        // 基于内容的推荐算法
        List<Long> userInterestCategories = getUserInterestCategories(userId);
        List<String> userInterestTags = getUserInterestTags(userId);
        
        return articleService.findSimilarArticles(userInterestCategories, userInterestTags);
    }
}
```

### 热门内容算法
```java
@Service
public class HotContentService {
    
    public List<ArticleVO> getHotArticles(int limit) {
        // 热度计算公式：权重 = 浏览量 * 0.3 + 点赞量 * 0.4 + 评论量 * 0.2 + 分享量 * 0.1
        String sql = """
            SELECT *, 
                   (view_count * 0.3 + like_count * 0.4 + comment_count * 0.2 + share_count * 0.1) as hot_score
            FROM t_article 
            WHERE status = 1 
              AND publish_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)
            ORDER BY hot_score DESC 
            LIMIT ?
            """;
        
        return articleMapper.selectHotArticles(sql, limit);
    }
}
```

## 内容审核系统

### 敏感词过滤
```java
@Service
public class SensitiveWordFilter {
    
    private final Set<String> sensitiveWords = new HashSet<>();
    
    @PostConstruct
    public void init() {
        // 加载敏感词库
        loadSensitiveWords();
    }
    
    public boolean containsSensitiveWord(String content) {
        for (String word : sensitiveWords) {
            if (content.contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    public String replaceSensitiveWords(String content) {
        for (String word : sensitiveWords) {
            content = content.replace(word, "***");
        }
        return content;
    }
}
```

### 自动审核服务
```java
@Service
public class AutoAuditService {
    
    @Autowired
    private SensitiveWordFilter sensitiveWordFilter;
    
    public AuditResult auditContent(String content) {
        AuditResult result = new AuditResult();
        
        // 敏感词检查
        if (sensitiveWordFilter.containsSensitiveWord(content)) {
            result.setStatus(AuditStatus.REJECTED);
            result.setReason("包含敏感词");
            return result;
        }
        
        // 内容长度检查
        if (content.length() < 10) {
            result.setStatus(AuditStatus.REJECTED);
            result.setReason("内容过短");
            return result;
        }
        
        // 其他规则检查...
        
        result.setStatus(AuditStatus.APPROVED);
        return result;
    }
}
```

## 性能优化

### 缓存策略
- 热门文章缓存
- 分类数据缓存
- 搜索结果缓存
- 媒体文件CDN缓存

### 数据库优化
- 文章表分表策略
- 索引优化
- 读写分离
- 慢查询优化

### 搜索优化
- 索引分片策略
- 搜索结果缓存
- 热词预加载
- 搜索建议优化

## 安全考虑

- 🔒 文件上传安全检查
- 🔒 内容XSS防护
- 🔒 敏感信息过滤
- 🔒 访问权限控制
- 🔒 操作日志审计

## 故障排查

### 常见问题
1. **文件上传失败**: 检查存储服务连接和权限
2. **搜索功能异常**: 检查Elasticsearch服务状态
3. **内容审核延迟**: 检查审核队列和处理能力
4. **推荐结果不准确**: 检查用户行为数据和算法参数

### 日志查看
```bash
# 查看应用日志
tail -f logs/content-provider.log

# 查看上传日志
grep "UPLOAD" logs/content-provider.log

# 查看搜索日志
grep "SEARCH" logs/content-provider.log

# 查看审核日志
grep "AUDIT" logs/content-provider.log

# 查看错误日志
grep "ERROR" logs/content-provider.log
```

## 开发指南

### 添加新的内容类型
1. 定义内容类型枚举
2. 创建对应的实体类
3. 实现内容处理逻辑
4. 配置审核规则
5. 更新搜索索引

### 扩展推荐算法
1. 实现推荐算法接口
2. 配置算法参数
3. 添加A/B测试
4. 监控推荐效果
5. 优化算法性能

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x
- **Spring Cloud 版本**: 2023.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年