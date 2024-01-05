import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

public class FileWatchService {
    private static final Logger LOGGER = Logger.getLogger(FileWatchService.class.getName());

    public void watchDirectory(Path directory) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            LOGGER.info("Watching directory: " + directory);

            while (true) {
                WatchKey key = watchService.take();

                key.pollEvents().stream()
                        .filter(event -> event.kind() != StandardWatchEventKinds.OVERFLOW)
                        .map(event -> (WatchEvent<Path>) event)
                        .forEach(ev -> handleFileEvent(ev.context()));

                boolean valid = key.reset();
                if (!valid) {
                    LOGGER.warning("WatchKey no longer valid, stopping watch service for directory: " + directory);
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.warning("Error occurred: " + e.getMessage());
        }
    }

    private void handleFileEvent(Path file) {
        LOGGER.info("New file created: " + file);
        // Handle the new file here (e.g., process, read, etc.)
    }
}
