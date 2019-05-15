/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 *
 * @author jerzy.malyszko
 */
@Service
@ApplicationScope
public class ImageResourceFileWatcher {

    @Autowired
    private ImageResourceApplicationService rscService;

    @Async("photosFolderWatcherExecutor")
    public void watchForChange(Path path) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watchKey = null;
            Logger.getLogger(ImageResourceFileWatcher.class.getName()).log(Level.INFO, "Starting to watch " + path.toString());
            while ((watchKey = watchService.take()) != null) {
                List<WatchEvent<?>> pollEvents = watchKey.pollEvents();
                String deletedPath = null;
                for (WatchEvent<?> evnt : pollEvents) {
                    if (evnt.context() instanceof Path) {
                        WatchEvent.Kind<?> kind = evnt.kind();
                        if (StandardWatchEventKinds.ENTRY_CREATE.equals(kind) && deletedPath != null) {
                            rscService.replaceResourceMapping(deletedPath, preparePath(watchKey.watchable(), (Path) evnt.context()));
                        } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
                            final Path pth = (Path) evnt.context();
                            if (pollEvents.size() > 1) {
                                deletedPath = preparePath(watchKey.watchable(), pth);
                            } else {
                                rscService.deleteImageResources(preparePath(watchKey.watchable(), pth));
                            }
                        } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(kind)) {
                            rscService.scanResourceFolder(preparePath(watchKey.watchable(), (Path) evnt.context()));
                        }
                    }
                }
                watchKey.reset();
            }
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ImageResourceFileWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String preparePath(Watchable wa, Path pth) {
        return wa.toString() + "\\" + pth.toString();
    }

}
