/*
*学生B
 */
package Client;

public class ClientB {
    public static void main(String[] args){
        Thread studentB=new Thread(new ClientThread());
        studentB.start();
    }
}
