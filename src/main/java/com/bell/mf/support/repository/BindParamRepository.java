package com.bell.mf.support.repository;

import com.bell.mf.support.bind.BindParam;

import java.util.List;

/**
 * 参数绑定仓库
 * @author bell.zhouxiaobing
 * @since 1.5.5
 */
public interface BindParamRepository {

    /**
     * 获取参数绑定集
     * @return List<BindParam>
     */
    List<BindParam> getBindParamList();

    /**
     * 添加参数绑定类
     * @param bindParam
     * @return BindParam
     */
    boolean addBindParam(BindParam bindParam);

}