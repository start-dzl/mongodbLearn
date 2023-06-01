package com.dzl.mongodb.util;


import java.io.*;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;

public class WordRead {


    /**
     * 方式一：读取word中的文本内容（段落、表格统计获取）--- doc\docx 都可
     */
    public static String readWordText(){
        String result ="";
        String filePath = "e:\\w1.doc";
        String suffixName = filePath.substring(filePath.lastIndexOf("."));//从最后一个.开始截取。截取fileName的后缀名
        try {
            File file = new File(filePath);
            FileInputStream fs = new FileInputStream(file);
            if(suffixName.equalsIgnoreCase(".doc")){//doc
                StringBuilder result2 = new StringBuilder();
                WordExtractor re = new WordExtractor(fs);
                result2.append(re.getText());//获取word中的文本内容
                re.close();
                result = result2.toString();
            }else{//docx
                XWPFDocument doc = new XWPFDocument(fs);
                XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                String text = extractor.getText();//获取word中的文本内容
                extractor.close();
                fs.close();
                result = text;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 方式2：读取word中的文本内容（段落、表格分开处理）   docx后缀名的Word
     * @throws IOException
     */
    public static String readWord() throws IOException{
        XWPFDocument document = new XWPFDocument(new FileInputStream("E:\\w3.docx"));
        String htmlText="";
        try {
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    htmlText+=getParagraphText((XWPFParagraph) element);
                }
                // 表格
                else if (element instanceof XWPFTable) {
                    htmlText+=getTabelText((XWPFTable) element);
                }
            }
            //获取word中的所有图片
            List<XWPFPictureData> picLists= document.getAllPictures();
            for(XWPFPictureData pic:picLists){
                System.out.println("图片名称:\t" + pic.getFileName());
                System.out.println("图片类型：\t" + pic.getPictureType());
                byte[] data = pic.getData();
                System.out.println(data);
                //字节流图片上传，并返回服务器地址
                String imgUrl = getImageUrl(data, pic.getFileName());
                System.out.println("图片服务器地址："+imgUrl);
//			    //下载图片到w3.docx所在文件夹
//			    String imagePath = "E:\\w3.docx";
//			    FileOutputStream fos = new FileOutputStream(imagePath + pic.getFileName());
//                fos.write(data);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            document.close();
        }
        return htmlText;
    }

    /**
     * 方式2：读取word中的文本内容（段落、表格分开处理） docx后缀名的Word
     * @param filePath 文件路径
     * @throws IOException
     */
    public static String readWord2(String filePath) throws IOException{
        XWPFDocument document = new XWPFDocument(new FileInputStream(filePath));
        String htmlText="";
        try {
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    htmlText+=getParagraphText((XWPFParagraph) element);
                }
                // 表格
                else if (element instanceof XWPFTable) {
                    htmlText+=getTabelText((XWPFTable) element);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            document.close();
        }
        return htmlText;
    }

    /**
     * 方式2：读取word中的文本内容（段落、表格分开处理） docx后缀名的Word
     * @param fs 文件流
     * @throws IOException
     */
    public static String readWord3(FileInputStream fs) throws IOException{
        XWPFDocument document = new XWPFDocument(fs);
        String htmlText="";
        try {
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    htmlText+=getParagraphText((XWPFParagraph) element);
                }
                // 表格
                else if (element instanceof XWPFTable) {
                    htmlText+=getTabelText((XWPFTable) element);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            document.close();
        }
        return htmlText;
    }

    /**
     * 获取段落内容（ docx后缀名的Word）
     * @param paragraph
     */
    private static String getParagraphText(XWPFParagraph paragraph) {
        // 获取段落中所有内容
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs.size() == 0) {
            //System.out.println("按了回车（新段落）");
            return "";
        }
        StringBuffer runText = new StringBuffer();
        for (XWPFRun run : runs) {
            runText.append(run.text());
        }
//		if (runText.length() > 0) {
//			runText.append("，对齐方式：").append(paragraph.getAlignment().name());
//			System.out.println(runText);
//		}
        return runText.toString();
    }

    /**
     * 获取表格内容（ docx后缀名的Word）
     * @param table
     */
    private static String getTabelText(XWPFTable table) {
        String result="";
        //获取表格数据行
        List<XWPFTableRow> rows = table.getRows();
        if(rows.size()>0){
            //遍历
            for (int i=0;i<rows.size();i++) {
                XWPFTableRow row = rows.get(i);
                //获取每行的数据列
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    String cellText="";
                    // 简单获取内容（简单方式是不能获取字体对齐方式的）
                    // System.out.println(cell.getText());
                    // 一个单元格可以理解为一个word文档，单元格里也可以加段落与表格
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        cellText+=getParagraphText(paragraph);
                    }
                    result+=cellText;
                }
            }
        }
        return result;
    }


    /**
     * 读取word中的文本内容（段落、表格分开处理）转HTML docx后缀名的Word
     * @param filePath 文件路径
     * @throws IOException
     */
    public static String readWordToHtml(String filePath) throws IOException{
        XWPFDocument document = new XWPFDocument(new FileInputStream(filePath));
        String htmlText="";
        try {
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    htmlText+=getParagraphHtmlText((XWPFParagraph) element);
                }
                // 表格
                else if (element instanceof XWPFTable) {
                    htmlText+=getTabelHtmlText((XWPFTable) element);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            document.close();
        }
        return htmlText;
    }

    /**
     * 获取段落内容并组装段落HTML
     * @param paragraph
     */
    private static String getParagraphHtmlText(XWPFParagraph paragraph) {
        // 获取段落中所有内容
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs.size() == 0) {
            //System.out.println("按了回车（新段落）");
            return "";
        }
        StringBuffer runText = new StringBuffer();
        for (XWPFRun run : runs) {
            runText.append(run.text());
        }
        return "<p style='margin:unset;text-align:"+paragraph.getAlignment().name()+"'>"+runText.toString()+"</p>";
    }

    /**
     * 获取表格内容并组装表格HTML
     * @param table
     */
    private static String getTabelHtmlText(XWPFTable table) {
        String result="";
        //获取表格数据行
        List<XWPFTableRow> rows = table.getRows();

        if(rows.size()>0){
            result+="<table border='1' cellspacing=0 style='border-collapse: collapse;'>";
            //遍历
            for (int i=0;i<rows.size();i++) {
                XWPFTableRow row = rows.get(i);
                result+="<tr style='font-weight: bold;'>";
                //获取每行的数据列
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    //获取单元格跨列个数
                    BigInteger gridSpanNum = getCellGridSpanNum(cell);

                    result+="<td colspan="+gridSpanNum+" valign=center style='text-align: center;vertical-align: middle;'>";
                    String cellText="";
                    // 简单获取内容（简单方式是不能获取字体对齐方式的）
                    // System.out.println(cell.getText());
                    // 一个单元格可以理解为一个word文档，单元格里也可以加段落与表格
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        cellText+="<p style='margin: unset;text-align:"+paragraph.getAlignment().name()+"'>"+getParagraphText(paragraph)+"</p>";
                    }
                    result+=cellText;
                    result+="</td>";
                }

                result+="</tr>";
            }
            result+="</table>";
        }
        return result;
    }

    /**
     * 通过XWPFWordExtractor读取word(后缀名为docx的文件)
     * @return
     */
    public static String readWordDocx1(){
        String result ="";
        String filePath = "e:\\w1.doc";
        try {
            File file = new File(filePath);
            FileInputStream fs = new FileInputStream(file);

            XWPFDocument doc = new XWPFDocument(fs);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            String text = extractor.getText();//获取word中的文本内容
            extractor.close();
            fs.close();
            result = text;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过XWPFDocument读取word(后缀名为docx的文件)
     * @return
     */
    public static String readWordDocx2(){
        String result ="";
        String filePath = "e:\\w1.doc";
        try {
            File file = new File(filePath);
            FileInputStream fs = new FileInputStream(file);

            XWPFDocument doc = new XWPFDocument(fs);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            String text = extractor.getText();//获取word中的文本内容
            extractor.close();
            fs.close();
            result = text;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 获取单元格跨列个数
     * @return
     */
    public static BigInteger getCellGridSpanNum(XWPFTableCell cell){
        BigInteger gridSpanNum =null;
        //获取单元格跨列
        CTDecimalNumber gridSpanXml = cell.getCTTc().getTcPr().getGridSpan();
        if(gridSpanXml!=null){
            gridSpanNum = gridSpanXml.getVal();
            System.out.println("gridSpanNum:"+gridSpanNum);
        }
        return gridSpanNum;
    }

    /**
     * 获取单元格跨行
     * @return
     */
    public static int getCellVMergeNum(XWPFTableCell cell){
        int vMergeNum =0;
        //获取单元格跨行
        CTVMerge vMergeXml = cell.getCTTc().getTcPr().getVMerge();
        if(vMergeXml!=null){
            System.out.println("vMergeXml:"+vMergeXml);
        }
        return vMergeNum;
    }

    /**
     * 读取word中的文本内容（段落、表格、图片分开处理）转HTML docx后缀名的Word
     * @param filePath 文件路径
     * @throws IOException
     */
    public static String readWordImgToHtml(String filePath) throws IOException, InvalidFormatException, GeneralSecurityException {
//        Biff8EncryptionKey.setCurrentUserPassword("11");

        POIFSFileSystem fs = new POIFSFileSystem();

        EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        Encryptor encryptor = info.getEncryptor();

        OPCPackage opc = OPCPackage.open(new FileInputStream(filePath));
        opc.save(encryptor.getDataStream(fs));

        XWPFDocument document = new XWPFDocument(opc);
        String htmlText="";
        try {
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    htmlText+=getParagraphHtmlText((XWPFParagraph) element);
                }
                // 表格
                else if (element instanceof XWPFTable) {
                    htmlText+=getTabelHtmlText((XWPFTable) element);
                }
            }
            //获取word中的所有图片
            List<XWPFPictureData> picLists= document.getAllPictures();
            for(XWPFPictureData pic:picLists){
                System.out.println("图片名称:\t" + pic.getFileName());
                System.out.println("图片类型：\t" + pic.getPictureType());
                byte[] data = pic.getData();
                System.out.println(data);
                //字节流图片上传，并返回服务器地址
                String imgUrl = getImageUrl(data, pic.getFileName());
                System.out.println("图片服务器地址："+imgUrl);
                //组装img
                htmlText+="<p><img alt='' src='"+imgUrl+"'></p>";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            document.close();
            opc.close();
        }
        return htmlText;
    }

    /**
     * 字节流图片上传
     * @param data:图片字节流
     * @param fileName:图片名称
     */
    public static String getImageUrl(byte[] data,String fileName) throws Exception{
        String imgUrl="";
        Long res =new Date().getTime();
        //设置文件存储路径，可以存放在你想要指定的路径里面
        String rootPath="D:/mimi/"+File.separator+"upload/images/";
        // 新文件名
        String newFileName =res + fileName.substring(fileName.lastIndexOf("."));
        //新文件
        File newFile=new File(rootPath+File.separator+newFileName);
        //判断文件目录是否存在
        if(!newFile.getParentFile().exists()){
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }

        //-------把图片文件写入磁盘 start ----------------
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(data);
        fos.close();
        //-------把图片文件写入磁盘 end ----------------
        //服务器图片地址
        String baseURL = "http://192.168.0.76:8080/mimi/upload/images/";
        imgUrl=baseURL+newFileName;

        return imgUrl;
    }


}
