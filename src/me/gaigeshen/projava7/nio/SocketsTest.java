package me.gaigeshen.projava7.nio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 10/05 2018
 */
public class SocketsTest {

  // Socket options
  
  // 1.IP_MULTICAST_IF: This option is used to specify the network interface
  // (NetworkInterface) used for multicast datagrams sent by the datagram-oriented
  // socket; if it is null, then the OS will choose the outgoing interface (if one is
  // available). By default, it is null, but the option’s value can be set after the socket is
  // bound. When we talk about sending datagrams, you will see how to find out what
  // multicast interfaces are available on your machine.
  
  // 2.IP_MULTICAST_LOOP: This option’s value is a boolean that controls the loopback of
  // multicast datagrams (this is OS dependent). You have to decide, as the application
  // writer, whether you want the data you send to be looped back to your host or not.
  // By default, this is TRUE, but the option’s value can be set after the socket is bound.
  
  // 3.IP_MULTICAST_TTL: This option’s value is an integer between 0 and 255, and it
  // represents the time-to-live for multicast packets sent out by the datagramoriented socket.
  // If not otherwise specified, multicast datagrams are sent with a
  // default value of 1, to prevent them to be forwarded beyond the local network. With
  // this option we can control the scope of the multicast datagrams. By default this is
  // set to 1, but the option’s value can be set after the socket is bound.
  
  // 4.IP_TOS: This option’s value is an integer representing the value of the Type of
  // Service (ToS) octet in IP packets sent by sockets—the interpretation of this value is
  // specific to the network. Currently this is available only for IPv4, and by default its
  // value is typically 0. The option’s value can be set any time after the socket is
  // bound.
  
  // 5.SO_BROADCAST: This option’s value it is a boolean that indicates if transmission of
  // broadcast datagrams is allowed or not (specific to datagram-oriented sockets
  // sending to IPv4 broadcast addresses). By default, it is FALSE, but the option’s value
  // can be set any time.
  
  // 6.SO_KEEPALIVE: This option’s value it is a boolean indicating if the connection
  // should be kept alive or not. By default, it is set to FALSE, but the option’s value can
  // be set any time.
  
  // 7.SO_LINGER: This option’s value is an integer that represents a timeout in seconds
  // (the linger interval). When attempting to close a blocking-mode socket via the
  // close() method, it will wait for the duration of the linger interval before
  // transmitting the unsent data (not defined for non-blocking mode). By default, it is
  // a negative value, which means that this option is disabled. The option’s value can
  // be set any time and the maximum value is OS dependent.
  
  // 8.SO_RCVBUF: This option’s value is an integer that represents the size in bytes of the
  // socket receive buffer—the input buffer used by the networking implementation. By
  // default, the value is OS dependent, but it can be set before the socket is bound or
  // connected. Depending on the OS, the value can be changed after the socket is
  // bound. Negative values are not allowed.
  
  // 9.SO_SNDBUF: This option’s value is an integer that represents the size in bytes of the
  // socket send buffer—the output buffer used by the networking implementation. By
  // default, the value is OS dependent, but it can be set before the socket is bound or
  // connected. Depending on the OS, the value can be changed after the socket is
  // bound. Negative values are not allowed.
  
  // 10.SO_REUSEADDR: This option’s value is an integer that represents if an address can be
  // reused or not. This is very useful in datagram multicasting when we want multiple
  // programs to be bound to the same address. In the case of stream-oriented sockets,
  // the socket can be bound to an address when a previous connection is in the
  // TIME_WAIT state – TIME_WAIT means the OS has received a request to close the
  // socket, but waits for possible late communications from the client side. By default,
  // the option’s value is OS dependent, but it can be set before the socket is bound or
  // connected.
  
  // 11.TCP_NODELAY:  This option’s value is an integer that enables/disables Nagle’s
  // algorithm (for more information on Nagle’s algorithm, see
  // http://en.wikipedia.org/wiki/Nagle%27s_algorithm). By default it is FALSE, but it
  // can be set at any time.
  
  @Test
  public void listSupportedSocketOptions() throws Exception {
    
    // Retrieving the supported options for a specific channel (for a network socket) can be accomplished
    // by calling the NetworkChannel.supportedOptions() method over that channel:
    
    // Set<SocketOptions<?>> supportedOptions();
  }
  
  @Test
  public void blockingTcpServer() throws Exception {

    ServerSocketChannel channel = ServerSocketChannel.open();
    
    // Keep in mind that a newly created server socket channel is not bound or connected. Binding and
    // connecting will be accomplished in the next steps.
    // You can check if a server socket is already open or has been successfully opened by calling the
    // ServerSocketChannel.isOpen() method, which returns the corresponding Boolean value:
    if (channel.isOpen()) {
      // Configuring blocking mechanisms
      channel.configureBlocking(true);
      
      // Setting server socket channel options
      // This is an optional step, there is no required option (you can use the default values).
      // a server socket channel supports
      // two options: SO_RCVBUF and SO_REUSEADDR. We’ll set them both, as shown here:
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      
      // Binding server socket channel
      channel.bind(new InetSocketAddress("127.0.0.1", 5555));
      
      // Accepting connections
      // Since we are in blocking mode,
      // accepting a connection will block the application until a new connection is available
      // or an I/O error occurs.
      SocketChannel socket = channel.accept();
      
      // Information
      System.out.println("Incoming connection from: " + socket.getRemoteAddress());
      
      // Echo server mode
      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
      while (socket.read(buffer) != -1) {
        buffer.flip();
        socket.write(buffer);
        if (buffer.hasRemaining()) {
          buffer.compact();
        } else {
          buffer.clear();
        }
      }
      
      // Shutdown
      // These methods are very useful if you want to reject read/write attempts without closing the channel
      socket.shutdownInput();
      socket.shutdownOutput();
      
      // Close
      socket.close();

      // this will close the server for listening for
      // incoming connections; further clients won’t be able to locate the server anymore.
      channel.close();
    }
  }

  @Test
  public void blockingTcpClient() throws Exception {
    
    SocketChannel socket = SocketChannel.open();
    
    if (socket.isOpen()) {
      
      socket.configureBlocking(true);
      
      socket.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
      socket.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
      socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
      socket.setOption(StandardSocketOptions.SO_LINGER, 5);
      
      socket.connect(new InetSocketAddress("127.0.0.1", 5555));
      if (socket.isConnected()) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer msgBuff = ByteBuffer.wrap("Hello !".getBytes());
        ByteBuffer randomBuff;
        CharBuffer charBuffer;
        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        socket.write(msgBuff);
        while (socket.read(buffer) != -1) {
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
            System.out.println("50 was generated ! Close the socket channel");
            break;
          } else {
            randomBuff = ByteBuffer.wrap("Random number: ".concat(String.valueOf(r)).getBytes());
            socket.write(randomBuff);
          }
        }
        socket.close();
      }
    }
  }

  // SelectionKey
  
  // SelectionKey.OP_ACCEPT: The associated client requests a connection
  // (usually created on the server side for indicating that a client requires a
  // connection).
  
  // SelectionKey.OP_CONNECT: The server accepts the connection
  // (usually created on the client side).
  
  // SelectionKey.OP_READ: This indicates a read operation.
  
  // SelectionKey.OP_WRITE: This indicates a write operation.
  
  @Test
  public void nonBlockingServer() throws Exception {
    
    Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
    
    try (Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open()) {
      if (!channel.isOpen() || !selector.isOpen()) {
        System.out.println("The server socket channel or selector cannot be opened!");
        return;
      }
      // Configure non-blocking mode
      channel.configureBlocking(false);
      
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      
      channel.bind(new InetSocketAddress("127.0.0.1", 5555));
      
      // Register the current channel with the given selector
      channel.register(selector, SelectionKey.OP_ACCEPT);
      
      System.out.println("Waiting for connections...");
      
      while (true) {
        // Wait for incoming events
        // Blocking here
        selector.select();
        // There is something to process on selected keys
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          // Prevent the same key from coming up again
          keys.remove();
          if (!key.isValid()) {
            continue;
          }
          if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel socket = serverChannel.accept();
            socket.configureBlocking(false);
            System.out.println("Incoming connection from: " + socket.getRemoteAddress());
            socket.write(ByteBuffer.wrap("Hello!\n".getBytes()));
            keepDataTrack.put(socket, new ArrayList<>());
            // Register channel with selector for further IO
            socket.register(selector, SelectionKey.OP_READ);
          } else if (key.isReadable()) {
            SocketChannel socket = (SocketChannel) key.channel();
            buffer.clear();
            int readed = socket.read(buffer);
            if (readed == -1) {
              keepDataTrack.remove(socket);
              System.out.println("Connection closed by: " + socket.getRemoteAddress());
              socket.close();
              key.cancel();
            } else {
              byte[] data = new byte[readed];
              System.arraycopy(buffer.array(), 0, data, 0, readed);
              System.out.println(new String(data) + " from " + socket.getRemoteAddress());
              List<byte[]> channelData = keepDataTrack.get(socket);
              channelData.add(data);
              key.interestOps(SelectionKey.OP_WRITE);
            }
          } else if (key.isWritable()) {
            SocketChannel socket = (SocketChannel) key.channel();
            List<byte[]> channelData = keepDataTrack.get(socket);
            Iterator<byte[]> iterator = channelData.iterator();
            while (iterator.hasNext()) {
              byte[] data = iterator.next();
              iterator.remove();
              socket.write(ByteBuffer.wrap(data));
            }
            key.interestOps(SelectionKey.OP_READ);
          }
        }
      }
    }
  }

  @Test
  public void nonBlockingClient() throws Exception {

    ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);
    ByteBuffer randomBuffer;
    CharBuffer charBuffer;
    CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

    try (Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open()) {
      if (!channel.isOpen() || !selector.isOpen()) {
        System.out.println("The server socket channel or selector cannot be opened!");
        return;
      }
      // Configure non-blocking mode
      channel.configureBlocking(false);
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
      channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
      channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
      // Register the current channel with the given selector
      channel.register(selector, SelectionKey.OP_CONNECT);
      // Connect to remote host
      channel.connect(new java.net.InetSocketAddress("127.0.0.1", 5555));
      // Waiting for the connection
      while (selector.select(1000) > 0) {
        // Get keys
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        // Process each key
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          // Remove the current key
          keys.remove();
          // Get the socket channel for this key
          try (SocketChannel socket = (SocketChannel) key.channel()) {
            // Attempt a connection
            if (key.isConnectable()) {
              // Signal connection success
              System.out.println("I am connected!");
              // Close pending connections
              if (socket.isConnectionPending()) {
                socket.finishConnect();
              }
              // Read/write from/to server
              while (socket.read(buffer) != -1) {
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
                  System.out.println("50 was generated! Close the socket channel!");
                  break;
                } else {
                  randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                  socket.write(randomBuffer);
                  try {
                    Thread.sleep(1500);
                  } catch (InterruptedException ex) {
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
