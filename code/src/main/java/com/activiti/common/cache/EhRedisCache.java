package com.activiti.common.cache;

import com.activiti.common.redis.RedisCommonUtil;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;


/**
 * 两级缓存
 * Created by 12490 on 2017/8/6.
 */
public class EhRedisCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(EhRedisCache.class);

    @Autowired
    private RedisCommonUtil redisCommonUtil;

    private String name;

    private net.sf.ehcache.Cache ehCache;

    private RedisTemplate<String, String> redisTemplate;

    private long liveTime = 1 * 60 * 60; //默认1h=1*60*60

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {
        Element value = ehCache.get(key);
        logger.info("Cache L1 get (ehcache) :{}={}", key, value);
        if (value != null) {
            return (value != null ? new SimpleValueWrapper(value.getObjectValue()) : null);
        }
        final String keyStr = key.toString();
        Object objectValue = redisCommonUtil.get(keyStr);
        ehCache.put(new Element(key, objectValue));//取出来之后缓存到本地
        return (objectValue != null ? new SimpleValueWrapper(objectValue) : null);
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        if (null!=value){
            ehCache.put(new Element(key, value));
            logger.info("Cache L1 put (ehcache) :{}={}", key, value);
            final String keyStr = key.toString();
            final Object valueStr = value;
            redisCommonUtil.put(keyStr, value, liveTime);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    @Override
    public void evict(Object key) {
        ehCache.remove(key);
        final String keyStr = key.toString();
        redisCommonUtil.deleteOne(keyStr);
    }

    @Override
    public void clear() {
        ehCache.removeAll();
        redisCommonUtil.deleteAll();
    }

    public void setName(String name) {
        this.name = name;
    }

    public net.sf.ehcache.Cache getEhCache() {
        return ehCache;
    }

    public void setEhCache(net.sf.ehcache.Cache ehCache) {
        this.ehCache = ehCache;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(long liveTime) {
        this.liveTime = liveTime;
    }
}
