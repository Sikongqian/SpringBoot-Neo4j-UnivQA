



$(function () {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    //modal绑定hide事件
    $('#modalAdd').on('hide.bs.modal', function () {
        reset();
    })
    $('#modalEdit').on('hide.bs.modal', function () {
        reset();
    })
    

  $("#jqGrid").jqGrid({
	  
	url: 'recommend/list',
    datatype: "json",
    colModel: [
    	 
        {label: '课程名', name: 'cname', index: 'cname', sortable: false,  width: 80,  },
        {label: '开课学校', name: 'university', index: 'university', sortable: false,  width: 40},
        {label: '报名人数', name: 'num', index: 'num', sortable: false,  width: 35},
        {label: '课程链接', name: 'clink', index: 'clink', sortable: false,  width: 130,  formatter: function(value, options, row){
		    return '<a href='+value+'>'+value+'</a>'}
  	 },

        ],
    height: 485,
    rowNum: 10,
    rowList: [10, 30, 50],
    styleUI: 'Bootstrap',
    loadtext: '信息读取中...',
    rownumbers: true,
    rownumWidth: 50,
    autowidth: true,
    multiselect: true,
    pager: "#jqGridPager",
    jsonReader: {
        root: "data.list",
        page: "data.currPage",
        total: "data.totalPage",
        records: "data.totalCount"
    },
    prmNames: {
        page: "page",
        rows: "limit",
        order: "order"
    },
    gridComplete: function () {
        //隐藏grid底部滚动条
        $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
});

$(window).resize(function () {
    $("#jqGrid").setGridWidth($(".card-body").width());
});

  
});









/**
 * jqGrid重新加载
 */
function reload() {
    reset();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}