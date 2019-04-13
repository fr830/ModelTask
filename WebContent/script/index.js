var app = new Vue({
	el: '#app',
	data() {
		return {
			tableData: [],
			option:{
					tooltip: {},
					legend: {
						data: ['服务端使用量','客户端使用量','合计量']
					},
					xAxis: {
						data: ["3/8", "3/9", "3/10", "3/11", "3/12", "3/13"]
					},
					yAxis: {},
					series: [{
						name: '服务端使用量',
						type: 'line',
						data: [5, 20, 36, 10, 10, 20]
					},{
						name: '客户端使用量',
						type: 'line',
						data: [25, 80, 46, 10, 230, 120]
					},{
						name: '合计量',
						type: 'bar',
						data: [30, 100, 82, 20, 240, 140]
					}]
				}			
		}
	},
	methods:{
		changeMenu(index){
			if(index==1)
				window.location.href="index.html"
			else if(index==2)
				window.location.href="models.html" 
		}
	},
	mounted() {
		
		var weekChart = echarts.init(document.getElementById('weekNum'));
		var weekChart2 = echarts.init(document.getElementById('weekNum2'));
		
		weekChart.setOption(this.option);
		weekChart2.setOption(this.option);
		
		const that =this
			axios.get('getSystemLog?from=' + 0)
			.then(function(response){
				that.tableData = response.data;
				setInterval(function(){
					var cid = 0;
					if(that.tableData.length!=0)
						cid = that.tableData[0].iId
					axios.get('getSystemLog?from=' + cid)
					.then(function(response){
						that.tableData = response.data.concat(that.tableData);
					}).catch(function(error){
						// that.$message.error("加载失败！")
					})
				},2000)
			}).catch(function(error){
				// that.$message.error("加载失败！")
			})
	}
})
