package openfire;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

//连接服务器
public class ServiceSmackClient {
    private static final String TAG = "ServiceSmackClient";
    private static  int fail;
    ///连接服务器
    public static void connect(final String username, String password) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setPort(5222);
        configBuilder.setHost("192.168.124.22");
        configBuilder.setServiceName("192.168.124.9");
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
                            if (message.getBody() != null){
                                System.out.println(username+"收到来自："+chat.getParticipant()+"==>addChatListener get a message :" + message.getBody());
                            }
                        }
                    });
                };
            });
            while (true) {
                //暂时不发消息  不关闭
                Thread.sleep(100*1000);
            }

        } catch (Exception e) {
            if (e instanceof SmackException.NoResponseException){
                System.out.println("错误："+(++fail));
            }

            e.printStackTrace();
        }
    }
}
