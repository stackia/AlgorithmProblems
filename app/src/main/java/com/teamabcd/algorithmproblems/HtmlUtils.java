package com.teamabcd.algorithmproblems;

import android.text.Html;
import android.text.Spanned;

public class HtmlUtils {

    public static Spanned fromHtmlTrimTrailingLineBreak(String string) {
        return Html.fromHtml(string.replaceAll("(<br>\\s*)+$", ""));
    }
}
