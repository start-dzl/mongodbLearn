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
import com.dzl.mongodb.strategy.ExcelWidthStyleStrategy;
import com.dzl.mongodb.util.EasyExcelUtil;
import com.dzl.mongodb.util.Pinyin4jUtil;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class MongodbApplicationTests {

	@Autowired
	private PersonService personService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	void test1() {
		//保存
		Person save = personService.save("test1");
		System.out.println("save == " + save);
		//读取
		Person person = personService.findById(save.getId());
		System.out.println("findById == " + person);

		//删除
		 personService.delete(person.getId());
		System.out.println("delete == ");

		//读取
		Person result = personService.findById(save.getId());
		System.out.println("result == " + result);

	}

	@Test
	void test2() {
		//保存
		Person save = personService.save("test2");
		System.out.println("save == " + save);

		//更新
		personService.update("test2", 18);
		System.out.println("update == " + save);

		//读取
		Person result = personService.findById(save.getId());
		System.out.println("result == " + result);
	}

	@Test
	void test3() {
		createBatch();

		Page<Person> page = personService.page("es", PageRequest.of(0, 5));
		System.out.println("getTotalElements == "+page.getTotalElements());
		System.out.println("getSize == "+page.getSize());
		List<Person> content = page.getContent();
		for (Person person : content) {

			System.out.println("person == "+person);
		}
	}
	@Test
	void test4() {
		Classt classt = personService.saveClasst("一班");
		Person save = personService.save("test4");
		System.out.println("save == " + save);
		List<Classt> classts = new ArrayList<>();
		classts.add(classt);
		save.setClasstes(classts);
		personService.save(save, classts);

		//读取
		Person result = personService.findById(save.getId());
		System.out.println("result == " + result);

	}

	@Test
	void test5() {
		personService.testTransactional("4班级");
	}

	@Test
	void test6() throws FileNotFoundException {
		personService.storeFileToGridFs("E:\\IMG_0602.JPG");
	}


	@Test
	void test8() throws IOException {
		personService.findFilesInGridFs("602.JPG");
	}

	@Test
	void test9() throws IOException {
		createBatch();
		List<Person> content = personService.findAllQueryDsl("te", 5);
		for (Person person : content) {
			System.out.println("person == "+person);
		}
	}

	@Test
	void test11() throws IOException {
		//createBatch();
		List<Person> content = personService.findAllByTemplate("te", 5);
		for (Person person : content) {
			System.out.println("person == "+person);
		}
	}

	@Test
	void test10() throws IOException {
		personService.saveMap("date");
		List<Map> map = personService.getMap("date");
		for (Map m : map) {
			System.out.println(" == "+m);
		}
	}


	@Test
	void test12mo() throws IOException {
		personService.lookUp();

	}

	@Test
	void test123() throws IOException {
		String reg = "\\w\\d+$";
		String str = "1d3s32d号3房2间1233";
		Pattern pa = Pattern.compile(reg, Pattern.DOTALL);
		Matcher matcher = pa.matcher(str);
		if(matcher.find()) {
			String replaceAll = matcher.replaceAll("");
			System.out.println(replaceAll);
			System.out.println(str.replace(reg,""));
		}

	}


	@Test
	public void synchronousRead() {
		String fileName =".\\testFile\\t_menu.xlsx";

		// 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
		NoModleDataListener dataListener = new NoModleDataListener();
		ExcelReaderBuilder builder = EasyExcel.read(fileName, dataListener);
		//builder.ignoreEmptyRow(true);
		List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
		Map<Integer, String> map = dataListener.head;
		rebuild(map);
		List<HashMap<String, Object>> hashMaps = new ArrayList<>();
		for (Map<Integer, String> data : listMap) {
			// 返回每条数据的键值对 表示所在的列 和所在列的值
			HashMap<String, Object> hashMap = new HashMap<>();
			data.forEach((integer, s) ->{
						String s1 = map.get(integer);
						Object sv = s;
						if("CREATE_TIME".equals(s1)) {
							SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
							try {
								sv = sdf.parse(s);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						hashMap.put(map.get(integer), sv);
					}
					 );
			hashMap.put("_id",data.get(0));
			hashMap.put("dept",(int)(1+Math.random()*10));
			hashMaps.add(hashMap);
			System.out.println("读取到数据:{}"+ JSON.toJSONString(hashMap));
		}
		personService.saveMap(hashMaps);
	}

	@Test
	public void  testpy() {
		List<Map> maps = mongoTemplate.findAll(Map.class, "excelt");
//		String fileName =".\\testFile\\t_menu.xlsx";
//
//		// 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
//		NoModleDataListener dataListener = new NoModleDataListener();
//		ExcelReaderBuilder builder = EasyExcel.read(fileName, dataListener);
//		//builder.ignoreEmptyRow(true);
//		List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
//		Map<Integer, String> map = dataListener.head;
//		rebuild(map);
		List<Head> heads = personService.excelShowHead();
		Object[] values = heads.stream().map(Head::getPinyin).toArray();
		Object[] names = heads.stream().map(Head::getName).toArray();
		String writefileName ="D:\\Desktop\\t_menu1.xlsx";
		WriteCellStyle headWriteCellStyle = EasyExcelUtil.getHeadStyle();
		HorizontalCellStyleStrategy horizontalCellStyleStrategy =
				new HorizontalCellStyleStrategy(headWriteCellStyle, new WriteCellStyle());

		EasyExcel.write(writefileName)
				//.registerWriteHandler(new ExcelWidthStyleStrategy())
				.registerWriteHandler(horizontalCellStyleStrategy)
				.head(head(names)).sheet("模板").doWrite(dataList(values, maps));

	}

	@Test
	public void  testsim() {
		List<Car> maps = mongoTemplate.findAll(Car.class, "excelt");

		for (Car map : maps) {
			System.out.println(map.toString());
		}


	}

	@Test
	public void testpy4() {
		String converter = Pinyin4jUtil.converterToSpell("蜜蜂");
		System.out.println("蜜蜂" + converter);

		String converter2 = Pinyin4jUtil.converterToSpell("密封");
		System.out.println("密封" + converter2);
	}

	private List<List<String>> head(Object[] values) {
		List<List<String>> list = new ArrayList<List<String>>();
		for (int i = 0; i < values.length; i++) {
			String value = values[i].toString();
			List<String> head0 = new ArrayList<String>();
			head0.add(value);
			list.add(head0);
		}
		return list;
	}

	private List<List<Object>> dataList(Object[] values, List<Map> maps) {
		List<List<Object>> list = new ArrayList<List<Object>>();
		for (int i = 0; i < maps.size(); i++) {
			List<Object> data = new ArrayList<Object>();
			Map map = maps.get(i);
			for (int j = 0; j < values.length; j++) {
				Object value = values[j];
				Object o = map.get(value);
				data.add(o);
			}
			list.add(data);
		}
		return list;
	}

	private void rebuild(Map<Integer, String> map) {

		mongoTemplate.dropCollection( Head.class);
		List<Head> heads = Lists.newArrayList();
		for (Integer integer : map.keySet()) {
			String headName = map.get(integer);
			String converter = Pinyin4jUtil.firstConverterToSpell(headName);
			map.put(integer, converter);
			Head head = new Head();
			head.setName(headName);
			head.setPinyin(converter);
			head.setOrder(integer);
			heads.add(head);
			mongoTemplate.save(head);
		}

	}

	private void createBatch() {
		Random random = new Random();
		List<Person> list = new ArrayList<>();
		String name = "test";
		for (int i = 0; i < 10; i++) {
			Person person = new Person();
			person.setName(name);
			person.setAge(random.nextInt(50));
			person.setClassname("一班");
			list.add(person);
		}
		personService.saveAll(list);

	}


}
