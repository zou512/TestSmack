package company;

import  java.awt.AlphaComposite;
import  java.awt.Color;
import  java.awt.Font;
import  java.awt.Graphics2D;
import  java.awt.Image;
import  java.awt.RenderingHints;
import  java.awt.image.BufferedImage;
import  java.io.File;
import  java.io.FileOutputStream;
import  java.io.InputStream;
import  java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import  javax.imageio.ImageIO;

public class TestWatermark {
    public   static   void  main(String[] args) {
        String srcImgPath = "C:/Users/gongxing/Desktop/00315162348.jpg" ;
        String logoText = "0001" ;
//        String targerPath = "C:/Users/gongxing/Desktop/testTarger/00315162348_rotate.jpg" ;

//        String targerPath2 = "d:/test/michael/img_mark_text_rotate.jpg" ;

      /*  ///获取图片的路径返回
        HashMap<String, String> files = getHashMapFiles("C:/Users/gongxing/Desktop/image2/");
        Set<String> keySet = files.keySet();
        int number = 1;
        for (String name : keySet) {
            String path = files.get(name);
            String targerPath = "C:/Users/gongxing/Desktop/testTarger/"+name;
            markByText(getNumber(number),path, targerPath);
            number++;
        }*/

        ArrayList<String> pathList = getListFiles("C:/Users/gongxing/Desktop/image/");
        for (int i = 0; i < pathList.size(); i++) {
            String path = pathList.get(i);
            String id = getNumber(i + 1);
            markByText(id,path,  "C:/Users/gongxing/Desktop/imageTarger/"+id+".jpg");
        }

        // 给图片添加水印
//        markByText(logoText, srcImgPath, targerPath);

        // 给图片添加水印,水印旋转-45
//        markByText(logoText, srcImgPath, targerPath2, -45 );
    }

    public static String getNumber(int number){
        if (number<10){
            return "000"+number;
        }else if (number<100){
            return "00"+number;
        }else if (number<1000){
            return "0"+number;
        }else {
            return String.valueOf(number);
        }
    }


   ////根据给定文件夹路径返回文件夹下所有文件的名称和路径
    public static HashMap<String, String> getHashMapFiles(String path) {

        File file = new File(path);

        File[] files = file.listFiles();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (File file1 : files) {
//            String file1Path = file1.getPath();
            hashMap.put(file1.getName(),file1.getPath());
//            System.out.println("名称："+file1.getName()+"========>路径:"+file1Path);
        }
        return hashMap;
    }

    ////根据给定文件夹路径返回文件夹下所有文件路径
    public static ArrayList<String> getListFiles(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        ArrayList<String> list = new ArrayList<String>(files.length);
        for (File file1 : files) {
            list.add(file1.getPath());
        }

        return list;
    }
    


    /**
     * 给图片添加水印
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     */
    public   static   void  markByText(String logoText, String srcImgPath,
                                       String targerPath) {
        markByText(logoText, srcImgPath, targerPath, null );
    }

    /**
     * 给图片添加水印、可设置水印的旋转角度
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public   static   void  markByText(String logoText, String srcImgPath,
                                       String targerPath, Integer degree) {
        // 主图片的路径
        InputStream is = null ;
        OutputStream os = null ;
        try  {
            Image srcImg = ImageIO.read(new  File(srcImgPath));

            BufferedImage buffImg = new  BufferedImage(srcImg.getWidth( null ),
                    srcImg.getHeight(null ), BufferedImage.TYPE_INT_RGB);

            // 得到画笔对象
            // Graphics g= buffImg.getGraphics();
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null ), srcImg
                    .getHeight(null ), Image.SCALE_SMOOTH),  0 ,  0 ,  null );

            if  ( null  != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double ) buffImg.getWidth() /  2 , ( double ) buffImg
                                .getHeight() / 2 );
            }

            // 设置颜色
            g.setColor(Color.WHITE);

            // 设置 Font
            g.setFont(new  Font( "宋体" , Font.BOLD,  30 ));

            float  alpha =  1f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
            g.drawString(logoText, 10 ,  30);

            g.dispose();

            os = new  FileOutputStream(targerPath);

            // 生成图片
            ImageIO.write(buffImg, "JPG" , os);

            System.out.println("图片完成添加文字印章。。。。。。" );
        } catch  (Exception e) {
            e.printStackTrace();
        } finally  {
            try  {
                if  ( null  != is)
                    is.close();
            } catch  (Exception e) {
                e.printStackTrace();
            }
            try  {
                if  ( null  != os)
                    os.close();
            } catch  (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
