package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestNet {
    private String stringUrlBean = "https://qq.ip138.com/idsearch/index.asp?userid=51162219990621";
    private String userid;
    private String stringUrlEnd = "&action=idcard";
    public static void main(String[] args) {
        try {
            //根据URL地址创建一个URL对象
            URL url = new URL("https://qq.ip138.com/idsearch/index.asp?userid=511622199906210003&action=idcard");

            //获取URL连接，open方法返回一个URLConnection类的对象
            URLConnection conn = url.openConnection();

            //从连接获取输入流，请求的输入也就是对请求的输入，即是相应，
            InputStream in = conn.getInputStream();
            InputStreamReader is = new InputStreamReader(in);

            //将字节流转换成字符流，方便操作
            BufferedReader br = new BufferedReader(is);
            String line = null;
            while((line = br.readLine())!=null) {
                //打印相应的内容
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
