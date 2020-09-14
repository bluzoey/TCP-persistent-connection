/*
*服务端收线程
 */
package Server;

import com.mfc.base.practice.practice5_round2.Rule.PackageRule;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReceiveThread implements Runnable{

    protected InputStream inputStream;
    protected Socket socket;
    protected String result;
    protected long waitTime;
    protected long receiveTime;

    ReceiveThread(Socket socket){
        inputStream=null;
        this.socket=socket;
        result=null;
    }

    public void run(){
        try {
            while(true){
                waitTime=System.currentTimeMillis();

                inputStream = socket.getInputStream();

                receiveTime=System.currentTimeMillis();

                if(receiveTime-waitTime<30000) {
                    //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    //String content = bufferedReader.readLine();
                    String content= PackageRule.handlePackage(inputStream);

                    if(content!=null) {
                        System.out.println("receive:" + content);

                        result = Server.doCommand(content);

                        if (result != "heartbeat") {

                            Server.Map.put(socket, result);

                            new Thread(new SendThread(socket)).start();
                        }
                    }else {
                        break;
                    }
                }else {
                    socket.close();
                    inputStream.close();
                    Server.Map.remove(socket);
                    System.out.println("hear no heartbeat");
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}