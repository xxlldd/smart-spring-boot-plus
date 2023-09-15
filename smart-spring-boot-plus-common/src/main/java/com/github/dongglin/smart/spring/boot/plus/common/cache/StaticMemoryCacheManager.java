package com.github.dongglin.smart.spring.boot.plus.common.cache;

import com.github.dongglin.smart.spring.boot.plus.common.exception.BizException;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * 静态不变化的数据内存缓存manager
 * @author JerryMa
 * @version v2.2.1
 * @date 2021/4/17
 * Copyright © diboot.com
 */
public class StaticMemoryCacheManager extends BaseMemoryCacheManager implements BaseCacheManager {

    public StaticMemoryCacheManager(String... cacheNames){
        List<Cache> caches = new ArrayList<>();
        for(String cacheName : cacheNames){
            caches.add(new ConcurrentMapCache(cacheName));
        }
        setCaches(caches);
        super.afterPropertiesSet();
    }

    @Override
    public void clearOutOfDateData(String cacheName) {
        throw new BizException("StaticMemoryCacheManager 缓存不存在过期，不支持清理！");
    }
}
