package com.zhen.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String工具
 * 主要对 org.apache.commons.lang3.StringUtils 的一些方法进行重写,达到更方便的使用
 *
 * @author
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final Logger logger = Logger.getLogger(StringUtils.class);

    private static Pattern pattern = Pattern.compile("<(span)?\\sstyle.*?style>|(span)?\\sstyle=.*?>", Pattern.DOTALL);
    private static Pattern pattern2 = Pattern.compile("(<[^>]+>)", Pattern.DOTALL);
    private static Pattern patterncomma = Pattern.compile("(&[^;]+;)", Pattern.DOTALL);
    /**
     * 字母
     */
    private static final String CHAR = "char";
    /**
     * 数字
     */
    private static final String NUM = "num";

    /**
     * 一次性判断多个或单个对象为空。
     *
     * @param objects
     * @return 只要有一个元素为Blank，则返回true
     * @author zhou-baicheng
     */
    public static boolean isBlank(Object... objects) {
        Boolean result = false;
        for (Object object : objects) {
            if (null == object || "".equals(object.toString().trim())
                    || "null".equals(object.toString().trim())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 获得指定长度的随机字符串
     * 参照ASCII码表 65到90为大写字母，97到122为小写字母
     *
     * @param length 要生成的字符串长度
     * @return 返回生成的随机字符串
     */
    public static String getStringRandom(int length) {
        StringBuilder valSb = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            //输出是数字还是字母
            String charOrNum = random.nextInt(2) % 2 == 0 ? CHAR : NUM;
            switch (charOrNum) {
                case CHAR:
                    // 输出是大写字母还是小写字母
                    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    valSb.append((char) (random.nextInt(26) + choice));
                    break;
                case NUM:
                    valSb.append(random.nextInt(10));
                    break;
                default:
                    break;
            }
        }
        return valSb.toString().toLowerCase();
    }

    /**
     * 一次性判断多个或单个对象不为空。
     *
     * @param objects
     * @return 只要有一个元素不为Blank，则返回true
     * @author wuhengzhen
     */
    public static boolean isNotBlank(Object... objects) {
        return !isBlank(objects);
    }

    public static boolean isBlank(String... objects) {
        Object[] object = objects;
        return isBlank(object);
    }

    public static boolean isNotBlank(String... objects) {
        Object[] object = objects;
        return !isBlank(object);
    }

    public static boolean isBlank(String str) {
        Object object = str;
        return isBlank(object);
    }

    public static boolean isNotBlank(String str) {
        Object object = str;
        return !isBlank(object);
    }

    /**
     * 判断一个字符串在数组中存在几个
     *
     * @param baseStr
     * @param strings
     * @return
     */
    public static int indexOf(String baseStr, String[] strings) {

        if (null == baseStr || baseStr.length() == 0 || null == strings) {
            return 0;
        }

        int i = 0;
        for (String string : strings) {
            boolean result = baseStr.equals(string);
            i = result ? ++i : i;
        }
        return i;
    }

    /**
     * 判断一个字符串是否为JSONObject,是返回JSONObject,不是返回null
     *
     * @param args
     * @return
     */
    public static net.sf.json.JSONObject isJSONObject(String args) {
        net.sf.json.JSONObject result = null;
        if (isBlank(args)) {
            return result;
        }
        try {
            return net.sf.json.JSONObject.fromObject(args.trim());
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * 判断一个字符串是否为JSONArray,是返回JSONArray,不是返回null
     *
     * @param args
     * @return
     */
    public static net.sf.json.JSONArray isJSONArray(Object args) {
        JSONArray result = new JSONArray();
        if (isBlank(args)) {
            return null;
        }
        if (args instanceof net.sf.json.JSONArray) {

            net.sf.json.JSONArray arr = (net.sf.json.JSONArray) args;
            for (Object json : arr) {
                if (json != null && json instanceof net.sf.json.JSONObject) {
                    result.add(json);
                    continue;
                } else {
                    result.add(JSONObject.fromObject(json));
                }
            }
            return result;
        } else {
            return null;
        }

    }

    public static String trimToEmpty(Object str) {
        return (isBlank(str) ? "" : str.toString().trim());
    }

    /**
     * 把Map转换成get请求参数类型,如 {"name"=20,"age"=30} 转换后变成 name=20&age=30
     *
     * @param map
     * @return
     */
    public static String mapToGet(Map<? extends Object, ? extends Object> map) {
        String result = "";
        if (map == null || map.size() == 0) {
            return result;
        }
        Set<? extends Object> keys = map.keySet();
        for (Object key : keys) {
            result += ((String) key + "=" + (String) map.get(key) + "&");
        }

        return isBlank(result) ? result : result.substring(0, result.length() - 1);
    }

    /**
     * 把一串参数字符串,转换成Map 如"?a=3&b=4" 转换为Map{a=3,b=4}
     *
     * @param args
     * @return
     */
    public static Map<String, ? extends Object> getToMap(String args) {
        if (isBlank(args)) {
            return null;
        }
        args = args.trim();
        //如果是?开头,把?去掉
        if (args.startsWith("?")) {
            args = args.substring(1, args.length());
        }
        String[] argsArray = args.split("&");

        Map<String, Object> result = new HashMap<String, Object>();
        for (String ag : argsArray) {
            if (!isBlank(ag) && ag.indexOf("=") > 0) {

                String[] keyValue = ag.split("=");
                //如果value或者key值里包含 "="号,以第一个"="号为主 ,如  name=0=3  转换后,{"name":"0=3"}, 如果不满足需求,请勿修改,自行解决.

                String key = keyValue[0];
                String value = "";
                for (int i = 1; i < keyValue.length; i++) {
                    value += keyValue[i] + "=";
                }
                value = value.length() > 0 ? value.substring(0, value.length() - 1) : value;
                result.put(key, value);

            }
        }

        return result;
    }

    /**
     * 转换成Unicode
     *
     * @param str
     * @return
     */
    public static String toUnicode(String str) {
        String[] as = new String[str.length()];
        StringBuilder s1 = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int v = str.charAt(i);
            if (v >= 19968 && v <= 171941) {
                as[i] = Integer.toHexString(str.charAt(i) & 0xffff);
                s1.append("\\u").append(as[i]);
            } else {
                s1.append(str.charAt(i));
            }
        }
        return s1.toString();
    }

    /**
     * 合并数据
     *
     * @param v
     * @return
     */
    public static String merge(Object... v) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < v.length; i++) {
            sb.append(v[i]);
        }
        return sb.toString();
    }

    /**
     * 字符串转urlcode
     *
     * @param value
     * @return
     */
    public static String strToUrlcode(String value) {
        try {
            value = java.net.URLEncoder.encode(value, "utf-8");
            return value;
        } catch (UnsupportedEncodingException e) {
            logger.error("字符串转换为URLCode失败,value:" + value, e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * urlcode转字符串
     *
     * @param value
     * @return
     */
    public static String urlcodeToStr(String value) {
        try {
            value = java.net.URLDecoder.decode(value, "utf-8");
            return value;
        } catch (UnsupportedEncodingException e) {
            logger.error("URLCode转换为字符串失败;value:" + value, e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断字符串是否包含汉字
     *
     * @param txt
     * @return
     */
    public static Boolean containsCN(String txt) {
        if (isBlank(txt)) {
            return false;
        }
        for (int i = 0; i < txt.length(); i++) {

            String bb = txt.substring(i, i + 1);

            boolean cc = Pattern.matches("[\u4E00-\u9FA5]", bb);
            if (cc) {
                return cc;
            }
        }
        return false;
    }

    /**
     * 去掉HTML代码
     *
     * @param news
     * @return
     */
    public static String removeHtml(String news) {
        String s = news.replaceAll("amp;", "").replaceAll("<", "<").replaceAll(">", ">");

        Matcher matcher = pattern.matcher(s);
        String str = matcher.replaceAll("");

        Matcher matcher2 = pattern2.matcher(str);
        String strhttp = matcher2.replaceAll(" ");


        String regEx = "(((http|https|ftp)(\\s)*((\\:)|：))(\\s)*(//|//)(\\s)*)?"
                + "([\\sa-zA-Z0-9(\\.|．)(\\s)*\\-]+((\\:)|(:)[\\sa-zA-Z0-9(\\.|．)&%\\$\\-]+)*@(\\s)*)?"
                + "("
                + "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])"
                + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
                + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
                + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])"
                + "|([\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*)*[\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*[\\sa-zA-Z]*"
                + ")"
                + "((\\s)*(\\:)|(：)(\\s)*[0-9]+)?"
                + "(/(\\s)*[^/][\\sa-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";
        Pattern p1 = Pattern.compile(regEx, Pattern.DOTALL);
        Matcher matchhttp = p1.matcher(strhttp);
        String strnew = matchhttp.replaceAll("").replaceAll("(if[\\s]*\\(|else|elseif[\\s]*\\().*?;", " ");

        Matcher matchercomma = patterncomma.matcher(strnew);
        String strout = matchercomma.replaceAll(" ");
        String answer = strout.replaceAll("[\\pP‘’“”]", " ")
                .replaceAll("\r", " ").replaceAll("\n", " ")
                .replaceAll("\\s", " ").replaceAll("　", "");


        return answer;
    }

    /**
     * 把数组的空数据去掉
     *
     * @param array
     * @return
     */
    public static List<String> array2Empty(String[] array) {
        List<String> list = new ArrayList<String>();
        for (String string : array) {
            if (StringUtils.isNotBlank(string)) {
                list.add(string);
            }
        }
        return list;
    }

    /**
     * 把数组转换成set
     *
     * @param array
     * @return
     */
    public static Set<?> array2Set(Object[] array) {
        Set<Object> set = new TreeSet<Object>();
        for (Object id : array) {
            if (null != id) {
                set.add(id);
            }
        }
        return set;
    }

    /**
     * serializable toString
     *
     * @param serializable
     * @return
     */
    public static String toString(Serializable serializable) {
        if (null == serializable) {
            return null;
        }
        try {
            return (String) serializable;
        } catch (Exception e) {
            return serializable.toString();
        }
    }

    /**
     * 如果Object为null则返回“”空字符串，反之返回（String）Object
     */
    public static String checkNull(Object o) {
        String result = "";
        if (o != null) {
            result = o.toString();
        }
        return result;
    }

    /**
     * BASE64编码
     *
     * @param input
     * @return
     */
    public static String encodeBase64(String input) {
        Base64.Encoder encoder = Base64.getEncoder();
        if (StringUtils.isEmpty(input)) {
            return "";
        } else {
            String enCodeStr = encoder.encodeToString(input.getBytes());
            return enCodeStr;
        }
    }

    /**
     * BASE64解码
     *
     * @param enCodeStr
     * @return
     */
    public static String decodeBase64(String enCodeStr) {
        Base64.Decoder decoder = Base64.getDecoder();
        if (StringUtils.isEmpty(enCodeStr)) {
            return "";
        } else {
            String str = null;
            try {
                str = new String(decoder.decode(enCodeStr), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    /**
     * 将数据库字段/表名换成代码中的方法名格式（首字符大写、去_线）
     * 例如 s_role_all 转换成SRoleAll
     *
     * @param name
     * @return
     */
    public static String transDBField2MethodNm(String name) {///
        if (name == null || name.trim().equals("")) {
            return name;
        }

        name = name.toLowerCase();
        String[] seg = name.split("_");
        String _field = "";
        for (String s : seg) {
            if (!s.trim().equals("")) {
                _field += upperFirstChar(s);
            }
        }

        return _field.trim();
    }

    /**
     * 将首字符换成大写
     *
     * @param fieldNm
     * @return
     */
    public static String upperFirstChar(String fieldNm) {

        if (fieldNm == null || fieldNm.trim().equals("")) {
            return "";
        }
        fieldNm = fieldNm.toLowerCase();
        char firstChar = fieldNm.charAt(0);
        StringBuffer retFields = new StringBuffer();

        if (fieldNm.length() > 1) {
            retFields.append(String.valueOf(firstChar).toUpperCase()).append(fieldNm.substring(1));
            return retFields.toString();
        } else {
            return String.valueOf(firstChar).toUpperCase();
        }
    }
}
