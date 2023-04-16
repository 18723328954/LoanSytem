package com.dafuweng.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    private String path; // 路由地址
    private String component; // 路由组件
    private String alwaysShow; // 是否显示
    private String name; // 路由名称
    private Meta Meta; // 路由meta信息
    private List<RouterVo> children = new ArrayList<RouterVo>(); // 子路由

    @Data
    @AllArgsConstructor
    public class Meta{
        private String title; // 标题
        private String icon; // 图标
        private Object[] role; // 角色权限id
    }
}
