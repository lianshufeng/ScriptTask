package com.github.script.task.bridge.model.userrobot.robot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotOcr extends RobotInterface {
    //base64编码
    private String picture;
}