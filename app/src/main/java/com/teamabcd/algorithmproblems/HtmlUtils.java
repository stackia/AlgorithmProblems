package com.teamabcd.algorithmproblems;

import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    public static Spanned fromHtmlTrimTrailingLineBreak(String string) {
        Pattern pattern = Pattern.compile("(<br>\\s*)+$");
        Matcher matcher = pattern.matcher(string);
        return Html.fromHtml(matcher.replaceAll(""));
    }
}
