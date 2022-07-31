package io.github.cocodx.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author amazfit
 * @date 2022-08-01 上午6:20
 **/
@Data
public class User implements Serializable {
    /* 主键id */
    private Long userId;
    /* 用户名称 */
    private String userName;
    /* 密码 */
    private String password;
    /* 昵称 */
    private String nickName;
    /* 头像 */
    private String imageName;
    /* 心情 */
    private String mood;
}
