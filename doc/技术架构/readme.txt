服务名称	核心关注点 (它只关心什么？)	拥有的核心数据 (它的“地盘”)	主要交互的服务 (它的“邻居”)
gxz-user	C端用户的 身份与属性	tb_user (用户), tb_user_address (地址)	基本不主动调用其他服务
gxz-merchant	B端商家的 资质与店铺信息	tb_merchant (商户)	基本不主动调用其他服务
gxz-goods	所有 “可被售卖的物品” 的定义	tb_product (商品/菜品), 分类, 规格	基本不主动调用其他服务
gxz-order	一笔 “交易” 的完整生命周期	tb_order (订单), tb_order_status_log (订单日志)	gxz-goods, gxz-payment, gxz-delivery, gxz-promotion
gxz-payment	资金的进出与记录	tb_payment_transaction (支付流水), tb_refund_transaction (退款流水)	调用第三方支付API
gxz-delivery	外卖订单的 物理交付过程	tb_fulfillment (履约单), tb_rider (骑手信息)	gxz-order (获取订单信息), 调用腾讯地图API
gxz-promotion	如何让交易 “变得更便宜”	tb_coupon (优惠券), tb_coupon_usage (使用记录)	基本不主动调用其他服务
gxz-content	非交易性内容 (如团购、公告)	tb_groupbuy (团购商品), tb_voucher (核销码)	基本不主动调用其他服务
gxz-wallet	商家和骑手的 “钱包”	tb_wallet (钱包), tb_wallet_log (流水)	基本不主动调用其他服务
gxz-settlement	交易完成后的 资金清分规则	tb_settlement_log (清分记录)	gxz-order, gxz-wallet
gxz-admin	后台管理权限与功能聚合	tb_admin_user, tb_role, tb_permission	调用所有其他服务
gxz-gateway	所有请求的 “安全门卫”	N/A	调用所有其他服务