package main;

import org.junit.Test;

public class TestRunScript extends SuperRunScript {


    @Test
    public void testScript() {
        Object ret = runScript("simple/TestScript.groovy");
        System.out.println("ret : " + ret);
    }

    @Test
    public void noneDeviceScript() {
        Object ret = runScript("simple/NoneDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


    @Test
    public void webDeviceScript() {
        Object ret = runScript("simple/WebDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }


    @Test
    public void androidSimulatorDeviceScript() {
        Object ret = runScript("simple/AndroidSimulatorDeviceScript.groovy");
        System.out.println("ret : " + ret);
    }

    @Test
    public void GetWeiBoUserScript() {
        Object ret = runScript("GetWeiBoUserScript.groovy");
        System.out.println("ret : " + ret);
    }

    @Test
    public void AnalysisUserScript() {
        Object ret = runScript("AnalysisUserScript.groovy");
        System.out.println("ret : " + ret);
    }


}
