package me.gaigeshen.projava7.nio;

import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 09/30 2018
 */
public class MetadataTest {

  // Supported Views in NIO.2
  // BasicFileAttributeView:
  //  This is a view of basic attributes that must be supported by all file system implementations.
  //  The attribute view name is basic
  // DosFileAttributeView:
  //  This view provides the standard four supported attributes on file systems that support the DOS attributes.
  //  The attribute view name is dos
  // PosixFileAttributeView:
  //  This view extends the basic attribute view with attributes supported on file systems that support the POSIX
  //  (Portable Operating System Interface for Unix) family of standards, such as Unix. The attribute view name is posix
  // FileOwnerAttributeView:
  //  This view is supported by any file system implementation that supports the concept of a file owner.
  //  The attribute view name is owner
  // AclFileAttributeView:
  //  This view supports reading or updating a file’s ACL. The NFSv4 ACL model is supported. The attribute view name is acl
  // UserDefinedFileAttributeView:
  //  This view enables support of metadata that is user defined
  
  @Test
  public void listSupportedViews() throws Exception {
    
    Set<String> views = FileSystems.getDefault().supportedFileAttributeViews();
    
    // All file systems support the basic view,
    // so you should get at least the basic name in your output
    for (String view : views) {
      System.out.println(view);
    }
  }
  
  @Test
  public void ifFileStoresSupportedViews() throws Exception {
    
    FileSystem fs = FileSystems.getDefault();
    
    // Is basic view supported of : true
    // Is basic view supported of : true
    // Is basic view supported of : true
    // Is basic view supported of KINGSTON: true
    for (FileStore s : fs.getFileStores()) {
      System.out.println("Is basic view supported of " + s.name() + ": "
          + s.supportsFileAttributeView(BasicFileAttributeView.class));
    }
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    boolean supported = Files.getFileStore(path).supportsFileAttributeView("basic");
    // true
    System.out.println(supported);
    
  }
  
  @Test
  public void readAttributes() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    
    BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
    
    // sun.nio.fs.WindowsFileAttributes@42110406 (Windows)
    System.out.println(attributes);
    
    System.out.println(attributes.size());
    System.out.println(attributes.creationTime());
    System.out.println(attributes.lastAccessTime());
    System.out.println(attributes.lastModifiedTime());
    System.out.println(attributes.isDirectory());
    System.out.println(attributes.isRegularFile());
    System.out.println(attributes.isSymbolicLink());
    System.out.println(attributes.isOther());
    
    // Get single attribute
    Object size = Files.getAttribute(path, "basic:size");
    System.out.println(size);
  }
  
  @Test
  public void updateAttribute() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    
    // Update create time
    Files.getFileAttributeView(path, BasicFileAttributeView.class)
      .setTimes(null, null, FileTime.fromMillis(System.currentTimeMillis()));
    
    // Update last modified time
    Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
    
    FileTime time = (FileTime) Files.getAttribute(path, "basic:lastModifiedTime");
    System.out.println(time);
  }
  
  @Test
  public void setOwner() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    
    UserPrincipal principal = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("gaigeshen");
    Files.setOwner(path, principal);
    
    principal = (UserPrincipal) Files.getAttribute(path, "owner:owner");
    // GAIGESHEN-THINK\gaigeshen (User)
    System.out.println(principal);
  }
  
  @Test
  public void fileStoreAttributes() throws Exception {
    
    FileSystem fs = FileSystems.getDefault();
    for (FileStore store : fs.getFileStores()) {
      System.out.println("store name: " + store.name());
      System.out.println("store type: " + store.type());
      System.out.println("total space: " + store.getTotalSpace() / 1024 / 1024 / 1024);
      System.out.println("used space: " + (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024 / 1024 / 1024);
      System.out.println("available space: " + store.getUsableSpace() / 1024 / 1024 / 1024);
      
      System.out.println("======================");
    }
    
    // Also can get file store from path object
    // Files.getFileStore(path)
  }
  
  @Test
  public void userDefineAttribute() throws Exception {
    
    Path path = Paths.get("E:\\workspace\\pro-java7-nio");
    UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
    
    // Write user attribute
    view.write("file.desc", Charset.defaultCharset().encode("This file contains private information!"));
    
    // List user attribute
    // 39 file.desc
    for (String name: view.list()) {
      System.out.println(view.size(name) + " " + name);
    }
    
    // Delete user attribute
    view.delete("file.desc"); 
  }
}
