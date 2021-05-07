package com.github.script.task.bridge.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TextUtil {

    //中文正则表达式
    private final static Pattern ChinesePattern = Pattern.compile("[(a-zA-Z0-9\\u4e00-\\u9fa5)]");

    private final static Map<String, Character> Width2Map = new HashMap<>() {{
        put("，", ',');
        put("。", '.');
        put("、", '/');
        put("；", ';');
        put("‘", '\'');
        put("【", '[');
        put("】", ']');
        put("、", '\\');


        put("《", '<');
        put("》", '>');
        put("？", '?');
        put("：", ':');
        put("“", '\"');
        put("”", '\"');
        put("{", '{');
        put("}", '}');
        put("—", '-');
        put("+", '+');
        put("|", '|');

        put("·", '`');
        put("！", '!');
        put("@", '@');
        put("#", '#');
        put("￥", '$');
        put("%", '%');
        put("…", '^');
        put("&", '&');
        put("*", '*');
        put("（", '(');
        put("）", ')');


    }};


    /**
     * 格式化中文
     *
     * @return
     */
    public static String format(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final String str = text.substring(i, i + 1);
            final int ascii = Integer.valueOf(str.charAt(0));
//            final Character charset = Width2Map.get(str);
            if (ChinesePattern.matcher(str).find() || (ascii >= 32 && ascii <= 126)) {
                sb.append(str);
            } else if (Width2Map.containsKey(str) || Width2Map.containsValue(str.charAt(0))) {
                sb.append(str);
            } else {
                sb.append(" ");
            }

        }
        return sb.toString();
    }


}
