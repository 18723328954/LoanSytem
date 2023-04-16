package com.dafuweng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 销售员代表客户表
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("salesman_representative_customer")
public class SalesmanRepresentativeCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 代表客户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id = -1L;

    /**
     * 销售员ID
     */
    private Long salesmanId = -1L;

    /**
     * 客户ID
     */
    private Long customerId = -1L;

    /**
     * 合同编号
     */
    private Long contractNo = -1L;

    /**
     * 记录创建时间
     */
    private Date createTime = new Date();

    /**
     * 记录修改时间
     */
    private Date updateTime = new Date();


}
