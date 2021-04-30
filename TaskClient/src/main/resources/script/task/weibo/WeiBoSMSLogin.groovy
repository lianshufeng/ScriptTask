package script.task.weibo

import com.github.script.task.bridge.device.impl.WebDevice
import com.github.script.task.bridge.model.param.TaskParam
import com.github.script.task.bridge.model.userrobot.robot.RobotInput
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.Parameter
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.async.AsyncAction
import com.github.script.task.bridge.script.action.robot.UserRobotAction
import com.github.script.task.bridge.service.TaskService
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired

/**
 *  短信验证码登录
 */
class WeiBoSMSLogin extends SuperScript {

    ChromeDriver driver;
    AsyncAction asyncAction;
    String phone;


    @Autowired
    private TaskService taskService;


    @Override
    String name() {
        return "二维码登录微博并取登录信息"
    }

    @Override
    Map<String, Parameter> parameters() {
        return [
                "phone": [
                        'value' : '15123241353',
                        'remark': '手机号码'
                ] as Parameter
        ]
    }

    @Override
    ScriptEvent event() {
        return [
                'onCreate': {
                    log.info('event : {}', 'create')
                },
                'onClose' : {
                    log.info('event : {}', 'close')
                },
                'onRun'   : {
                    log.info('event : {}', 'run')

                    //驱动
                    driver = getRuntime().getDriver()
                    asyncAction = action(AsyncAction.class)
                    phone = getRuntime().getParameters()['phone']
                }
        ] as ScriptEvent
    }

    @Override
    Environment environment() {
        return [
                'device': [
                        'headless': false, //无头模式
//                        'width'   : 1920,
//                        'height'  : 1080
                ] as WebDevice
        ] as Environment
    }


    /**
     * 通过遍历的方式查询元素
     * @param webElement
     * @param by
     * @param name
     * @param value
     * @return
     */
    private WebElement findWebElementFromAttribute(WebElement webElement, By by, String name, String value) {
        try {
            for (def input in webElement.findElements(by)) {
                if (input.getAttribute(name) == value) {
                    return input;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    private WebElement findWebElementFromText(WebElement webElement, By by, String value) {
        try {
            for (def input in webElement.findElements(by)) {
                if (input.getText() == value) {
                    return input;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    private void welcome() {
        //异步查询并点击登录按钮
        asyncAction.where().execute(300, () -> {
            return driver.findElement(By.id("pl_web_top"));
        }, (WebElement it) -> {
            log.info("打开登录弹窗 : {}", it)
            it.findElement(By.className("login_link")).click();
            loginLayer();
        })
    }


    /**
     * 打开登录弹窗
     */
    private void loginLayer() {
        asyncAction.where().execute(300, () -> {
            return findWebElementFromText(driver.findElement(By.className("content")), By.tagName("a"), '短信登录');
        }, (WebElement it) -> {
            log.info("点击登录 -> {}", it)
            it.click();
            inputPhone();
        })
    }


    /**
     * 输入手机号码
     */
    private void inputPhone() {
        asyncAction.where().execute(300, () -> {
            WebElement webElement = driver.findElement(By.className("content"));
            return findWebElementFromAttribute(webElement, By.tagName("input"), 'action-data', 'text=请输入您的手机号码');
        }, (WebElement it) -> {
            log.info("登录手机号码 : {} -> {}", it, phone)
            it.sendKeys(phone)
            clickSendSmsBtn();
        })
    }

    /**
     * 点击获取短信验证码
     */
    private void clickSendSmsBtn() {
        asyncAction.where().execute(300, () -> {
            return findWebElementFromText(
                    driver.findElement(By.className("content")),
                    By.tagName("a"),
                    '获取短信验证码');
        }, (WebElement it) -> {
            log.info("点击获取短信验证码按钮 :  {}", it)
            it.click();


            //等待人机交互
            UserRobotAction.UserInput userRobotAction = action(UserRobotAction.class).waitUserInput([
                    'tips'   : '请输入您收到的短信验证码,当前手机号码:' + phone,
                    'value'  : [] as RobotInput,
                    'timeOut': 60 * 1000 * 10
            ] as UserRobotAction.UserRobotInterface)
            Optional.ofNullable(userRobotAction.getUserInterface()).ifPresent((ui) -> {
                def input = ui['text'];
                log.info("user input : {}", input)

                //输入框中输入用户输入的值
                findWebElementFromAttribute(driver.findElement(By.className("content")), By.tagName("input"), 'action-data', 'text=短信验证码').sendKeys(input);

                //点击登录按钮
                findWebElementFromText(driver.findElement(By.className("content")), By.tagName("a"), '登录').click();

                //等待几秒
                Thread.sleep(1000);

                isLogin();

            })

//

        })

    }


    /**
     * 是否已登录
     */
    private void isLogin() {
        asyncAction.where().execute(300, () -> {
            return driver.findElement(By.className("user_info_inner"));
        }, (WebElement it) -> {
            String nickName = it.findElement(By.className("user_name")).getText();
            String cookies = getCookies();
            log.info("登录成功： {}", nickName);
            //打印cookies
            log.info("更新脚本cookies  : {} ", 'GetWeiBoUserScript');
            //通过脚本更新cookies
            taskService.updateParamByScript([
                    'scriptName': 'GetWeiBoUserScript',
                    'parameters': [
                            'cookie': cookies
                    ]
            ] as TaskParam)

            asyncAction.proceed();
        })
    }


    /**
     * 读取cookies
     * @return
     */
    private String getCookies() {
        StringBuilder sb = new StringBuilder();
        driver.manage().getCookies().forEach((it) -> {
            sb.append(it.getName() + "=" + it.getValue() + "; ")
        })
        return sb.toString().trim();
    }

    @Override
    Object run() {
        driver.get("https://open.weibo.com/");

        //开始
        welcome();

        //异步等待时间
        asyncAction.await(60 * 1000 * 10);


        return null
    }
}
