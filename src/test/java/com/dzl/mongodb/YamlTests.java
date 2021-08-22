package com.dzl.mongodb;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.entity.Rule;
import com.dzl.mongodb.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class YamlTests {


	@Test
	void test1() {
		Yaml yaml = new Yaml();
		String document = "- Hesperiidae- Papilionidae- Apatelodidae- Epiplemidae";
		List<String> list = (List<String>) yaml.load(document);
		System.out.println(list);

	}

	@Test
	public void testLoadFromString() {
		Yaml yaml = new Yaml();
		String document = "hello: 25";
		Map map = (Map) yaml.load(document);
		assertEquals("{hello=25}", map.toString());
		assertEquals(25, map.get("hello"));
	}
	@Test
	public void testLoadFromStream() throws FileNotFoundException {
		InputStream input = new FileInputStream(new File("E:\\pj\\mongodbLearn\\src\\main\\resources\\application.yaml"));
		Yaml yaml = new Yaml();
		Map data = yaml.load(input);
		System.out.println(data);
	}

	@Test
	public void testLoadManyDocuments() throws FileNotFoundException {
		InputStream input = new FileInputStream(new File(
				"E:\\pj\\mongodbLearn\\application.yaml"));
		Yaml yaml = new Yaml();
		int counter = 0;
		for (Object data : yaml.loadAll(input)) {
			System.out.println(data);
			counter++;
		}
		assertEquals(3, counter);
	}

	/**
	 * create JavaBean
	 */
	@Test
	public void testGetBeanAssumeClass() throws FileNotFoundException {
		Yaml yaml = new Yaml(new Constructor(Rule.class));
		InputStream input = new FileInputStream(new File(
				"E:\\pj\\mongodbLearn\\application.yaml"));
		Object obj = yaml.load(input);
		assertNotNull(obj);
		//assertTrue("Unexpected: " + obj.getClass().toString(), obj instanceof Person);
		Rule person = (Rule) obj;
		Double aDouble = person.getPart1();
		System.out.printf(aDouble.toString());
		Map<Integer, Integer> part1Map = person.getPart2Map();
		Integer integer = part1Map.get(2);
		System.out.printf(integer.toString());
		System.out.printf(person.toString());
		String dump = yaml.dump(person);
		System.out.printf(dump);
	}






}
