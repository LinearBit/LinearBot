package org.linear.linearbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

    public static String filterColor(String text){

        String regEx = "§[0-9a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher nameMatcher = p.matcher(text);
        return nameMatcher.replaceAll("").trim();

    }
}
