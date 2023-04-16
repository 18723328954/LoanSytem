package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.DepartmentMapper;
import com.dafuweng.entity.Department;
import com.dafuweng.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    // 获取部门按照部门id
    @Override
    public Department getDepartmentByDepartmentId(Long departmentId) {
        return baseMapper.selectById(departmentId);
    }
}
