package com.github.script.task.server.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataModel {
    //用户标识(不能为空)
    private String user;
    //平台标识
    private String platform;

    private String hash;
    private String text;
}
