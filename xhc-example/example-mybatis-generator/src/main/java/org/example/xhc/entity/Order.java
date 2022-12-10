package org.example.xhc.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table oms_order
 */
/**
* Created by Mybatis Generator on 2022/12/10
*/
@Data
public class Order implements Serializable {
    /**
     * Database Column Remarks:
     *   订单id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.member_id
     *
     * @mbg.generated
     */
    private Long memberId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.coupon_id
     *
     * @mbg.generated
     */
    private Long couponId;

    /**
     * Database Column Remarks:
     *   订单编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.order_sn
     *
     * @mbg.generated
     */
    private String orderSn;

    /**
     * Database Column Remarks:
     *   提交时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.create_time
     *
     * @mbg.generated
     */
    private LocalDateTime createDate;

    /**
     * Database Column Remarks:
     *   用户帐号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.member_username
     *
     * @mbg.generated
     */
    private String memberUsername;

    /**
     * Database Column Remarks:
     *   订单总金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.total_amount
     *
     * @mbg.generated
     */
    private BigDecimal totalAmount;

    /**
     * Database Column Remarks:
     *   应付金额（实际支付金额）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.pay_amount
     *
     * @mbg.generated
     */
    private BigDecimal payAmount;

    /**
     * Database Column Remarks:
     *   运费金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.freight_amount
     *
     * @mbg.generated
     */
    private BigDecimal freightAmount;

    /**
     * Database Column Remarks:
     *   促销优化金额（促销价、满减、阶梯价）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.promotion_amount
     *
     * @mbg.generated
     */
    private BigDecimal promotionAmount;

    /**
     * Database Column Remarks:
     *   积分抵扣金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.integration_amount
     *
     * @mbg.generated
     */
    private BigDecimal integrationAmount;

    /**
     * Database Column Remarks:
     *   优惠券抵扣金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.coupon_amount
     *
     * @mbg.generated
     */
    private BigDecimal couponAmount;

    /**
     * Database Column Remarks:
     *   管理员后台调整订单使用的折扣金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.discount_amount
     *
     * @mbg.generated
     */
    private BigDecimal discountAmount;

    /**
     * Database Column Remarks:
     *   支付方式：0->未支付；1->支付宝；2->微信
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.pay_type
     *
     * @mbg.generated
     */
    private Integer payType;

    /**
     * Database Column Remarks:
     *   订单来源：0->PC订单；1->app订单
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.source_type
     *
     * @mbg.generated
     */
    private Integer sourceType;

    /**
     * Database Column Remarks:
     *   订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * Database Column Remarks:
     *   订单类型：0->正常订单；1->秒杀订单
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.order_type
     *
     * @mbg.generated
     */
    private Integer orderType;

    /**
     * Database Column Remarks:
     *   物流公司(配送方式)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.delivery_company
     *
     * @mbg.generated
     */
    private String deliveryCompany;

    /**
     * Database Column Remarks:
     *   物流单号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.delivery_sn
     *
     * @mbg.generated
     */
    private String deliverySn;

    /**
     * Database Column Remarks:
     *   自动确认时间（天）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.auto_confirm_day
     *
     * @mbg.generated
     */
    private Integer autoConfirmDay;

    /**
     * Database Column Remarks:
     *   可以获得的积分
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.integration
     *
     * @mbg.generated
     */
    private Integer integration;

    /**
     * Database Column Remarks:
     *   可以活动的成长值
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.growth
     *
     * @mbg.generated
     */
    private Integer growth;

    /**
     * Database Column Remarks:
     *   活动信息
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.promotion_info
     *
     * @mbg.generated
     */
    private String promotionInfo;

    /**
     * Database Column Remarks:
     *   发票类型：0->不开发票；1->电子发票；2->纸质发票
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.bill_type
     *
     * @mbg.generated
     */
    private Integer billType;

    /**
     * Database Column Remarks:
     *   发票抬头
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.bill_header
     *
     * @mbg.generated
     */
    private String billHeader;

    /**
     * Database Column Remarks:
     *   发票内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.bill_content
     *
     * @mbg.generated
     */
    private String billContent;

    /**
     * Database Column Remarks:
     *   收票人电话
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.bill_receiver_phone
     *
     * @mbg.generated
     */
    private String billReceiverPhone;

    /**
     * Database Column Remarks:
     *   收票人邮箱
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.bill_receiver_email
     *
     * @mbg.generated
     */
    private String billReceiverEmail;

    /**
     * Database Column Remarks:
     *   收货人姓名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_name
     *
     * @mbg.generated
     */
    private String rcvName;

    /**
     * Database Column Remarks:
     *   收货人电话
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_phone
     *
     * @mbg.generated
     */
    private String rcvPhone;

    /**
     * Database Column Remarks:
     *   收货人邮编
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_post_code
     *
     * @mbg.generated
     */
    private String rcvPostCode;

    /**
     * Database Column Remarks:
     *   省份/直辖市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_province
     *
     * @mbg.generated
     */
    private String rcvProvince;

    /**
     * Database Column Remarks:
     *   城市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_city
     *
     * @mbg.generated
     */
    private String rcvCity;

    /**
     * Database Column Remarks:
     *   区
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_region
     *
     * @mbg.generated
     */
    private String rcvRegion;

    /**
     * Database Column Remarks:
     *   详细地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receiver_detail_address
     *
     * @mbg.generated
     */
    private String rcvDetailAddress;

    /**
     * Database Column Remarks:
     *   订单备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.note
     *
     * @mbg.generated
     */
    private String note;

    /**
     * Database Column Remarks:
     *   确认收货状态：0->未确认；1->已确认
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.confirm_status
     *
     * @mbg.generated
     */
    private Integer confirmStatus;

    /**
     * Database Column Remarks:
     *   删除状态：0->未删除；1->已删除
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.delete_status
     *
     * @mbg.generated
     */
    private Integer deleteStatus;

    /**
     * Database Column Remarks:
     *   下单时使用的积分
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.use_integration
     *
     * @mbg.generated
     */
    private Integer useIntegration;

    /**
     * Database Column Remarks:
     *   支付时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.payment_time
     *
     * @mbg.generated
     */
    private LocalDateTime paymentTime;

    /**
     * Database Column Remarks:
     *   发货时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.delivery_time
     *
     * @mbg.generated
     */
    private LocalDateTime deliveryTime;

    /**
     * Database Column Remarks:
     *   确认收货时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.receive_time
     *
     * @mbg.generated
     */
    private LocalDateTime receiveTime;

    /**
     * Database Column Remarks:
     *   评价时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oms_order.comment_time
     *
     * @mbg.generated
     */
    private LocalDateTime commentTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table oms_order
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;
}