/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { filterObj } from '@/utils/util';
import { deleteAction, getAction } from '@/api/manage'
export const MdaLookupMixin = {
  data(){
    return {
      /* 查询条件 */
      queryParam: {},
			filters: {},
      /* 数据源 */
      dataSource:[],
      /* 分页参数 */
      ipagination:{
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      isorter:{
        column: 'createTime',
        order: 'desc',
      },
      /* 筛选参数 */
      filters: {},
      /* table加载状态 */
      loading:false,
      /* table选中keys*/
      selectedRowKeys: [],
      /* table选中records*/
      selectionRows: [],
      /* 查询折叠 */
      toggleSearchStatus:false,
      /* 高级查询条件生效状态 */
      superQueryFlag:false,
      /* 高级查询条件 */
      superQueryParams:"",
    }
  },
  created() {
    // this.loadData();
    //初始化字典配置 在自己页面定义
    // this.initDictConfig();
  },
  methods:{
		handleOk () {
		    this.$emit('close');
		    this.visible = false;
		},
		handleCancel () {
		    this.$emit('close');
		    this.visible = false;
		},
    loadData(filters) {
      if (!this.url.list) {
        this.$message.error("请设置url.list属性!")
        return
      }
      if (typeof (filters) == "object") {
         this.filters = filters;
      }
      //加载第一页的内容
      this.ipagination.current = 1;

      var params = this.getQueryParams();//查询条件
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
        }
      })
    },
    // initDictConfig(){
    //   console.log("--这是一个假的方法!")
    // },
    // handleSuperQuery(arg) {
    //   //高级查询方法
    //   if(!arg){
    //     this.superQueryParams=''
    //     this.superQueryFlag = false
    //   }else{
    //     this.superQueryFlag = true
    //     this.superQueryParams=JSON.stringify(arg)
    //   }
    //   this.loadData()
    // },
    getQueryParams() {
      //获取查询条件
      let sqp = {};
      var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = "id,";
      this.columns.forEach(function (value) {
        str += "," + value.dataIndex;
      });
      return str;
    },

    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
			// console.dir(selectedRowKeys);
			// console.dir(selectionRows);
    },
		
		onSelect(record, selected, selectedRows, nativeEvent) {
			console.info("szy:onSelect()");
			console.dir(record);
			console.dir(selected);
			console.dir(selectedRows);
			console.dir(nativeEvent);
		},
		
    onClearSelected() {
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    searchQuery() {
      this.loadData(1);
    },
    superQuery() {
      this.$refs.superQueryModal.show();
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1);
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.ipagination = pagination;
      this.loadData();
    },
    handleToggleSearch(){
      this.toggleSearchStatus = !this.toggleSearchStatus;
    },
    modalFormOk() {
      // 新增/修改 成功时，重载列表
      this.loadData();
    },
    handleDetail:function(record){
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title="详情";
      this.$refs.modalForm.disableSubmit = true;
    },
    /* 导出 */
    handleExportXls(){
      let paramsStr = encodeURI(JSON.stringify(this.getQueryParams()));
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`;
      window.location.href = url;
    },
    /* 导入 */
    handleImportExcel(info){
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
      }
      if (info.file.status === 'done') {
        this.$message.success(`${info.file.name} 文件上传成功`);
        this.loadData();
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} 文件上传失败.`);
      }
    },
  }

}