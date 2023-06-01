package com.dzl.mongodb;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.entity.Rule;
import com.dzl.mongodb.service.PersonService;
import com.dzl.mongodb.util.PdfConvertUtil;
import com.dzl.mongodb.util.WordRead;
import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.fit.pdfdom.PDFDomTree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import com.spire.doc.*;

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

//		HashMap<List<Integer>, Integer> hashMap = new HashMap<>();
//		ArrayList<Integer> list = Lists.newArrayList(1, 2);
//		hashMap.put(list, 2);
//		person.setPart4Map(hashMap);
		String dump = yaml.dump(person);
		System.out.printf(dump);
	}


	@Test
	public void readWordFile2(){
		Map<String,Object> result = new HashMap<String, Object>();

		String filePath = "E:\\data\\q.docx";
		try {
			//读取Word中的文本内容包含表格
			String wordhtml = WordRead.readWordImgToHtml(filePath);
			result.put("content", wordhtml);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(JSON.toJSON(result));
	}

    @Test
	public  void tv() {
		//实例化Document类的对象
		Document doc = new Document();

		//加载Word
		doc.loadFromFile("E:\\data\\q.docx", FileFormat.Auto, "11");

		//保存为PDF格式
		doc.saveToFile("E:\\data\\WordToPDF.pdf",FileFormat.PDF);
	}


	@Test
	public void readWordFile(){
		Map<String,Object> result = new HashMap<String, Object>();
		//word文件地址放在src/main/webapp/下
		//表示到项目的根目录（webapp）下，要是想到目录下的子文件夹，修改"/"即可

		String filePath = "E:\\data\\q.docx";

		String suffixName = filePath.substring(filePath.lastIndexOf("."));//从最后一个.开始截取。截取fileName的后缀名
		try {
			File file = new File(filePath);
			FileInputStream fs = new FileInputStream(file);
			if(suffixName.equalsIgnoreCase(".doc")){//doc
				StringBuilder result2 = new StringBuilder();
				WordExtractor re = new WordExtractor(fs);
				result2.append(re.getText());//获取word中的文本内容
				re.close();
				result.put("content", result2.toString());
			}else{//docx
				XWPFDocument doc = new XWPFDocument(fs);
				XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
				String text = extractor.getText();//获取word中的文本内容
				extractor.close();
				fs.close();
				result.put("content", text);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(JSON.toJSON(result));
	}


	/*
  读取pdf文字
   */
	@Test
	public void readPdfTextTest() throws IOException {
		byte[] bytes = PdfConvertUtil.getBytes("F:\\1.pdf");
		//加载PDF文档
		PDDocument document = PDDocument.load(bytes);
		String readText = PdfConvertUtil.readText(document);
		System.out.println(readText);
	}



	/*
    pdf转换html
     */
	@Test
	public void pdfToHtmlTest() {
		String outputPath = "D:\\Desktop\\bk.html";
		byte[] bytes = PdfConvertUtil.getBytes("F:\\2.pdf");

//        try() 写在()里面会自动关闭流
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputPath)), "UTF-8"));) {
			//加载PDF文档
			PDDocument document = PDDocument.load(bytes, "meiya!8848");
			PDFDomTree pdfDomTree = new PDFDomTree();
			pdfDomTree.writeText(document, out);
		} catch (Exception e) {
            e.printStackTrace();
//			System.out.println("haha");
		}
	}






}
