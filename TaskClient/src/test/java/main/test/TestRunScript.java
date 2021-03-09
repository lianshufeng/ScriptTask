package main.test;

import main.SuperRunScript;
import org.junit.Test;

public class TestRunScript extends SuperRunScript {


    @Test
    public void testScript1() {
        Object ret = runScript("script/TestScript.groovy");
        System.out.println("ret : " + ret);
    }


}
