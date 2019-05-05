package com.calldll.inputFileWriter;

import java.util.List;
import java.util.Map;

import com.kio.entity.input.FskInfo;
import com.kio.entity.input.JzzInfo;
import com.kio.entity.input.ModelParam;
import com.kio.entity.input.TszInfo;

/**
 * 常规调度模式下的输入文件组装类
 * 常规模式下分水计划取当前流量值，且计划分水量保持不变
 *        节制闸的闸前闸后水位取当前实时值
 * @author Frank
 * 
 */
public class NormalModelInputFileWriter implements ModelInputFileWriter {
	/**   模型输入文件	 **/
	private String modelInputFile = null; 
	
	public NormalModelInputFileWriter(String filename) {
		this.modelInputFile = filename;
	}
	
	
	/**
	 * 根据模型参数文件模板modelInputFile，以及模型参数、节制闸、分水口水情数据生成新的输入文件
	 * @param param -- 模型运行参数对象，包含模型的参数
	 * @param jzzMap -- 节制闸数据对象映射表，存储的数据例如 [{jctid, JzzInfo}, {jctid, JzzInfo}, ......]
	 * @param fskMap -- 分水口数据对象映射表，存储的数据例如[{jctid, FskInfo}, {jctid, FskInfo}, ......]
	 * @param tszMap -- 退水闸数据对象映射表，存储的数据例如[{jctid, TszInfo}, {jctid, TszInfo}, ......]
	 * @param outputFile -- 生成的新模型参数文件完整路径名，如 d:\input\...
	 * @return 操作成功与否 
	 * @throws Exception 
	 */
	public boolean composeInputFile(ModelParam param,
                                    Map<String, JzzInfo> jzzMap,
                                    Map<String, FskInfo> fskMap,
                                    Map<String, TszInfo> tszMap,
                                    String outputFile) throws Exception {
		boolean result = false;
		try {
			FileReadTool tool = new FileReadTool(modelInputFile);
			List<String> lines = tool.readFile();
	
			if (lines != null && lines.size() > 0) {
				int rowIndex = 0;
				while (rowIndex < lines.size()) {
					switch (rowIndex) {
					case 0: // 第1行值不变 //200 0 1 1.00E-09 1.00E-09
						break;
					case 1: // 第2行 0	40000	300	1	2
						String[] word1 = lines.get(rowIndex).split("\t");
						word1[1] = String.valueOf(param.getLastTime());
						word1[2] = String.valueOf(param.getStepLength());
						word1[4] = String.valueOf(param.getProcessType());
						lines.set(1, FileReadTool.join("\t", word1));
						break;
					case 2: // 第3行 1 6 98 1000
						//String[] word2 = lines.get(2).split("\t"); // 将一行数据拆分为多个单值
						String[] word2 = {"", "", "", ""};
						word2[0] = String.valueOf(param.getPollutedType());
						word2[1] = String.valueOf(param.getPollutedJctIndex());
						word2[2] = String.valueOf(param.getPollutedPosition());
						word2[3] = String.valueOf(param.getPollutedQuality());
						lines.set(2, FileReadTool.join("\t", word2));  // 再合并回一行
						break;
					default:
						String[] word = lines.get(rowIndex).split("\t"); // 将一行数据拆分为多个单值
						
					
						/* 4开头，表示这一行是分水口，需要替换  当前时间点	当前分水流量值	分水计划时间	分水计划流量  */
						if (word.length >= 10 && "4".equals(word[0])) { // 4 3 肖楼分水口 4.196 4.2 0 0 0 3600 0
							String jctid = word[1];
							int currentTime = fskMap.containsKey(jctid) ? fskMap.get(jctid).getCurrentTime() : -1; // 取当前时间，如果没有，则取-1
							float currentflow = fskMap.containsKey(jctid) ? fskMap.get(jctid).getCurrentFlow() : -1; // 取当前分水流量，如果没有，则取-1
							int expectTime = fskMap.containsKey(jctid) ? fskMap.get(jctid).getExpectTime() : -1; // 取计划时间，如果没有，则取-1
							float expectflow = fskMap.containsKey(jctid) ? fskMap.get(jctid).getExpectFlow() : -1; // 取计划分水流量，如果没有，则取-1
							word[6] = String.valueOf(currentTime);
							word[7] = String.valueOf(currentflow);
							word[8] = String.valueOf(expectTime);
							word[9] = String.valueOf(expectflow);
							lines.set(rowIndex, FileReadTool.join("\t", word));  // 再合并回一行
						}
						
						/* 14开头，表示这一行是退水闸，需要替换  当前时间点	当前分水流量值	分水计划时间	分水计划流量  */
						else if (word.length >= 10 && "14".equals(word[0])) { // 14	9	刁河退水闸	14.516	14.526	0	0	0	3600	0
							String jctid = word[1];
							int currentTime = tszMap.containsKey(jctid) ? tszMap.get(jctid).getCurrentTime() : -1; // 取当前时间，如果没有，则取-1
							float currentflow = tszMap.containsKey(jctid) ? tszMap.get(jctid).getCurrentFlow() : -1; // 取当前分水流量，如果没有，则取-1
							int expectTime = tszMap.containsKey(jctid) ? tszMap.get(jctid).getExpectTime() : -1; // 取计划时间，如果没有，则取-1
							float expectflow = tszMap.containsKey(jctid) ? tszMap.get(jctid).getCurrentFlow() : -1; // 取计划分水流量，如果没有，则取-1
							word[6] = String.valueOf(currentTime);
							word[7] = String.valueOf(currentflow);
							word[8] = String.valueOf(expectTime);
							word[9] = String.valueOf(expectflow);
							lines.set(rowIndex, FileReadTool.join("\t", word));  // 再合并回一行
						}
						
						/* 6开头，表示这一行是节制闸信息，需要替换	闸前水位	闸后水位	过闸流量 */
						else if (word.length >= 10 && "6".equals(word[0])) { // 6 13 刁河渡槽进口节制闸 14.626 14.646 146.8 146.45 147.18 147.66 146.8 1 2 140.7 13 8 11 0.85 0 0
							String jctid = word[1];
							float frontwl = jzzMap.containsKey(jctid) ? jzzMap.get(jctid).getFrontwl() : -1;
							float backwl = jzzMap.containsKey(jctid) ? jzzMap.get(jctid).getBackwl() : -1;
							float afterflow = jzzMap.containsKey(jctid) ? jzzMap.get(jctid).getAfterFlow() : -1;
							word[8] = String.valueOf(frontwl);
							word[9] = String.valueOf(backwl);
							word[12] = String.valueOf(afterflow);
							lines.set(rowIndex, FileReadTool.join("\t", word));  // 再合并回一行
						}
						
						/* 处理最后一行 */
						// 1	1421	给定水位	1199.382	56.5	0	0	0	40	40000	40	0	59
						else if (word.length >= 13 && "1".equals(word[0]) && "1421".equals(word[1])) {
							String jctid = "1421";  // 北拒马河节制闸
							float backwl = jzzMap.containsKey(jctid) ? jzzMap.get(jctid).getBackwl() : -1;
							float flow = jzzMap.containsKey(jctid) ? jzzMap.get(jctid).getAfterFlow() : -1;
							word[8] = String.valueOf(backwl);
							word[10] = String.valueOf(backwl);
							word[12] = String.valueOf(flow);
							lines.set(rowIndex, FileReadTool.join("\t", word));  // 再合并回一行
						}
						
					}
					rowIndex++;
				}
				
				// 写入到输出文件 outputFile中
				tool.writeFile(outputFile, lines);
				result = true;
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}
}
