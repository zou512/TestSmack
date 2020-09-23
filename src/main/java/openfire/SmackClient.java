package openfire;

import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class SmackClient {

    public static void countPeople(Roster r){
        System.out.println("在线人数变为：" + r.getEntryCount());
    }

    public static void go(){
        try{
            //对连接的配置
            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
            builder.setUsernameAndPassword("zhouxing", "123456");
            builder.setServiceName("192.168.124.9");
            builder.setHost("192.168.124.9");
            builder.setPort(5222);
            //不加这行会报错，因为没有证书
            builder.setSecurityMode(SecurityMode.disabled);
            builder.setDebuggerEnabled(true);
            XMPPTCPConnectionConfiguration config = builder.build();

            //建立连接并登陆
            AbstractXMPPConnection c = new XMPPTCPConnection(config);

            c.addPacketSendingListener(new StanzaListener(){
                @Override
                public void processPacket(Stanza st)
                        throws NotConnectedException {
                    System.out.println("in StanzaListener:" + st.getFrom());
                }
            }, new StanzaFilter(){
                @Override
                public boolean accept(Stanza st) {
                    System.out.println("in StanzaFilter:" + st.getFrom());
                    return false;
                }
            });

            c.connect();
            c.login();

            final Roster roster = Roster.getInstanceFor(c);
            Presence p = roster.getPresence("admin@192.168.124.9");
            System.out.println(p.getType());
            roster.addRosterListener(new RosterListener() {
                public void entriesAdded(Collection<String> arg0) {countPeople(roster);}
                public void entriesDeleted(Collection<String> addresses) {countPeople(roster);}
                public void entriesUpdated(Collection<String> addresses) {countPeople(roster);}
                public void presenceChanged(Presence presence) {countPeople(roster);}
            });

//            //设置是否在线状态，和状态说明
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
            Chat chat = chatmanager.createChat("admin@192.168.124.9");
            chat.getThreadID();

            //发消息
            Message msg = new Message();
            msg.setBody("hello!服务");
            chat.sendMessage(msg);

            while(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("可以吗");
        go();
        System.out.println("可以吧");
    }
}