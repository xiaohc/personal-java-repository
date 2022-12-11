package org.example.xhc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.xhc.entity.Order;

import java.util.List;

/**
 * oms_order
 * 
 * @author xiaohongchao
 * @since 1.0.0
 */
@Mapper
public interface OrderMapper {
    /**
     * 按主键删除数据
     * 
     * @param id 待删除数据的主键
     * @return 删除成功行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入数据
     * 
     * @param row 待插入数据
     * @return 插入成功行数
     */
    int insert(Order row);

    /**
     * 按主键查询数据
     * 
     * @param id 待查询数据的主键
     * @return 当前业务数据内容
     */
    Order selectByPrimaryKey(Long id);

    /**
     * 查询当前业务所有数据
     * 
     * @return 当前业务所有数据内容
     */
    List<Order> selectAll();

    /**
     * 按主键更新数据
     * 
     * @param row 待更新数据内容
     * @return 更新成功行数
     */
    int updateByPrimaryKey(Order row);
}