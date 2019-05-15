/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.service.ImageResourceApplicationService;

/**
 *
 * @author bartosz.szymborski
 */
@Component
@Scope("view")
public class AuctionItemPhotosView {

    public static final String SERIAL_NUMBER = "SERIAL_NUMBER";

    private boolean iterDirection;

    @Autowired
    private ImageResourceApplicationService imgService;

    private String serialNumber;
    private ListIterator<String> photosIterator;
    private StreamedContent photo;

    @PostConstruct
    public void init() {
        final Map<String, String> requestParameterMap = PrimeFacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        serialNumber = requestParameterMap.get(SERIAL_NUMBER);
        String equipType = requestParameterMap.get(EquipType.class.getSimpleName());
        if (serialNumber == null) {
            return;
        }

        EquipType et = EquipType.valueOf(equipType);
        String photosFolder = et.getPathChunk() + "\\" + serialNumber;

        List<String> itemImages = imgService.getImageNamesForItem(photosFolder);
        if(itemImages.isEmpty()) return;
        photosIterator = itemImages.listIterator();
        photo = imgService.getAuctionItemPhoto(photosIterator.next());
        iterDirection = true;
    }

    public StreamedContent getPhoto() {
        return photo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setPhoto(StreamedContent photo) {
        this.photo = photo;
    }

    public int getPhotoWidth() {
        return 1024;
    }

    public int getPhotoHeight() {
        return 768;
    }

    public void prevPhoto() {
        String phot = null;
        if (photosIterator.hasPrevious()) {
            phot = photosIterator.previous();
            if (iterDirection) {
                iterDirection = false;
                prevPhoto();
                return;
            }
        } else {
            while (photosIterator.hasNext()) {
                phot = photosIterator.next();
            }
            photosIterator.previous();
        }
        photo = imgService.getAuctionItemPhoto(phot);
        iterDirection = false;
    }

    public void nextPhoto() {
        String phot = null;
        if (photosIterator.hasNext()) {
            phot = photosIterator.next();
            if (!iterDirection) {
                iterDirection = true;
                nextPhoto();
                return;
            }
        } else {
            while (photosIterator.hasPrevious()) {
                phot = photosIterator.previous();
            }
            photosIterator.next();
        }
        photo = imgService.getAuctionItemPhoto(phot);
        iterDirection = true;
    }

}
