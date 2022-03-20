package com.dzl.mongodb.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class MongodbSearchAspect {

    private final String POINT_CUT = "execution (* org.springframework.data.mongodb.core.MongoTemplate.find*(..))";

//    @Around(value = POINT_CUT)
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        Class<Query> queryClass = Query.class;
//        Object o = joinPoint.getTarget();
//        // 判断是否是自定义的MongoTemplate
//        if (o.getClass() == MongoTemplate.class) {
//            MongoTemplate mongoTemplate = (MongoTemplate) o;
//            Object[] args = joinPoint.getArgs();
//            for (Object arg : args) {
//                if(arg.getClass() ==  queryClass) {
//                    Query query = (Query) arg;
//                    query.addCriteria(Criteria.where("age").is(199));
//                }
//            }
//        }
//        Object result = joinPoint.proceed();
//        return result;
//    }
}
