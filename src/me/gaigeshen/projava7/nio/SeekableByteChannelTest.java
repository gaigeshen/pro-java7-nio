package me.gaigeshen.projava7.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 10/05 2018
 */
public class SeekableByteChannelTest {
  
  @Test
  public void readingAFile() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/src/me/gaigeshen/projava7/nio/SeekableByteChannelTest.java");
    
    try (SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ))) {
      // Buffer with 12 bytes
      ByteBuffer buffer = ByteBuffer.allocate(12);
      // Get encoding from system
      String encoding = System.getProperty("file.encoding");
      // Clears this buffer. The position is set to zero, the limit is set to the capacity, and the mark is discarded.
      buffer.clear();
      while (channel.read(buffer) > 0) {
        // Flips this buffer. The limit is set to the current position and then the position is set to zero. If the mark is defined then it is discarded.
        buffer.flip();
        System.out.print(Charset.forName(encoding).decode(buffer));
        buffer.clear();
      }
    }
  }
  
  @Test
  public void writingAFile() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    
    //  An optional list of file attributes to set atomically when creating the file, if you need.
    try (SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {
      ByteBuffer buffer = ByteBuffer.wrap("A test message to write!".getBytes());
      int len = channel.write(buffer);
      System.out.println("Number of written bytes: " + len);
      buffer.clear();
    }
  }
  
  // Play with SeekableByteChannel position !
  // You know how to read and write an entire file with SeekableByteChannel, you are ready to
  // discover how you can do the same operations but at a specified channel (entity) position.
  // Keep in mind that the position() method without arguments returns the
  // current channel (entity) position, while the position(long) method sets the current position in the
  // channel (entity) by counting the number of bytes from the beginning of it. The first position is 0 and the
  // last valid position is the channel (entity) size.
  
  @Test
  public void createFileChannel() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    
    try (FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE))) {
      // sun.nio.ch.FileChannelImpl@531d72ca
      System.out.println(channel);
    }
    
    // Or casting SeekableByteChannel to FileChannel
    try (FileChannel channel = (FileChannel) Files.newByteChannel(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE))) {
      // sun.nio.ch.FileChannelImpl@22d8cfe0
      System.out.println(channel);
    }
  }
  
  @Test
  public void mappingFileRegion() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    MappedByteBuffer buffer = null;
    try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
      buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
    }
    
    if (buffer != null) {
      Charset charset = Charset.defaultCharset();
      CharBuffer decoded = charset.newDecoder().decode(buffer);
      // Print content from the file
      System.out.println(decoded.toString());
      buffer.clear();
    }
  }
  
  @Test
  public void lockFile() throws Exception {
    
    ByteBuffer buffer = ByteBuffer.wrap("Hei, a joke".getBytes());
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    try (FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE))) {
      // Use the file channel to create a lock on the file
      // This method blocks until it can retrieve the lock
      FileLock lock = channel.lock();
      
      if (lock.isValid()) {
        System.out.println("Locked");
        Thread.sleep(60000);
        
        channel.position(0);
        channel.write(buffer);
        
        lock.release();
        System.out.println("Lock released");
      }
    }
  }
  
  @Test
  public void writeLockedFile() throws Exception {
    ByteBuffer buffer = ByteBuffer.wrap("Hei, another joke".getBytes());
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    try (FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE))) {
      channel.position(0);
      // Can not write, throws exception
      // After the lock release by lockFile() method
      channel.write(buffer);
    }
  }
  
  @Test
  public void copyFile() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    try (FileChannel fromChannel = FileChannel.open(path, StandardOpenOption.READ);
        FileChannel toChannel = FileChannel.open(Paths.get("E:/workspace/pro-java7-nio/new_copy.txt"), EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))) {
      ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 1024);
      while (fromChannel.read(buffer) > 0) {
        buffer.flip();
        toChannel.write(buffer);
        // For the next read
        buffer.clear();
      }
    }
  }
  
  @Test
  public void copyFileUsingTransferMethod() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio/new.txt");
    try (FileChannel fromChannel = FileChannel.open(path, StandardOpenOption.READ);
        FileChannel toChannel = FileChannel.open(Paths.get("E:/workspace/pro-java7-nio/new_copy.txt"), EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))) {
      fromChannel.transferTo(0, fromChannel.size(), toChannel);
    }
  }
  
  // Files.copy() with Path to Path seams to be the fastest solution for copying a file.
}
