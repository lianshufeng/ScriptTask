package top.dzurl.task.server.core.controller;


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
import top.dzurl.task.bridge.helper.ScriptHelper;
import top.dzurl.task.bridge.script.Environment;
import top.dzurl.task.bridge.script.SuperScript;
import top.dzurl.task.server.core.service.ScriptService;

import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("script")
public class ScriptController {

    @Autowired
    private ScriptHelper scriptHelper;

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
        SuperScript script = this.scriptHelper.parse(new String(buffer, "UTF-8"));

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


    private void validate(final SuperScript script) {
        //数据校验
        Assert.hasText(script.name(), "脚本名不能为空");

        //环境描述
        Environment environment = script.environment();
        Assert.notNull(environment, "脚本环境参数不能为空");
        Assert.notNull(environment.getDevice(), "脚本环境参数里设备类型不能为空");


    }

}
