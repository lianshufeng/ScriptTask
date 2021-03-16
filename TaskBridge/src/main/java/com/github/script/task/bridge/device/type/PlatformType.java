package com.github.script.task.bridge.device.type;

import lombok.Getter;

public enum PlatformType {

    Android("Android"),
    Web("Web"),
    Ios("Ios"),
    None("None"),
    ;

    @Getter
    private String name;

    PlatformType(String name) {
        this.name = name;
    }
}
