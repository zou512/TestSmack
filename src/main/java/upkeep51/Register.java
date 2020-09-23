package upkeep51;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import sun.plugin.javascript.navig.JSObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

///注册
public class Register {
//18628289976创建成功
    private static int i=0;
    public static void main(String[] args) {

        for (int j = 0; j < 10; j++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Register.logon(getPhone());
                }
            }).start();
        }

    }
    private static synchronized String getPhone(){
        return "186281"+i++;
    }

    ///注册10000个
    public static void logon(String phone) {
        String phoneString;
        for (int i = 0; i < 10000; i++) {
            if (i < 10) {
                phoneString = phone + "000" + i;
            } else if (i < 100) {
                phoneString = phone + "00" + i;
            } else if (i < 1000) {
                phoneString = phone + "0" + i;
            } else {
                phoneString = phone + i;
            }
            String id = oneConnect(phoneString);
            if (id != null && id.length() > 0) {
                boolean twoConnect = twoConnect(phoneString, id);
                if (twoConnect) {
                    boolean threeConnect = threeConnect(phoneString, id);
                    if (threeConnect) {
                        System.out.println(phoneString + "创建成功");
                    } else {
                        System.out.println(phoneString+"第三步失败");
                    }
                } else {
                    System.out.println(phoneString+"第二步失败");
                }
            } else {
                System.out.println(phoneString+"第一步失败");
            }
        }
    }

    ///第一次连接
    private static String oneConnect(String phoneString) {
        //http://192.168.124.30:8086/passport/user/INSPHONE?p=18628288951
        String jsonData = loadJSON("http://192.168.124.30:8086/passport/user/INSPHONE?p=" + phoneString);
        System.out.println("第一步返回=" + jsonData);
        JSONObject object = JSONObject.parseObject(jsonData);
        return (String) object.get("id");
    }

    ///第二次连接
    private static boolean twoConnect(String phoneString, String id) {
        //http://192.168.124.30:8086/passport/user/SMSPUSH?p=18628288951&id=d6712456270d2e87a88454f870f8f89b
        String json = loadJSON("http://192.168.124.30:8086/passport/user/SMSPUSH?p=" + phoneString + "&id=" + id);
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
    private static boolean threeConnect(String phoneString, String id) {
        //http://192.168.124.30:8086/passport/user/SMSCOMP?p=18628288951&id=d6712456270d2e87a88454f870f8f89b&sms=152526&password=123456
        String json = loadJSON("http://192.168.124.30:8086/passport/user/SMSCOMP?p=" + phoneString + "&id=" + id + "&sms=152526&password=123456");
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


