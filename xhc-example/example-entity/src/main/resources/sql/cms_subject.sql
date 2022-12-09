/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

create table cms_subject
(
    id               bigint auto_increment primary key,
    category_id      bigint null,
    title            varchar(100) null,
    pic              varchar(500) null comment '专题主图',
    product_count    int null comment '关联产品数量',
    recommend_status int(1) null,
    create_time      datetime null,
    collect_count    int null,
    read_count       int null,
    comment_count    int null,
    album_pics       varchar(1000) null comment '画册图片用逗号分割',
    description      varchar(1000) null,
    show_status      int(1) null comment '显示状态：0->不显示；1->显示',
    content          text null,
    forward_count    int null comment '转发数',
    category_name    varchar(200) null comment '专题分类名称'
) comment '专题表' charset = utf8mb3;

