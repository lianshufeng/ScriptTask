jQuery(function ($) {
    console.log('人机交互加载...');
    const GetUserRobotSleepTime = 1500;


    /**
     * 人机交互对象
     * @constructor
     */
    let getUserRobot = function () {
        return new Promise((resolve, reject) => {
            //进行网络请求
            let request = function () {
                $.ajax({
                    type: 'post',
                    url: "../ur/listRobotInput",
                    datatype: "json",
                    data: "size=1&page=0&sort=createTime,asc",
                    success: function (data) {
                        try {
                            if (data.state == 'Success' && data.content.content.length > 0) {
                                resolve(data.content.content[0]);
                            } else {
                                setTimeout(request, GetUserRobotSleepTime);
                            }
                        } catch (e) {
                            console.error(e)
                            setTimeout(request, GetUserRobotSleepTime);
                        }
                    },
                    error: function (error) {
                        //请求失败则继续请求
                        setTimeout(request, GetUserRobotSleepTime);
                    }
                })
            }
            request();
        });
    }

    /**
     * 提交用户的输入信息
     * @param data
     */
    let submitUserInput = function (data) {
        $.ajax({
            type: 'post',
            contentType: "application/json",
            url: "../ur/updateUserInput",
            datatype: "json",
            data: JSON.stringify(data),
            success: function (data) {
                if (data && data.state == 'Success') {
                    $.aceToaster.add({
                        placement: 'tr',
                        body: "<div class='bgc-success-d1 text-white px-3 pt-3'>\
                        <div class='border-2 brc-white px-3 py-25 radius-round'>\
                            <i class='fa fa-check text-150'></i>\
                        </div>\
                    </div>\
                    <div class='p-3 mb-0 flex-grow-1'>\
                        <h4 class='text-130'>提示</h4>\
                        提交成功 \
                    </div>\
                    <button data-dismiss='toast' class='align-self-start btn btn-xs btn-outline-grey btn-h-light-grey py-2px mr-1 mt-1 border-0 text-150'>&times;</button>",
                        width: 420,
                        delay: 3000,
                        close: false,
                        className: 'bgc-white-tp1 shadow border-0',
                        bodyClass: 'd-flex border-0 p-0 text-dark-tp2',
                        headerClass: 'd-none',
                    });
                }
            },
            complete: function () {
                //开始监视用户交互
                listenUserRobot();
            }
        })
    }

    let showUserRobot = function (data) {
        let page = $("#userRobotInputPage");

        //设置提示
        page.find('label').text(data.tips);

        //隐藏默认的背景页
        $("#backgroundPage").hide();
        let UserRobotInputClass = {
            "Input": UserRobotInput
        }
        let URClass = UserRobotInputClass[data.robotInterface.type];
        if (!URClass) {
            console.error("没有实现的类型 : ", data.robotInterface.type)
            return;
        }

        let urInputObject = new URClass(data);

        //渲染UI
        $(".user-robot-item").hide();
        urInputObject.render();

        //绑定提交按钮
        let submitBtn = page.find("#userRobotConsole").find("button");
        submitBtn.unbind();
        submitBtn.bind('click', urInputObject.submit);
        submitBtn.bind('tap', urInputObject.submit);

        //绑定回车事件
        page.unbind();
        page.bind('keydown', (e) => {
            if (e.keyCode == 13) {
                urInputObject.submit();
            }
        });


        //显示页面
        page.show();

    }

    /**
     * 动态调用
     */
    let UserRobotInput = function (data) {
        let contentInput = $("#userRobotPageContentInput");
        //渲染
        this.render = function () {
            contentInput.find("input[type='text']").val("");
            contentInput.css("display", "inline-flex");
        }
        //提交按钮触发
        this.submit = function () {
            submitUserInput({
                "id": data.id,
                "value": {
                    "text": contentInput.find("input[type='text']").val()
                },
            });
        }
    }


    /**
     * 监听人机交互
     */
    let listenUserRobot = function () {

        $("#userRobotInputPage").hide();

        $("#backgroundPage").show();


        getUserRobot().then((data) => {
            showUserRobot(data);
        })
    }


    //开始任务
    listenUserRobot();

})