package com.autoask.cache.impl;

import com.alibaba.fastjson.JSON;
import com.autoask.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyy
 * @craete 2016/7/22 16:26
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private ShardedJedisPool shardedJedisPool;



    @Override
    public String get(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> type) {

        String result = get(key);

        if (result == null) {
            //找不到缓存对象
            return null;
        }
        return JSON.parseObject(result, type);
    }

    @Override
    public String set(String key, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.set(key, value);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public String set(String key, String value, int seconds) {
        ShardedJedis jedis = shardedJedisPool.getResource();

        try {
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public <T> String set(String key, T value) {
        try {
            return set(key, JSON.toJSONString(value));
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }
        return null;
    }

    @Override
    public <T> String set(String key, T value, int seconds) {
        try {
            return set(key, JSON.toJSONString(value), seconds);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }
        return null;
    }

    @Override
    public Long del(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.del(key);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public String hGet(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T hGet(String key, String field, Class<?> type) {
        String result = hGet(key, field);
        return (T) JSON.parseObject(result, type);
    }

    @Override
    public Long hSet(String key, String field, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();

        try {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public <T> Long hSet(String key, String field, T value) {
        try {
            return hSet(key, field, JSON.toJSONString(value));
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }
        return null;
    }


    @Override
    public Boolean exists(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        } finally {
            jedis.close();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean hExists(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hexists(key, field);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        } finally {
            jedis.close();
        }
        return Boolean.FALSE;
    }

    @Override
    public List<String> hmGet(String key, String... fields) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        } finally {
            jedis.close();
        }
        return new ArrayList<String>(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> hmGet(String key, Class<?> type, String... fields) {
        List<String> result = hmGet(key, fields);
        List<T> list = new ArrayList<T>(result.size());
        for (String jsonStr : result) {
            list.add((T) JSON.parseObject(jsonStr, type));
        }
        return list;
    }

    @Override
    public Long hDel(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();

        try {

            return jedis.hdel(key, field);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public Long expire(String key, int seconds) {
        ShardedJedis jedis = shardedJedisPool.getResource();

        try {
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            LOG.error("Execute redis command failure:{}, message:{}", e.getClass(), e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }
}