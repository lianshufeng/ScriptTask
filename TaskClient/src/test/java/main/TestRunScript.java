package main;

import org.junit.Test;

public class TestRunScript extends SuperRunScript {


    @Test
    public void testScript() {
        Object ret = runScript("TestScript.groovy");
        System.out.println("ret : " + ret);
    }

    @Test
    public void noneDeviceScript() {
        Object ret = runScript("NoneDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


    @Test
    public void webDeviceScript() {
        Object ret = runScript("WebDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


    @Test
    public void androidSimulatorDeviceScript() {
        Object ret = runScript("AndroidSimulatorDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }

    @Test
    public void GetWeiBoUserScript() {
        Object ret = runScript("GetWeiBoUserScript.groovy");
        System.out.println("ret : " + ret);
    }


}
