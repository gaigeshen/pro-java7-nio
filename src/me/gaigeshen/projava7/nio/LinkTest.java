package me.gaigeshen.projava7.nio;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 09/30 2018
 */
public class LinkTest {

  // Hard links can be created only for files, not for directories.
  // Symbolic links can link to a file or a directory.
  
  // Hard links cannot exist across file systems. Symbolic links can exist across file systems.
  
  // The target of a hard link must exist. The target of a symbolic link may not exist.
  
  // Removing the original file that your hard link points to does not remove the hard
  // link itself, and the hard link still provides the content of the underlying file.
  // Removing the original file that your symbolic link points to does not remove the
  // attached symbolic link, but without the original file, the symbolic link is useless.
  
  // If you remove the hard link or the symbolic link itself, the original file stays intact.
  
  // A hard link is the same entity as the original file. All attributes are identical. A
  // symbolic link is not so restrictive.
  
  // A hard link looks, and behaves, like a regular file, so hard links can be hard to find.
  // A symbolic link’s target may not even exist, therefore it is much flexible.
  
  @Test
  public void createLink() throws Exception {
    
    Path target = Paths.get("E:\\workspace\\pro-java7-nio");
    Path link = Paths.get("E:\\workspace\\pro-java7-nio-link");
    // Can be file or directory
    Files.createSymbolicLink(link, target);
    
    // Hard link can be created only for existing "files"
    Files.createLink(
        Paths.get("E:\\workspace\\pro-java7-nio\\lib\\junit-4.12.link.jar"),
        Paths.get("E:\\workspace\\pro-java7-nio\\lib\\junit-4.12.jar"));
    
  }
  
  @Test
  public void readSymbolicLink() throws Exception {
    
    Path link = Files.readSymbolicLink(Paths.get("E:\\workspace\\pro-java7-nio-link"));
    
    // E:\workspace\pro-java7-nio
    System.out.println(link);
    
    System.out.println("Is same file: " + Files.isSameFile(link, Paths.get("E:\\workspace\\pro-java7-nio")));
  }
}
