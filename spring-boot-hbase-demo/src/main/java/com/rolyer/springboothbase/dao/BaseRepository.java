package com.rolyer.springboothbase.dao;

import java.util.List;

/**
 * @ClassName: City
 * @Package com.rolyer.springboothbase.dao
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/30 14:50
 * @Version V1.0
 */
public interface BaseRepository<T> {
    /**
     * 保存
     * @param entity
     * @return
     */
    T create(T entity);

    boolean delete(String rowName);

    T update(T entity);

    /**
     * 查询所有
     * @param
     * @return
     */
    List<T> findAll();
}
