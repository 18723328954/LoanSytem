package com.dafuweng.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.dafuweng.service.CustomerService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id = -1L;

    /**
     * 客户姓名
     */
    private String name = "";

    /**
     * 身份证号
     */
    private String idCard = "";

    /**
     * 公司名称
     */
    private String companyName = "";

    /**
     * 营业执照号码
     */
    private String businessLicense = "";

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();
    /**
     * 意向分数
     */
    private Integer intensionScore = 0;

    /**
     * 销售代表id
     */
    private Long salesmanId = -1L;
}
