package upkeep51;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

///测试多线程读写文件
public class MultithreadingIO {

    private static String lineSpace = "\n";
    private static int number = 0;
    //        private File file=new File("C:\\jar\\test.txt");

    private static BufferedWriter bufferedWriter;


    public static void main(String[] args) {
        File file = new File("C:\\jar\\MultithreadingIO.txt");
        if (!file.exists()){
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
        for (int i = 0; i < 100; i++) {
            new Thread(new DealThread()).start();
        }
    }

    private static class DealThread implements Runnable{

        @Override
        public void run() {

            synchronized (lineSpace){
                number++;
            }
            File file = new File("C:\\jar\\线程" + number+".txt");
            if (!file.exists()){
                ///文件不存在，新建
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BufferedWriter dealWriter = null;
            try {
                dealWriter = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                try {
                    bufferedWriter.write("线程"+number+":编号==>"+i+lineSpace);
                    bufferedWriter.flush();
                    dealWriter.write("线程"+number+"编号==>"+i+lineSpace);
                    dealWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                dealWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
