/*
*学生A
 */
package Client;

public class ClientA {
    public static void main(String[] args){
        Thread studentA=new Thread(new ClientThread());
        studentA.start();
    }
}
