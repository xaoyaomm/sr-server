package com.store.api.utils.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 封装HTTP请求
 * 
 * @author songhaipeng 2013年12月4日
 */
public class HttpUtil {
    protected static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private final static String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    /**
     * 发送POST请求
     * 
     * @param url
     * @param params
     * @return
     */
    
    public static HttpCallResult get(String url){
        HttpCallResult result = new HttpCallResult();
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(httpget);
            if (response != null) {
                result.setContent(EntityUtils.toString(response.getEntity()));
                result.setStatusCode(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(">>E:" + e);
            result.setStatusCode(999);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(">>E:" + e);
                }
            }
        }
        return result;
    }
    
    public static HttpCallResult post(String url, Map<String, String> params) {
        return post(url, params, "");
    }

    /**
     * 发送POST请求
     * 
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static HttpCallResult post(String url, Map<String, String> params, String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            encoding = DEFAULT_CHARACTER_ENCODING;
        }
        HttpCallResult result = new HttpCallResult();
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        log.debug(">>Http Call Url is:" + url);
        HttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(buildDefaultRequestConfig());
            List<NameValuePair> nvps = map2NameValuePair(params);
            post.setEntity(new UrlEncodedFormEntity(nvps, encoding));
            response = client.execute(post);
            if (response != null) {
                result.setContent(EntityUtils.toString(response.getEntity()));
                result.setStatusCode(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(">>E:" + e);
            result.setStatusCode(999);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(">>E:" + e);
                }
            }
        }
        return result;
    }

    /**
     * 异步发送HTTP请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static HttpCallResult postAsync(String url, Map<String, String> params) {
        return post(url, params, "");
    }

    /**
     * 异步发送POST请求
     * 
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static HttpCallResult postAsync(String url, Map<String, String> params, String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            encoding = DEFAULT_CHARACTER_ENCODING;
        }
        HttpCallResult result = new HttpCallResult();

        CloseableHttpAsyncClient client = HttpAsyncClients.custom().setDefaultRequestConfig(buildDefaultRequestConfig()).build();
        log.debug(">>Http Call Url is:" + url);
        HttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = map2NameValuePair(params);
            post.setEntity(new UrlEncodedFormEntity(nvps, encoding));
            
            client.start();
            Future<HttpResponse> future = client.execute(post, null);
            response = future.get();
            if (response != null) {
                result.setContent(EntityUtils.toString(response.getEntity()));
                result.setStatusCode(response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(">>E:" + e);
            result.setStatusCode(999);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(">>E:" + e);
                }
            }
        }

        return result;
    }

    public static String postJosn(String json, String url) {

        byte[] json2Bytes = json.getBytes();
        InputStream instr = null;
        java.io.ByteArrayOutputStream out = null;
        try {
            URL URL = new URL(url);
            URLConnection urlCon = URL.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("Content-Type", "text/xml");
            urlCon.setRequestProperty("Content-length", String.valueOf(json2Bytes.length));
            DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());
            printout.write(json2Bytes);
            printout.flush();
            printout.close();
            instr = urlCon.getInputStream();
            byte[] bis = IOUtils.toByteArray(instr);
            String responseString = new String(bis, "UTF-8");
            if ((responseString == null) || ("".equals(responseString.trim()))) {

            }
            return responseString;

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        } finally {
            try {
                out.close();
                instr.close();

            } catch (Exception ex) {
                return "0";
            }
        }

    }

    /**
     * 
     * @return
     */
    private static RequestConfig buildDefaultRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(500000000).setConnectTimeout(500000000).setConnectionRequestTimeout(500000000).build();
        return requestConfig;
    }

    /**
     * 组装参数
     * 
     * @param params
     * @return
     */
    private static List<NameValuePair> map2NameValuePair(Map<String, String> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (MapUtils.isNotEmpty(params)) {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
    //            if (StringUtils.isNotEmpty(value)) {
                    NameValuePair nvp = new BasicNameValuePair(key, value);
                    log.debug(">>HttpCall params:->" + key + "    =   " + value);
                    nvps.add(nvp);
//                }
            }
        }
        return nvps;
    }

}
