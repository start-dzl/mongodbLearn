package com.dzl.mongodb.util;
import com.dzl.mongodb.entity.Classt;
import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("rawtypes")
public class OgnlUtil {

    private static Map expressions = new HashMap();

    private static OgnlContext context = new OgnlContext(new DefaultClassResolver(),
            new DefaultTypeConverter(),
            new DefaultMemberAccess(true));

    public static Object getValue(String expression, Object root) throws OgnlException {
        return Ognl.getValue(compile(expression), context, root);
    }

    public static Object getValue(String expression, Map context, Object root) throws OgnlException {
        return Ognl.getValue(compile(expression), context, root);
    }

    public static Object getValue(String name, Map context, Object root, Class resultType) throws OgnlException {
        return Ognl.getValue(compile(name), context, root, resultType);
    }

    public static void setValue(String name, Object root, Object value) throws OgnlException {
        Ognl.setValue(compile(name), context, root, value);
    }

    public static void setValue(String name, Map context, Object root, Object value) throws OgnlException {
        Ognl.setValue(compile(name), context, root, value);
    }


    private static Object compile(String expression) throws OgnlException {
        synchronized (expressions) {
            String expr = replaceExpression(expression);
            Object o = expressions.get(expr);
            if (o == null) {
                o = Ognl.parseExpression(expr);
                expressions.put(expr, o);
            }
            return o;
        }
    }

    /**
     * 判断是否为运算操作并替换做非空判断
     *
     * @author yulj
     * @create_date 2017-09-21
     */
    private static String replaceExpression(String expression) {
        String expr = expression;
        if (!regex(expr, "\\+|\\-|\\*|\\/")) {
            return expr;
        }
        List<String> filter = new ArrayList<>();
        Pattern p = Pattern.compile("#\\w+(\\.\\w+)*");
        Matcher matcher = p.matcher(expr);
        while (matcher.find()) {
            String temp = matcher.group();
            if (filter.contains(temp)) {
                continue;
            }
            filter.add(temp);
            expr = StringUtils.replace(expr, temp, String.format("(%1$s eq null ? 0 : %2$s)", temp, temp));
        }
        return expr;
    }

    public static boolean regex(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).find();
    }

    public static void main(String[] args) throws Exception {

        String opt = "aaa*/";
        boolean regex = regex(opt, "\\+|\\-|\\*|\\/");
        String re = "paymentedInvoiceAmt=#pcOrderInfo.paymentedInvoiceAmt + #invoiceInfo.invoiceAmt - #num";
        Pattern p = Pattern.compile("#\\w+(\\.\\w+)*");
        Matcher matcher = p.matcher(re);
        while (matcher.find()) {
            String temp = matcher.group();
            re = re.replace(temp, String.format("(%1$s eq null ? 0 : %2$s)", temp, temp));
        }
        System.out.println(re);
        Map map = new HashMap();
        map.put("user", "Keven");
        BigDecimal bigDecimal = new BigDecimal("0");
        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        map.put("a",bigDecimal);
        map.put("b", null);
        map.put("c", new BigDecimal(5));
        List list = new ArrayList();
        map.put("details", list);
        list.add("BPM1");
        list.add("BPM2");
        Classt classt = new Classt();
        map.put("classt",classt);
        //System.out.println(OgnlUtil.getValue("a/(3.06+a)", map));
        System.out.println(AviatorEvaluator.execute("(a<5 ? 10:a*0.3)", map));
        System.out.println(AviatorEvaluator.execute("classt.num=3", map));
        System.out.println(classt.toString());
        System.out.println(OgnlUtil.getValue("a*0.75", map));
        System.out.println(OgnlUtil.getValue("(b eq null ? 0:b)+a", map));
        System.out.println(OgnlUtil.getValue("details[1]", map));
        OgnlUtil.setValue("details[1]", map, "Modify Data");
        System.out.println(list.get(1));
    }

}
