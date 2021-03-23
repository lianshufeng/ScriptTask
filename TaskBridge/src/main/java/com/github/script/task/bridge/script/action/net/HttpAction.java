package com.github.script.task.bridge.script.action.net;

import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.util.HttpClient;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 * 网络请求，支持 Get post form , 支持自动维护cookies , 以及Dom JSON 的处理
 *
 */
@Slf4j
public class HttpAction extends SuperScriptAction {


    @Delegate(types = HttpClient.class)
    private HttpClient httpClient = new HttpClient();


}
