package org.example.xhc.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.example.xhc.entity.Order;

@Mapper
public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order row);

    Order selectByPrimaryKey(Long id);

    List<Order> selectAll();

    int updateByPrimaryKey(Order row);
}