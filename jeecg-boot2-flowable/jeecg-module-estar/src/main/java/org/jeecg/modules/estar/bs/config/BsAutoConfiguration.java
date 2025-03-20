package org.jeecg.modules.estar.bs.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.jeecg.modules.estar.bs.cache.CacheHelper;
import org.jeecg.modules.estar.bs.cache.ReportCacheHelper;


/**
 * bs配置类
 * @author nbacheng
 * @since 2023-03-28
 */
@Configuration
@MapperScan(basePackages = {
        "org.jeecg.modules.estar.bs.mapper"
})
public class BsAutoConfiguration {

    @Bean
    public CacheHelper bsCacheHelper(){
        return new ReportCacheHelper();
    	//return new RedisCacheHelper();
    }

    @Bean
    public EhCacheCache ehCacheCache() {
        return (EhCacheCache) ehCacheCacheManager().getCache("reportCache");
    }

    /**
     * 创建ehCacheCacheManager
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {

        return new EhCacheCacheManager();
    }
}
