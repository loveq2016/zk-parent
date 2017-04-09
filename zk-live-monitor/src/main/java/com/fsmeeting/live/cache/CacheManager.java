package com.fsmeeting.live.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 缓存管理器
 *
 * @Author:yicai.liu<虚竹子>
 */
public class CacheManager<K, V> {

    private Map<K, V> cache = new ConcurrentHashMap<K, V>();

    private CacheManager() {
    }

    private static class CacheManagerHolder {
        private static final CacheManager INSTANCE = new CacheManager();
    }

    public static final CacheManager getInstance() {
        return CacheManagerHolder.INSTANCE;
    }

    public Map<K, V> getCache() {
        return cache;
    }

}
