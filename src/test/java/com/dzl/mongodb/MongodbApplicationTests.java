package com.dzl.mongodb;

import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

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

}
