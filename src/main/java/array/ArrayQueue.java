package array;

///数组模拟队列
//一次性的队列
public class ArrayQueue {
    protected int maxSize;//队列容量
    protected int front;//队列头
    protected int rear;//队列尾
    protected int[] arr;//模拟队列的数组

    public ArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        arr = new int[maxSize];
        front = -1;
        rear = -1;
    }

    //判断队列是否装满
    public boolean isFuss() {
        return rear == maxSize - 1;
    }

    //判断队列是否为空
    public boolean isEmpty() {
        return rear == front;
    }

    //加入队列
    public void addQueue(int n){
        if (!isFuss()){
            rear++;
            arr[rear] = n;
        }
    }

    //取队列
    public int getQueue(){
        if (isEmpty()){
            throw new RuntimeException("--队列为空--");
        }else {
            front++;
            return arr[front];
        }
    }
}
