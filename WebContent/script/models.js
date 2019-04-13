var app = new Vue({
	el: '#app',
	data() {
		return {
			lock: false,
			creating: false,
			modelType: 1,
			executing: false, //是否正在发送执行任务的请求
			progress: 0,
			resultData: [],
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
			tskId: "",
			fTskId: "",
			lockModelType: 1,
			lockTskId: ""
		}
	},
	methods: {
		helpExecute() {
			this.$alert('先选择要使用的模型，然后在下方输入参数，系统会自动根据模型计算得出结果，系统的运行结果将在最下方展示。', '提示', {
				confirmButtonText: '确定',
				type: 'info'
			})
		},
		changeMenu(index){
			if(index==1)
				window.location.href="index.html"
			else if(index==2)
				window.location.href="models.html" 
		},
		createTask() {
			if (this.creating) return;
			if (this.lock) {
				this.$message.error("任务正在执行中，请稍后操作！");
				return;
			}

			this.creating = true
			this.lockTskId = ""
			this.resultData = []
			this.progress = 0

			const that = this
			axios.get('createTask?iTaskType=' + this.modelType)
				.then(function(response) {
					const result = response.data;
					if (result.code == 0) {
						that.tskId = result.msg
						that.fTskId = result.msg
					} else {
						that.$message.error(result.msg + "   错误码:" + result.code);
					}
					that.creating = false
				})
				.catch(function(error) {
					that.$message.error("" + error);
					that.creating = false
				});
		},
		executeTask() {
			if (this.lock) {
				this.$message.error("任务正在执行中，请稍后操作！");
				return
			}
			if (this.executing) return;
			this.lock = true
			
			this.executing = true
			this.resultData = [] //运行信息清零
			this.lockTskId = this.fTskId //运行中固定的任务编号
			this.lockModelType = this.modelType //运行中固定的任务类型

			const that = this
			axios.get('executeTask?iTaskType=' + this.modelType + '&sTaskCode=' + this.fTskId)
				.then(function(response) {
					that.executing = false
					const result = response.data
					if (result.code == 0) {
						//开始读取进度
						if (that.modelType == 1)
							that.getTaskProgress1();
						else if (that.modelType == 2)
							that.getTaskProgress2();
						else if (that.modelType == 3){
							that.progress = 0 //进度清零
							that.getTaskProgress3();
						}		
					}else if(result.code == 4){
						that.$message.warning(result.msg);
						setTimeout(function(){
							if (that.modelType == 1)
								that.getTaskProgress1();
							else if (that.modelType == 2)
								that.getTaskProgress2();
							else if (that.modelType == 3)
								that.getTaskProgress3();
						},10000);
						
					} else {
						that.lock = false;
						that.$message.error(result.msg + "   错误码:" + result.code);
					}
					that.gettingTskId = false
				})
				.catch(function(error) {
					that.$message.error("" + error);
					that.executing = false
					that.lock = false
				});
		},
		getTaskProgress1() {
			
			const that = this
			axios.get('getTaskProgress?iTaskType=1&sTaskCode=' + that.lockTskId)
				.then(function(response) {
					const result = response.data
					that.progress = result.progress
					if (result.code == 0) {
						that.resultData = that.resultData.concat(result.data)
					} else if (result.code != 1) {
						that.$message.error("" + rseult.msg + "错误码:" + result.code);
					}
					if (!result.isEnd) {
						setTimeout(function() {
							that.getTaskProgress1()
						}, 500);
					} else {
						that.executing = false
						that.lock = false
						 that.$notify({
					          title: '提示',
					          message: '任务执行完毕!',
					          type: 'success'
					        });
					}
				})
				.catch(function(error) {
					that.lock = false
					that.$message.error("" + error);
				})
		},
		getTaskProgress3() {
			const that = this
			axios.get('getTaskProgress?iTaskType=3&sTaskCode=' + that.lockTskId)
				.then(function(response) {
					const result = response.data
					if (result.code == 0) {
						that.progress = 100
						that.resultData = result.msg
						that.lock = false
						 that.$notify({
					          title: '提示',
					          message: '任务执行完毕!',
					          type: 'success'
					        });
					} else if (result.code == 1) {
						that.resultData = []
						setTimeout(function() {
							that.progress += Math.floor(90 * Math.random() + 1);
							if (that.progress >= 90) that.progress = 90
							that.getTaskProgress3()
						}, 1000);
					} else if (result.code == 2) {
						that.lock = false
						that.$message.error("" + error);
					}
				})
				.catch(function(error) {
					that.$message.error("" + error);
				});
		}
	},
	mounted() {

	}
})
