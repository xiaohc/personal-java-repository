package org.example.xhc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.xhc.entity.Coupon;

import java.util.List;

/**
 * sms_coupon
 * @author xiaohongchao
 * @since 1.0.0
 */
@Mapper
public interface CouponMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coupon row);

    Coupon selectByPrimaryKey(Long id);

    List<Coupon> selectAll();

    int updateByPrimaryKey(Coupon row);
}