package com.github.script.task.bridge.model.userrobot.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDrag extends UserInterface {
    //base64编码
    private String picture;
}
