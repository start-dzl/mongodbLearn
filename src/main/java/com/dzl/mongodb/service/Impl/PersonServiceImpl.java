package com.dzl.mongodb.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.dzl.mongodb.Listener.NoModleDataListener;
import com.dzl.mongodb.Repository.ClasstRepository;
import com.dzl.mongodb.Repository.PersonRepository;
import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.entity.QPerson;
import com.dzl.mongodb.service.PersonService;
import com.dzl.mongodb.util.LookupLetPipelinesOperation;
import com.dzl.mongodb.util.LookupSimLetPipelinesOperation;
import com.dzl.mongodb.util.Pinyin4jUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ClasstRepository classtRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private QPerson qPerson = QPerson.person;

    @Override
    public void update(String name, Integer age) {
        Optional<Person> first = personRepository.findFirstByName(name);
        if (first.isPresent()) {
            Person person = first.get();
            person.setAge(age);
            personRepository.save(person);
        }
    }

    @Override
    public Person findById(String id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    @Override
    public void delete(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person save(String name) {
        Person person = new Person();
        person.setName(name);
        return personRepository.save(person);
    }

    @Override
    public Person save(Person person, List<Classt> classts) {
        person.setClasstes(classts);
        return personRepository.save(person);
    }

    @Override
    public void saveAll(List<Person> persons) {
        personRepository.saveAll(persons);
    }

    @Override
    public Page<Person> page(String name, Pageable pageable) {
        return personRepository.findAllByNameContains(name, pageable);
    }

    @Override
    public Classt saveClasst(String name) {
        Classt classt = new Classt(name);
        return classtRepository.save(classt);
    }

    @Override
    public Person testTransactional(String name) {
        Classt classt = new Classt(name);
        classtRepository.save(classt);
        return null;
    }

    @Override
    public void storeFileToGridFs(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        gridFsTemplate.store(fis, "filename.txt");
    }

    @Override
    public void findFilesInGridFs(String fileName) throws IOException {
        GridFsResource resource = gridFsTemplate.getResource("filename.txt");
        InputStream stream = resource.getInputStream();
        byte[] bytes = StreamUtils.copyToByteArray(stream);
        Path path = Paths.get("E:\\", fileName);
        Files.write(path, bytes);
    }

    @Override
    public List<Person> findAllQueryDsl(String name, Integer age) {
        BooleanExpression expression = qPerson.name.contains(name)
                .or(qPerson.age.goe(age));
        return Lists.newArrayList(personRepository.findAll(expression));
    }

    @Override
    public List<Person> findAllByTemplate(String name, Integer age) {
        List<Person> people = mongoTemplate.find(Query.query(Criteria.where("name").regex(".*\\Qte\\E.*")),
                Person.class);
        return people;
    }

    @Override
    public void saveMap(String str) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("_id", str);
        map.put("id", str);
        mongoTemplate.save(map, "wjm");
    }

    @Override
    public void saveMap(List<HashMap<String, Object>> hashMaps) {
        for (HashMap<String, Object> hashMap : hashMaps) {
            mongoTemplate.save(hashMap, "excelt");
        }

    }

    @Override
    public List<Map> getMap(String str) throws IOException {
        Query query = Query.query(Criteria.where("_id").is(str));
        return mongoTemplate.find(query, Map.class, "wjm");
    }

    @Override
    public List<Map> simLookUp() {
        LookupSimLetPipelinesOperation operation = new LookupSimLetPipelinesOperation("{\n" +
                "  from: 't_person',\n" +
                "  let: { classt_name: \"$name\" },\n" +
                "  pipeline:[\n" +
                "    {\n" +
                "      $group:\n" +
                "      {\n" +
                "      _id: '$classname',\n" +
                "      ages: {\n" +
                "        $sum: \"$age\"\n" +
                "      },\n" +
                "       classname: {\n" +
                "        $first: \"$classname\"\n" +
                "      }\n" +
                "      } \n" +
                "      \n" +
                "    },\n" +
                "    { $match: \n" +
                "    { $expr:\n" +
                "                    { $and:\n" +
                "                       [\n" +
                "                         { $eq: [ '$classname',  \"$$classt_name\" ] },\n" +
                "                         { $gte: [ '$ages', 35 ] }\n" +
                "                       ]\n" +
                "                    }\n" +
                "    }\n" +
                "                 }\n" +
                "\n" +
                "    ],\n" +
                "  as: 'string'\n" +
                "}");
        /* FacetOperation facets = Aggregation.facet();*/
        Aggregation aggregation = Aggregation.newAggregation(
                /* Aggregation.match(Criteria.where("_id").is("")),*/
                Aggregation.limit(1),
                Aggregation.skip(1l),
                // 获取 MessageId对应的from&to，根据这两个id进行多条件连接
                // 多条件连接
                operation
               /* Aggregation.unwind("ms"),
                Aggregation.project()
                        .and("ms._id").as("_id")
                        .and("ms.from").as("from")
                        .and("ms.to").as("to")
                        .and("ms.content").as("content")
                        .and("ms.property_id").as("property_id")
                        .and("ms.created_at").as("created_at")
                        .and("ms.updated_at").as("updated_at"),*/
               /* facets,
                Aggregation.unwind("introduction"),
                Aggregation.addFields().addField("introduction").withValue("$introduction.share_title").build()*/
        );
        List<Map> resultList = mongoTemplate.aggregate(aggregation, "classt", Map.class).getMappedResults();
        return resultList;
    }

    @Override
    public List<Map> lookUp() {

        String group = " $group: { _id: '$classname', ages: { $sum: '$age'},classname: { $first: '$classname' } } ";
        String match = " $match: { $expr: { $and: [ { $eq: [ '$classname',  '$$classt_name'] }, { $gte: [ '$ages', 35 ] } ]}}";
        LookupLetPipelinesOperation letPipelineLookup = LookupLetPipelinesOperation.LookupLetPipelinesOperation()
                .from("t_person")
                .let("classt_name: \"$name\"")
                .pipelines(Arrays.asList(group, match))
                .as("ms")
                .build();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.limit(1),
                // 获取 MessageId对应的from&to，根据这两个id进行多条件连接
                // 多条件连接
                letPipelineLookup
        );
        List<Map> resultList = mongoTemplate.aggregate(aggregation, "classt", Map.class).getMappedResults();

        return resultList;

    }

    private Set<Class<? extends Object>> getTestClass() {
        Reflections reflections = new Reflections("类所在包名");
        //返回带有指定注解的所有类对象
        return reflections.getTypesAnnotatedWith(Configuration.class);
    }

    @Override
    public Map<String, Object> excelShow() {
        Map<String, Object> reTurnMap = new HashMap<>();
        List<Map> maps = mongoTemplate.findAll(Map.class, "excelt");
        String fileName ="D:\\Desktop\\t_menu.xlsx";

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        NoModleDataListener dataListener = new NoModleDataListener();
        ExcelReaderBuilder builder = EasyExcel.read(fileName, dataListener);
        //builder.ignoreEmptyRow(true);
        List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
        Map<Integer, String> map = dataListener.head;

        List<Map<String, Object>> mapList = rebuild(map);
        Object[] values = map.values().toArray();
        List<Map<String, Object>> list = dataList(values, maps);
        reTurnMap.put("data", list);
        reTurnMap.put("head", mapList);
        return reTurnMap;
    }


    private List<Map<String, Object>> dataList(Object[] values, List<Map> maps) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> data = new HashMap<>();
            Map map = maps.get(i);
            for (int j = 0; j < values.length; j++) {
                Object value = values[j];
                Object o = map.get(value);
                data.put(value.toString(), o);
            }
            list.add(data);
        }
        return list;
    }

    private List<Map<String, Object>> rebuild(Map<Integer, String> map) {
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (Integer integer : map.keySet()) {
            String headName = map.get(integer);
            String converter = Pinyin4jUtil.firstConverterToSpell(headName);
            map.put(integer, converter);
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", headName);
            hashMap.put("pinyin", converter);
            hashMap.put("order", integer);
            arrayList.add(hashMap);
        }
        return arrayList;
    }
}
