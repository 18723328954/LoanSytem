package com.dafuweng.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
    private Long id; //用户编号
    private int code; // 用户状态码
    private String token; // token令牌
    private Long expireTime; // token过期时间
}
