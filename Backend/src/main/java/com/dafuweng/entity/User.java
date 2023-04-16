package com.dafuweng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id = -1L;

    /**
     * 用户名
     */
    private String username = "";

    /**
     * 密码
     */
    private String password = "";

    /**
     * 用户角色
     */
    private String userRole = "";

    /**
     * 角色id
     */
    private Integer roleId = -1;

    /**
     * 战区ID
     */
    private Integer militaryRegionId = -1;

    /**
     * 部门ID
     */
    private Integer departmentId = -1;

    /**
     * 创建时间
     */
    private Date createTime = null;

    /**
     * 更新时间
     */
    private Date updateTime = null;

    /**
     * 头像路径
     */
    private String avatar = "../../assets/avatar_images/male1.jpg";

    /**
     * 性别
     */
    private Boolean sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 月薪
     */
    private BigDecimal salary;

    /**
     * 帐户是否过期(1 未过期，0已过期)
     */
    @TableField(exist = false)
    private boolean isAccountNonExpired = true;
    /**
     * 帐户是否被锁定(1 未过期，0已过期)
     */
    @TableField(exist = false)
    private boolean isAccountNonLocked = true;
    /**
     * 密码是否过期(1 未过期，0已过期)
     */
    @TableField(exist = false)
    private boolean isCredentialsNonExpired = true;
    /**
     * 帐户是否可用(1 可用，0 删除用户)
     */
    @TableField(exist = false)
    private boolean isEnabled = true;
    /**
     * 权限列表
     */
    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;
}
