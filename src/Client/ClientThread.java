/*
*
 */
package Client;


import com.mfc.base.practice.practice5_round2.Rule.PackageRule;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread implements Runnable{

    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected long lastSendTime;

    ClientThread(){
        socket =null;
        inputStream=null;
        outputStream=null;
        lastSendTime=System.currentTimeMillis();
    }
    public void run() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            socket = new Socket(host, 9999);
            new Thread(new HeartbeatThread(lastSendTime,socket)).start();
            while(true) {
                lastSendTime=System.currentTimeMillis();
                System.out.println("Please enter your operation:");
                InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String operation = bufferedReader.readLine();


                outputStream = socket.getOutputStream();
                //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                //bufferedWriter.write(operation + "\n");
                //bufferedWriter.flush();
                byte[] sendPackage= PackageRule.pack(operation.getBytes().length,operation.getBytes());
                outputStream.write(sendPackage);

                inputStream = socket.getInputStream();
                //BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                //String getContent=br.readLine();
                String getContent=PackageRule.handlePackage(inputStream);


                if(getContent.equals("over")) {
                    break;
                }else{
                    System.out.println(getContent);
                }

                //another method of reading inputStream
                    /*
                    byte[] bys = new byte[1024];
                    int len = inputStream.read(bys);
                        String getContent = new String(bys, 0, len);

                        if(getContent.equals("over")) {
                            break;
                        }else{
                            System.out.println(getContent);
                        }
                     */
            }
        }
        catch(IOException e){
            e.printStackTrace();
            // TODO: 2020/8/17
        }finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (inputStream!=null){
                    inputStream.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            // TODO: 2020/8/14
        }
    }
}




























//import Client.Client;

//import java.io.*;

/*public class ClientThread {
    public static void main(String[] args) {
        while (true) {
                System.out.println("Welcome to the library system, please tell us who you are first.");
                System.out.println("Are you A or B?");
                InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                try {
                    String studentName = bufferedReader.readLine();
                    studentName=studentName.toUpperCase();
                    switch (studentName) {
                        case "A": {
                            Thread studentA = new Thread(new Client(), "studentA");
                            studentA.start();
                            //thread_main only goes on until thread_studentA ends
                            studentA.join();
                            break;
                        }
                        case "B": {
                            Thread studentB = new Thread(new Client(), "studentB");
                            studentB.start();
                            studentB.join();
                            break;
                        }
                        default:
                            System.out.println("Sorry,We don't have this student");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: 2020/8/16 ioexception
                }finally {
                    try {
                        if(System.in!=null){
                            System.in.close();
                        }
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                        if(bufferedReader!=null){
                            bufferedReader.close();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
}
*/