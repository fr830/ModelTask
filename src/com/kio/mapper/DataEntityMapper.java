package com.kio.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.kio.entity.output.DataEntity;

/**
 * 结果行匹配类
 *
 * @author KIO
 */
public interface DataEntityMapper {
    /**
     * 插入一条结果
     *
     * @param dataEntity 结果
     * @return 是否插入成功
     */
    boolean insertAllData(DataEntity dataEntity);

    /**
     * 通过任务编号获取该任务结果中编号大于from的结果行列表
     *
     * @param uuid 任务编号
     * @param from 结果行编号
     * @return 结果行列表
     */
    ArrayList<DataEntity> getItems(@Param("uuid") String uuid, @Param("from") int from);

    ArrayList<DataEntity> getAllRecords(@Param("uuid") String uuid);

    boolean deleteDataEntity(String uuid);
}
