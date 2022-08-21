package com.dzl.mongodb;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.dzl.mongodb.Listener.NoModleDataListener;
import com.dzl.mongodb.entity.Car;
import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Head;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.service.PersonService;
import com.dzl.mongodb.strategy.MyMergeStrategy;
import com.dzl.mongodb.util.EasyExcelUtil;
import com.dzl.mongodb.util.Pinyin4jUtil;
import com.github.jsonzou.jmockdata.JMockData;
import com.google.common.collect.Lists;
import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.OgnlException;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
class EsApplicationTests {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Test
	void test1() {
		List<Head> heads = mongoTemplate.findAll(Head.class);
		for (Head head : heads) {
			head.setTime(new Date());
		}
		elasticsearchRestTemplate.save(heads);
	}


	@Test
	void test2() {
		QueryBuilder queryBuilder =
				QueryBuilders
						.multiMatchQuery("计分", "name")
						.fuzziness(Fuzziness.AUTO);

		Query searchQuery = new NativeSearchQueryBuilder()
				.withFilter(queryBuilder)
				.build();
		SearchHits<Head> searchHits = elasticsearchRestTemplate.search(searchQuery, Head.class);
		List<Head> headList = searchHits.stream().map(e -> e.getContent()).collect(Collectors.toList());
		int size = headList.size();
	}






}
