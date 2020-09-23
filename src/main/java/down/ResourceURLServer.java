package down;

import com.sun.deploy.util.StringUtils;
import sun.corba.OutputStreamFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

///访问网址下载文件
public class ResourceURLServer {

    public static void getURLResource(String ourputFile, String urlStr) throws Exception {
        FileWriter fw = new FileWriter(ourputFile);
        PrintWriter pw = new PrintWriter(fw);
        URL resourceUrl = new URL(urlStr);
        InputStream content = (InputStream) resourceUrl.getContent();
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = in.readLine()) != null) {
            pw.println(line);
        }
        pw.close();
        fw.close();
    }

    //String route,String fileName,String url)
    public static void down(String route, String fileName, String urlStr) {
        long begin = System.currentTimeMillis();
        File file = null;
        try {
            file = new File(route + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = new byte[8 * 1024];
            int count = 0;
            while ((count = in.read(bytes, 0, bytes.length)) > 0) {
                bufferedOutputStream.write(bytes, 0, count);
            }
            in.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        ///下载时长  换算成秒
        float duration = (end - begin) / 1000f;
        ///下载的总大小  单位为兆
        float size = file.length() / 1024f / 1024;
        ///下载平均速度
        float speed = size / duration;
        System.out.println(fileName + "下载时长=" + duration + "s;下载总大小：" + size + "m;平均速度：" + speed + "m/s");
    }

    //下载的时候打印实时网速
    public static void downSpeed(String route, String fileName, String urlStr) {
        long begin = System.currentTimeMillis();
        long intervalTime = begin;
        long intervalSize = 0;
        long sTime = 0;
        File file = null;
        try {
            file = new File(route + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = new byte[8 * 1024];
            int count = 0;
            while ((count = in.read(bytes, 0, bytes.length)) > 0) {
                bufferedOutputStream.write(bytes, 0, count);
                bufferedOutputStream.flush();
                sTime = System.currentTimeMillis() - intervalTime;
                if (sTime >= 1000) {
                    long sSize = file.length() - intervalSize;
                    System.out.println(fileName+"实时速度："+sSize/1024f/1024f+"m/"+sTime/1000f+"s");
                    intervalTime = intervalTime+sTime;
                    intervalSize= file.length();
                }

            }
            in.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        ///下载时长  换算成秒
        float duration = (end - begin) / 1000f;
        ///下载的总大小  单位为兆
        float size = file.length() / 1024f / 1024;
        ///下载平均速度
        float speed = size / duration;
        System.err.println(fileName + "下载时长=" + duration + "s;下载总大小：" + size + "m;平均速度：" + speed + "m/s");
    }
}
