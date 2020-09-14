package Server;


import Rule.PackageRule;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendThread implements Runnable{

    protected OutputStream outputStream;
    protected Socket socket;
    protected String result;

    SendThread(Socket socket){
        outputStream=null;
        this.socket=socket;
        result=Server.Map.get(socket);
    }

    public void run(){
        try {
                outputStream = socket.getOutputStream();
                //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                byte[] sendPackage= PackageRule.pack(result.getBytes().length,result.getBytes());
                outputStream.write(sendPackage);
                //bufferedWriter.write(result + "\n");
               // bufferedWriter.flush();

                if(result=="over"){
                    System.out.println("This client has left.");
                    outputStream.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
