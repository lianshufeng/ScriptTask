package script.simple

import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice
import com.github.script.task.bridge.helper.MapHelper
import com.github.script.task.bridge.model.userrobot.robot.RobotInput
import com.github.script.task.bridge.model.userrobot.user.UserInput
import com.github.script.task.bridge.model.userrobot.user.UserInterface
import com.github.script.task.bridge.result.ResultState
import com.github.script.task.bridge.script.Environment
import com.github.script.task.bridge.script.ScriptEvent
import com.github.script.task.bridge.script.SuperScript
import com.github.script.task.bridge.script.action.async.AsyncAction
import com.github.script.task.bridge.script.action.bind.DeviceBindAction
import com.github.script.task.bridge.script.action.robot.UserRobotAction
import io.appium.java_client.android.Activity
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By
import org.springframework.beans.factory.annotation.Autowired

class AndroidSimulatorDeviceScript extends SuperScript {

    //抖音的包名
    private final static String DouyinPackage = "com.ss.android.ugc.aweme"
    private final static String DouyinActivity = ".splash.SplashActivity"


    @Override
    String name() {
        return "AndroidSimulatorDeviceScript"
    }

    @Override
    String remark() {
        '我的快乐至上11'
    }

    @Override
    Environment environment() {
        return [
                'device': [
                        "resolution": "1080,1920,260"
                ] as AndroidSimulatorDevice
        ] as Environment
    }

    @Override
    ScriptEvent event() {
        return [
                'onCreate'   : {
                    log.info('event : {}', 'create')
                },
                'onClose'    : {
                    log.info('event : {}', 'close')
                },
                'onRun'      : {
                    log.info('event : {}', 'run')
                },
                'onException': {
                    it ->
                        log.info('event : {} -> {}', 'exception ', it)
                }
        ] as ScriptEvent
    }


    @Autowired
    private MapHelper mapHelper;

    @Override
    Object run() {
        //设备绑定
//        action(DeviceBindAction.class).bind()

        //异步方法
//        def asyncAction = action(AsyncAction.class)


        AndroidDriver driver = getRuntime().getDriver();

        //判断抖音是否已安装
        if (!driver.isAppInstalled(DouyinPackage)) {
            println "安装 抖音app ..."
            driver.installApp("https://lf9-apk.ugapk.cn/package/apk/aweme/1015_210001/aweme_aweGW_v1015_210001_7240_1654787809.apk")
        }

        //启动抖音
        if (!DouyinActivity.equals(driver.currentActivity())) {
            println "启动 抖音app ..."
            driver.startActivity(new Activity(DouyinPackage, DouyinActivity))
        }

        //抖音同意
        action(AsyncAction.class).where().execute(500, {
            return driver.findElement(By.id("com.ss.android.ugc.aweme:id/cxy"))
        }, { it ->
             it.click()
        })

        //app允许拨打电话
        action(AsyncAction.class).where().execute(500, {
            return driver.findElement(By.id("com.android.packageinstaller:id/permission_allow_button"))
        }, { it ->
             it.click()
        })

        //点击我知道了
        action(AsyncAction.class).where().execute(500, {
            return driver.findElement(By.id("com.ss.android.ugc.aweme:id/a=h"))
        }, { it ->
             it.click()
        })


        //首次启动滑动
        action(AsyncAction.class).where().execute(500, {
            return driver.findElement(By.id("com.ss.android.ugc.aweme:id/poz"))
        }, { it ->
            println "滑动.............................->"
            int width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
            int height = driver.manage().window().getSize().height;//获取当前屏幕的高度
            driver.swipe(width/2, height*3/4, width/2, height/4, 300);
        })





//        //等待用户输入
//        UserRobotAction.UserInput userInput = action(UserRobotAction.class).waitUserInput([
//                'tips'   : '请输入收到的短信',
//                'value'  : [] as RobotInput,
//                'timeOut': 6000
//        ] as UserRobotAction.UserRobotInterface)
//        if (userInput.getState() == ResultState.Success) {
//            UserInput userRobot = userInput.getUserInterface();
//            println "用户输入 : " + userRobot.getText()
//        }

        Thread.sleep(1000L * 60)

        return [
                'time': System.currentTimeMillis(),
        ]
    }
}
