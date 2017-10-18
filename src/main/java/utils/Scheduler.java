package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by c-denipost on 18-Oct-17.
 **/
public class Scheduler {

    private Map<Path, ScheduledFuture> futures = new HashMap<>();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private static final TimeUnit UNITS = TimeUnit.SECONDS;

    public void scheduleForDeletion(Path path, long delay) {
        ScheduledFuture future = executor.schedule(() -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                // failed to delete
            }
        }, delay, UNITS);

        futures.put(path, future);
    }

}
