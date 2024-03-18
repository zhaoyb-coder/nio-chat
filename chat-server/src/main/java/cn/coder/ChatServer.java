package cn.coder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.coder.message.MessageHandler;
import cn.coder.message.MessageHandlerFactory;
import cn.coder.module.Message;
import cn.coder.util.ProtoStuffUtil;

/**
 * @author zhaoyubo
 * @title ChatServer
 * @description 服务器启动入口
 * @create 2024/3/8 14:32
 **/
public class ChatServer {

    private BossThread bossThread;
    private Selector selector;
    private AtomicInteger onlineUsers;
    private ExecutorService readPool;
    private ServerSocketChannel serverSocketChannel;
    public static final int PORT = 9000;
    public static final String QUIT = "QUIT";
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public ChatServer() {
        System.out.println("服务器启动...");
        initServer();
    }

    private void initServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            // 切换为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            // 获得选择器
            selector = Selector.open();
            // 将channel注册到selector上
            // 第二个参数是选择键，用于说明selector监控channel的状态
            // 可能的取值：SelectionKey.OP_READ OP_WRITE OP_CONNECT OP_ACCEPT
            // 监控的是channel的接收状态
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.readPool = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
            this.bossThread = new BossThread();
            this.onlineUsers = new AtomicInteger(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("开始初始化服务器...");
        ChatServer chatServer = new ChatServer();
        chatServer.launch();
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.equalsIgnoreCase(QUIT)) {
                System.out.println("服务器准备关闭");
                chatServer.shutdownServer();
                System.out.println("服务器已关闭");
            }
        }
    }

    public void launch() {
        new Thread(bossThread).start();
    }

    /**
     * 关闭服务器
     */
    public void shutdownServer() {
        try {
            bossThread.shutdown();
            readPool.shutdown();
            serverSocketChannel.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class BossThread extends Thread {

        @Override
        public void interrupt() {
            try {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                super.interrupt();
            }
        }

        @Override
        public void run() {
            try {
                System.out.println("调用。。1");
                // 如果有一个及以上的客户端的数据准备就绪
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("222");
                    // 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
                    selector.select();
                    // 获取当前选择器中所有注册的监听事件
                    for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();) {
                        SelectionKey key = it.next();
                        // 删除已选的key,以防重复处理
                        it.remove();
                        // 如果"接收"事件已就绪
                        if (key.isAcceptable()) {
                            System.out.println(11);
                            // 交由接收事件的处理器处理
                            handleAcceptRequest();
                        } else if (key.isReadable()) {
                            // 如果"读取"事件已就绪
                            // 取消可读触发标记，本次处理完后才打开读取事件标记
                            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                            // 交由读取事件的处理器处理
                            readPool.execute(new ReadEventHandler(key));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void shutdown() {
            Thread.currentThread().interrupt();
        }
    }

    private void handleAcceptRequest() {
        try {
            SocketChannel client = serverSocketChannel.accept();
            // 接收的客户端也要切换为非阻塞模式
            client.configureBlocking(false);
            // 监控客户端的读操作是否就绪
            client.register(selector, SelectionKey.OP_READ);
            System.out.printf("服务器连接客户端:%s", client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReadEventHandler implements Runnable {

        private ByteBuffer buf;
        private SocketChannel client;
        private ByteArrayOutputStream baos;
        private SelectionKey key;

        public ReadEventHandler(SelectionKey key) {
            this.key = key;
            this.client = (SocketChannel)key.channel();
            this.buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
            this.baos = new ByteArrayOutputStream();
        }

        @Override
        public void run() {
            try {
                int size;
                while ((size = client.read(buf)) > 0) {
                    buf.flip();
                    baos.write(buf.array(), 0, size);
                    buf.clear();
                }
                if (size == -1) {
                    return;
                }
                System.out.println("读取完毕，继续监听");
                // 继续监听读取事件
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                key.selector().wakeup();
                byte[] bytes = baos.toByteArray();
                baos.close();
                Message message = ProtoStuffUtil.deserialize(bytes, Message.class);
                MessageHandler messageHandler = MessageHandlerFactory.getMessageHandler(message);
                try {
                    messageHandler.handle(message, selector, key, null, onlineUsers);
                } catch (InterruptedException e) {
                    System.out.println("服务器线程被中断");
                    // exceptionHandler.handle(client, message);
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
