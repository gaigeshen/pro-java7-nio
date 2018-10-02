package me.gaigeshen.projava7.nio;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 10/01 2018
 */
public class FileAndDirectoryTest {

  @Test
  public void checkExistsStatus() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    
    System.out.println(Files.exists(path));
    System.out.println(Files.notExists(path));
    
    // !Files.exists(…) is not equivalent to Files.notExists(…) and the notExists() method is
    // not a complement of the exists() method.
  }
  
  @Test
  public void checkFileVisibility() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    // false
    System.out.println(Files.isHidden(path));
  }
  
  @Test
  public void checks() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    
    boolean readable = Files.isReadable(path);
    boolean writable = Files.isWritable(path);
    boolean executable = Files.isExecutable(path);
    boolean regularFile = Files.isRegularFile(path);
    
    // readable: true
    // writable: true
    // executable: true
    // regularFile: false
    System.out.println("readable: " + readable);
    System.out.println("writable: " + writable);
    System.out.println("executable: " + executable);
    System.out.println("regularFile: " + regularFile);
    
    // accessible: false
    // You can combine these four methods in different ways depending on what level of accessibility you need to get.
    boolean accessible = Files.isRegularFile(path) & Files.isReadable(path) & Files.isExecutable(path) & Files.isWritable(path);
    System.out.println("accessible: " + accessible);
  }

  @Test
  public void listRootDir() throws Exception {
    
    Iterable<Path> paths = FileSystems.getDefault().getRootDirectories();
    // C:\
    // D:\
    // E:\
    // F:\
    // G:\
    for (Path path : paths) {
      System.out.println(path);
    }
    
    // Use java6 solution
    File[] files = File.listRoots();
    for (File file : files) {
      System.out.println(file);
    }
  }
  
  @Test
  public void createDir() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\new");
    Path newPath = Files.createDirectory(path);
    
    // E:\workspace\pro-java7-nio\new
    System.out.println(newPath);
    
    // With posix file permissions
    // Path path1 = Paths.get("/home/gaigeshen/workspace/pro-java7-nio/new");
    // Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-x---");
    // Path newPath1 = Files.createDirectory(path1, PosixFilePermissions.asFileAttribute(permissions));
    // System.out.println(newPath1);
  }
  
  @Test
  public void createDirs() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\foo\\bar");
    
    // java.nio.file.NoSuchFileException: E:\workspace\pro-java7-nio\foo\bar
    // Path newPath = Files.createDirectory(path);
    
    Path newPath = Files.createDirectories(path);
    
    // E:\workspace\pro-java7-nio\foo\bar
    System.out.println(newPath);
    
    // If in the sequence of directories one or more directories already exist, then the createDirectories()
    // method will not throw an exception, but rather will just “jump” that directory and go to the next one. This method
    // may fail after creation of some directories, but not all of them.
  }
  
  @Test
  public void createFile() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\new-file.txt");
    // Can add a set of attributes at creation time
    Path newFile = Files.createFile(path);
    // E:\workspace\pro-java7-nio\new-file.txt
    System.out.println(newFile);
  }
  
  @Test
  public void writeBytesToFile() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\new-file.txt");
    // In short, this method acts as if the CREATE, TRUNCATE_EXISTING, and WRITE options are present—of
    // course, this is applicable by default when no other options are specified
    Files.write(path, "A test text inputed".getBytes());
  }
  
  @Test
  public void writeLinesToFile() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\new-file-with-line.txt");
    Charset charset = Charset.forName("UTF-8");
    ArrayList<String> lines = new ArrayList<>();
    lines.add("\n");
    lines.add("Rome Masters - 5 titles in 6 years");
    lines.add("Monte Carlo Masters - 7 consecutive titles (2005-2011)");
    lines.add("Australian Open - Winner 2009");
    lines.add("Roland Garros - Winner 2005-2008, 2010, 2011");
    lines.add("Wimbledon - Winner 2008, 2010");
    lines.add("US Open - Winner 2010");
    Files.write(path, lines, charset, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);
  }
  
  @Test
  public void readAllBytesFromFile() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\new-file-with-line.txt");
    byte[] all = Files.readAllBytes(path);
    
    System.out.println(new String(all));
    
    // Content is same as new-file-with-line.txt
    // Files.write(path.resolveSibling("new-file-with-line-copy.txt"), all);
  }
  
  @Test
  public void readAllLinesFromFile() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio\\new-file-with-line.txt");
    List<String> lines = Files.readAllLines(path);
    for (String line : lines) {
      System.out.println(line);
    }
  }
  
  // Working with Buffered Streams ?
  // NIO.2 provides two methods for reading and writing files through
  // buffers: Files.newBufferedReader() and Files.newBufferedWriter(), respectively. Both of these
  // methods get a Path instance and return an old JDK 1.1 BufferedReader or BufferedWriter instance.
  
  // Working with Unbuffered Streams ?
  // The unbuffered streams methods are Files.newInputStream() (input stream to read from the file)
  // and Files.newOutputStream() (output stream to write to a file).
  
  @Test
  public void createTempDir() throws Exception {
    
    Path temp = Files.createTempDirectory(null);
    // C:\Users\GAIGES~1\AppData\Local\Temp\3501399879427079526
    System.out.println(temp);
    
    Path temp1 = Files.createTempDirectory("nio_");
    // C:\Users\GAIGES~1\AppData\Local\Temp\nio_1287724092794640123
    System.out.println(temp1);
    
    // Change the temp dir
    Path temp2 = Files.createTempDirectory(Paths.get("E:\\workspace\\pro-java7-nio"), "nio");
    // E:\workspace\pro-java7-nio\nio8215909436155899550
    System.out.println(temp2);
    
    // Get default temp dir
    // C:\Users\GAIGES~1\AppData\Local\Temp\
    System.out.println(System.getProperty("java.io.tmpdir"));
  }
  
  @Test
  public void createTempFile() throws Exception {
    
    Path temp = Files.createTempFile(null, null);
    
    // C:\Users\GAIGES~1\AppData\Local\Temp\8660755772685001943.tmp
    System.out.println(temp);
    
    // Can change base dir of temp dir
  }
  
  @Test
  public void listDirContent() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    // .classpath
    // .git
    // .gitignore
    // .project
    // .settings
    // bin
    // lib
    // src
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
      for (Path p : ds) {
        System.out.println(p.getFileName());
      }
    }
  }
  
  @Test
  public void listDirContentWithFilter() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    // .classpath
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*.{classpath}")) {
      for (Path p : ds) {
        System.out.println(p.getFileName());
      }
    }
  }
  
  @Test
  public void listDirContentWithUserDefinedFilter() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    // .git
    // .settings
    // bin
    // lib
    // src
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, new DirectoryStream.Filter<Path>() {
      @Override
      public boolean accept(Path path) throws IOException {
        // Only dirs
        return Files.isDirectory(path);
      }
    })) {
      for (Path p : ds) {
        System.out.println(p.getFileName());
      }
    }
  }
}
