package openfire;

public class Entrance {
    private static int i ;
    /// 在使用java命令运行jar包的时候  传入参数i和参数number
    ///参数i是用户名后面的数字  用于用户名
    public static void main(String[] args) {
        i = Integer.parseInt(args[0]);
        int number = Integer.parseInt(args[1]);
        for (int j = 0; j < number; j++) {
            try {
                Thread.sleep(50);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String userName = getUserName();
                        System.out.println("连接用户名："+userName);
                        ComSmackClient.go7(userName,"123456","zhouxing@windows10.microdone.cn");
                    }
                }) .start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    private static synchronized String getUserName(){
        return "testAccount"+i++;
    }
}
