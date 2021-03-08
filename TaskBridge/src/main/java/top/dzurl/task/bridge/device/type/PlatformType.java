package top.dzurl.task.bridge.device.type;

import lombok.Getter;

public enum PlatformType {

    Android("Android"),
    Web("Web"),
    Ios("Ios")
    ;

    @Getter
    private String name;

    PlatformType(String name) {
        this.name = name;
    }
}
