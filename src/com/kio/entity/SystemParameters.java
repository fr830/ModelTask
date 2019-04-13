package com.kio.entity;

import net.sf.json.JSON;
import org.json.JSONObject;

import java.io.*;

/**
 * 系统配置实体类
 * @author Kio
 */
public class SystemParameters {
    private int maxThreadNum;
    private String model_1_path;
    private String model_2_path;
    private String model_3_path;

    /**
     * 从系统配置文件中生成一个系统配置实体类
     * @return 配置文件中读取到的系统配置
     * @throws IOException 当读取配置文件失败时抛出
     */
    public static synchronized SystemParameters loadFromFile() throws IOException {
        //类路径
        String classPath = SystemParameters.class.getResource("/").getPath();
        File file = new File(new File(classPath).getParent()+"/SystemParameter.json");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder data = new StringBuilder();
        String line;
        while((line = reader.readLine())!=null){
            data.append(line);
        }
        reader.close();
        JSONObject object = new JSONObject(data.toString());
        return new SystemParameters(object.getInt("maxThreadNum"),
                object.getString("model_1_path"),
                object.getString("model_2_path"),
                object.getString("model_3_path"));
    }

    /**
     *
     * @param maxThreadNum 最大并发数
     * @param model_1_path 模型一路径
     * @param model_2_path 模型二路径
     * @param model_3_path 模型三路径
     */
    public SystemParameters(int maxThreadNum,
                            String model_1_path,
                            String model_2_path,
                            String model_3_path) {
        this.maxThreadNum = maxThreadNum;
        this.model_1_path = model_1_path;
        this.model_2_path = model_2_path;
        this.model_3_path = model_3_path;
    }

    /**
     * 将该配置写入配置文件中
     */
    public synchronized void commit() throws IOException {
        //类路径
        String classPath = SystemParameters.class.getResource("/").getPath();
        File file = new File(new File(classPath).getParent()+"/SystemParameter.json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        JSONObject object = new JSONObject(this);
        writer.write(object.toString());
        writer.flush();
        writer.close();
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public String getModel_1_path() {
        return model_1_path;
    }

    public void setModel_1_path(String model_1_path) {
        this.model_1_path = model_1_path;
    }

    public String getModel_2_path() {
        return model_2_path;
    }

    public void setModel_2_path(String model_2_path) {
        this.model_2_path = model_2_path;
    }

    public String getModel_3_path() {
        return model_3_path;
    }

    public void setModel_3_path(String model_3_path) {
        this.model_3_path = model_3_path;
    }
}
