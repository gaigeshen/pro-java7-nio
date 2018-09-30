package me.gaigeshen.projava7.nio;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 09/30 2018
 */
public class PathTest {

  
  @Test
  public void fileSystem() throws Exception {
    
    // This is a static method that returns the default FileSystem to the
    // JVM—commonly the operating system default file system
    FileSystem fs = FileSystems.getDefault();
    
    // sun.nio.fs.WindowsFileSystem(Windows)
    // E:\\workspace\\pro-java7-nio(defaultDirectory)
    // E:\\(defaultRoot)
    System.err.println(fs);
    
    // \
    System.err.println(fs.getSeparator());
    
    // [C:\, D:\, E:\, F:\, G:\]
    System.err.println(fs.getRootDirectories());
    
  }
  
  @Test
  public void defineAbsolutePath() throws Exception {
    
    Path path1 = Paths.get("E:\\workspace\\pro-java7-nio");
    Path path2 = Paths.get("E:", "workspace", "pro-java7-nio");
    
    // E:\workspace\pro-java7-nio
    System.out.println(path1);
    System.out.println(path2);
    
  }
  
  @Test
  public void defineRelativePath() throws Exception {
    
    // to the file store root
    Path path1 = Paths.get("\\workspace\\pro-java7-nio");
    
    // to the working folder, if current working folder is E:
    Path path2 = Paths.get("workspace\\pro-java7-nio");
    
    // \workspace\pro-java7-nio
    System.out.println(path1);
    // workspace\pro-java7-nio
    System.out.println(path2);
  }
  
  @Test
  public void defineUsingShortcuts() throws Exception {
    
    Path path1 = Paths.get("E:/workspace/pro-java7-nio/../pro-java7-nio");
    Path path2 = Paths.get("E:/workspace/pro-java7-nio/../pro-java7-nio").normalize();
    Path path3 = Paths.get("/workspace/pro-java7-nio/../pro-java7-nio").normalize();
    
    // E:\workspace\pro-java7-nio\..\pro-java7-nio
    System.out.println(path1);
    // E:\workspace\pro-java7-nio
    System.out.println(path2);
    // \workspace\pro-java7-nio
    System.out.println(path3);
  }
  
  @Test
  public void defineFromURI() throws Exception {
    
    Path path1 = Paths.get(URI.create("file:///E:/workspace/pro-java7-nio"));
    
    // E:\workspace\pro-java7-nio
    System.out.println(path1);
  }
  
  @Test
  public void defineUsingFileSystem() throws Exception {
    
    Path path = FileSystems.getDefault().getPath("E:/workspace/pro-java7-nio");
    
    // E:\workspace\pro-java7-nio
    System.out.println(path);
  }
  
  @Test
  public void getHomeDirectory() throws Exception {
    
    Path path = Paths.get(System.getProperty("user.home"), "Downloads");
    
    // C:\Users\gaigeshen\Downloads
    System.out.println(path);
    
  }
  
  @Test
  public void getPathRoot() throws Exception {
    
    Path path1 = Paths.get("E:\\workspace\\pro-java7-nio");
    Path path2 = Paths.get("\\workspace\\pro-java7-nio");
    
    // E:\
    System.out.println(path1.getRoot());
    // \
    System.out.println(path2.getRoot());
    
  }
  
  @Test
  public void getPathParent() throws Exception {
    
    Path path1 = Paths.get("E:\\workspace\\pro-java7-nio");
    Path path2 = Paths.get("E:");
    
    // E:\workspace
    System.out.println(path1.getParent());
    
    // null
    System.out.println(path2.getParent());
  }
  
  @Test
  public void getPathNameElements() throws Exception {
    
    Path path1 = Paths.get("E:\\workspace\\pro-java7-nio");
   
    // Number of name elements in path: 2
    System.out.println("Number of name elements in path: " + path1.getNameCount());
    
    // Name element 0 is: workspace
    // Name element 1 is: pro-java7-nio
    for (int i = 0; i < path1.getNameCount(); i++) {
      System.out.println("Name element " + i + " is: " + path1.getName(i));
    }
  }
  
  @Test
  public void getSubpath() throws Exception {
    
    Path path1 = Paths.get("E:\\workspace\\pro-java7-nio");
    
    // Subpath(0, 2) is: workspace\pro-java7-nio
    System.out.println("Subpath(0, 2) is: " + path1.subpath(0, 2));
    
  }
  
  @Test
  public void convertPath() throws Exception {
    
    Path path = Paths.get("\\workspace\\pro-java7-nio");
    
    // to string
    // \workspace\pro-java7-nio
    System.out.println(path.toString());
    
    // to URI
    // file:///E:/workspace/pro-java7-nio/
    System.out.println(path.toUri());
    
    // to absolute path
    // E:\workspace\pro-java7-nio
    System.out.println(path.toAbsolutePath());
    
    // to real path
    // E:\workspace\pro-java7-nio
    // May throw NoSuchFileException
    // If you want to ignore symbolic links, then pass to the method the LinkOption.NOFOLLOW_LINKS enum constant
    // If the Path is relative, it returns an absolute path, and if the Path contains any redundant elements, it returns a path with those elements removed
    System.out.println(path.toRealPath());
    
    // to file
    System.out.println(path.toFile());
  }
  
  @Test
  public void combining() throws Exception {
    
    Path base = Paths.get("\\workspace");
    Path path1 = base.resolve("pro-java7-nio");
    
    Path path2 = path1.resolveSibling("python-tutorial");
    
    // \workspace\pro-java7-nio
    System.out.println(path1);
    // \workspace\python-tutorial
    System.out.println(path2);
  }
  
  @Test
  public void constructingBetweenTwoLocations() throws Exception {
    
    Path path1 = Paths.get("\\workspace\\pro-java7-nio");
    Path path2 = Paths.get("\\workspace\\python-tutorial");
    
    // ..\python-tutorial
    System.out.println(path1.relativize(path2));
  }
  
  @Test
  public void comparingTwoPaths() throws Exception {
    
    Path path1 = Paths.get("\\workspace\\pro-java7-nio");
    Path path2 = Paths.get("E:\\workspace\\pro-java7-nio");
    
    // false
    // This method respects the Object.equals() specification. 
    // It does not access the file system, so the compared paths are not required to exist, and it
    // does not check if the paths are the same file
    System.out.println(path1.equals(path2));
    
    // true
    // If Path.equals() returns true, the paths are equal, 
    // and therefore no further comparisons are needed. 
    // If it returns false, then the isSameFile() method enters into action to double-check
    // Notice that this method requires that the compared files exist on the file system; otherwise, it throws an IOException
    System.out.println(Files.isSameFile(path1, path2));
    
    // 23
    // The method returns zero if the argument is equal to this path, 
    // a value less than zero if this path is lexicographically less than the argument, 
    // or a value greater than zero if this path is lexicographically greater than the argument
    System.out.println(path1.compareTo(path2));
    
    // true
    System.out.println(path1.startsWith("\\"));
    // false
    System.out.println(path1.startsWith("E:"));
  }
  
  @Test
  public void iterate() throws Exception {
    
    Path path1 = Paths.get("\\workspace\\pro-java7-nio");
    
    // workspace
    // pro-java7-nio
    for (Path path : path1) {
      System.out.println(path);
    }
  }
}
