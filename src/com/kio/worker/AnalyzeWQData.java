package com.kio.worker;

import com.kio.dao.BizComputerOutDao;
import com.kio.entity.output.DataEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeWQData {
    private static ArrayList list = new ArrayList();

    public AnalyzeWQData(){}

    public static ArrayList getList(String sTaskCode){
        //sTaskCode的水质.dat所有记录
        ArrayList<DataEntity> allRecords = BizComputerOutDao.getAllRecords(sTaskCode);
        // map<k, v> k是一个节点号，v是存储该节点号数据的list。如k:97.08900, v:[0.00006, 0.00010, 0.00015, 0.00017...]
        Map<String, Float[]> map = new HashMap<>();

        // 非零数据的节点号的数量，随着污染物后移，节点号增加
        int mapNum = 0;
        // 时间点的数量，y轴的size
        int timeNum = allRecords.size();
        ArrayList<Float> timeList = new ArrayList<>();
        ArrayList<String> nodeList = new ArrayList<>();

        for(DataEntity records : allRecords){
            timeList.add(records.getTime());
            String str = records.getList().substring(0, records.getList().length()-1);  //去除结尾多余的一个“;”
            //例：str1 = [[97.08900, 0.00006], [97.17400, 0.00041], [97.21400, 0.00245]...]
            String[] str1 = str.trim().split(";");

            for(String s : str1){
                //例：str2 = [97.08900, 0.00006]
                String[] str2 = s.trim().split(",");
                if(map.containsKey(str2[0])){
                    map.get(str2[0])[mapNum] = Float.parseFloat(str2[1]);
                } else {
                    Float[] floatsList = new Float[timeNum];
                    for(Float e: floatsList)
                        e = (float)0;
                    nodeList.add(str2[0]);
                    floatsList[mapNum] = Float.parseFloat(str2[1]);
                    map.put(str2[0], floatsList);
                    mapNum++;
                }
            }
        }

        Float[][] wqData = new Float[timeNum][map.size()];
        for(int i = 0; i < wqData.length; i++){
            for(int j = 0; j < wqData[0].length; j++){
                wqData[i][j] = map.get(nodeList.get(i))[j];
            }
        }

        //list.addAll(timeList, nodeList, wqData);
        return list;
    }
}
