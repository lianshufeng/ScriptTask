var copySelectCell = function () {
    var text = $(".cellSelectContent").text();
    navigator.clipboard.writeText(text).then(function () {
        console.log('[clipboard] : ', text);
        $.aceToaster.removeAll();
    }, function (err) {
        alert("复制剪贴板失败");
    });
}

/**
 * 删除线索
 */
var removeClue = function (id) {
    var swalWithBootstrapButtons = Swal.mixin({
        buttonsStyling: false
    })
    swalWithBootstrapButtons.fire({
        text: "确认删除线索 : " + id,
        type: 'warning',
        showCancelButton: true,
        scrollbarPadding: false,
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        reverseButtons: true,
        customClass: {'confirmButton': 'btn btn-green mx-2 px-3', 'cancelButton': 'btn btn-red mx-2 px-3'}
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                type: 'post',
                url: "../userClue/del",
                datatype: "json",
                data: "id=" + id,
                success: function (data) {
                    if (data.state != "Success") {
                        alert(JSON.stringify(data));
                        return;
                    }
                    swalWithBootstrapButtons.fire({
                        title: '删除成功!',
                        type: 'success',
                        icon: 'success',
                        customClass: {'confirmButton': 'btn btn-info px-5'}
                    })
                    //刷新脚本
                    $("#grid-table").jqGrid("setGridParam", {
                        postData: {}
                    }).trigger("reloadGrid");
                }
            })


        }
    })
}

var setData = function (data) {
    $("#clueId").val(data.id)
    $("#remarkText").val(data.remark)
}


jQuery(function ($) {


    var grid_data =
        [
            {id: "1", name: "Desktop Computer", note: "note", stock: "Yes", ship: "FedEx", sdate: "2017-12-13"}
        ]

    var subgrid_data =
        [
            {id: "1", name: "sub grid item 1", qty: 11},
            {id: "2", name: "sub grid item 2", qty: 3},
            {id: "3", name: "sub grid item 3", qty: 12}
        ]


    var grid_selector = "#grid-table"
    var pager_selector = "#grid-pager"
    var grid_box = '#gbox_grid-table'


    // resize to fit page size
    var parent_column = $(grid_selector).closest('.col-12')
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', parent_column.width())
    })

    //resize on sidebar collapse/expand
    $('.sidebar')
        .on('expand.ace.sidebar', function () {
            $(grid_box).hide()
        })
        .on('collapsed.ace.sidebar expanded.ace.sidebar', function () {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width())
            $(grid_box).show()
        })


    // update zIndex for jqGrid modals, to appear above other elements
    $.extend($.jgrid.jqModal, {zIndex: 1100})


    // set custom classes
    $.jgrid.guiStyles.bootstrap4ace = {
        baseGuiStyle: "bootstrap4",

        gBox: "",
        gView: "",

        hDiv: "border-y-1 brc-grey-l2 mt-n1px bgc-secondary-l3",
        hTable: "text-uppercase text-80 text-dark-tp3",
        colHeaders: "pl-2 pr-1 text-left",


        gridTitle: "bgc-primary-d1 text-white text-125 p-25",
        grid: "table table-hover table-bordered text-dark-m1 text-95 border-0 brc-grey-l2",
        titleButton: "btn btn-primary border-0 mr-2 px-2 w-auto radius-1",

        gridRow: "bgc-h-success-l3",

        actionsButton: "action-btn mx-1 px-2px float-none border-0",

        states: {
            select: "bgc-success-l2 bgc-h-success-l1",
            th: "bgc-yellow-l1 text-blue-d2",
            //hoverTh: "bgc-default-l2 text-default-d3",
            hoverTh: "bgc-yellow-l1 text-dark-m3",

            error: "alert bgc-danger-l3",
            //active: "active",
            //textOfClickable: ""
        },


        //dialogs
        overlay: "modal-backdrop",
        dialog: {
            header: "modal-header bgc-default-l4 text-blue-m1 py-2 px-3",
            window: "modal mw-100",
            document: "modal-dialog mw-none",

            content: "modal-content p-0",
            body: "modal-body px-2 py-25 text-130",
            footer: "modal-footer",

            closeButton: "mr-1 mt-n25 px-2 py-1 w-auto h-auto border-1 brc-h-warning-m1 bgc-h-warning-l1 text-danger radius-round",
            fmButton: "btn btn-sm btn-default",

            viewLabel: "control-label py-2",
            dataField: "form-control my-2 ml-1 w-auto",
            viewCellLabel: "text-right w-4 pr-2",
            viewData: "text-left text-secondary-d2 d-block border-1 brc-grey-l2 p-2 radius-1 my-2 ml-1"
        },

        searchDialog: {
            elem: "form-control w-95",
            operator: "form-control w-95",
            label: "form-control w-95",

            addRuleButton: "btn btn-xs btn-outline-primary radius-round btn-bold px-2 mx-1 text-110",
            addGroupButton: "btn btn-xs btn-primary mx-1 text-110",
            deleteRuleButton: "h-4 px-2 pt-0 text-150 mr-1 btn btn-xs btn-outline-danger border-0",
            deleteGroupButton: "h-4 px-2 pt-0 text-150 mr-1 btn btn-xs btn-outline-danger border-0",
        },

        navButton: "action-btn border-0 text-110 mx-1",
        pager: {
            pager: "py-3 px-1 px-md-2 bgc-primary-l4 border-t-1 brc-default-l2 mt-n1px",
            pagerInput: "form-control form-control-sm text-center d-inline-block",
            pagerSelect: "form-control w-6 px-1",
            pagerButton: "p-0 m-0 border-0 radius-round text-110",
        },

        subgrid: {
            button: "",//don't remove
            row: "bgc-secondary-l4 p-0",
        },

        loading: "alert bgc-primary-l3 brc-primary-m2 text-dark-tp3 text-120 px-4 py-3",
    }

    // use the following icons
    var _pageBtn = "btn w-4 h-4 px-0 mx-2px btn-outline-lightgrey btn-h-outline-primary btn-a-outline-primary radius-round bgc-white"
    $.jgrid.icons.icons4ace = {
        baseIconSet: "fontAwesome5",
        common: "fas",
        actions: {
            edit: "fa-pencil-alt text-blue",
            del: "fa-trash-alt text-danger-m1",
            save: "fa-check text-success",
            cancel: "fa-times text-orange-d2"
        },

        pager: {
            first: "fa-angle-double-left " + _pageBtn,
            prev: "fa-angle-left " + _pageBtn,
            next: "fa-angle-right " + _pageBtn,
            last: "fa-angle-double-right " + _pageBtn
        },

        gridMinimize: {
            visible: "fa-chevron-up",
            hidden: "fa-chevron-down"
        },

        sort: {
            common: "far",
            asc: "fa-caret-up",
            desc: "fa-caret-down"
        },

        form: {
            close: "fa-times my-2px",
            prev: "fa-chevron-left",
            next: "fa-chevron-right",
            save: "fa-check",
            undo: "fa-times",
            del: "fa-trash-alt",
            cancel: "fa-times",

            resizableLtr: "fa-rss fa-rotate-270 text-orange-d1 text-105"
        },
    }


    // initiate plugin
    $(grid_selector).jqGrid({
        //direction: "rtl",

        iconSet: "icons4ace",
        guiStyle: "bootstrap4ace",

        multiselectWidth: 36,

        // data: grid_data,
        // datatype: "local",

        url: "../userClue/list",
        mtype: "post",
        datatype: "json",
        jsonReader: {
            root: function (obj) {
                return obj.content.content;
            },
            total: function (obj) {
                return obj.content.totalPages;
            },
            records: function (obj) {
                return obj.content.totalElements;
            }
        },

        height: 500,//optional
        prmNames: {page: "page", rows: "size", sort: "sort"},

        //sortable: true,// requires jQuery UI Sortable

        colNames: ['平台', '用户标识', '匹配信息', '备注', '权重值', '创建时间', '操作'],
        colModel: [
            {
                resizable: false,
                name: 'platform',
                index: 'platform',
                width: 20,
            },
            {
                resizable: false,
                name: 'user',
                index: 'user',
                width: 100,
            },
            {
                resizable: false,
                name: 'matchInfoKeyWord',
                index: 'matchInfo',
                width: 100,
                formatter: function (val, options, rowdata) {
                    let line = rowdata.matchInfo;
                    for (let i in rowdata.matchWords) {
                        let matchWord = rowdata.matchWords[i];
                        line = line.split(matchWord.keyWord).join("<span class='text-orange-d2'>" + matchWord.keyWord + "</span>");
                    }
                    return line;
                }
            },
            {
                resizable: false,
                name: 'remark',
                index: 'remark',
                width: 50,
            },
            {
                resizable: false,
                name: 'weightValue',
                index: 'weightValue',
                align: 'center',
                width: 15,
            },
            {
                resizable: false,
                name: 'displayTime',
                index: 'createTime',
                width: 40,
                formatter: function (val, options, rowdata) {
                    return Date.format(new Date(rowdata.createTime), 'yyyy-MM-dd HH:mm:ss');
                }
            },
            {
                resizable: false,
                name: 'option',
                index: '',
                width: 120,
                align: "center",
                fixed: true,
                sortable: false,
                formatter: function (val, options, rowdata) {
                    /*return '<button class="card-toolbar-btn btn btn-sm radius-1 btn-outline-danger btn-h-outline-danger btn-tp" type="button" onclick="removeScript(\'' + rowdata.id + '\')" ><i class="fa fa-times text-danger-m2"></i></button>'*/
                    return '<button type="button" class="btn btn-bgc-tp btn-lighter-blue btn-h-primary btn-a-primary" data-toggle="modal" data-target="#exampleModal2" onclick="setData(' + JSON.stringify(rowdata).replace(/"/g, '&quot;') + ')"><i class="fa fa-edit"></i></button> &nbsp;' +
                        '<button type="button" class="btn btn-bgc-tp btn-lighter-red btn-h-primary btn-a-primary"  onclick="removeClue(\'' + rowdata.id + '\')"><i class="fa fa-trash-alt"></i></button>'

                }
            }
        ],


        altRows: true,
        altclass: 'bgc-secondary-l5',

        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],

        pager: pager_selector,
        //toppager: true,

        multiselect: false,
        multiboxonly: true,
        //multikey: "ctrlKey",

        editurl: null,//nothing is saved
        caption: "线索列表",

        //autowidth: true, shrinkToFit: true,
        autowidth: true,
        shrinkToFit: $(window).width() > 600,
        forceFit: true,


        // resize grid after pagination
        onPaging: function (pgButton) {
            setTimeout(function () {
                $(grid_box).hide();
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                $(grid_box).show();
            }, 0);
        },
        //修改向服务端请求的数据，把page数量-1
        serializeGridData: function (postData) {


            var platform = $("#platform").val().trim()
            var remark = $("#remark").val().trim()

            if (postData['page']) {
                postData.page--;
            }

            postData.platform = platform
            postData.remark = remark
            //排序
            console.log(JSON.stringify(postData))
            if (postData['sort']) {
                postData['sort'] = postData['sort'] + "," + postData['sord']
                /*delete postData['sort']*/
            }

            return postData;
        },
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
                enableTooltips(table);
            }, 0);
        },

        onCellSelect: function (rowid, iCol, cellcontent, e) {
            if (iCol > 5 || $.trim(cellcontent) == '&nbsp;'){
                return
            }
            let content = "<span class='cellSelectContent'>" + cellcontent + "</span>"
            $.aceToaster.removeAll();
            $.aceToaster.add({
                placement: 'center',
                width: 600,
                title: '<button class="btn btn-danger btn-sm mb-1" onclick="copySelectCell();"><i class="fa fa-copy text-80 mr-1"></i>复制</button>',
                body: content,
                // delay: 1000,
                close: true,
                autohide: false,
                closeClass: 'btn btn-light-danger border-0 btn-bgc-tp btn-xs px-2 py-0 text-150 position-tr mt-n25',
                className: 'bgc-white-tp1 border-none border-t-4 brc-primary-tp1 rounded-sm pl-3 pr-1',
                headerClass: 'bg-transparent border-0 text-120 text-blue-d3 font-bolder mt-3',
                bodyClass: 'pt-0 pb-6 text-105'
            })
        }
    })


    // enable search/filter toolbar
    // jQuery(grid_selector).jqGrid('filterToolbar',{defaultSearch:true,stringResult:true})
    // jQuery(grid_selector).filterToolbar({})


    // navButtons
    $(grid_selector)
        .jqGrid('navGrid', pager_selector,
            {	//navbar options
                add: false,
                addicon: 'fa fa-plus-circle text-purple text-100',

                edit: false,
                editicon: 'fa fa-edit text-blue text-100',

                del: false,
                delicon: 'fa fa-trash text-danger-m1 text-100',

                search: false,
                searchicon: 'fa fa-search text-orange-d1 text-100',

                refresh: true,
                refreshicon: 'fa fa-sync text-success-m1 text-100',

                view: false,
                viewicon: 'fa fa-search-plus text-grey-d1 text-100',
            },
            {
                // edit record form
                // closeAfterEdit: true,
                width: 320,
                recreateForm: true,
                beforeShowForm: function (e) {
                    style_edit_form(e[0]);
                }
            },
            {
                // new record form
                width: 320,
                closeAfterAdd: true,
                recreateForm: true,
                viewPagerButtons: false,
                beforeShowForm: function (e) {
                    style_edit_form(e[0]);
                }
            },
            {
                // delete record form
                recreateForm: true,
                beforeShowForm: function (e) {
                    style_delete_form(e[0]);
                },
                onClick: function (e) {
                }
            },
            {
                // search form
                recreateForm: true,
                afterShowSearch: function (e) {
                    style_search_form(e[0]);
                },
                afterRedraw: function (e) {
                },
                multipleSearch: true,

                // multipleGroup:true,
                // showQuery: true
            },
            {
                // view record form
                recreateForm: true,
                beforeShowForm: function (e) {
                    style_edit_form(e[0]);
                    e[0].querySelector('tr[data-rowpos="1"]').classList.add('d-none');
                }
            }
        )


    // navGrid buttons don't work on touch devices, so trigger them on 'touch'
    $(pager_selector).find('.navtable .action-btn').on('touchend', function () {
        $(this).triggerHandler('click')
    })


    ////////////////////////////////

    // change buttons colors in modals
    function style_edit_form(form) {
        form = $(form)
        // enable datepicker on "sdate" field
        form.find('input[name=sdate]').attr('type', 'date')
        form.find('input[name=stock]').attr('style', 'width: 1.25rem !important;')

        // update buttons classes
        var buttons = form.parent().next().find('.EditButton .fm-button').attr('href', '#')// to disable for Bootstrap's "a:not([href])" style
        buttons.eq(0).removeClass('btn-default').addClass('btn-lighter-success border-2 text-600')
        buttons.eq(1).removeClass('btn-default').addClass('btn-lighter-grey border-2')

        //update next / prev buttons
        form.parent().next()
            .find('.navButton .fm-button')
            .removeClass('btn-default')
            .addClass('px-25 mx-2px btn-outline-secondary btn-h-outline-primary btn-a-outline-primary radius-round')
    }

    function style_delete_form(form) {
        form = $(form)
        var buttons = form.parent().next().find('.EditButton .fm-button').attr('href', '#')
        buttons.eq(0).removeClass('btn-default').addClass('btn-lighter-danger border-2 text-600')
        buttons.eq(1).removeClass('btn-default').addClass('btn-lighter-grey border-2')
    }

    function style_search_form(form) {
        form = $(form)
        var dialog = form.closest('.ui-jqdialog')
        var buttons = dialog.find('.EditTable').addClass('text-white')

        buttons.find('.EditButton a').removeClass('btn-default')
        buttons.find('.EditButton a[id*="_reset"]').addClass('btn-default')
        buttons.find('.EditButton a[id*="_query"]').addClass('btn-grey')
        buttons.find('.EditButton a[id*="_search"]').addClass('btn-primary')
    }


    // enable tooltips
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body', trigger: 'hover'})
        $(table).find('.ui-pg-div').tooltip({container: 'body', trigger: 'hover'})
    }

    //var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow')


    $("#search").click(function () {
        $(grid_selector).jqGrid().trigger("reloadGrid");
    });


    $("#remarkSave").click(function () {
        var clueId = $("#clueId").val()
        var remark = $("#remarkText").val()

        $.ajax({
            type: 'post',
            url: "../userClue/update",
            datatype: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                remark: remark,
                id: clueId
            }),
            success: function (data) {
                if (data.state != "Success") {
                    alert(JSON.stringify(data));
                    return;
                }
                //刷新脚本
                $("#clueId").val("")
                $("#remarkText").val("")
                $('#exampleModal2').modal('hide');
                $(grid_selector).jqGrid().trigger("reloadGrid");
            }
        })

    });

})
