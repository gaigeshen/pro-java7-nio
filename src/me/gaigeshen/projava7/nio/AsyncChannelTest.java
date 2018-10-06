package me.gaigeshen.projava7.nio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 10/06 2018
 */
public class AsyncChannelTest {

  // Types of Asynchronous Channels
  // 1. AsynchronousFileChannel
  // 2. AsynchronousServerSocketChannel
  // 3. AsynchronousSocketChannel
  
  
  @Test
  public void readFileContentUsingFileChannel() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    
    try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
      // Use future mode
      ByteBuffer buffer = ByteBuffer.allocate(100);
      Future<Integer> result = channel.read(buffer, 0);
      while (!result.isDone()) {
        System.out.println("Do something else while reading...");
      }
      
      System.out.println("Read done: " + result.get() + " bytes");
      
      buffer.flip();
      System.out.println("Readed: " + Charset.forName("utf-8").decode(buffer));
      buffer.clear();
      
      // Print:
      // Do something else while reading...
      // Do something else while reading...
      // Read done: 26 bytes
      // Readed: The test content for you !
    }
  }
  
  @Test
  public void writeFileContentUsingFileChannel() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    
    try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
      
      Future<Integer> result = channel.write(
          ByteBuffer.wrap("I appended something.".getBytes()), 100);
      
      while (!result.isDone()) {
        System.out.println("Do something else while reading...");
      }
      
      System.out.println("Written done: " + result.get() + " bytes");
    }
  }

  // You can do same things with CompletionHandler
  
  @Test
  public void writeFileContentUsingFileChannelAndLock() throws Exception {
    
    ByteBuffer buffer = ByteBuffer.wrap("I am the test content using file lock".getBytes());
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
      
      FileLock lock = channel.lock().get();
      if (lock.isValid()) {
        Future<Integer> result = channel.write(buffer, 0);
        System.out.println("Written done: " + result.get() + " bytes");
        
        lock.release();
      }
    }
  }

  // So far, you’ve seen at work only the first AsynchronousFileChannel.open() method, which uses the
  // default pool thread. It is time to see the second open() method at work, which allows us to specify a
  // custom thread pool through an ExecutorService object...
  
  @Test
  public void asyncEchoServer() throws Exception {
    
    try (AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open()) {
      if (!channel.isOpen()) {
        System.out.println("The async server-socket channel cannot be opened!");
        return;
      }
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      
      channel.bind(new InetSocketAddress("127.0.0.1", 5555));
      System.out.println("Waiting for connections...");
      
      while (true) {
        Future<AsynchronousSocketChannel> result = channel.accept();
        try (AsynchronousSocketChannel socket = result.get()) {
          System.out.println("Incoming connection from: " + socket.getRemoteAddress());
          
          ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
          while (socket.read(buffer).get() != -1) {
            buffer.flip();
            socket.write(buffer).get();
            if (buffer.hasRemaining()) {
              buffer.compact();
            } else {
              buffer.clear();
            }
          }
          
          System.out.println(socket.getRemoteAddress() + " was successfully served");
        }
      }
    }
  }
  
  @Test
  public void asyncEchoClient() throws Exception {

    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
    ByteBuffer randomBuffer;
    CharBuffer charBuffer;
    CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

    try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
      channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
      channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

      Void result = channel.connect(new InetSocketAddress("127.0.0.1", 5555)).get();
      if (result != null) {
        System.out.println("Cannot connect!");
        return;
      }

      System.out.println("Local address: " + channel.getLocalAddress());
      
      // Say hello
      channel.write(helloBuffer).get();

      while (channel.read(buffer).get() != -1) {
        buffer.flip();
        charBuffer = decoder.decode(buffer);
        System.out.println(charBuffer.toString());

        if (buffer.hasRemaining()) {
          buffer.compact();
        } else {
          buffer.clear();
        }
        int r = new Random().nextInt(100);
        if (r == 50) {
          System.out.println("50 was generated! Close the asynchronous socket channel!");
          break;
        } else {
          randomBuffer = ByteBuffer.wrap("Random number: ".concat(String.valueOf(r)).getBytes());
          channel.write(randomBuffer).get();
        }
      }
    }
  }

  // You can using Read/Write operations and CompletionHandler too.
  
  // TIPs: The applications presented in this chapter are fine for educational purposes but not for a production
  // environment. If you need to write applications for a production environment, then it is a good idea to
  // keep in mind the following tips.
  // 1. Use Byte Buffer Pool and Throttle Read Operations
  // 2. Use Blocking Only for Short Reading Operations
  // 3. Use FIFO-Q and Allow Blocking for Write Operations
}
