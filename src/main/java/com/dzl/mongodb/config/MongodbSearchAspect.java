package com.dzl.mongodb.config;

import com.dzl.mongodb.service.Impl.MyMongoTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;

import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class MongodbSearchAspect {

//    private final String POINT_CUT = "execution (* org.springframework.data.mongodb.core.MongoTemplate.find*(..))";
//
//    @Around(value = POINT_CUT)
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        Class<Query> queryClass = Query.class;
//        Object o = joinPoint.getTarget();
//        // 判断是否是自定义的MongoTemplate
//        if (o.getClass() == MyMongoTemplate.class) {
//            Object[] args = joinPoint.getArgs();
//            Query query = null;
//            for (Object arg : args) {
//                if(arg.getClass() ==  queryClass) {
//                    query = (Query) arg;
//                }
//            }
//            if(Objects.isNull(query)) {
//               //todo
//            }else {
//                query.addCriteria(Criteria.where("dept").lt(5));
//            }
//
//
//        }
//        Object result = joinPoint.proceed();
//        return result;
//    }
}
