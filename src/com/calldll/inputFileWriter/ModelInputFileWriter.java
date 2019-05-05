package com.calldll.inputFileWriter;

import java.util.Map;

import com.kio.entity.input.FskInfo;
import com.kio.entity.input.JzzInfo;
import com.kio.entity.input.ModelParam;
import com.kio.entity.input.TszInfo;


public interface ModelInputFileWriter {
	
	/**
	 * 组装模型的输入文件 
	 * @param param 模型运行参数
	 * @param jzzMap 节制闸水情数据
	 * @param fskMap 分水口水情数据
	 * @param tszMap 退水闸水情数据
	 * @param outputFile 产生的输出文件名（含路径）
	 * @return true -- 生成成功， false -- 生成失败
	 * @throws Exception 过程中产生的各种异常
	 */
	public boolean composeInputFile(ModelParam param,
                                    Map<String, JzzInfo> jzzMap,
                                    Map<String, FskInfo> fskMap,
                                    Map<String, TszInfo> tszMap,
                                    String outputFile) throws Exception;
	
}
