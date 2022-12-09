/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

create table sms_coupon
(
    id            bigint auto_increment primary key,
    type          int(1) null comment '优惠券类型；0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券',
    name          varchar(100) null,
    platform      int(1) null comment '使用平台：0->全部；1->移动；2->PC',
    count         int null comment '数量',
    amount        decimal(10, 2) null comment '金额',
    per_limit     int null comment '每人限领张数',
    min_point     decimal(10, 2) null comment '使用门槛；0表示无门槛',
    start_time    datetime null,
    end_time      datetime null,
    use_type      int(1) null comment '使用类型：0->全场通用；1->指定分类；2->指定商品',
    note          varchar(200) null comment '备注',
    publish_count int null comment '发行数量',
    use_count     int null comment '已使用数量',
    receive_count int null comment '领取数量',
    enable_time   datetime null comment '可以领取的日期',
    code          varchar(64) null comment '优惠码',
    member_level  int(1) null comment '可领取的会员类型：0->无限时'
) comment '优惠券表' charset = utf8mb3;

