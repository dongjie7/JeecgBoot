package org.jeecg.modules.estar.bs.service.impl;

import org.jeecg.modules.estar.bs.constant.BusinessConstant;
import org.jeecg.modules.estar.bs.constant.JdbcConstants;
import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.DataSetDto;
import org.jeecg.modules.estar.bs.dto.DataSourceDto;
import org.jeecg.modules.estar.bs.entity.BsDataSource;
import org.jeecg.modules.estar.bs.mapper.BsDataSourceMapper;
import org.jeecg.modules.estar.bs.param.ConnectionParam;
import org.jeecg.modules.estar.bs.service.IBsDataSourceService;
import org.jeecg.modules.estar.bs.service.IDataSetParamService;
import org.jeecg.modules.estar.bs.service.IJdbcService;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bs_data_source
 * @Author: nbacheng
 * @Date:   2023-03-14
 * @Version: V1.0
 */

@Service
@Slf4j
public class BsDataSourceServiceImpl extends ServiceImpl<BsDataSourceMapper, BsDataSource> implements IBsDataSourceService {

	@Resource(name = "dataSourceRestTemplate")
    private RestTemplate restTemplate;
	 
	 @Autowired
	 private IJdbcService jdbcService;
	 
	 @Autowired
	    private IDataSetParamService dataSetParamService;
	
	/**
     * 测试 连接
     *
     * @param connectionParam
     * @return
     */
    @Override
    public Result testConnection(ConnectionParam connectionParam) {
        String sourceType = connectionParam.getSourceType();
        String sourceConfig = connectionParam.getSourceConfig();
        DataSourceDto dto = new DataSourceDto();
        dto.setConfig(sourceConfig);
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
                Result elasticresult =  testElasticsearchSqlConnection(dto);
                if(elasticresult.isSuccess()) {
                	break;
                }
                else {
                	return elasticresult;
                }
            case JdbcConstants.MYSQL:
            case JdbcConstants.KUDU_IMAPLA:
            case JdbcConstants.ORACLE:
            case JdbcConstants.SQL_SERVER:
            case JdbcConstants.JDBC:
            case JdbcConstants.POSTGRESQL:
                Result testresult =  testRelationalDb(dto);
                if(testresult.isSuccess()) {
                	break;
                }
                else {	
                	return testresult;
                }
            case JdbcConstants.HTTP:
                Result httpresult =  testHttp(dto);
                if(httpresult.isSuccess()) {
                	break;
                }
                else {
                	return httpresult;
                }
            default:
            	return Result.error("连接失败:" + ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
        }
        log.info("测试连接成功：{}", JSONObject.toJSONString(connectionParam));
        return Result.OK("测试连接成功!");
    }
    
    @Override
    public List<JSONObject> execute(DataSourceDto dto) {
        String sourceType = dto.getType();
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
                return executeElasticsearchSql(dto);
            case JdbcConstants.MYSQL:
            case JdbcConstants.KUDU_IMAPLA:
            case JdbcConstants.ORACLE:
            case JdbcConstants.SQL_SERVER:
            case JdbcConstants.JDBC:
            case JdbcConstants.POSTGRESQL:
                return executeRelationalDb(dto);
            case JdbcConstants.HTTP:
                return executeHttp(dto);
            default:
            	Result.error(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
            	return null;
        }
    }

    /**
     * 执行sql,统计数据total
     *
     * @param dto
     * @return
     */
    @Override
    public long total(DataSourceDto sourceDto, DataSetDto dto) {
        //区分数据类型
        String sourceType = sourceDto.getType();
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
                return 0;
            case JdbcConstants.MYSQL:
                return mysqlTotal(sourceDto, dto);
            default:
            	Result.error(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
            	return -1;
        }

    }

    /**
     * 获取mysql count 和添加limit分页信息
     * @param sourceDto
     * @param dto
     * @return
     */
    public long mysqlTotal(DataSourceDto sourceDto, DataSetDto dto){
        String dynSentence = sourceDto.getDynSentence();
        String sql = "select count(1) as count from (" + dynSentence + ") as gaeaExecute";
        sourceDto.setDynSentence(sql);
        List<JSONObject> result = execute(sourceDto);

        //sql 拼接 limit 分页信息
        int pageNumber = Integer.parseInt(dto.getContextData().getOrDefault("pageNumber", "1").toString());
        int pageSize = Integer.parseInt(dto.getContextData().getOrDefault("pageSize", "10").toString());
        String sqlLimit = " limit " + (pageNumber - 1) * pageSize + "," + pageSize;
        sourceDto.setDynSentence(dynSentence.concat(sqlLimit));
        log.info("当前total：{}, 添加分页参数,sql语句：{}", JSONObject.toJSONString(result), sourceDto.getDynSentence());
        return result.get(0).getLongValue("count");
    }



    public List<JSONObject> executeElasticsearchSql(DataSourceDto dto) {
        analysisHttpConfig(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(dto.getDynSentence(), headers);
        ResponseEntity<JSONObject> exchange = null;
        try {
            exchange = restTemplate.exchange(dto.getApiUrl(), HttpMethod.valueOf(dto.getMethod()), entity, JSONObject.class);
        } catch (Exception e) {
            log.error("error",e);
            Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
        }
        if (exchange.getStatusCode().isError()) {
        	Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, exchange.getBody());
        }
        List<JSONObject> result = null;
        try {
            JSONObject body = exchange.getBody();
            //解析es sql数据
            if (null == body) {
                return null;
            }
            JSONArray columns = body.getJSONArray("columns");
            JSONArray rows = body.getJSONArray("rows");
            result = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                JSONArray row = rows.getJSONArray(i);
                JSONObject jsonObject = new JSONObject();
                for (int j = 0; j < row.size(); j++) {
                    String name = columns.getJSONObject(j).getString("name");
                    String value = row.getString(j);
                    jsonObject.put(name, value);
                }
                result.add(jsonObject);
            }
        } catch (Exception e) {
            log.error("error",e);
            Result.error(ResponseCode.ANALYSIS_DATA_ERROR, e.getMessage());
            
        }
        return result;
    }

    public List<JSONObject> executeRelationalDb(DataSourceDto dto) {
        analysisRelationalDbConfig(dto);
        Connection pooledConnection = null;
        try {
            pooledConnection = jdbcService.getPooledConnection(dto);

            PreparedStatement statement = pooledConnection.prepareStatement(dto.getDynSentence());
            ResultSet rs = statement.executeQuery();

            int columnCount = rs.getMetaData().getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnLabel(i);
                columns.add(columnName);
            }
            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                columns.forEach(t -> {
                    try {
                        Object value = rs.getObject(t);
                        //数据类型转换
                        Object result = dealResult(value);
                        jo.put(t, result);
                    } catch (SQLException throwable) {
                        log.error("error",throwable);
                        Result.error(ResponseCode.EXECUTE_SQL_ERROR, throwable.getMessage());
                    }
                });
                list.add(jo);
            }
            return list;
        } catch (Exception throwable) {
            log.error("error",throwable);
            Result.error(ResponseCode.EXECUTE_SQL_ERROR, throwable.getMessage());
        } finally {
            try {
                if (pooledConnection != null) {
                    pooledConnection.close();
                }
            } catch (SQLException throwable) {
                log.error("error",throwable);
                Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, throwable.getMessage());
            }
        }
		return null;
    }

    /**
     * 解决sql返回值 类型问题
     * (through reference chain: java.util.HashMap["pageData"]->java.util.ArrayList[0]->java.util.HashMap["UPDATE_TIME"]->oracle.sql.TIMESTAMP["stream"])
     * @param result
     * @return
     * @throws SQLException
     */
    private Object dealResult(Object result) throws SQLException {
        if (null == result) {
            return result;
        }
        String type = result.getClass().getName();
        if ("oracle.sql.TIMESTAMP".equals(type)) {
            //oracle.sql.TIMESTAMP处理逻辑
            return new Date((Long) JSONObject.toJSON(result));
        }

        return result;
    }

    /**
     * http 执行获取数据
     *
     * @param dto
     */
    public List<JSONObject> executeHttp(DataSourceDto dto) {
        analysisHttpConfig(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(dto.getDynSentence(), headers);
        ResponseEntity<Object> exchange = null;
        try {
            exchange = restTemplate.exchange(dto.getApiUrl(), HttpMethod.valueOf(dto.getMethod()), entity, Object.class);
        } catch (Exception e) {
            log.error("error",e);
            Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
        }
        if (exchange.getStatusCode().isError()) {
        	Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, exchange.getBody());
        }
        Object body = exchange.getBody();
        String jsonStr = JSONObject.toJSONString(body);
        List<JSONObject> result = new ArrayList<>();
        if (jsonStr.trim().startsWith(BusinessConstant.LEFT_BIG_BOAST) && jsonStr.trim().endsWith(BusinessConstant.RIGTH_BIG_BOAST)) {
            //JSONObject
            result.add(JSONObject.parseObject(jsonStr));
        } else if (jsonStr.trim().startsWith(BusinessConstant.LEFT_MIDDLE_BOAST) && jsonStr.trim().endsWith(BusinessConstant.RIGHT_MIDDLE_BOAST)) {
            //List
            result = JSONArray.parseArray(jsonStr, JSONObject.class);
        } else {
            result.add(new JSONObject());
        }
        return result;
    }

    /**
     * 关系型数据库 测试连接
     *
     * @param dto
     */
    public Result testRelationalDb(DataSourceDto dto) {
        analysisRelationalDbConfig(dto);
        try {
            Connection unPooledConnection = jdbcService.getUnPooledConnection(dto);
            String catalog = unPooledConnection.getCatalog();
            log.info("数据库测试连接成功：{}", catalog);
            unPooledConnection.close();
            return Result.OK("数据库测试连接成功");
        } catch (SQLException e) {
            log.error("error",e);
            if (e.getCause() instanceof ClassNotFoundException) {
            	return Result.error(ResponseCode.CLASS_NOT_FOUND, "数据库测试连接失败:" + e.getCause().getMessage());
            } else {
            	return Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, "数据库测试连接失败:" + e.getMessage());
            }

        }
    }

    /**
     * http 测试连接
     *
     * @param dto
     */
    public Result testHttp(DataSourceDto dto) {
        analysisHttpConfig(dto);
        String apiUrl = dto.getApiUrl();
        String method = dto.getMethod();
        String body = dto.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> exchange;
        try {
            exchange = restTemplate.exchange(apiUrl, HttpMethod.valueOf(method), entity, Object.class);
            if (exchange.getStatusCode().isError()) {
            	return Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, "http测试连接失败:" + exchange.getBody());
            }
        } catch (RestClientException e) {
        	return Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, "http测试连接失败:" + e.getMessage());
        }
        return Result.OK("http测试连接成功");
    }


    /**
     * 关系型数据库 测试连接
     *
     * @param dto
     */
    public Result testElasticsearchSqlConnection(DataSourceDto dto) {
        analysisHttpConfig(dto);
        String apiUrl = dto.getApiUrl();
        String method = dto.getMethod();
        String body = dto.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> exchange;
        try {
            exchange = restTemplate.exchange(apiUrl, HttpMethod.valueOf(method), entity, Object.class);
            if (exchange.getStatusCode().isError()) {
            	return Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, "Elasticsearch测试连接失败:" +exchange.getBody());
            }
        } catch (RestClientException e) {
        	return Result.error(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, "Elasticsearch测试连接失败:" +e.getMessage());
        }
        return Result.OK("Elasticsearch测试连接成功");
    }


    public void analysisRelationalDbConfig(DataSourceDto dto) {
        JSONObject json = JSONObject.parseObject(dto.getConfig());
        Assert.isTrue(json.containsKey("jdbcUrl"), ResponseCode.PARAM_IS_NULL,"jdbcUrl not empty");
        Assert.isTrue(json.containsKey("driverName"), ResponseCode.PARAM_IS_NULL,"driverName not empty");
        String jdbcUrl = json.getString("jdbcUrl");
        String username = json.getString("username");
        String password = json.getString("password");
        String driverName = json.getString("driverName");
        dto.setJdbcUrl(jdbcUrl);
        dto.setDriverName(driverName);
        dto.setUsername(username);
        dto.setPassword(password);
    }


    /**
     * es通过api获取数据
     *
     * @param dto
     * @return
     */
    public void analysisHttpConfig(DataSourceDto dto) {
        JSONObject json = JSONObject.parseObject(dto.getConfig());
        Assert.isTrue(json.containsKey("apiUrl"), ResponseCode.PARAM_IS_NULL,"apiUrl not empty");
        Assert.isTrue(json.containsKey("method"), ResponseCode.PARAM_IS_NULL,"method not empty");
        Assert.isTrue(json.containsKey("header"), ResponseCode.PARAM_IS_NULL,"header not empty");
        Assert.isTrue(json.containsKey("body"), ResponseCode.PARAM_IS_NULL,"body not empty");
        String apiUrl = json.getString("apiUrl");
        String method = json.getString("method");
        String header = json.getString("header");
        String body = json.getString("body");
        //解决url中存在的动态参数
        apiUrl = dataSetParamService.transform(dto.getContextData(), apiUrl);
        //请求头中动态参数
        header = dataSetParamService.transform(dto.getContextData(), header);
        dto.setApiUrl(apiUrl);
        dto.setMethod(method);
        dto.setHeader(header);
        dto.setBody(body);
    }

    /**
     * 操作后续处理
     *
     * @param entity
     * @param operationEnum 操作类型
     * @throws JeecgBootException 阻止程序继续执行或回滚事务
     */

	@Override
	public void processAfterOperation(BsDataSource entity,
			org.jeecg.modules.estar.bs.enums.BaseOperationEnum operationEnum) throws JeecgBootException {
		// TODO Auto-generated method stub
		jdbcService.removeJdbcConnectionPool(Long.valueOf(entity.getId()));
	}

}
