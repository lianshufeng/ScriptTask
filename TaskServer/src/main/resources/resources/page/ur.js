jQuery(function ($) {
    console.log('人机交互加载...');
    const GetUserRobotSleepTime = 1500;


    /**
     * 用户交互的父类
     */
    class SuperUserRobot {
        data = null;
        contentInput = null;
        me = this;


        constructor(data) {
            this.data = data;
            this.contentInput = this.getContentInput();
        }

        /**
         * 返回jquery的对象
         */
        getContentInput() {
            return null;
        }

        /**
         * 渲染
         */
        render() {

        }

        /**
         * 请求
         * @param data
         */
        request(data) {
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


        /**
         * 提交按钮
         */
        submit() {
        }

        /**
         * 重置
         */
        reset() {
            this.render();
        }

        /**
         * 备注信息
         */
        remark() {
            return '暂无描述信息';
        }

    }


    /**
     * 用户输入
     */
    class UserRobotInput extends SuperUserRobot {

        getContentInput() {
            return $("#userRobotPageContentInput");
        }

        render() {
            this.contentInput.find("input[type='text']").val("");
            this.contentInput.css("display", "inline-flex");
        }

        submit() {
            this.request({
                "id": this.data.id,
                "value": {
                    "text": this.contentInput.find("input[type='text']").val()
                },
            });
        }

        remark() {
            return "输入内容";
        }


    }


    /**
     * 图像识别
     * @param data
     * @constructor
     */
    class UserRobotOcr extends SuperUserRobot {

        getContentInput() {
            return $("#userRobotPageContentOcr");
        }

        //渲染
        render() {
            this.contentInput.find("input[type='text']").val("");
            this.contentInput.css("display", "inline-flex");
            this.contentInput.find('.ocr-image').attr('src', this.data.robotInterface.picture);
        }

        //提交按钮触发
        submit() {
            this.request({
                "id": this.data.id,
                "value": {
                    "text": this.contentInput.find("input[type='text']").val()
                },
            });
        }

        remark() {
            return "识别图片";
        }
    }


    /**
     * 用户触摸图片
     */
    class UserRobotTap extends SuperUserRobot {

        /**
         * 标记内容
         * @type {[]}
         */
        markTapContainer = [];

        getContentInput() {
            return $("#userRobotPageContentTap");
        }


        /**
         * 取图片宽度与高度
         * @param picture
         */
        getPictureSize(picture) {
            return new Promise(function (resolve, reject) {
                let image = new Image();
                image.onload = () => {
                    resolve(image);
                };
                image.src = picture;
            });
        }


        /**
         * 渲染画布
         */
        renderCanvas(canvas, item) {
            let element = canvas.get(0);
            let markTapItem = {
                "name": item.name,
                "tap": []
            };
            this.markTapContainer.push(markTapItem);

            /**
             * 记录触摸数据
             * @param layer
             */
            let markTap = function (layer) {
                markTapItem.tap.push({
                    "time": new Date().getTime(),
                    "x": layer.eventX,
                    "y": layer.eventY
                })
                let badge = markTapItem.tap.length.toString();


                canvas.addLayer({
                    type: 'arc',
                    fillStyle: 'rgba(255, 255, 255, 0.3)',
                    strokeStyle: '#69f',
                    strokeWidth: 2,
                    x: layer.eventX,
                    y: layer.eventY,
                    fromCenter: true,
                    radius: 20
                }).addLayer({
                    type: 'text',
                    fillStyle: 'rgba(255, 255, 255, 0.1)',
                    strokeStyle: '#df4b30',
                    strokeWidth: 3,
                    x: layer.eventX,
                    y: layer.eventY,
                    fromCenter: true,
                    fontSize: 30,
                    text: badge
                }).drawLayers();


            }


            this.getPictureSize(item.picture).then((img) => {
                element.width = img.width;
                element.height = img.height;

                canvas.addLayer({
                    type: 'image',
                    source: item.picture,
                    width: img.width,
                    height: img.height,
                    fromCenter: false,
                    mousedown: markTap,
                    touchstart: markTap
                }).drawLayers();
            });
        }

        render() {
            let container = this.contentInput.find('.tap-image-container');
            //清空容器里的所有元素
            container.empty();
            let template = $('.tap-image-template');
            for (let i in this.data.robotInterface.items) {
                let item = this.data.robotInterface.items[i];
                let img = template.clone();
                this.renderCanvas(img.find('canvas'), item);
                img.show();
                container.append(img);
            }

            this.contentInput.css("display", "inline-flex");
        }

        //提交按钮触发
        submit() {
            this.request({
                "id": this.data.id,
                "value": {
                    "tap": this.markTapContainer
                },
            });
        }

        reset() {
            this.markTapContainer.splice(0, this.markTapContainer.length);
            super.reset();
        }


        remark() {
            return "点击图片"
        };
    }

    const UserRobotInputClass = {
        "Input": UserRobotInput,
        "Ocr": UserRobotOcr,
        "Tap": UserRobotTap
    }


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
     * 刷新显示时间
     * @param time
     */
    let refreshWaitTime = function (createTime) {
        var waitTime = parseInt(new Date().getTime() - createTime) / 1000;
        if (waitTime > 60) {
            waitTime = parseInt(waitTime / 60) + "分" + parseInt(waitTime % 60) + "秒"
        } else {
            waitTime = parseInt(waitTime) + "秒";
        }
        $(".job-wait-time").text(waitTime);


        //设置自动刷新，判断默认页是否出现
        setTimeout(function () {
            if ($("#backgroundPage").is(":hidden")) {
                refreshWaitTime(createTime);
            }
        }, 1000)


    }


    let showUserRobot = function (data) {
        let page = $("#userRobotInputPage");

        let UserRobotInputCls = UserRobotInputClass[data.type];
        page.find(".job-id").text(data.jobId);
        page.find(".job-tips").text(data.tips);
        if (UserRobotInputCls) {
            page.find('.job-type').text("(" + data.type + ") " + new UserRobotInputCls().remark());
        }
        refreshWaitTime(data.createTime);


        //隐藏默认的背景页
        $("#backgroundPage").hide();

        let URClass = UserRobotInputClass[data.robotInterface.type];
        if (!URClass) {
            console.error("没有实现的类型 : ", data.robotInterface.type)
            return;
        }

        let urInputObject = new URClass(data);

        //渲染UI
        $(".user-robot-item").hide();
        urInputObject.render();


        //取消页面所有事件
        page.unbind();

        //绑定提交按钮
        let submitBtn = page.find("#userRobotConsole").find(".ur-submit-btn");
        submitBtn.unbind();
        submitBtn.bind('click', (e) => urInputObject.submit(e));
        submitBtn.bind('tap', (e) => urInputObject.submit(e));
        page.bind('keydown', (e) => {
            if (e.keyCode == 13) {
                urInputObject.submit(e);
            }
        });


        //重置按钮
        let resetBtn = page.find("#userRobotConsole").find(".ur-reset-btn");
        resetBtn.unbind();
        resetBtn.bind('click', (e) => urInputObject.reset(e));
        resetBtn.bind('tap', (e) => urInputObject.reset(e));
        //绑定ESC事件
        page.bind('keydown', (e) => {
            if (e.keyCode == 27) {
                urInputObject.reset(e);
            }
        });


        //显示页面
        page.show();

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