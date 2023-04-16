package com.dafuweng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 合同表
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("contract")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同编号
     */
    @TableId(value = "contract_no", type = IdType.AUTO)
    private Long contractNo = -1L;

    /**
     * 签约金额
     */
    private BigDecimal contractAmount = BigDecimal.valueOf(0.0);

    /**
     * 贷款金额
     */
    private BigDecimal loanAmount = BigDecimal.valueOf(0.0);

    /**
     * 服务费
     */
    private BigDecimal serviceFee = BigDecimal.valueOf(0.0);

    /**
     * 贷款期限
     */
    private Integer loanTerm = 0;

    /**
     * 申请日期
     */
    private Date applyDate = new Date();

    /**
     * 审批状态
     */
    private Integer approveStatus = -1;

    /**
     * 备注
     */
    private String remark = "";

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

    /**
     * 审批人id
     */
    private Long approveId = 0L;
}
