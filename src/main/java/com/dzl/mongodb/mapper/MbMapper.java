package com.dzl.mongodb.mapper;

import java.util.List;
import java.util.Map;

public interface MbMapper {

    /**
     * 通过主键获取一行数据
     *
     * @return
     */
    Map getById(Long id);

    List<Map> getAll(String name);

    List<Map> getBySql(String sql);
}
