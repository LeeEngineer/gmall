package com.atguigu.gmall.index.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gmall.index.aspect.GmallCache;
import com.atguigu.gmall.index.feign.GmallPmsClient;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Lee_engineer
 * @create 2020-07-30 20:09
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private GmallPmsClient pmsClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String PRE_KEY = "index:category:";

    @Override
    @GmallCache(prefix = PRE_KEY,timeout = 60 * 24 * 7,random = 30,lock = "index:lock:lev1catelock")
    public List<CategoryEntity> getLev1Category() throws JsonProcessingException {

        List<CategoryEntity> categoryEntities = pmsClient.getCateGoryByPid(0l).getData();
        return categoryEntities;

    }

    @Override
    @GmallCache(prefix = PRE_KEY,timeout = 60 * 24 * 7,random = 30,lock = "index:lock:lev2catelock")
    public List<CategoryEntity> getSubCategories(Long pid) throws JsonProcessingException {

        List<CategoryEntity> categoryEntities = pmsClient.queryCatogoriesWithSub(pid).getData();
        return categoryEntities;

    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


}
