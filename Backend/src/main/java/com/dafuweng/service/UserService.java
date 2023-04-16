package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface UserService extends IService<User> {
    @Data
    class UserTableData {
        public Long id;
        public String username;
        public String userRole;
        public Boolean sex;
        public Integer age;
        public String departmentName;
        public String militaryRegionName;
        public BigDecimal salary;

        public UserTableData(Long id, String username, String userRole, Boolean sex, Integer age, String departmentName, String militaryRegionName, BigDecimal salary) {
            this.id = id;
            this.username = username;
            this.userRole = userRole;
            this.sex = sex;
            this.age = age;
            this.departmentName = departmentName;
            this.militaryRegionName = militaryRegionName;
            this.salary = salary;
        }
    }

    /*
    -----------------------------------------
                     增删改查
    -----------------------------------------
    */
    public User findUserByUserName(String userName); // 按用户name查找

    public User findUserById(Long id); // 通过id查找用户

    public boolean insertUser(User insertUser); // 添加用户信息

    public boolean updateUserById(Long updateId, User updatedUser); // 按照用户的id更新用户信息

    public boolean deleteUserById(Long deleteId); // 按照用户id删除用户

    public List<User> getAllUserByDepartmentId(Long departmentId); // 按照部门id获取所有用户

    public List<User> getAllUserByMilitaryRegionId(Long militaryRegionId); // 获取战区所有用户

    public List<User> getAllUser(); // 返回表中所有用户
}
