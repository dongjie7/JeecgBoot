package org.jeecg.modules.estar.bs.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.jeecg.modules.estar.bs.dto.DataSourceDto;

/**
 * @Description: IJdbcService
 * @Author: nbacheng
 * @Date:   2023-03-16
 * @Version: V1.0
 */
public interface IJdbcService {

    /**
     * 删除数据库连接池
     *
     * @param id
     */
    void removeJdbcConnectionPool(Long id);


    /**
     * 获取连接
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    Connection getPooledConnection(DataSourceDto dataSource) throws SQLException;

    /**
     * 测试数据库连接  获取一个连接
     *
     * @param dataSource
     * @return
     * @throws ClassNotFoundException driverName不正确
     * @throws SQLException
     */
    Connection getUnPooledConnection(DataSourceDto dataSource) throws SQLException;
}
