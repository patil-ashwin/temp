import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

public class FileWatchService {
    private static final Logger LOGGER = Logger.getLogger(FileWatchService.class.getName());

    public void watchDirectory(Path directory) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        LOGGER.info("Watching directory: " + directory);

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                LOGGER.warning("Interrupted while waiting for file events: " + e.getMessage());
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    LOGGER.warning("Overflow occurred, some events might have been lost.");
                    continue;
                }

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path createdFile = ev.context();
                    LOGGER.info("New file created: " + createdFile);
                    // Handle the new file here (e.g., process, read, etc.)
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                LOGGER.warning("WatchKey no longer valid, stopping watch service for directory: " + directory);
                break;
            }
        }
    }
}



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class FileWatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileWatcherApplication.class, args);

        // Example usage: Watch a directory for file creation
        FileWatchService fileWatchService = new FileWatchService();
        try {
            Path directoryToWatch = Paths.get("path/to/your/directory");
            fileWatchService.watchDirectory(directoryToWatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

