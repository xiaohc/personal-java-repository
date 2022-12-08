/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.dao;

import org.example.xhc.entity.PmsProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品信息(PmsProduct)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-07 17:32:38
 */
public interface PmsProductDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PmsProduct queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param pmsProduct 查询条件
     * @param pageable   分页对象
     * @return 对象列表
     */
    List<PmsProduct> queryAllByLimit(PmsProduct pmsProduct, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param pmsProduct 查询条件
     * @return 总行数
     */
    long count(PmsProduct pmsProduct);

    /**
     * 新增数据
     *
     * @param pmsProduct 实例对象
     * @return 影响行数
     */
    int insert(PmsProduct pmsProduct);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PmsProduct> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PmsProduct> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PmsProduct> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PmsProduct> entities);

    /**
     * 修改数据
     *
     * @param pmsProduct 实例对象
     * @return 影响行数
     */
    int update(PmsProduct pmsProduct);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

