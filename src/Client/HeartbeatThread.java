package Client;


import com.mfc.base.practice.practice5_round2.Rule.PackageRule;

import java.io.OutputStream;
import java.net.Socket;

public class HeartbeatThread implements Runnable{
    //延迟时间
    long checkDelay = 10;
    //间隔时间，也就是当5s之内没有数据交互，那么客户端就需要发送心跳包给服务端
    long intervalDelay = 5000;

    long lastSendTime;

    public Socket socket;

    HeartbeatThread(long lastSendTime,Socket socket){
        this.lastSendTime=lastSendTime;
        this.socket=socket;
    }

    public void sendObject(String obj){
        try{
                OutputStream oos = socket.getOutputStream();
                //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(oos));
                //bufferedWriter.write(obj + "\n");
                //bufferedWriter.flush();
                byte[] sendPackage = PackageRule.pack(obj.getBytes().length, obj.getBytes());
                oos.write(sendPackage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while (socket.isClosed()==false){
                //如果当前时间距离最后发送数据的时间间隔大于定义好的时间间隔，就发送心跳
                if (System.currentTimeMillis() - lastSendTime > intervalDelay) {
                    try {
                        sendObject(new String("heartbeat " + System.currentTimeMillis()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lastSendTime = System.currentTimeMillis();
                } else {
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}