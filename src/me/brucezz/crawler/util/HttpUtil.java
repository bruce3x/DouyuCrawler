package me.brucezz.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brucezz on 2016/01/04.
 * DouyuCrawler
 */
public class HttpUtil {

    /**
     * GET方法
     */
    public static String get(String url) {

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(20 * 1000);
            conn.setRequestMethod("GET");
            InputStream in = conn.getInputStream();

            LogUtil.d("HTTP GET", url);

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            in.close();

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
