package array;

///二维数组 稀疏数组
public class SparseArray {
    public static void main(String[] args) {
        //int数组的数组  套娃出来一个二维数组
        int[][] chessArr = new int[11][11];
        chessArr[1][2] = 1;
        chessArr[2][3] = 2;

        for (int[] ints : chessArr) {

            for (int anInt : ints) {
                System.out.print(" "+ anInt);
            }
            System.out.println();
        }



        //创建稀疏数组
        int sum = 0;
        for (int[] ints : chessArr) {
            for (int anInt : ints) {
                if (anInt != 0){
                    sum++;
                }
            }
        }

        int[][] sparseArr = new int[sum + 1][3];
        //给稀疏数组第一行赋值
        sparseArr[0][0] = chessArr.length;          //第一个值 多少行
        sparseArr[0][1] = chessArr[0].length;       //第二个值 多少列
        sparseArr[0][2] = sum;                      //第三个值 总共有多少个值
        //给稀疏数组其他行赋值

        int count = 0;//用于记录是第几个非0数据
        for (int i = 0; i < chessArr.length; i++) {
            for (int j = 0; j < chessArr[i].length; j++) {
                if (chessArr[i][j] != 0){
                    count++;
                    sparseArr[count][0] = i;
                    sparseArr[count][1] = j;
                    sparseArr[count][2] = chessArr[i][j];
                }
            }
        }

        System.out.println("转换为稀疏数组");

        for (int i = 0; i < sparseArr.length; i++) {
            System.out.printf("%d\t%d\t%d\t\n",sparseArr[i][0],sparseArr[i][1],sparseArr[i][2]);
        }
    }
}
