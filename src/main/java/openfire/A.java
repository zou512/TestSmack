package openfire;


public class A {
    public static int i = 0;

    public static void main(String[] args) {
//        createDataUser();
        manyConnectPC();
//        ComSmackClient.go6();
//        ComSmackClient.createData(1000*1000*50,1000*1000*10,"C:\\down\\ofuser.txt");
    }
    public static class LoginAnonymously implements Runnable{

        @Override
        public void run() {
                ComSmackClient.loginAnonymously();
        }
    }

    ///执行并发测试  本机
    public static void manyConnectPC() {

        System.out.println("你好啊");
        for (int j = 0; j < 5000; j++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new ConcurrentThread().start();
            System.out.println("testAccount" + i);
        }
        System.out.println("好你妈");
        System.out.println("最终总数："+i+";错误："+(ComSmackClient.getFail()-1));
        //https://github.com/Blazemeter/jmeter-bzm-plugins/blob/master/xmpp/XMPPSampler.md
        //www.blazemeter.com
    }//iperf3 -c 192.168.124.9 -p 5201 -i 1 -t 10

    ///测试并发线程
    public static class ConcurrentThread extends Thread {
        @Override
        public void run() {
            //2u服务器
//            ComSmackClient.go7(getUserName(),"123456","zhouxing@2u-centos7");
            //本机
            ComSmackClient.go7(getUserName(),"123456","zhouxing@win-jn1ul9f8j6b");
            //大勇
//            ComSmackClient.go7(getUserName(), "123456", "hongyan@192.168.124.9");

        }
    }

    public synchronized static String getUserName() {
        return "testAccount" + i++;
    }

    //测试并发 连接服务器
    public static void manyConnectService(){
        for (int j = 0; j < 5*1000; j++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServiceSmackClient.connect(getUserName(), "123456");
                }
            }).start();
            System.out.println("线程：" + i);
        }
    }
}
