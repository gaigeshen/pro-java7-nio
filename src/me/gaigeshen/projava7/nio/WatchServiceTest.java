package me.gaigeshen.projava7.nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.junit.Test;

/**
 * 
 * @author gaigeshen
 * @since 10/04 2018
 */
public class WatchServiceTest {

  @Test
  public void test() throws Exception {
    Path path = Paths.get("E:/test");
    WatchRafaelNadal watch = new WatchRafaelNadal();
    watch.watch(path);
  }
  
}

/**
 * Example class
 * 
 * @author gaigeshen
 * @since 10/04 2018
 */
class WatchRafaelNadal {
  /**
   * Watching path
   */
  public void watch(Path path) throws IOException, InterruptedException {
    try (WatchService service = FileSystems.getDefault().newWatchService()) {
      path.register(service,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_MODIFY,
          StandardWatchEventKinds.ENTRY_DELETE);
      while (true) {
        WatchKey key = service.take();
        for (WatchEvent<?> event : key.pollEvents()) {
          Kind<?> kind = event.kind();
          if (kind == StandardWatchEventKinds.OVERFLOW) {
            continue;
          }
          WatchEvent<?> pathEvent = event;
          Path path1 = (Path) pathEvent.context();
          System.out.println(kind + " " + path1);
        }
        boolean valid = key.reset();
        // Exit loop if the key is not vaid
        // If the directory was deleted, for example.
        if (!valid) {
          break;
        }
      }
    }
  }
}