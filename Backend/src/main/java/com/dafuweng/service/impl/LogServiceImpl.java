package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.LogMapper;
import com.dafuweng.entity.Log;
import com.dafuweng.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-04-03
 */
@Service
@Transactional
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Override
    public boolean insertLog(Log log) {
        return baseMapper.insert(log) > 0;
    }

    @Override
    public List<Log> getAllLog() {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Log> getLogByName(String name) {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", name);

        return baseMapper.selectList(queryWrapper);
    }
}
