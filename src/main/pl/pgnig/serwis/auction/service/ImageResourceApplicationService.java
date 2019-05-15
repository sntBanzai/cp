/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.apache.commons.compress.utils.IOUtils;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pgnig.serwis.auction.view.EquipType;

/**
 *
 * @author a6jmalyszko
 */
@Service
@ApplicationScope
public class ImageResourceApplicationService implements InitializingBean {

    public static final String IMAGESACKNOWLEDGEMENT_PHOTO2JPG = "/images/acknowledgementPhoto2.jpg";
    public static final String IMAGESACKNOWLEDGEMENT_PHOTO1JPG = "/images/acknowledgementPhoto1.jpg";
    public static final String LOGOPGNiG_JPG = "/images/logoPGNiG.jpg";
    public static final String EXCELLOGO_JPG = "/images/excel-logo.jpg";
    public static final String FAVICON_ICO = "/images/favicon.ico";
    public static final String LOGOPGNiG_CID = "logoPGNiG.jpg";
    public static final String LOCATION_PARAM_KEY = "pl.pgnig.serwis.auction.AUCTION_IMAGES_LOCATION";

    public static final String IMAGEJPG = "image/jpg";
    public static final String IMAGEICO = "image/ico";

    private ByteBuffer imgOne;
    private ByteBuffer imgTwo;
    private ByteBuffer imgLogo;
    private ByteBuffer imgICO;
    private ByteBuffer imgExcl;

    @Autowired
    private ImageResourceFileWatcher resourceWatcher;

    private final Map<String, ByteBuffer> auctionItemImages = new ConcurrentHashMap<>();
    private final Map<String, File> imgTempFiles = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        try ( InputStream resourceAsStream1 = getClass().getResourceAsStream(IMAGESACKNOWLEDGEMENT_PHOTO1JPG);  InputStream resourceAsStream2 = getClass().getResourceAsStream(IMAGESACKNOWLEDGEMENT_PHOTO2JPG);  InputStream logoPGNiG = getClass().getResourceAsStream(LOGOPGNiG_JPG);  InputStream logoICO = getClass().getResourceAsStream(FAVICON_ICO)) {
            imgOne = ByteBuffer.wrap(IOUtils.toByteArray(resourceAsStream1));
            imgTwo = ByteBuffer.wrap(IOUtils.toByteArray(resourceAsStream2));
            imgLogo = ByteBuffer.wrap(IOUtils.toByteArray(logoPGNiG));
            imgICO = ByteBuffer.wrap(IOUtils.toByteArray(logoICO));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public StreamedContent getAcknowledgementImageOne() {
        return streamize(imgOne, IMAGEJPG);
    }

    public StreamedContent getAcknowledgementImageTwo() {
        return streamize(imgTwo, IMAGEJPG);
    }

    public StreamedContent getLogoPGNiG() {
        return streamize(imgLogo, IMAGEJPG);
    }

    public StreamedContent getFavi() {
        return streamize(imgICO, IMAGEICO);
    }

    public StreamedContent getAuctionItemPhoto(String filePath) {
        final Optional<ByteBuffer> stepOne = Optional.ofNullable(auctionItemImages.get(filePath));
        Optional<ByteBuffer> imgByteArray = Optional.ofNullable(stepOne.orElseGet(() -> getAuctionItemPhotoByteArray(filePath)));
        return streamize(imgByteArray.orElse(imgLogo), IMAGEJPG);
    }

    private ByteBuffer getAuctionItemPhotoByteArray(String filePath) {
        File file = new File(filePath);
        try ( FileChannel fc = FileChannel.open(file.toPath())) {
            ByteBuffer buff = ByteBuffer.allocate((int) file.length());
            fc.read(buff);
            auctionItemImages.put(file.getAbsolutePath(), buff);
            return buff;
        } catch (IOException ex) {
            Logger.getLogger(ImageResourceApplicationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private StreamedContent streamize(ByteBuffer imgByteArray, String imageFormat) {
        return new ByteArrayContent(imgByteArray.array().clone(), imageFormat);
    }

    public File getFileLogoPGNiG() {
        File logoFile = imgTempFiles.get(LOGOPGNiG_JPG);
        if (logoFile == null) {
            try {
                File tempFile = File.createTempFile("logo", ".jpg");
                FileCopyUtils.copy(imgLogo.array().clone(), tempFile);
                tempFile.deleteOnExit();
                imgTempFiles.put(LOGOPGNiG_JPG, tempFile);
                logoFile = tempFile;
            } catch (IOException ex) {
                Logger.getLogger(ImageResourceApplicationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return logoFile;
    }

    @Async
    @Override
    public void afterPropertiesSet() throws Exception {
        final Logger logger = Logger.getLogger(ImageResourceApplicationService.class.getName());
        String location = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(LOCATION_PARAM_KEY);
        try {
            logger.log(Level.INFO, "afterPropertiesSet()");
            Path startPath = Paths.get(location);
            Set<String> files = Files.walk(startPath)
                    .filter(path -> path.toFile().isFile())
                    .map(path -> path.toFile().getAbsolutePath()).collect(Collectors.toSet());
            for (String file : files) {
                getAuctionItemPhotoByteArray(file);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        Set<Path> pathsToWatch = Stream.of(EquipType.values()).map(et -> Paths.get(location + "\\" + et.getPathChunk())).collect(Collectors.toSet());
        for (Path watchPath : pathsToWatch) {
            resourceWatcher.watchForChange(watchPath);
        }
    }

    public List<String> getImageNamesForItem(String photosFolder) {
        return auctionItemImages.keySet().stream().filter(path -> path.contains(photosFolder)).collect(Collectors.toList());
    }

    public void deleteImageResources(String absolutePath) {
        Logger.getLogger(this.getClass().toString()).log(Level.INFO, "deleteImageResources");
        Set<String> deleteItems = auctionItemImages.keySet().stream().filter(key -> key.startsWith(absolutePath)).collect(Collectors.toSet());
        for (String item : deleteItems) {
            auctionItemImages.remove(item);
        }
    }

    public void replaceResourceMapping(String deletedPath, String newPath) throws IOException {
        deleteImageResources(deletedPath);
        scanResourceFolder(newPath);
    }

    public void scanResourceFolder(String absolutePath) throws IOException {
        Logger.getLogger(this.getClass().toString()).log(Level.INFO, "scanResourceFolder(" + absolutePath + ')');
        Iterator<Path> iterator = Files.newDirectoryStream(Paths.get(absolutePath)).iterator();
        Set<String> paths = new HashSet<>();
        while (iterator.hasNext()) {
            Path file = iterator.next();
            final String filePath = file.toFile().getAbsolutePath();
            paths.add(filePath);
            getAuctionItemPhotoByteArray(filePath);
        }
        Set<String> delItems = auctionItemImages.keySet().stream().filter(p -> p.startsWith(absolutePath))
                .filter(p -> !paths.contains(p)).collect(Collectors.toSet());
        for (String file : delItems) {
            auctionItemImages.remove(file);
        }
    }

}
