package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.Log;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-04-03
 */
public interface LogService extends IService<Log> {
    public boolean insertLog(Log log);

    public List<Log> getAllLog();

    public List<Log> getLogByName(String name);
}
