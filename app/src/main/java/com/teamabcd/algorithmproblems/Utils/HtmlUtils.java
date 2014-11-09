package com.teamabcd.algorithmproblems.Utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/6/14
 */
public class HtmlUtils {

    public static Spanned fromHtmlTrimTrailingLineBreak(String string) {
        return Html.fromHtml(string.replaceAll("(<br>\\s*)+$", ""));
    }
}
