package org.example.xhc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.xhc.entity.Order;

import java.util.List;

/**
 * oms_order
 * @author xiaohongchao
 * @since 1.0.0
 */
@Mapper
public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order row);

    Order selectByPrimaryKey(Long id);

    List<Order> selectAll();

    int updateByPrimaryKey(Order row);
}