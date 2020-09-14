package Rule;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class PackageRule {
    public static final int SAPARATOR=9876;//分隔符，int类型数据长4字节

    //byte 数组与 int 的相互转换
    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;

    }

    //将直接暴露的字节流组装成结构：分隔符+数据长度+数据
    public static byte[] pack(int length,byte[] data){
        byte[] lengthByteArray=intToByteArray(length);
        byte[] saparatorByteArray=intToByteArray(SAPARATOR);

        ByteBuffer bbf=ByteBuffer.allocate(lengthByteArray.length+data.length+saparatorByteArray.length);
        bbf.put(saparatorByteArray);
        bbf.put(lengthByteArray);
        bbf.put(data);
        return bbf.array();
    }

    //拼装两个字节流
    public static byte[] combine(byte[]part1,byte[]part2){
        ByteBuffer bbf=ByteBuffer.allocate(part1.length+part2.length);
        bbf.put(part1);
        bbf.put(part2);
        return bbf.array();
    }

    //字节流结构：分隔符（sparator）、有效数据长度（int型）、有效数据
    //解读出字节流中的有效数据
    public static String handlePackage(InputStream is) {
        String content=null;
        try {

            byte[] readByte=new byte[1024*128];
            int len;

            byte[] headByte = new byte[4];//存放头部的分隔符号
            byte[] lengthByte = new byte[4];//存放有效数据的长度
            byte[] contentByte;//存放有效数据

            int thislength = 0;//当前读取到的总字节数

            while ((len = is.read(readByte))>0) {
                byte[] thisByte = new byte[]{};
                thislength = len + thislength;
                thisByte = PackageRule.combine(thisByte, readByte);

                // TODO: 2020/8/27
                while (thislength > 8) {
                    System.arraycopy(thisByte, 0, headByte, 0, 4);
                    if (PackageRule.byteArrayToInt(headByte) != PackageRule.SAPARATOR) {
                        thislength = thislength - 4;
                        System.arraycopy(thisByte, 4, thisByte, 0, thislength);
                    }
                    break;
                }

                System.arraycopy(thisByte, 4, lengthByte, 0, 4);
                int contentLen = PackageRule.byteArrayToInt(lengthByte);
                if (thislength < contentLen + 8) {
                    continue;
                }

                contentByte = new byte[contentLen];
                System.arraycopy(thisByte, 8, contentByte, 0, contentLen);
                content = new String(contentByte, "UTF-8");

                int aPackageLength = contentLen + 8;
                thislength = thislength - aPackageLength;
                System.arraycopy(thisByte, aPackageLength, thisByte, 0, thislength);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}
