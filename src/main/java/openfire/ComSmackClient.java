package openfire;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.io.*;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

///连接pc上的服务器
public class ComSmackClient {
    private static final String TAG = "ComSmackClient";
    private static int fail;

    public static synchronized int getFail() {
        return ++fail;
    }


    public static void countPeople(Roster r) {
        System.out.println("在线人数变为：" + r.getEntryCount());
    }

    ///https://www.cnblogs.com/zidafone/p/4742813.html
    public static void go() {
        //对连接的配置
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setUsernameAndPassword("qiudayong", "123456");
        builder.setServiceName("windows10.microdone.cn");
        builder.setPort(5222);
        //不加这行会报错因为没有证书
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        builder.setDebuggerEnabled(true);
        XMPPTCPConnectionConfiguration config = builder.build();

        //建立连接并登陆
        AbstractXMPPConnection c = new XMPPTCPConnection(config);
        try {
            c.addPacketSendingListener(new StanzaListener() {
                @Override
                public void processPacket(Stanza st) {
                    System.out.println("in StanzaListener:" + st.getFrom());
                }
            }, new StanzaFilter() {
                @Override
                public boolean accept(Stanza st) {
                    System.out.println("in StanzaFilter:" + st.getFrom());
                    return false;
                }
            });

            c.connect();
            c.login();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        final Roster roster = Roster.getInstanceFor(c);
        Presence p = roster.getPresence("zhouxing");
        System.out.println(p.getType());
        roster.addRosterListener(new RosterListener() {
            public void entriesAdded(Collection<String> arg0) {
                countPeople(roster);
            }

            public void entriesDeleted(Collection<String> addresses) {
                countPeople(roster);
            }

            public void entriesUpdated(Collection<String> addresses) {
                countPeople(roster);
            }

            public void presenceChanged(Presence presence) {
                countPeople(roster);
            }
        });

        //            //设置是否在线状态和状态说明
//            Presence presence = new Presence(Presence.Type.unavailable);
//            presence.setStatus("Gone fishing");
//            c.sendStanza(presence);

        //会话管理者的建立和配置监听
        ChatManager chatmanager = ChatManager.getInstanceFor(c);
        chatmanager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat cc, boolean bb) {
                //当收到来自对方的消息时触发监听函数
                cc.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat cc, Message mm) {
                        System.out.println(mm.getBody());
                    }
                });
            }
        });

        //建立会话
        Chat chat = chatmanager.createChat("zhouxing");
        chat.getThreadID();

        //发消息
        Message msg = new Message();
        msg.setBody("hello!");
        try {
            chat.sendMessage(msg);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        while (true) ;
    }

    ////自己估摸着写了下  不得行
    public static void go2() {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost("192.168.124.9");
        builder.setPort(5222);
        builder.setServiceName("admin@192.168.124.9");
        builder.setCompressionEnabled(true);
        builder.setConnectTimeout(1000 * 3);

        XMPPTCPConnectionConfiguration xmpptcpConnectionConfiguration = builder.build();
        XMPPTCPConnection connection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);

        try {
            connection.connect();
            connection.login("zhouxing", "123456", "这个是发出去的消息吗");
            connection.send(new PlainStreamElement() {
                @Override
                public CharSequence toXML() {
                    return "女人你在玩儿火！";
                }
            });
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.createChat("张德帅").sendMessage("Hello word!");
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }


    ///https://www.jianshu.com/p/677892ad6235
    public static void go3() {
        AbstractXMPPConnection connection = new XMPPTCPConnection("qiudayong", "123456", "windows10.microdone.cn");
        try {
            connection.connect().login();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = new Message(" 1835255394@qq.com", "Howdy! How are you?");
        try {
            connection.sendStanza(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    ////成功发消息啦！
    ///https://www.cnblogs.com/code0001/p/6495851.html
    public static void go4(String name) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");
        configBuilder.setUsernameAndPassword("qiudayong", "123456");
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());
        //Set ConnectionListener here to catch initial connect();
        mConnection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection xmppConnection) {
                System.out.println(TAG + ":connected连接");
            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {
                System.out.println(TAG + ":authenticated已验证");
            }

            @Override
            public void connectionClosed() {
                System.out.println(TAG + ":connectionClosed连接关闭哦");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                System.out.println(TAG + ":connectionClosedOnError连接关闭错误");
            }

            @Override
            public void reconnectionSuccessful() {
                System.out.println(TAG + ":reconnectionSuccessful重连成功");
            }

            @Override
            public void reconnectingIn(int i) {
                System.out.println(TAG + ":reconnectingIn重新连接");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                System.out.println(TAG + ":reconnectionFailed重新连接失败");
            }
        });
        try {
            mConnection.connect();
            mConnection.login();
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            int number = 0;
            String text;
            while (true) {
                Thread.sleep(1000);
                text = name + ":Hello dad!" + number++;
                chatManager.createChat("zhouxing@windows10.microdone.cn").sendMessage(text);
                System.out.println("邱大勇给周星发送消息：" + text);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //发送文件  比如图片 视频
    public static void go8(String name) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");
        configBuilder.setUsernameAndPassword("qiudayong", "123456");
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());
        //Set ConnectionListener here to catch initial connect();
        mConnection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection xmppConnection) {
                System.out.println(TAG + ":connected连接");
            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {
                System.out.println(TAG + ":authenticated已验证");
            }

            @Override
            public void connectionClosed() {
                System.out.println(TAG + ":connectionClosed连接关闭哦");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                System.out.println(TAG + ":connectionClosedOnError连接关闭错误");
            }

            @Override
            public void reconnectionSuccessful() {
                System.out.println(TAG + ":reconnectionSuccessful重连成功");
            }

            @Override
            public void reconnectingIn(int i) {
                System.out.println(TAG + ":reconnectingIn重新连接");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                System.out.println(TAG + ":reconnectionFailed重新连接失败");
            }
        });
        try {
            mConnection.connect();
            mConnection.login();

//            File file = new File("C:\\Users\\gongxing\\Desktop\\00315162348.jpg");
            File file = new File("C:/Users/gongxing/Desktop/00315162348.jpg");
            FileTransferManager fileTransferManager = FileTransferManager.getInstanceFor(mConnection);
            while (true) {
                Thread.sleep(1000);
                fileTransferManager
                        .createOutgoingFileTransfer("zhouxing@windows10.microdone.cn/smack 2.3.6")
                        .sendFile(file, file.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    ////http://www.voidcn.com/article/p-hhnocndy-bx.html
   /* void sendFile() {
        // 连接参数

        ConnectionConfiguration connConfig = new ConnectionConfiguration("192.168.0.1", 5222);
        connConfig.setReconnectionAllowed(true);
        connConfig.setSecurityMode(SecurityMode.disabled);
        // SecurityMode.required/disabled
        connConfig.setSASLAuthenticationEnabled(false); // true/false
        connConfig.setCompressionEnabled(false);
        // 配置服务器
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            // 连接服务器
            connection.connect();                                                         // 用户登录
            connection.login("joe", "123456");
            // 向另一个用户发出聊天
            Chat chat = connection.getChatManager().createChat("admin@192.168.0.1/Spark 2.6.3", new MessageListener() {
                // 消息回复函数
                @Override
                public void processMessage(Chat arg0, Message arg1) {
                    System.out.println("Received message: " + arg1.getBody());
                    try {
                        arg0.sendMessage("我已收到");
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
            });
            // 发送聊天信息

            chat.sendMessage("Hello!"); //文件传输的重要代码片段：(请注意更换你自己的服务器地址、用户名和密码以及想要访问的用户名)
            // 连接参数
            ConnectionConfiguration connConfig = new ConnectionConfiguration("192.168.0.1", 5222);
            connConfig.setReconnectionAllowed(true);
            connConfig.setSecurityMode(SecurityMode.disabled); // SecurityMode.required/disabled
            connConfig.setSASLAuthenticationEnabled(false); // true/false
            connConfig.setCompressionEnabled(false);
            // 配置服务器
            XMPPConnection connection = new XMPPConnection(connConfig);
            try {
                // 连接服务器
                connection.connect();
                // 用户登录
                connection.login("joe", "123456");
                // 准备发送的文件
                File file = new File(PATH);
                FileTransferManager transferManager = new FileTransferManager(connection);
                OutgoingFileTransfer outgoingFileTransfer = transferManager.createOutgoingFileTransfer("admin@192.168.0.1/Spark 2.6.3");
                // 发送文件
                outgoingFileTransfer.sendFile(file, file.getName());
                // 接收文件监听
                transferManager.addFileTransferListener(new FileTransferListener() {
                    public void fileTransferRequest(FileTransferRequest request) {
                        try {
                            // 接收文件
                            IncomingFileTransfer transfer = request.accept();
                            // 接收文件存放的位置
                            transfer.recieveFile(new File(PATH));
                        } catch (Exception e) {
                            Log.e("RecFile Ex In!", e.getMessage());
                        }
                    }
                });
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 通过用户名密码给指定发消息
     *
     * @param username  登录用户名
     * @param password  登录用户密码
     * @param toUserJID 接收消息的id
     */
    public static void go7(final String username, String password, String toUserJID) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        //本机
        /*configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");*/
        //大勇
        /*configBuilder.setHost("192.168.124.9");
        configBuilder.setServiceName("192.168.124.9");*/
        //服务器
        configBuilder.setHost("192.168.124.22");
        configBuilder.setServiceName("win-jn1ul9f8j6b");
        //2u服务器
        /*configBuilder.setHost("192.168.124.15");
        configBuilder.setServiceName("2u-centos7");*/
        //虚拟机
        /*configBuilder.setHost("192.168.124.26");
        configBuilder.setServiceName("localhost");*/
        configBuilder.setUsernameAndPassword(username, password);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());
        try {
            mConnection.connect();
            mConnection.login();
            //以下两行设置状态在线
            Presence presence = new Presence(Presence.Type.available);
            mConnection.sendStanza(presence);

            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);//消息管理器
            ///给消息管理器添加接收消息
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean b) {
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            if (message.getBody() != null) {
                                System.out.println(username + "收到来自：" + chat.getParticipant() + "==>addChatListener get a message :" + message.getBody());
                            }

                        }
                    });
                }

                ;
            });
            int number = 0;
            String text;
            while (true) {
                Thread.sleep(1000 * 1000);

                ///发送消息
               /* text = username + ":Hello dad!" + number++;
                chatManager.createChat(toUserJID).sendMessage(text);*/
//                System.out.println("儿子给爸爸发送消息："+text);
            }

        } catch (Exception e) {
            if (e instanceof SmackException.NoResponseException) {
                System.out.println("错误：" + (++fail));
            }

            e.printStackTrace();
        }
    }


    ///试着用smack创建账户
    public static void go5() {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");
        configBuilder.setUsernameAndPassword("zhouxing", "123456");
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());

        try {
            mConnection.connect();
            mConnection.login();

        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        AccountManager accountManager = AccountManager.getInstance(mConnection);
        accountManager.sensitiveOperationOverInsecureConnection(true);
        try {
            accountManager.createAccount("niepeng", "123456");
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    ////循环创建5w个账户
    public static void go6() {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        //本机
       /* configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");
        configBuilder.setUsernameAndPassword("zhouxing", "123456");*/
        //大勇
       /* configBuilder.setHost("192.168.124.9");
        configBuilder.setServiceName("192.168.124.9");
        configBuilder.setUsernameAndPassword("zhouxin", "123456");*/

        //服务器
       /* configBuilder.setHost("192.168.124.22");
        configBuilder.setServiceName("win-jn1ul9f8j6b");
        configBuilder.setUsernameAndPassword("admin", "admin");*/

        //2u服务器
        /*configBuilder.setHost("192.168.124.15");
        configBuilder.setServiceName("2u-centos7");*/
        //虚拟机
        configBuilder.setHost("192.168.124.26");
        configBuilder.setServiceName("localhost");
        configBuilder.setUsernameAndPassword("zhouxing", "123456");
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());

        try {
            mConnection.connect();
            mConnection.login();

        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        AccountManager accountManager = AccountManager.getInstance(mConnection);
        accountManager.sensitiveOperationOverInsecureConnection(true);


        try {
            for (int i = 30145; i < 50 * 1000-30145; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                accountManager.createAccount("testAccount" + i, "123456");
            }

        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    ///匿名登录
    public static void loginAnonymously() {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");
//        configBuilder.setUsernameAndPassword("zhouxing", "123456");
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());

        try {
            mConnection.connect();
            mConnection.loginAnonymously();//匿名登录
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            String text = "消息：";
            int number = 0;
            while (true) {
                Thread.sleep(1000);
                chatManager
                        .createChat("zhouxing@windows10.microdone.cn")
                        .sendMessage(text + number);
                number++;
            }
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //获取所有用户数
    public static void getUserCount() {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        //本机
        configBuilder.setHost("127.0.0.1");
        configBuilder.setServiceName("windows10.microdone.cn");
        configBuilder.setUsernameAndPassword("qiudayong", "123456");
        //大勇
       /* configBuilder.setHost("192.168.124.9");
        configBuilder.setServiceName("192.168.124.9");
        configBuilder.setUsernameAndPassword("zhouxin", "123456");*/
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(configBuilder.build());

        try {
            mConnection.connect();
            mConnection.login();

        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        ///这个是可以获取在线好友吗
        Roster roster = Roster.getInstanceFor(mConnection);
        if (!roster.isLoaded()) {
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }

//        roster.getPresence()
        /*Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println("entry:"+entry);
        }*/

    }

    ///直接写sql文件创建表  然后导入mysql
    private static void createDateByBufferedWriter(int amount, int time, String filePath) throws Exception {
        File file = new File(filePath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        long start = System.currentTimeMillis();
        long row = 1000000001L + 18377085;
        long j = 1757288 + 6503;
        long sine = 1;
        StringBuffer bf = new StringBuffer();
        for (int s = 0; s < amount; s++) {
            for (int i = 0; i < time; i++) {
                // String uuid = UUID.randomUUID().toString();
                // String name = uuid.substring(0, 4);
                // String phone = "18628288942";
                j++;
                sine++;
                bf.append("\nN" + row + "," + j + ",1,tr,sssssssssssssssssssssssss," + System.currentTimeMillis() + sine + ",null,null,null");
                row++;
            }
            j++;
            bw.write(bf.substring(0, bf.length() - 1));
            bf.setLength(0);
        }
        bw.close();
        long end = System.currentTimeMillis();
        System.out.println("生成10000万条数据共花费" + (end - start) + "毫秒");

    }

    ///直接写sql文件创建表  然后导入mysql
    ///第一个参数是从编号多少开始   amount是创建多少条  filePath是创建的文件放在哪儿
    public static void createData(int initial, int amount, String filePath) {
        long start = System.currentTimeMillis();
        File file = new File(filePath);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer bf = new StringBuffer();
        String userName;
        String lineSeparator = System.getProperty("line.separator");//换行
        String storedKey = "DK4eXz33YaDPsAQ/3tafLNNUM/A=";
        String serverKey = "3h8iPPjj+UTPiAW08oGR/IabuM4=";
        String salt = "w+MoWli6EfwRrMd5pTQsD7vG8vh2QO12";
        String createDate;
        for (int i = 0; i < amount; i++) {
            userName = "testaccount" + initial++;
            createDate = "00" + System.currentTimeMillis();
            bf.append(userName).append(",")
                    .append(storedKey).append(",")
                    .append(serverKey).append(",")
                    .append(salt).append(",")
                    .append("4096").append(",")
                    .append(",").append(",").append(",").append(",")
                    /*.append("null").append(",")
                    .append("null").append(",")
                    .append("null").append(",")
                    .append("null").append(",")*/
                    .append(createDate).append(",")
                    .append(createDate)
                    .append(lineSeparator);
            if (i%1000==0){
                try {
                    bw.write(bf.substring(0, bf.length() - 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bf.setLength(0);
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("生成"+amount+"条数据共花费" + (end - start) + "毫秒");
    }

    ///获取百家姓字符数组
    public static char[] getSurNames(){
        //百家姓
        String hundredNames = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄曲家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴鬱胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍卻璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于单于太叔申屠公孙仲孙轩辕令狐钟离宇文长孙慕容鲜于闾丘司徒司空丌官司寇仉督子车颛孙端木巫马公西漆雕乐正壤驷公良拓跋夹谷宰父谷梁晋楚闫法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况郈有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福百家姓终";
        return hundredNames.toCharArray();
    }
    ///生成随机数
    private static Random random = new Random();
    ///生成常见的汉字作为名称
    public static String getGene(){
        byte hightPos = (byte) (176+random.nextInt(39));
        byte lowPos = (byte) (161+random.nextInt(93));
        byte[] bytes = {hightPos,lowPos};
        try {
            return new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGender(){
        if (random.nextBoolean()){
            return "男";
        }else {
            return "女";
        }
    }

    ///写user表数据
    public static void createDataUser(){
        char[] surname = getSurNames();
        int surNamesLength = surname.length;
        File file = new File("C:\\down\\user.txt");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer stringBuffer = new StringBuffer();
        String lineSeparator = System.getProperty("line.separator");//换行
        for (int i = 7; i < 100000; i++) {
            stringBuffer.append(i).append(",")
                    .append(surname[random.nextInt(surNamesLength)])//随机获取一个姓
                    .append(getGene())//随机获取一个汉字作为名字
                    .append(",")
                    .append(getGender()).append(",")
                    .append(random.nextInt(100)).append(",")
                    .append(lineSeparator);
            if (i %1000==0){
                try {
                    bw.write(stringBuffer.substring(0, stringBuffer.length() - 1));
                    bw.flush();
                    stringBuffer.setLength(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
