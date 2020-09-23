package down;

///访问网址下载
public class FileDownLoad {
    private static int i =0;
    private  synchronized static String getFileName(){
        return "文件"+(i++)+".apk";
    }

    public static void main(String[] args) {
//        ResourceURLServer.down("C:/down/","51.apk","http://192.168.124.9/Upkeeps51.apk");
        for (int j = 0; j < 10; j++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ResourceURLServer.downSpeed("C:/down/",getFileName(),"http://192.168.124.9/Upkeeps51.apk");
                }
            }).start();
        }


    }

    public static void downSpringBoot(){
        ResourceURLServer.down("C:/down/","文件.7z","http://192.168.124.10:8080/fileDownLoad2");
    }
}
