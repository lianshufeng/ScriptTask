package com.github.script.task.bridge.script.action.net;

import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.util.HttpClient;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpAction extends SuperScriptAction {


    @Delegate(types = HttpClient.class)
    private HttpClient httpClient = new HttpClient();


}
