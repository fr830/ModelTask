<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title></title>
<!--
		<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>

		<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
		<script src="https://unpkg.com/element-ui/lib/index.js"></script>
-->

<script src="./script/vue.js"></script>
<link rel="stylesheet" href="./style/element.css">
<script src="./script/element.js"></script>

<!--阿里图标-->
<link rel="stylesheet"
	href="https://at.alicdn.com/t/font_1062079_o4sgewsdxth.css">

<!-- axios -->
<script src="./script/axios.js"></script>
<style>
body {
	background-color: #eee;
}

.add-icon {
	font-size: 14px;
	color: white;
}

#app {
	width: 60%;
	margin: 20px auto;
}

.runtimeBoard {
	border: none;
	margin: 0 -20px;
}

.feedbackTaskId {
	font-size: 14px;
	color: #ee2222;
	display: inline-block;
	margin: 0px 10px;
}

.marginLr {
	margin: 0 20px;
}

.result {
	background-color: #fffff6;
}

.dataBoard {
	max-height: 400px;
	overflow-y: auto;
	margin: -20px;
	padding: 20px;
}

.dataBoard::-webkit-scrollbar { /*滚动条整体样式*/
	width: 4px; /*高宽分别对应横竖滚动条的尺寸*/
	height: 1px;
}

.dataBoard::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
	-webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
	background: #ccccc6;
}

.dataBoard::-webkit-scrollbar-track { /*滚动条里面轨道*/
	-webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
	border-radius: 10px;
	background: #aaa;
}
</style>

</head>
<body>
	<div id="app">
		<el-card class="box-card">
		<div slot="header" class="clearfix">
			<span style="color: #888;">任务创建</span>
		</div>
		<el-select v-model="modelType" placeholder="请选择模型"> <el-option
			v-for="item in options" :key="item.value" :label="item.label"
			:value="item.value"> </el-option> </el-select> <el-button type="primary"
			class="marginLr" @click="createTask"> <i
			v-if='!gettingTskId' class="iconfont icon-zengjia add-icon"></i> <i
			v-else class="el-icon-loading"></i>&nbsp;&nbsp;创建任务</el-button> <span
			v-if='showGettedTaskId' class="feedbackTaskId">您获得的任务编号为<strong>{{gettedTskId}}</strong></span>

		</el-card>
		<br>
		<el-card class="box-card">
		<div slot="header" class="clearfix">
			<span style="color: #888;">执行参数</span>
			<el-button style="float: right; padding: 3px 0" @click="helpExecute"
				type="text"> <i class="iconfont icon-bangzhu"></i></el-button>
		</div>
		<el-form ref="form" :model="form" label-width="90px"> <el-form-item
			label="任务编号"> <el-input v-model="form.fTskId"></el-input>
		</el-form-item>
		<div v-if="modelType=='1'">水动力模型</div>
		<div v-if="modelType=='2'">水质模型</div>

		<div v-if="modelType=='3'">
			<el-form-item label="固定参数"> <el-input
				v-model="form.name1"></el-input> </el-form-item>
			<el-form-item label="实时参数"> <el-input
				v-model="form.name2"></el-input> </el-form-item>
		</div>
		<el-button type="warning" @click="executeTask"> <i
			v-if='!executing' class="iconfont icon-xitongyunxing add-icon"></i> <i
			v-else class="el-icon-loading"></i>&nbsp;&nbsp;立即执行</el-button> </el-form> <br>
		<transition name="el-zoom-in-top"> <el-card
			class='runtimeBoard' shadow='never' v-if='execute' class="box-card"
			element-loading-spinner="el-icon-loading"
			element-loading-background="rgba(0, 0, 0, 0.6)">
		<div slot="header" class="clearfix">
			<span style="color: #888;">运行情况</span>
		</div>
		<el-progress :text-inside="true" :stroke-width="18" status="success"
			:percentage="progress"></el-progress> <br>
		<el-card shadow="hover" class="box-card result">
		<div slot="header" class="clearfix">
			<span style="color: #88a">{{progressText}}</span>
		</div>
		<div class="dataBoard" v-loading="loading"
			element-loading-spinner="el-icon-loading"
			element-loading-background="rgba(0, 0, 0, 0)"
			style="minHeight: 50px;minWidth=100px" element-loading-text="加载中">
			<div v-for="msg in progressData" :key="msg11" class="text item">{{msg}}</div>

		</div>
		</el-card> </el-card> </transition> </el-card>
		<br> <br>
	</div>
</body>
<script>
		var app = new Vue({
			el: '#app',
			data() {
				return {
					progress:0,
					progressData:[],
					progressText:"",
					gettingTskId: false,
					gettedTskId: '---------',
					showGettedTaskId: false,
					modelType: 3,
					execute: false,
					executing:false,
					loading:false,
					form: {
						fTskId: '',
						name1: '',
						name2: ''
					},
					options: [{
						value: 1,
						label: '水动力模型'
					}, {
						value: 2,
						label: '水质模型'
					}, {
						value: 3,
						label: '追踪溯源模型'
					}],
				}
			},
			methods: {
				helpExecute() {
					this.$alert('先选择要使用的模型，然后在下方输入参数，系统会自动根据模型计算得出结果，系统的运行结果将在最下方展示。', '提示', {
						confirmButtonText: '确定',
						type: 'info'
					})
				},
				createTask() {
					//如果正在请求创建任务，则此按钮不响应
					if(this.gettingTskId)return;
					//如果正在運行，則此按鈕提示等待
					if(this.executing){
						that.$message.error("請耐心等待任務執行完成后再進行新建任務");
						return;
					}
					this.progressText='等待执行'
					this.gettingTskId = true
					const that = this
					that.progressData = []
					
					//向服务器发送请求
					axios.get('createTask?iTaskType='+this.modelType)
					  .then(function (response) {
						  const result = response.data;
						if(result.code==0){
							that.gettedTskId = result.msg
							that.form.fTskId = result.msg
							that.showGettedTaskId = true
						}else{
							that.$message.error(result.msg+"   错误码:"+result.code);
						}
						that.gettingTskId = false
					  })
					  .catch(function (error) {
					    that.$message.error(""+error);
						that.gettingTskId = false
					  });
				},
				executeTask() {					
					if(this.executing)return;
					this.progress=0
					this.executing = true
						const that = this
						that.progressData = []
						var sTaskCode = this.form.fTskId
						
						axios.get('executeTask?iTaskType='+this.modelType+'&sTaskCode='+this.form.fTskId)
					  .then(function (response) {
						  const result = response.data
						if(result.code==0){
							that.execute = true
							//开始读取进度
							that.getTaskProgress(sTaskCode)
							
						}else{
							that.$message.error(result.msg+"   错误码:"+result.code);
						}
						that.gettingTskId = false
					  })
					  .catch(function (error) {
					    that.$message.error(""+error);
					    that.executing = false
					  });
				},
				getTaskProgress(sTaskCode){
					
					const that = this
					this.loading = true
					if(this.modelType==1){
						this.getTaskProgress1(sTaskCode)
						return
					}
					axios.get('getTaskProgress?iTaskType='+this.modelType+'&sTaskCode='+sTaskCode)
					  .then(function (response) {
						  const result = response.data
						  if(result.code==0){
							  that.loading = false
							  that.executing = false
							  that.progress = 100
							  that.progressText = "执行结果"
							  that.progressData = result.msg
						  }else if(result.code==1){
							  that.progressText = "程序正在执行，请稍侯"
							  that.progressData = []
							 setTimeout(function(){
								 that.progress+=Math.floor(60*Math.random()+1);
								  if(that.progress>=90)that.progress=90
								 that.getTaskProgress(sTaskCode)
								 },500);
						  }else if(result.code==2){
							  that.loading = false
							  that.$message.error(""+error);
						  }
					  })
					  .catch(function (error) {
						  that.loading = false
					    that.$message.error(""+error);
					  });
				},
				getTaskProgress1(sTaskCode){
					const that = this
					this.loading = true
					axios.get('getTaskProgress?iTaskType='+this.modelType+'&sTaskCode='+sTaskCode)
					  .then(function (response) {
						  const result = response.data
						  that.progress = result.progress;
						  that.loading = false
						  if(result.code==0){
							  that.progressText = "取到新數據"
							  that.progressData = that.progressData.concat(result.data)
						  }else if(result.code==1){
							  that.progressText = "程序正在执行，请稍侯"
						  }else if(result.code==2){
							  that.loading = false
							  that.$message.error(""+error);
						  }
						  if(result.progress<100){
							  setTimeout(function(){
									 that.getTaskProgress1(sTaskCode)
								},2000);
						  }else{
							  that.executing = false
						  }						 
					  })
					  .catch(function (error) {
						  that.loading = false
					    that.$message.error(""+error);
					  });
				}
			},
			watch:{
				modelType(){
					this.progressData=[]
					this.execute = false
					this.showGettedTaskId = false
					this.form.fTskId = ""
				}
			},
			mounted() {

			}
		})
	</script>

</html>
