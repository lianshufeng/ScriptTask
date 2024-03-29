package com.github.script.task.server.core.controller;


import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.server.core.service.ScriptService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.github.script.task.bridge.script.Environment;
import com.github.script.task.bridge.script.SuperScript;
import com.github.script.task.bridge.util.ScriptUtil;

import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("script")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @SneakyThrows
    @RequestMapping("put")
    public Object put(@RequestParam("file") MultipartFile file) {
        log.info("upload : " + file.getOriginalFilename());

        //将上传的文件转换为脚本
        @Cleanup InputStream inputStream = file.getInputStream();
        final byte[] buffer = StreamUtils.copyToByteArray(inputStream);

        //转换为脚本对象
        SuperScript script = ScriptUtil.parse(new String(buffer, "UTF-8"));

        //数据校验
        validate(script);

        //入库
        return this.scriptService.put(buffer);
    }

    /**
     * 分页条件查询,查询脚本名或者备注
     *
     * @param word
     * @param pageable
     * @return
     */
    @RequestMapping("list")
    public Object list(String word, Pageable pageable) {
        return this.scriptService.list(word, pageable);
    }

    /**
     * 删除脚本
     *
     * @param scriptName
     * @return
     */
    @RequestMapping("del")
    public Object del(String scriptName) {
        Assert.hasText(scriptName, "脚本名不能为空");
        return this.scriptService.del(scriptName);
    }

    /**
     * 检查脚本
     *
     * @param scriptName
     * @param hash
     * @return
     */
    @RequestMapping("check")
    public Object check(String scriptName, String hash) {
        Assert.hasText(scriptName, "脚本名不能为空");
        Assert.hasText(hash, "脚本hash不能为空");
        return this.scriptService.check(scriptName, hash);
    }

    /**
     * 获取脚本
     * @param scriptName
     * @return
     */
    @RequestMapping("get")
    public Object get(String scriptName){
        Assert.hasText(scriptName, "脚本名不能为空");
        return ResultContent.buildContent(this.scriptService.get(scriptName));
    }


    private void validate(final SuperScript script) {
        //数据校验
        Assert.hasText(script.name(), "脚本名不能为空");

        //环境描述
        Environment environment = script.environment();
        Assert.notNull(environment, "脚本环境参数不能为空");
        Assert.notNull(environment.getDevice(), "脚本环境参数里设备类型不能为空");


    }

}
