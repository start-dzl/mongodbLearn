package com.dzl.mongodb.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.ModelBuildEventListener;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.dzl.mongodb.Listener.NoModleDataListener;
import com.dzl.mongodb.Repository.ClasstRepository;
import com.dzl.mongodb.Repository.PersonRepository;
import com.dzl.mongodb.entity.*;
import com.dzl.mongodb.service.PersonService;
import com.dzl.mongodb.util.EasyExcelUtil;
import com.dzl.mongodb.util.LookupLetPipelinesOperation;
import com.dzl.mongodb.util.LookupSimLetPipelinesOperation;
import com.dzl.mongodb.util.Pinyin4jUtil;
import com.github.jsonzou.jmockdata.JMockData;
import com.google.common.collect.Lists;
import com.googlecode.aviator.AviatorEvaluator;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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

    @Autowired
    @Qualifier("myMongoTemplate")
    private MongoTemplate myMongoTemplate;

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
    public Map<String, Object> excelShow(String fildName, String fildValue) {
        Map<String, Object> reTurnMap = new HashMap<>();
        Query query = new Query();
        if (!StringUtils.isEmpty(fildName) && !StringUtils.isEmpty(fildValue)) {
            query.addCriteria(Criteria.where(fildName).regex(".*\\Q" + fildValue + "\\E.*"));
        }
        List<Map> maps = myMongoTemplate.find(query, Map.class, "excelt");
        //String fileName =".\\testFile\\t_menu.xlsx";

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
//        NoModleDataListener dataListener = new NoModleDataListener();
        //ExcelReaderBuilder builder = EasyExcel.read(fileName);
        //builder.ignoreEmptyRow(true);
        //List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
        List<Head> heads = excelShowHead();

        Object[] values = heads.stream().map(Head::getPinyin).toArray();
        List<Map<String, Object>> list = dataList(values, maps);
        reTurnMap.put("data", list);
        reTurnMap.put("head", heads);
        return reTurnMap;
    }

    @Override
    public void createHead(MultipartFile file) throws IOException {

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        NoModleDataListener dataListener = new NoModleDataListener();
        ExcelReaderBuilder builder = EasyExcel.read(file.getInputStream(), dataListener);
        //builder.ignoreEmptyRow(true);
        List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
        Map<Integer, String> map = dataListener.head;
        rebuildAll(map);
        List<HashMap<String, Object>> hashMaps = new ArrayList<>();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            HashMap<String, Object> hashMap = new HashMap<>();
            data.forEach((integer, s) -> {
                        String s1 = map.get(integer);
                        Object sv = s;
                        if (s1.contains("jine")) {
                            sv = new BigDecimal(s);
                        }
                        hashMap.put(map.get(integer), sv);
                    }
            );
            hashMap.put("_id", data.get(0));
            hashMap.put("dept", (int) (1 + Math.random() * 10));
            hashMaps.add(hashMap);
            System.out.println("读取到数据:{}" + JSON.toJSONString(hashMap));
        }
        saveMap(hashMaps);
    }

    @Override
    public void updateValues(MultipartFile file) throws IOException {

        NoModleDataListener dataListener = new NoModleDataListener();
        ExcelReaderBuilder builder = EasyExcel.read(file.getInputStream(), dataListener);
        List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
        Map<Integer, String> map = dataListener.head;
        rebuildNoSave(map);

        Map<String, Head> headMap = excelMapHead();

        List<HashMap<String, Object>> hashMaps = new ArrayList<>();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            HashMap<String, Object> hashMap = new HashMap<>();
            data.forEach((integer, s) -> {
                        String s1 = map.get(integer);
                        Object sv = s;
                        if (s1.contains("jine")) {
                            sv = new BigDecimal(s);
                        }

                        hashMap.put(map.get(integer), sv);
                    }
            );
            hashMap.put("_id", data.get(0));
            hashMap.put("dept", (int) (1 + Math.random() * 10));
            for (Map.Entry<String, Head> headEntry : headMap.entrySet()) {
                Head value = headEntry.getValue();
                if (StringUtils.isNotBlank(value.getExpressions())) {
                    System.out.println("执行表达式：" + value.getExpressions());
                    Object value1 = AviatorEvaluator.execute(value.getExpressions(), hashMap);
                    hashMap.put(headEntry.getKey(), value1);
                }
            }
            hashMaps.add(hashMap);
        }
        saveMap(hashMaps);
    }

    @Override
    public List<Head> excelShowHead() {
        Query query = new Query();
        query.with(Sort.by("order"));
        List<Head> heads = mongoTemplate.find(query, Head.class);

        for (Head head : heads) {
            List<HeadExpress> expresses = head.getExpresses();
            if (!Objects.isNull(expresses) && !expresses.isEmpty()) {
                for (HeadExpress express : expresses) {
                    if (!Objects.isNull(express.getExpressEnum())) {
                        express.setName(express.getExpressEnum().getExpress());
                        express.setPinyin(express.getExpressEnum().getExpress());
                    } else if (StringUtils.isNotBlank(express.getId())) {
                        Head template = mongoTemplate.findById(express.getId(), Head.class);
                        if (!Objects.isNull(template)) {
                            express.setName(template.getName());
                            express.setPinyin(template.getPinyin());
                        }
                    }
                }
                List<HeadExpress> collect = expresses.stream().filter(f ->
                        StringUtils.isNotBlank(f.getPinyin()) || !Objects.isNull(f.getOrder()))
                        .sorted(Comparator.comparing(HeadExpress::getOrder))
                        .collect(Collectors.toList());

                StringBuilder nameBuilder = new StringBuilder();
                StringBuilder pyBuilder = new StringBuilder();
                for (HeadExpress headExpress : collect) {
                    nameBuilder.append(headExpress.getName());
                    pyBuilder.append(headExpress.getPinyin());
                }
                head.setExpresses(collect);
                head.setExpressions(pyBuilder.toString());
                head.setShowExpressions(nameBuilder.toString());
            }

        }

        return heads;

//        String fileName =".\\testFile\\t_menudand.xlsx";
//
//        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
//        List<Head> heads = Lists.newArrayList();
//        EasyExcel.read(fileName,
//                Head.class, new PageReadListener<Head>(heads::addAll)).sheet().doRead();
//        return heads.stream().sorted(Comparator.comparing(Head::getOrder)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Head> excelMapHead() {
        List<Head> heads = excelShowHead();
        Map<String, Head> map = new HashMap<>();
        for (Head head : heads) {
            map.put(head.getPinyin(), head);
        }
        return map;
    }

    @Override
    public Head saveAndUpdate(Head head) {

        if (StringUtils.isBlank(head.getId())) {
            if (StringUtils.isBlank(head.getName())) {
                head.setName(JMockData.mock(String.class));
            }

            if (Objects.isNull(head.getOrder())) {
                head.setOrder(JMockData.mock(Integer.class));
            }
            head.setPinyin(Pinyin4jUtil.firstConverterToSpell(head.getName()));
        }
        List<HeadExpress> expresses = head.getExpresses();
        if (!Objects.isNull(expresses) && !expresses.isEmpty()) {
            for (int i = 0; i < expresses.size(); i++) {
                expresses.get(i).setOrder(i);
            }
        }

        return mongoTemplate.save(head);
    }

    @Override
    public void excelOutput(HttpServletResponse response) throws IOException {
        List<Map> maps = mongoTemplate.findAll(Map.class, "excelt");
        List<Head> heads = excelShowHead();
        Object[] values = heads.stream().map(Head::getPinyin).toArray();
        Object[] names = heads.stream().map(Head::getName).toArray();

        WriteCellStyle headWriteCellStyle = EasyExcelUtil.getHeadStyle();
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, new WriteCellStyle());

        EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(horizontalCellStyleStrategy)
                .head(head(names)).sheet("模板").doWrite(dataListOut(values, maps));
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

    private List<List<Object>> dataListOut(Object[] values, List<Map> maps) {
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

    private void rebuildAll(Map<Integer, String> map) {

        mongoTemplate.dropCollection(Head.class);
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

    private void rebuildNoSave(Map<Integer, String> map) {

        for (Integer integer : map.keySet()) {
            String headName = map.get(integer);
            String converter = Pinyin4jUtil.firstConverterToSpell(headName);
            map.put(integer, converter);
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
}
