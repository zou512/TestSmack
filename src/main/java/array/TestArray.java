package array;

public class TestArray {
    public static void main(String[] args) {
        testCircleArray();
    }

    public static void testArrayQueue(){
        ArrayQueue arrayQueue = new ArrayQueue(5);
        for (int i = 0; i < 10; i++) {
            arrayQueue.addQueue(i);
        }
        while (!arrayQueue.isEmpty()) {
            System.out.println("arrayQueue取出了："+arrayQueue.getQueue());
        }
    }

    public static void testCircleArray(){
        CircleArray arrayQueue = new CircleArray(5);
        for (int i = 0; i < 10; i++) {
            arrayQueue.addQueue(i);
        }
        while (!arrayQueue.isEmpty()) {
            System.out.println("arrayQueue取出了："+arrayQueue.getQueue());
        }
        for (int i = 0; i < 10; i++) {
            arrayQueue.addQueue(i);
        }
        while (!arrayQueue.isEmpty()) {
            System.out.println("arrayQueue取出了："+arrayQueue.getQueue());
        }
    }
}
