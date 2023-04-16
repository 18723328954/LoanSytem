package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.UserMapper;
import com.dafuweng.entity.User;
import com.dafuweng.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    @Override
    public User findUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(); // 条件构造器
        queryWrapper.eq("username", userName); // 用户名
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 更具用户id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public User findUserById(Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", id);

        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 添加用户信息
     *
     * @param insertUser
     * @return
     */
    @Override
    public boolean insertUser(User insertUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", insertUser.getId());

        // 判断是否系统中已经存在相同的id号
        if (baseMapper.selectOne(queryWrapper) != null) {
            return false;
        }
        baseMapper.insert(insertUser); // 添加到数据库中
        return true;
    }

    /**
     * 根据用户id更新用户信息
     *
     * @param updateId
     * @param updatedUser
     * @return
     */
    @Override
    public boolean updateUserById(Long updateId, User updatedUser) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>();
        updateWrapper.eq("id", updateId);

        User updateUser = baseMapper.selectOne(updateWrapper);
        // 把updatedUser中不为null的属性更新到updateWrapper中按id查找的数据中
        if (updateUser != null) {
            // 判断需要不为默认值的属性修改
            if (updatedUser.getId() != -1) updateUser.setId(updatedUser.getId());
            if (updatedUser.getUsername() != "") updateUser.setUsername(updatedUser.getUsername());
            if (updatedUser.getPassword() != "") updateUser.setPassword(updatedUser.getPassword());
            if (updatedUser.getUserRole() != "") updateUser.setUserRole(updatedUser.getUserRole());
            if (updatedUser.getRoleId() != -1) updateUser.setUserRole(updatedUser.getUserRole());
            if (updatedUser.getMilitaryRegionId() != -1) updateUser.setMilitaryRegionId(updatedUser.getMilitaryRegionId());
            if (updatedUser.getDepartmentId() != -1) updateUser.setDepartmentId(updatedUser.getDepartmentId());
            if (updatedUser.getCreateTime() != null) updateUser.setCreateTime(updatedUser.getCreateTime());
            if (updatedUser.getUpdateTime() != null) updateUser.setUpdateTime(updatedUser.getUpdateTime());
            if (updatedUser.getAvatar() != "") updateUser.setAvatar(updatedUser.getAvatar());

            baseMapper.update(updateUser, updateWrapper);

            return true;
        }

        return false;
    }

    /**
     * 删除用户
     *
     * @param deleteId 员工id
     * @return
     */
    @Override
    public boolean deleteUserById(Long deleteId) {
        int rows = baseMapper.deleteById(deleteId);

        return rows > 0;
    }

    // 按照部门id获取所有用户
    @Override
    public List<User> getAllUserByDepartmentId(Long departmentId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<User> getAllUserByMilitaryRegionId(Long militaryRegionId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("military_region_id", militaryRegionId);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<User> getAllUser() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        return baseMapper.selectList(queryWrapper);
    }
}
