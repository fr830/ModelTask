package com.calldll.utils;

import com.kio.listener.Init;
import net.sf.json.JSON;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 模型三参数文件修改工具类
 * <h3><a href="#">M检测数据输入</a>文件说明</h3>
 * <code>
 * /**
 * *实际投放情况
 * /
 * 计算位置及时间浓度
 * -335.3793	！已溯源得到的源位置
 * 0	9	15	！已溯源得到的排放时间
 * 9	30	！初始监测时间
 * 1	!监测点数
 * <p>
 * //1508  8代表共输入8条数据
 * 1508	8
 * <p>
 * /**
 * *检测到的数据
 * /
 * 0	0.000 	！s	mg/L 10	50	时 分
 * 6000	0.000	！11	10
 * 12000	0.003	！12	50
 * 17400	5.74	！14	20
 * 20400	7.01	！15	10
 * 22200	7.12	！15	40
 * 27000	6.28	！17	0
 * 41400	3.24	！21	0
 * </code>
 */
public class Task3ParameterHelper {
    /**
     * @param sTaskCode   需要修改的任务编号
     * @param paramString 所有参数信息
     */
    public static void updateParameter(String sTaskCode, String paramString) throws FileNotFoundException {
        String path = Init.PARAMETERS.getModel_3_path() + File.separator +
                "workfile" + sTaskCode + File.separator +
                "M监测数据输入.dat";
        if (!new File(path).exists())
            throw new FileNotFoundException("没有找到参数输入文件");
        JSONObject parameters = new JSONObject(paramString);
        String startTime = parameters.getString("startTime");//开始时间
        JSONArray param = parameters.getJSONArray("param");//节点数据
        int nodeNum = param.length();
        PrintWriter out = new PrintWriter(new FileOutputStream(path));
        out.println("计算位置及时间浓度");
        out.println("-335.3793");
        out.println("0  9  15");
        out.println(startTime.substring(0, 2) + "  " + startTime.substring(2, 4));
        out.println(nodeNum);
        for (int i = 0; i < param.length(); i++) {
            JSONObject node = param.getJSONObject(i);
            JSONArray nodeData = node.getJSONArray("data");
            out.println(
                    node.getString("code") + "   " + nodeData.length());
            for (int j = 0; j < nodeData.length(); j++) {
                JSONObject nodeDataItem = nodeData.getJSONObject(j);
                out.println(nodeDataItem.getString("time") + "   " + nodeDataItem.getString("weight"));
            }
        }
        out.flush();
        out.close();
    }
}
