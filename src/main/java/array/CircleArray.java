package array;
///数组模拟队列

///继承自ArrayQueue  改为可以循环的队列
public class CircleArray extends ArrayQueue {
    public CircleArray(int maxSize) {
        //因为判断队列是否满的方法和是否为空的方法要区分开
        //为此浪费了一个位置做两个方法的区分 所以加一个位置
        super(maxSize+1);
        super.front = 0;
        super.rear = 0;
    }

    //重写父类判断队列是否满
    @Override
    public boolean isFuss() {
        return (super.rear + 1) % super.maxSize == super.front;
    }

    //重写父类加入队列方法
    @Override
    public void addQueue(int n) {
        if (!isFuss()){
            super.arr[super.rear] = n;
            super.rear = (super.rear +1)%super.maxSize;//将rear后移，这里必须考虑取模 以免越界
        }
    }

    //重写父类取队列方法
    @Override
    public int getQueue() {
        if (!isEmpty()){
            //因为front是指向队列的第一个元素,所以
            // 1. 先把 front 对应的值保留到一个临时变量
            // 2. 将 front 后移, 考虑取模(防止越界)
            // 3. 将临时保存的变量返回

            int value = arr[front];
            front = (front+1)%maxSize;
            return value;
        }else {
            throw new RuntimeException("--队列为空--");
        }
    }
}
