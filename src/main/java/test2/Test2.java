package test2;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLEngine;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lizhen on 2018/10/25.
 */
class Person{

    List<String> toys;

    public Person(List<String> toys) {
        this.toys = toys;
    }

    @Override
    public String toString() {
        return "Person{" +
                "toys=" + Arrays.toString(toys.toArray()) +
                '}';
    }
}


//@SpringBootApplication(scanBasePackages="test2")
public class Test2 {

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType,SSLEngine e)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            return;

        }
    }

//    @Bean
//    String str1(){
//        return "a";
//    }
//
//    @Bean
//    String str2(){
//        return "b";
//    }
//
//    @Bean
//    Person person(List<String> list){
//        return new Person(list);
//    }
//
//    @Bean
//    @Qualifier("httpsRestTemplate")
//    public RestTemplate buildRestTemplate(){
//        return new RestTemplate();
//    }

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(Test2.class);
//        Person person = context.getBean(Person.class);
//        System.out.println(person);

        RestTemplate httpsRestTemplate = new RestTemplate();

//        RestTemplate restTemplate = (RestTemplate)context.getBean("httpsRestTemplate");
        /**
         * 1、发现本机不能同时启动2个springboot项目...
         * 2、自签名证书服务器，需要手动添加信任证书...（这里自定义了一个信任类miTM并且信任所有本机的证书？）
         *    具体参考：https://blog.csdn.net/herotangabc/article/details/41824065
         */

        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc;
        try {
            sc = javax.net.ssl.SSLContext
                    .getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                    .getSocketFactory());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /**
         * 解决连接ip地址时报的No subject alternative names错误
         * 在创建 SSL 连接时，HttpsClient 步骤并进行基本的服务器身份验证，以防止 URL 欺骗，其中包括验证服务器的名称是否在证书中
           HttpsClient 主要使用 HostNameChecker 检查主机名和证书中指定的名称。如果失败了，HostNameVerifier 就会出现，它被用来验证主机名
           在 HostNameVerifier 没有被重写时，默认是这个验证是错误的，也就意味着 HostNameChecker 失败后就会抛出这个异常
           HostNameChecker 在实现上，如果传入的主机名是 IP 地址，将由 matchIP 方法在可用的条目中搜索IP地址对应的名称，同时在没有条目可以提供和IP地址匹配的名称时抛出 CertificateException 异常
           所以，如果想通过使用 IP 作为主机名连接，证书中应该包含名称和与其匹配的 IP 地址这个条目
         ---------------------
         作者：袭冷
         来源：CSDN
         原文：https://blog.csdn.net/guoxilen/article/details/78543456
         版权声明：本文为博主原创文章，转载请附上博文链接！
         */
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslsession) -> true);


        HttpEntity httpEntity = new HttpEntity(null,null);
        String responseBody = httpsRestTemplate.getForObject("https://127.0.0.1:8443/setx", String.class);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(responseBody).getAsJsonObject();

        System.out.println("sso返回响应："+jsonObject.toString());
    }
}
