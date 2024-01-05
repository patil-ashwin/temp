import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileWatchService {

    public Mono<Void> watchDirectory(Path directory) {
        return Mono.fromCallable(() -> {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for file events: " + e.getMessage());
                }

                try {
                    key.pollEvents().stream()
                            .filter(event -> event.kind() != StandardWatchEventKinds.OVERFLOW)
                            .map(event -> (WatchEvent<Path>) event)
                            .filter(ev -> ev.kind() == StandardWatchEventKinds.ENTRY_CREATE)
                            .forEach(ev -> {
                                Path createdFile = ev.context();
                                System.out.println("New file created: " + createdFile);
                                // Handle the new file here (e.g., process, read, etc.)
                            });
                } catch (Exception e) {
                    throw new RuntimeException("Error processing file events: " + e.getMessage());
                }

                if (!key.reset()) {
                    throw new RuntimeException("WatchKey no longer valid, stopping watch service for directory: " + directory);
                }
            }
        }).subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    System.err.println("Error occurred: " + e.getMessage());
                    return Mono.empty(); // Return an empty Mono on error
                })
                .doOnError(e -> System.err.println("Unhandled error occurred: " + e.getMessage()))
                .then();
    }
}
