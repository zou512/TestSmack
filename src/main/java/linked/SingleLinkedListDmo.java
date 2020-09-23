package linked;

///链表
public class SingleLinkedListDmo {
    public static void main(String[] args) {


        Node node1 = new Node(1, "n1");
        Node node2 = new Node(2, "n2");
        Node node3 = new Node(3, "n3");
        SingleLinkedList singleLinkedList = new SingleLinkedList();
        singleLinkedList.add(node1);
        singleLinkedList.add(node2);
        singleLinkedList.add(node3);
        singleLinkedList.show();

    }

    //定义链表
    protected static class SingleLinkedList {
        //初始化头结点
        protected Node headNode = new Node(0, "");

        //添加结点  从头开始一直找到最后一个添加进去
        public void add(Node node) {
            Node temp = headNode;
            while (true) {
                if (temp.next == null) {
                    break;
                }
                temp = temp.next;
            }
            temp.next = node;
        }

        //带序号插入
        public void addByOrder(Node node) {
            Node temp = headNode;
            boolean flag = false;//要添加的编号是否存在
            while (true) {
                if (temp.next == null) {
                    break;
                }
                if (temp.next.no > node.no) {
                    break;
                } else if (temp.next.no == node.no) {
                    flag = true;
                    break;
                }
                temp = temp.next;
            }
            if (flag) {
                System.out.println("待插入结点编号已经存在");
            } else {
                node.next = temp.next;
                temp.next = node;
            }
        }

        //链表根据给到的结点反转
        public void reversalList(Node node){
            ///如果是空链表或者只有一个值  那就直接返回
            if (headNode.next == null || headNode.next.next == null){
                return;
            }

            Node reversalNode = new Node(0, "");//1
            Node currentNode = headNode.next;
            Node nextNode = null;
            while (currentNode != null){
                nextNode = currentNode.next;//记录一下即将要变大的结点后的next
                currentNode.next = reversalNode.next;//当前节点的下一个节点指向链表最前端
                reversalNode.next = currentNode;//将当前节点连接到新链表上
                currentNode = nextNode;//当前节点后移
            }
            headNode.next = reversalNode.next;//3
        }

        //展示链表
        public void show() {
            Node temp = headNode;
            if (temp.next == null) {
                System.out.println("这是一个空链表");
                return;
            }
            while (true) {
                if (temp == null) {
                    break;
                }
                System.out.println(temp);
                temp = temp.next;
            }
        }

    }

    //定义表示单链表节点的pojo
    protected static class Node {
        public int no;
        public String name;
        public Node next;

        public Node(int no, String name) {
            this.no = no;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
