package Server;


import Library.Library;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable{

    protected ServerSocket serverSocket;
    protected Socket socket;
    protected static Library library;
    public static HashMap<Socket, String> Map = new HashMap<Socket, String>();//存储已连接的客户端
    public Thread receiveThread;
    public Thread sendThread;

    Server(){
        serverSocket = null;
        socket=null;
        library=new Library();
        receiveThread=null;
        sendThread=null;
    }

    public static String doCommand(String command){
        String[]commandString=command.split(" ");
        String result=null;
        switch(commandString[0]){
            case "list":{
                result=library.listBook().toString();
                break;
            }
            case "borrow":{
                int borrowId=Integer.parseInt(commandString[1]);
                if(library.listBook().equals(library.borrowBook(borrowId))){
                    result="We don't have this book.";
                }else {
                    result = library.borrowBook(borrowId).toString();
                }
                break;
            }
            case "return":{
                int returnId=Integer.parseInt(commandString[1]);
                if(library.listBook().equals(library.returnBook(returnId))){
                    result="return wrong.";
                }else {
                    result = library.returnBook(returnId).toString();
                }
                break;
            }
            case "over":{
                result="over";
                break;
            }
            case "heartbeat":{
                result="heartbeat";
                break;
            }
            default:{
                result="Operation wrong,please try again:";
            }
        }
        return result;
    }

    public void run(){
        try {
        serverSocket = new ServerSocket(9999);
        while (true) {
            System.out.println("The server is listening...");
            socket = serverSocket.accept();

            System.out.println("A client is connecting to the server now.");

            //System.out.println(socket.getPort());

            receiveThread=new Thread(new ReceiveThread(socket));

            receiveThread.start();
        }
    }catch(Exception e){
        // TODO: 2020/8/14  System.out.println();
        e.printStackTrace();
    } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Server server=new Server();
        Thread connection=new Thread(server);
        connection.start();
    }
}
