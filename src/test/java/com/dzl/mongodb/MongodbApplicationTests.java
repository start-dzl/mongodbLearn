package com.dzl.mongodb;

import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.service.PersonService;
import com.mongodb.util.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootTest
class MongodbApplicationTests {

	@Autowired
	private PersonService personService;

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
