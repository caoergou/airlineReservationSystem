# airlineReservationSystem
2020分布式课程实验，基于RESTful架构的航班订票网站
- 前端实现：JavaScript（要实现Ajax） *ZH负责*
- 后端实现：Java Spring Boot 框架 + restcontroller *ZY负责* 
- 接口设计: 定义主网站、银行、机票商的相关接口 *CFG负责*
- 数据库设计（包括DAO设计）： *DLY* 
- 服务器部署：负责项目整体部署 *WH负责*


# 项目流程


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

## 模块设计
API：https://app.swaggerhub.com/apis-docs/homicideGroup/flightReservationSystem/1.0.0