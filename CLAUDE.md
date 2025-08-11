# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **贡享臻选 (GongXiang ZhenXuan)**, a food delivery and e-commerce platform built with Spring Boot microservices architecture. The system supports:
- Food delivery (外卖) with merchant-rider-user workflow
- Group buying (团购) with voucher verification
- B2B procurement for merchants buying platform products
- Multi-client access (user app, merchant app, rider app, admin backend)

## Architecture

**Microservices Pattern**: 12 independent services coordinated through API Gateway
**Service Discovery**: Nacos (localhost:8848)
**API Gateway**: Spring Cloud Gateway (port 8080)
**Database**: Single MySQL database (MVP single-database architecture)
**Caching**: Redis
**JDK**: Java 8
**Framework**: Spring Boot 2.7.0, Spring Cloud 2021.0.3, Spring Cloud Alibaba 2021.1

## Build Commands

### Maven Project Structure
```bash
# Build entire project
mvn clean package

# Build specific service
cd gxz-user && mvn clean package

# Run specific service locally (after gateway is running)
cd gxz-user/user-provider && mvn spring-boot:run
```

### Service Management
```bash
# Start gateway (prerequisite for other services)
cd gxz-gateway
./start.sh start

# Check gateway status
./start.sh status

# Stop gateway
./start.sh stop

# View gateway logs
./start.sh logs
```

## Service Architecture

### Core Services (12 total)
- **gxz-gateway**: API Gateway (port 8080) - single entry point
- **gxz-user**: User management (C端用户)
- **gxz-merchant**: Merchant management (B端商户)
- **gxz-goods**: Product management (外卖菜品 + 平台自营商品)
- **gxz-order**: Order management (外卖 + 团购 + 采购订单)
- **gxz-payment**: Payment processing (微信支付)
- **gxz-promotion**: Marketing/coupons
- **gxz-delivery**: Delivery and rider dispatch
- **gxz-settlement**: Financial settlement
- **gxz-wallet**: Wallet management
- **gxz-content**: Content management
- **gxz-admin**: Admin backend operations

### Service Communication
- **External**: All clients → API Gateway → Services
- **Internal**: Services communicate via HTTP/REST with service names (e.g., `lb://gxz-user`)
- **Registration**: All services auto-register with Nacos on startup

### Gateway Routing
Gateway routes by path prefix:
- `/api/user/**` → gxz-user service
- `/api/merchant/**` → gxz-merchant service  
- `/api/goods/**` → gxz-goods service
- And so on for each service

## Business Domain Logic

### Order Types (核心业务类型)
1. **外卖订单 (takeaway)**: User orders from merchant, rider delivers
   - Flow: Order → Merchant accepts → Rider grabs → Delivery → Complete
2. **团购订单 (groupbuy)**: User buys vouchers, uses at merchant locations
   - Flow: Purchase → Get voucher code → Use at merchant → Verify/consume
3. **采购订单 (purchase)**: Merchant buys from platform (B2B)
   - Flow: Merchant orders → Platform ships → Logistics tracking → Complete

### Key Business Flows
- **Rider dispatch**: Location-based order assignment with Redis distributed locking
- **Payment**: WeChat Pay integration for all order types
- **Multi-role support**: Same merchant can be buyer (B2B orders) and seller (takeaway/groupbuy)

## Development Guidelines

### Adding New Features
1. Identify which service domain the feature belongs to
2. Follow the existing API-Provider module pattern
3. Update gateway routing if new endpoints are added
4. Consider impact on order state machine for order-related features

### Database Schema
- Uses unified order table for all order types with `order_type` discrimination
- Product table supports multiple product types (外卖菜品/平台自营商品/团购商品)
- Location-based queries for rider dispatch and merchant discovery

### Testing
- No specific test framework detected - check individual service READMEs
- Maven standard: `mvn test` for unit tests
- Integration testing likely requires Nacos + MySQL + Redis environment

## Dependencies

### Required Services for Development
- **Nacos Server**: localhost:8848 (service registry)
- **MySQL**: Database (check individual service configs)
- **Redis**: Caching and session storage

### Key Tech Stack
- Spring Boot 2.7.0 with Spring Cloud Gateway
- Spring Cloud Alibaba (Nacos integration)
- Apache Dubbo 3.0.9 (RPC framework)
- MyBatis Plus (likely for ORM, based on common patterns)

## Configuration Notes

- Services use Nacos for both service discovery and configuration management
- Environment-specific configs likely managed in Nacos Config Center
- Gateway handles CORS globally and request logging
- Each service follows gxz-{domain} naming convention consistently