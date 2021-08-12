package org.quickstart.redis.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class JDKRedisTest {

    // For Bulk Strings the first byte of the reply is "$"
    private static final byte DOLLAR_BYTE = '$';
    // For Arrays the first byte of the reply is "*"
    private static final byte ASTERISK_BYTE = '*';

    private static final byte CR = '\r';
    private static final byte LF = '\n';

    // For Simple Strings the first byte of the reply is "+"
    private static final byte PLUS_BYTE = '+';
    // For Errors the first byte of the reply is "-"
    private static final byte MINUS_BYTE = '-';
    // For Integers the first byte of the reply is ":"
    private static final byte COLON_BYTE = ':';

    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 6379;
        int timeout = 30 * 1000;

        Socket socket = new Socket();
        socket.setReuseAddress(true);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.setSoLinger(true, 0);
        socket.connect(new InetSocketAddress(host, port), timeout);
        socket.setSoTimeout(timeout);

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        // os.write(ASTERISK_BYTE); // 协议里定义发送数组数据第一个字节必须是"*"
        // os.writeIntCrLf(args.length + 1); // 接下来是数组长度(命令+参数)，并在后边加上"\r\n"
        // os.write(DOLLAR_BYTE); // 对于一批字符串用 $ 开头
        // os.writeIntCrLf(command.length); // 后边跟着字符串的字节数，并在后边加上"\r\n"
        // os.write(command);
        // os.writeCrLf(); // 假设发送一个"foo bar"这样的内容，按协议编码后是：
        // "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"

        os.write("*3\r\n$3\r\nSET\r\n$3\r\nfoo\r\n$7\r\nbartest\r\n".getBytes(StandardCharsets.UTF_8));
        os.flush();

        byte[] buf = new byte[3];
        is.read(buf);
        String result = new String(buf, StandardCharsets.UTF_8);
        System.out.println(result);

    }

}
