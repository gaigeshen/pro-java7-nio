package me.gaigeshen.projava7.nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 10/02 2018
 */
public class RecursiveOperationsTest {

  @Test
  public void walkFileTree() throws Exception {
    
    Path path = Paths.get("E:/workspace/pro-java7-nio");
    MyFileVisitor visitor = new MyFileVisitor();
    
    Files.walkFileTree(path, visitor);
  }
  
  @Test
  public void searchFile() throws Exception {
    Path root = Paths.get("E:/workspace");
    
    Path path = Paths.get("RecursiveOperationsTest.java");
    Search search = new Search(path);
    
    Files.walkFileTree(root, search);
    
    System.out.println(search.found());
  }
  
  @Test
  public void searchFileByMatcher() throws Exception {
    Files.walkFileTree(Paths.get("E:/workspace"),
        new PathMatcherSearch(FileSystems.getDefault().getPathMatcher("glob:*.java")));
  }
}

class MyFileVisitor extends SimpleFileVisitor<Path> {

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    System.out.println("Visit file: " + file);
    return super.visitFile(file, attrs);
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return super.visitFileFailed(file, exc);
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    System.out.println("After visit... " + dir);
    return super.postVisitDirectory(dir, exc);
  }
}

class Search implements FileVisitor<Path> {

  private final Path searched;
  private boolean found;
  
  public Search(Path searched) {
    this.searched = searched;
    this.found = false;
  }
  
  public Path found() {
    if (!found) {
      System.out.println(searched + " not found");
      return null;
    }
    return searched;
  }
  
  private void search(Path file) throws IOException {
    Path name = file.getFileName();
    if (name != null && name.equals(searched)) {
      System.out.println("Searched file was found: " + searched + " in " + file.toRealPath());
      found = true;
    }
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    search(file);
    if (!found) {
      return FileVisitResult.CONTINUE;
    }
    return FileVisitResult.TERMINATE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }
  
}

class PathMatcherSearch implements FileVisitor<Path> {

  private final PathMatcher matcher;
  
  public PathMatcherSearch(PathMatcher matcher) {
    this.matcher = matcher;
  }
  
  private void search(Path file) throws IOException {
    Path name = file.getFileName();
    if (name != null && matcher.matches(name)) {
      System.out.println("Searched file was found: " + name + " in " + file.toRealPath());
    }
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    search(file);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }
  
}
