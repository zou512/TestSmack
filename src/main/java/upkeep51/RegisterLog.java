package upkeep51;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

///注册测试并打印日志
public class RegisterLog {
    //18628289976创建成功
    private static String lineSpace = "\n";
    private static int i = 0;
    private static BufferedWriter bufferedWriter;

    public static void main(String[] args) {
        File file = new File("C:\\jar\\registerLog.txt");
        if (!file.exists()) {
            ///文件不存在，新建
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < 90; j++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RegisterLog.logon(getPhone());
                }
            }).start();
        }

    }

    private static synchronized String getPhone() {
        i++;
        String phone;
        if (i<10){
            phone = "00"+i;
        }else if (i<100){
            phone = "0"+i;
        }else {
            phone = String.valueOf(i);
        }
        return "18626" + phone;
    }

    ///注册10000个
    public static void logon(String phone) {
        int number = i;
    ///创建log文件
        File phoneLog = new File("C:\\jar\\线程" + number + ".txt");
        if (!phoneLog.exists()) {
            ///文件不存在，新建
            try {
                phoneLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter phoneWriter = null;
        try {
            phoneWriter = new BufferedWriter(new FileWriter(phoneLog));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String phoneString;
        for (int j = 0; j < 1000; j++) {
            if (j < 10) {
                phoneString = phone + "00" + j;
            } else if (j < 100) {
                phoneString = phone + "0" + j;
            } else{
                phoneString = phone + j;
            } /*else {
                phoneString = phone + i;
            }*/


            String id = oneConnect(number,phoneWriter, phoneString);
            if (id != null && id.length() > 0) {
                boolean twoConnect = twoConnect(number,phoneWriter, phoneString, id);
                if (twoConnect) {
                    boolean threeConnect = threeConnect(number,phoneWriter, phoneString, id);
                    if (threeConnect) {
                        System.out.println(phoneString + "创建成功");
                    } else {
                        System.out.println(phoneString + "第三步失败");
                    }
                } else {
                    System.out.println(phoneString + "第二步失败");
                }
            } else {
                System.out.println(phoneString + "第一步失败");
            }
        }
    }

    ///第一次连接  number是线程编号
    private static String oneConnect(int number,BufferedWriter phoneWriter, String phoneString) {
        //http://192.168.124.30:8086/passport/user/INSPHONE?p=18628288951
        String jsonData = loadJSON("http://192.168.124.30:8086/passport/user/INSPHONE?p=" + phoneString);
        try {
            bufferedWriter.write("线程编号"+number+"::"+phoneString + "第一次连接==" + jsonData +lineSpace);
            bufferedWriter.flush();
            phoneWriter.write(phoneString + "第一次连接==" + jsonData +lineSpace);
            phoneWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("第一步返回=" + jsonData);
        JSONObject object = JSONObject.parseObject(jsonData);
        return (String) object.get("id");
    }

    ///第二次连接
    private static boolean twoConnect(int number,BufferedWriter phoneWriter, String phoneString, String id) {
        //http://192.168.124.30:8086/passport/user/SMSPUSH?p=18628288951&id=d6712456270d2e87a88454f870f8f89b
        String json = loadJSON("http://192.168.124.30:8086/passport/user/SMSPUSH?p=" + phoneString + "&id=" + id);
        try {
            bufferedWriter.write("线程编号"+number+"::"+phoneString + "第二次连接==" + json +lineSpace);
            bufferedWriter.flush();
            phoneWriter.write(phoneString + "第二次连接==" + json +lineSpace);
            phoneWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("第二步返回=" + json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        Object query = jsonObject.get("Query");
        if ((query != null) && ((Integer) query == 1)) {
            return true;
        } else {
            return false;
        }
    }

    ///第三次连接
    private static boolean threeConnect(int number,BufferedWriter phoneWriter, String phoneString, String id) {
        //http://192.168.124.30:8086/passport/user/SMSCOMP?p=18628288951&id=d6712456270d2e87a88454f870f8f89b&sms=152526&password=123456
        String json = loadJSON("http://192.168.124.30:8086/passport/user/SMSCOMP?p=" + phoneString + "&id=" + id + "&sms=152526&password=123456");
        try {
            bufferedWriter.write("线程编号"+number+"::"+phoneString + "第三次连接==" + json +lineSpace);
            bufferedWriter.flush();
            phoneWriter.write(phoneString + "第三次连接==" + json +lineSpace);
            phoneWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("第三步返回=" + json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        Object query = jsonObject.get("Query");
        if (query != null && ((Integer) query == 1)) {
            return true;
        } else {
            return false;
        }
    }


    //网络请求返回string
    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();

        try {
            URL oracle = new URL(url);

            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "utf-8"));//防止乱码
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}


