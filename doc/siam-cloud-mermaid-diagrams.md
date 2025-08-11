# 🎨 暹罗外卖数据库 Mermaid 架构图

## 📊 主库架构图（水平布局）

### 🎯 分层架构视图

```mermaid
graph LR
    subgraph "核心业务域⭐"
        C["👥 用户域<br/>7张表"]
        F["📦 商品域<br/>7张表"]
        G["🛒 订单域<br/>6张表"]
        I["🎯 营销域<br/>6张表"]
    end

    subgraph "运营支撑域"
        D["🏪 商家域<br/>6张表"]
        E["🏬 门店域<br/>2张表"]
        H["🚚 配送域<br/>2张表"]
        K["💬 互动域<br/>3张表"]
    end

    subgraph "系统管理域"
        B["🔐 权限管理域<br/>3张表"]
        J["💳 会员充值域<br/>3张表"]
        L["📱 微信域<br/>1张表"]
        M["🖨️ 系统工具域<br/>6张表"]
    end

    subgraph "基础设施域"
        N["📧 消息通知域<br/>2张表"]
        O["⚙️ 系统配置域<br/>1张表"]
        P["🔄 事务管理域<br/>2张表"]
    end

    C --> C1["tb_member👑<br/>用户主表"]
    C --> C2["tb_member_billing_record💰<br/>资金流水"]
    
    F --> F1["tb_goods👑<br/>商品主表"]
    F --> F2["tb_goods_specification<br/>商品规格"]
    
    G --> G1["tb_order👑<br/>订单主表"]
    G --> G2["tb_order_detail<br/>订单明细"]
    
    I --> I1["tb_coupons👑<br/>优惠券主表"]
    I --> I2["tb_full_reduction_rule<br/>满减规则"]
    
    D --> D1["tb_merchant👑<br/>商家主表"]
    D --> D2["tb_merchant_billing_record💰<br/>商家流水"]
    
    E --> E1["tb_shop👑<br/>门店主表"]
    
    H --> H1["tb_courier<br/>骑手信息"]
    H --> H2["tb_delivery_address<br/>收货地址"]

    style C fill:#f3e5f5,color:#000
    style F fill:#f3e5f5,color:#000
    style G fill:#f3e5f5,color:#000
    style I fill:#f3e5f5,color:#000
    style C1 fill:#ffcdd2,color:#000
    style F1 fill:#ffcdd2,color:#000
    style G1 fill:#ffcdd2,color:#000
    style I1 fill:#ffcdd2,color:#000
    style D1 fill:#ffcdd2,color:#000
    style E1 fill:#ffcdd2,color:#000
```

### 🔗 详细展开视图

```mermaid
graph LR
    A["📊 siam_cloud<br/>主库 56张表"] --> B["🔐 权限管理域<br/>3张表"]
    A --> C["👥 用户域⭐<br/>7张表"]
    A --> D["🏪 商家域<br/>6张表"]
    A --> E["🏬 门店域<br/>2张表"]
    A --> F["📦 商品域⭐<br/>7张表"]
    A --> G["🛒 订单域⭐<br/>6张表"]
    A --> H["🚚 配送域<br/>2张表"]
    A --> I["🎯 营销域⭐<br/>6张表"]

    B --> B1["tb_admin👑"]
    B --> B2["tb_admin_token"]
    B --> B3["tb_menu"]

    C --> C1["tb_member👑"]
    C --> C2["tb_member_billing_record💰"]
    C --> C3["tb_member_token"]
    C --> C4["tb_member_goods_collect"]

    D --> D1["tb_merchant👑"]
    D --> D2["tb_merchant_billing_record💰"]
    D --> D3["tb_merchant_token"]
    D --> D4["tb_merchant_withdraw_record"]

    E --> E1["tb_shop👑"]
    E --> E2["tb_shop_change_record"]

    F --> F1["tb_goods👑"]
    F --> F2["tb_goods_specification"]
    F --> F3["tb_menu"]
    F --> F4["tb_rawmaterial"]

    G --> G1["tb_order👑"]
    G --> G2["tb_order_detail"]
    G --> G3["tb_shopping_cart"]
    G --> G4["tb_order_refund"]

    H --> H1["tb_courier"]
    H --> H2["tb_delivery_address"]

    I --> I1["tb_coupons👑"]
    I --> I2["tb_full_reduction_rule"]
    I --> I3["tb_advertisement"]
    I --> I4["tb_coupons_member_relation"]

    style A fill:#e1f5fe,color:#000
    style C fill:#f3e5f5,color:#000
    style F fill:#f3e5f5,color:#000
    style G fill:#f3e5f5,color:#000
    style I fill:#f3e5f5,color:#000
    style C1 fill:#ffcdd2,color:#000
    style D1 fill:#ffcdd2,color:#000
    style E1 fill:#ffcdd2,color:#000
    style F1 fill:#ffcdd2,color:#000
    style G1 fill:#ffcdd2,color:#000
    style I1 fill:#ffcdd2,color:#000
```

## 🛒 积分商城库架构图

```mermaid
graph TD
    A["📱 siam_cloud_mall<br/>积分商城库 17张表"] --> B["🏪 积分商品域<br/>6张表"]
    A --> C["🛒 积分订单域<br/>4张表"]
    A --> D["💰 积分营销域<br/>4张表"]
    A --> E["🔄 积分售后域<br/>3张表"]

    B --> B1["tb_points_mall_goods👑<br/>积分商品主表"]
    B --> B2["tb_points_mall_goods_specification<br/>积分商品规格"]
    B --> B3["tb_points_mall_menu<br/>积分商城分类"]
    B --> B4["tb_points_mall_*<br/>其他积分商品表"]

    C --> C1["tb_points_mall_order👑<br/>积分订单主表"]
    C --> C2["tb_points_mall_order_detail<br/>积分订单明细"]
    C --> C3["tb_points_mall_order_logistics<br/>积分订单物流"]
    C --> C4["tb_points_mall_shopping_cart<br/>积分购物车"]

    D --> D1["tb_points_mall_coupons<br/>积分优惠券"]
    D --> D2["tb_points_mall_coupons_*<br/>积分券关联表"]
    D --> D3["tb_points_mall_full_reduction_rule<br/>积分满减规则"]

    E --> E1["tb_points_mall_order_refund<br/>积分退款"]
    E --> E2["tb_points_mall_order_refund_goods<br/>积分退款商品"]
    E --> E3["tb_points_mall_order_refund_process<br/>积分退款流程"]

    style A fill:#e8f5e8
    style B1 fill:#ffcdd2
    style C1 fill:#ffcdd2
```

## 🔄 核心业务流程图

```mermaid
flowchart LR
    A["👤 用户登录<br/>tb_member"] --> B["🛍️ 浏览商品<br/>tb_goods<br/>tb_menu"]
    B --> C["🛒 加入购物车<br/>tb_shopping_cart"]
    C --> D["📝 创建订单<br/>tb_order<br/>tb_order_detail"]
    D --> E["💳 使用优惠<br/>tb_coupons<br/>tb_full_reduction_rule"]
    E --> F["💰 支付成功<br/>tb_member_billing_record"]
    F --> G["🏪 商家接单<br/>tb_merchant<br/>tb_shop"]
    G --> H["🚚 骑手配送<br/>tb_courier<br/>tb_delivery_address"]
    H --> I["✅ 完成订单<br/>tb_order status=completed"]
    I --> J["⭐ 用户评价<br/>tb_appraise<br/>tb_give_like"]

    style A fill:#e3f2fd
    style D fill:#fff3e0
    style F fill:#f1f8e9
    style I fill:#fce4ec
    style J fill:#f3e5f5
```

## 🗄️ 核心表关系图 (ER图)

```mermaid
erDiagram
    tb_member ||--o{ tb_order : "下单"
    tb_member ||--o{ tb_member_billing_record : "资金流水"
    tb_member ||--o{ tb_shopping_cart : "购物车"
    tb_member ||--o{ tb_delivery_address : "收货地址"

    tb_merchant ||--o{ tb_shop : "拥有门店"
    tb_shop ||--o{ tb_goods : "商品归属"
    tb_shop ||--o{ tb_order : "接收订单"

    tb_order ||--o{ tb_order_detail : "订单明细"
    tb_order ||--o{ tb_order_refund : "退款记录"
    tb_goods ||--o{ tb_order_detail : "商品明细"
    tb_goods ||--o{ tb_shopping_cart : "购物车商品"

    tb_coupons ||--o{ tb_coupons_member_relation : "用户领券"
    tb_coupons ||--o{ tb_coupons_shop_relation : "适用门店"
    tb_coupons ||--o{ tb_coupons_goods_relation : "适用商品"

    tb_courier ||--o{ tb_order : "配送订单"

    tb_member {
        int member_id PK "用户ID"
        string mobile "手机号"
        decimal balance "余额"
        int points "积分"
        int vip_status "VIP状态"
    }

    tb_order {
        int order_id PK "订单ID"
        string order_no "订单号"
        int member_id FK "用户ID"
        int shop_id FK "门店ID"
        decimal actual_price "实付金额"
        int status "订单状态"
    }

    tb_goods {
        int goods_id PK "商品ID"
        int shop_id FK "门店ID"
        string name "商品名称"
        decimal price "价格"
        int stock "库存"
        int status "状态"
    }

    tb_shop {
        int shop_id PK "门店ID"
        int merchant_id FK "商家ID"
        string shop_name "门店名称"
        decimal longitude "经度"
        decimal latitude "纬度"
        int audit_status "审核状态"
    }
```

## 📊 数据流向图

```mermaid
graph LR
    A[用户注册] --> B[用户登录]
    B --> C[浏览商品]
    C --> D[加入购物车]
    D --> E[创建订单]
    E --> F[使用优惠券]
    F --> G[支付订单]
    G --> H[商家接单]
    H --> I[骑手取餐]
    I --> J[配送中]
    J --> K[订单完成]
    K --> L[用户评价]

    subgraph "用户域"
        A
        B
        L
    end

    subgraph "商品域"
        C
        D
    end

    subgraph "订单域"
        E
        G
        K
    end

    subgraph "营销域"
        F
    end

    subgraph "商家域"
        H
    end

    subgraph "配送域"
        I
        J
    end
```

---

## 🎯 图表说明

### 📋 符号说明
- **👑**: 主表/核心表
- **⭐**: 核心业务域
- **💰**: 涉及资金流的表
- **🔄**: 系统技术表

### 🎨 颜色说明
- **蓝色**: 主库根节点
- **紫色**: 核心业务域
- **红色**: 主表/重要表
- **绿色**: 积分商城库

### 📖 使用建议
1. **架构了解**: 先看主库架构图和积分商城库架构图
2. **业务理解**: 重点关注核心业务流程图
3. **开发参考**: 使用ER图了解表关系
4. **数据流向**: 通过数据流向图理解完整业务链路

---

*📝 这些图表可以直接在支持 Mermaid 的平台（如 GitHub、GitLab、Notion 等）中渲染显示*
