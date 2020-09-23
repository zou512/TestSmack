package graphical;

import javax.swing.*;
import java.awt.*;

public class GraphicalJFrame {
    public static void main(String[] args) {
        showJFrame();
    }

    ///普通窗口
    public static void showJFrame(){
        JFrame jFrame = new JFrame("LoL");
        jFrame.setSize(400,-2);//容器大小 尺寸
        jFrame.setLocation(500,500);//容器位置
        jFrame.setLayout(null);//布局
        JButton b = new JButton("一键秒对方基地挂");
        b.setBounds(50, 50, 280, 30);//设置按钮在容器中的位置

        jFrame.add(b);//将按钮加在容器上
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//界面关闭后程序结束

        jFrame.setVisible(true);//界面可视化。
    }

    ///对话框
    public static void showJDialog(){
        JDialog a = new JDialog();
        a.setTitle("Demo");
        a.setSize(500, 600);
        a.setLocation(200, 400);
        a.setLayout(null);
        a.setVisible(true);

        JTextArea jTextArea = new JTextArea();
        jTextArea.append("你好吗大傻子");
        a.add(jTextArea);
        a.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//界面关闭后程序结束
    }
}
