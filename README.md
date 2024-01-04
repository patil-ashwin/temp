import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Component
public class FileWatcher {

    @Value("${directory.path}")
    private String directoryPath; // Path to the directory you want to watch

    @EventListener(ContextRefreshedEvent.class)
    public void watch() throws IOException {
        Path directory = Paths.get(directoryPath);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        Flux.<WatchEvent<?>>create(emitter -> {
            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    emitter.error(e);
                    return;
                }

                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> event : events) {
                    emitter.next(event);
                }
                key.reset();
            }
        }).subscribe(event -> {
            // Handle the file creation event
            System.out.println("New file created: " + event.context());
        });
    }
}
