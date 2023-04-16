package com.dafuweng.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long salesmanId = (long) -1;

    private Integer productId = -1;

    private Long productNum = (long) -1;

    /**
     * 创建时间
     */
    private Date createTime = null;

    public Product(Long salesmanId, Integer productId, Long productNum) {
        this.salesmanId = salesmanId;
        this.productId = productId;
        this.productNum = productNum;
    }
}
