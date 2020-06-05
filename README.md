# airlineReservationSystem
2020分布式课程实验，基于RESTful架构的航班订票网站
- 前端实现：JavaScript（要实现Ajax） *ZH负责*
- 后端实现：Java Spring Boot 框架 + restcontroller *ZY负责* 
- 接口设计: 定义主网站、银行、机票商的相关接口 *CFG负责*
- 数据库设计（包括DAO设计）： *DLY* 
- 服务器部署：负责项目整体部署 *WH负责*

# 项目流程

![img](https://cdn.nlark.com/yuque/__mermaid_v3/45d934a7278fb9cda8022b18bd6bcd2c.svg)


``` mermaid
gantt 
title 2020分布式课程实验二
dateFormat MM-DD  
section 基于RESTful架构的航班订票系统
接口设计 :active,des1,05-28,5d
前端实现 :des2,06-01,10d
数据库设计 :des3,after des1,7d
后端实现 :des4,06-03,10d
服务器部署 :des5,after des4,5d
```


# 项目流程
## 时序图

![img](https://cdn.nlark.com/yuque/__mermaid_v3/12a4c8980955e70ed2ae8c40aa043e64.svg)

``` mermaid
sequenceDiagram
participant 用户
participant 浏览器
participant 到哪儿Web服务器
participant Airline航空公司
participant Payment支付中心
activate Airline航空公司
activate Payment支付中心
activate 用户

用户->>+浏览器:打开浏览器
浏览器->>+到哪儿Web服务器:访问到哪儿Web服务器
到哪儿Web服务器-->>浏览器:网站主页
浏览器-->>用户:网站主页

用户->>浏览器:查询航班信息
浏览器->>到哪儿Web服务器:findByPeriodRoute 搜索航班 
到哪儿Web服务器->>+Airline航空公司:findByRoute 查找航班
Airline航空公司-->>到哪儿Web服务器:航班信息
到哪儿Web服务器-->>浏览器:航班信息
浏览器-->>用户:返回航班信息

用户->>浏览器:订票
浏览器->>到哪儿Web服务器:order 用户预定航班
到哪儿Web服务器->>+Payment支付中心:Payment 创建支付订单
Payment支付中心-->>到哪儿Web服务器:订单号
到哪儿Web服务器-->>浏览器:跳转到支付界面
浏览器-->>浏览器:弹窗产生支付界面
浏览器->>Payment支付中心:账户当前状态
Payment支付中心-->>浏览器:账户当前状态
浏览器-->>用户:账户当前状态

用户->>浏览器:确定支付
浏览器->>Payment支付中心:payment 用户支付请求
Payment支付中心-->>浏览器:用户支付结果
浏览器-->>用户:支付结果
浏览器-->>浏览器:跳转到航班预定界面
用户->>浏览器:选择支付结果
alt 用户选择支付成功
浏览器->>到哪儿Web服务器:支付成功
到哪儿Web服务器->>Payment支付中心:verifyOrder 验证支付订单信息
Payment支付中心-->>到哪儿Web服务器:订单支付信息
alt 订单已支付
到哪儿Web服务器->>Airline航空公司:order/{flight} 订票
Airline航空公司-->>到哪儿Web服务器:订票结果
到哪儿Web服务器-->>浏览器:订票结果
浏览器-->>用户:订票结果
else 订单未支付
到哪儿Web服务器-->>浏览器:订单未支付
浏览器-->>用户:订单未支付，订票失败
end
else 用户选择支付失败
浏览器-->>-用户:订单支付失败，支付失败
end

deactivate 到哪儿Web服务器
deactivate 用户
```


## 模块设计
API：https://app.swaggerhub.com/apis-docs/homicideGroup/flightReservationSystem/1.0.0

原型设计：https://fbp2kq.axshare.com
(国内可能访问极其慢)




