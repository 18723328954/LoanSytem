package com.dafuweng.controller;


import com.dafuweng.entity.Log;
import com.dafuweng.service.LogService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-04-03
 */
@RestController
@RequestMapping("/api/log")
public class LogController {
    @Resource
    private LogService logService;

    @GetMapping("/getAllLog")
    public Result getAllLog() {
        List<Log> logList = logService.getAllLog();
        System.out.println(logList);
        return Result.ok(logList);
    }

    @GetMapping("/getLogByName")
    public Result getLogByName(String name) {
        List<Log> logList = logService.getLogByName(name);

        return Result.ok(logList);
    }

    @PostMapping("/insertLog")
    public Result insertLog(@RequestBody Log log) {
        logService.insertLog(log);
        return Result.ok();
    }
}

