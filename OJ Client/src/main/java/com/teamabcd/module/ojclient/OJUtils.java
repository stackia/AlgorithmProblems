package com.teamabcd.module.ojclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Project: Algorithm Problems
 * Created by: Stackia<jsq2627@gmail.com>
 * Date: 10/14/14
 */
class OJUtils {
    static String generateURLForm(String... data) throws UnsupportedEncodingException {
        HashMap<String, String> keyValue = new HashMap<String, String>();
        String key = "";
        for (int i = 0; i < data.length; i++) {
            if (i % 2 == 0) { // Is key
                key = data[i];
            } else { // Is value
                keyValue.put(key, data[i]);
            }
        }
        return generateURLForm(keyValue);
    }

    static String generateURLForm(HashMap<String, String> data) throws UnsupportedEncodingException {
        Set<String> keys = data.keySet();
        Iterator<String> keyIterator = keys.iterator();
        String content = "";
        for (int i = 0; keyIterator.hasNext(); i++) {
            String key = keyIterator.next();
            if (i != 0) {
                content += "&";
            }
            content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
        }
        return content;
    }

    static String decodeGB2312(byte[] encodedString) {
        try {
            return new String(encodedString, "GB2312");
        } catch (IOException e) {
            return null;
        }
    }
}
