package main;

import main.SuperRunScript;
import org.junit.Test;

public class TestRunScript extends SuperRunScript {


    @Test
    public void testScript() {
        Object ret = runScript("script/TestScript.groovy");
        System.out.println("ret : " + ret);
    }

    @Test
    public void noneDeviceScript() {
        Object ret = runScript("script/demo/NoneDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


    @Test
    public void webDeviceScript() {
        Object ret = runScript("script/demo/WebDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


    @Test
    public void androidSimulatorDeviceScript() {
        Object ret = runScript("script/demo/AndroidSimulatorDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


}
