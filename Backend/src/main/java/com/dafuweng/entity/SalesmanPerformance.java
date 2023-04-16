package com.dafuweng.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 销售员绩效表
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("salesman_performance")
public class SalesmanPerformance implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "salesman_id", type = IdType.AUTO)
    private Long salesmanId = (long) -1;

    /**
     * 签约金额
     */
    private BigDecimal contractAmount = BigDecimal.valueOf(0);

    /**
     * 签约客户数
     */
    private Integer contractCustomers = 0;

    /**
     * 放款金额
     */
    private BigDecimal loanAmount = BigDecimal.valueOf(0);

    /**
     * 服务费
     */
    private BigDecimal serviceFee = BigDecimal.valueOf(0);

    /**
     * 拨打电话次数
     */
    private Integer callTimes = 0;

    /**
     * 有效通话次数
     */
    private Integer validCalls = 0;

    /**
     * 意向客户数
     */
    private Integer intentionCustomers = 0;

    /**
     * 面试客户数
     */
    private Integer interviewCustomers = 0;

    /**
     * 记录创建时间
     */
    private Date createTime = new Date();

    /**
     * 记录修改时间
     */
    private Date updateTime = new Date();


}
