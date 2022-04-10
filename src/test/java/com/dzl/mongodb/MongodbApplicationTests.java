package com.dzl.mongodb;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.dzl.mongodb.Listener.NoModleDataListener;
import com.dzl.mongodb.entity.Car;
import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.service.PersonService;
import com.dzl.mongodb.util.Pinyin4jUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	void test10() throws IOException {
		personService.saveMap("dfs");
		List<Map> map = personService.getMap("2");
	}

	@Test
	public void synchronousRead() {
		String fileName ="/Users/dengzuliang/Desktop/febs_cloud_base_t_menu.xlsx";

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
							SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
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
			hashMaps.add(hashMap);
			System.out.println("读取到数据:{}"+ JSON.toJSONString(hashMap));
		}
		personService.saveMap(hashMaps);
	}

	@Test
	public void  testpy() {
		List<Map> maps = mongoTemplate.findAll(Map.class, "excelt");
		String fileName ="/Users/dengzuliang/Desktop/febs_cloud_base_t_menu.xlsx";

		// 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
		NoModleDataListener dataListener = new NoModleDataListener();
		ExcelReaderBuilder builder = EasyExcel.read(fileName, dataListener);
		//builder.ignoreEmptyRow(true);
		List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
		Map<Integer, String> map = dataListener.head;
		rebuild(map);

		String writefileName ="/Users/dengzuliang/Desktop/febs_cloud_base_t_menu1.xlsx";
		Object[] values = map.values().toArray();
		EasyExcel.write(writefileName).head(head(values)).sheet("模板").doWrite(dataList(values, maps));

	}

	@Test
	public void  testsim() {
		List<Car> maps = mongoTemplate.findAll(Car.class, "excelt");

		for (Car map : maps) {
			System.out.println(map.toString());
		}


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
		for (Integer integer : map.keySet()) {
			String headName = map.get(integer);
			String converter = Pinyin4jUtil.firstConverterToSpell(headName);
			map.put(integer, converter);
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
			list.add(person);
		}
		personService.saveAll(list);

	}


}
