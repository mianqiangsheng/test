package breakpoint.downbit.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.login.LoginContext;

/**
 * <p>
 * 网络请求操作工具类
 *
 * @author niujinpeng
 * @Date 2020/7/19 21:25
 */
public class HttpUtls {

    /**
     * 获取 HTTP 链接
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection httpConnection = (HttpURLConnection)httpUrl.openConnection();
        httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        return httpConnection;
    }

    /**
     * 获取 HTTPS 链接
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsUrlConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpsURLConnection httpsConnection = (HttpsURLConnection)httpUrl.openConnection();
        httpsConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        return httpsConnection;
    }

    /**
     * 获取 HTTP 链接
     *
     * @param url
     * @param start
     * @param end
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpUrlConnection(String url, long start, Long end) throws IOException {
        HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);
        LogUtils.debug("此线程下载内容区间 {}-{}", start, end);
        if (end != null) {
            httpUrlConnection.setRequestProperty("RANGE", "bytes=" + start + "-" + end);
        } else {
            httpUrlConnection.setRequestProperty("RANGE", "bytes=" + start + "-");
        }
        Map<String, List<String>> headerFields = httpUrlConnection.getHeaderFields();
        for (String s : headerFields.keySet()) {
            LogUtils.debug("此线程相应头{}:{}", s, headerFields.get(s));
        }
        return httpUrlConnection;
    }

    /**
     * 获取网络文件大小 bytes
     * HTTP1.1 规定 HEAD 请求的响应中应当有 Content-Length 字段，但并非所有的服务器都遵守这个规定。
     * HTTPS协议没有强制规定Content-Length 字段，所以无法从HTTPS连接中获取文件大小，除非对方提供
     * @param url
     * @return
     * @throws IOException
     */
    public static long getHttpFileContentLength(String url) throws IOException {
        URL httpUrl = new URL(url);
        if ("http".equalsIgnoreCase(httpUrl.getProtocol())){
            HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);

            /**
             * 获取文件的 ETag.
             * 可以存储文件的ETag值，在续传的时候首先判断ETag是否变化，如果变化则丢弃之前下载重新下载
             */
            Map<String, List<String>> headerFields = httpUrlConnection.getHeaderFields();
            List<String> eTagList = headerFields.get("ETag");
            System.out.println(eTagList.get(0));

            int contentLength = httpUrlConnection.getContentLength();
            httpUrlConnection.disconnect();
            return contentLength;
        }else if ("https".equalsIgnoreCase(httpUrl.getProtocol())){
            /**
             * 必须要信任https提供的证书，否则不能建立连接
             */
            try {
                trustAllHttpsCertificates();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            HttpsURLConnection httpsUrlConnection = getHttpsUrlConnection(url);
            Map<String, List<String>> headerFields = httpsUrlConnection.getHeaderFields();
            //https连接无法获得content-length这个头部信息
            int contentLength = httpsUrlConnection.getContentLength();
            httpsUrlConnection.disconnect();
            return contentLength;
        }
        return 0L;
    }

    /**
     * 信赖所有https证书
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static void trustAllHttpsCertificates()
            throws NoSuchAlgorithmException, KeyManagementException
    {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new TrustAllManager();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());
    }

    private static class TrustAllManager
            implements X509TrustManager
    {
        public X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }
        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType)
                throws CertificateException
        {
        }
        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType)
                throws CertificateException
        {
        }
    }

    /**
     * 获取网络文件 Etag
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getHttpFileEtag(String url) throws IOException {
        HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);
        Map<String, List<String>> headerFields = httpUrlConnection.getHeaderFields();
        List<String> eTagList = headerFields.get("ETag");
        httpUrlConnection.disconnect();
        return eTagList.get(0);
    }

    /**
     * 获取网络文件名
     *
     * @param url
     * @return
     */
    public static String getHttpFileName(String url) {
        int indexOf = url.lastIndexOf("/");
        return url.substring(indexOf + 1);
    }
}
