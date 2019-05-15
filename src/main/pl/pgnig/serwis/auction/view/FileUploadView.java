/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.service.AuctionItemService;

/**
 *
 * @author a6jmalyszko
 */
@Component
@Scope("view")
public class FileUploadView {

    @Autowired
    @Qualifier("defaultAuctionItemServiceImpl")
    private AuctionItemService auctionItemService;

    private UploadedFile file;
    private Optional<Workbook> parsedWorkbook = Optional.empty();
    private List<RowWrapper> workbookRows;
    private List<String> columns;

    public void persistInDatabase() throws IOException {
        Map<ExcelImportOutcomeTypes, Set<String>> outcome = auctionItemService.persistExcelInput(parsedWorkbook.get());
        Map<String, List<String>> messagesAsParam = new HashMap<>();
        for (Map.Entry<ExcelImportOutcomeTypes, Set<String>> ent : outcome.entrySet()) {
            messagesAsParam.put(ent.getKey().toString(), Arrays.asList(ent.getValue().stream().reduce("", (x, y) -> x + '|' + y)));
        }
        PrimeFaces.current().dialog().openDynamic("excelImportOutcomeReport", getDialogOptions(), messagesAsParam);
    }

    private Map<String, Object> getDialogOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("height", 400);
        options.put("contentHeight", "100%");
        return options;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void onComplete() {
        InputStream resourceAsStream;
        try {
            resourceAsStream = file.getInputstream();
            parsedWorkbook = Optional.ofNullable(WorkbookFactory.create(resourceAsStream));
            Sheet sheet = parsedWorkbook.orElseThrow(WorkbookParsingException::new).getSheetAt(0);
            Iterator<Row> rowIter = sheet.rowIterator();
            Iterator<Cell> headerIter = rowIter.next().cellIterator();
            columns = new ArrayList<>();
            while (headerIter.hasNext()) {
                String col = headerIter.next().getRichStringCellValue().toString();
                columns.add(col);
            }
            workbookRows = new ArrayList<>();
            while (rowIter.hasNext()) {
                workbookRows.add(new RowWrapper(rowIter.next()));
            }
        } catch (EmptyFileException efe) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak pliku do za³adowania.", null);
            FacesContext.getCurrentInstance().addMessage("messages", message);
        } catch (IOException ex) {
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage message = new FacesMessage("Error occured while trying to upload a file.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void onAbort() {
        parsedWorkbook = Optional.empty();
        columns = null;
        file = null;
        workbookRows = null;
    }

    public void handlePhotoUpload(FileUploadEvent fue) {

    }

    public Optional<Workbook> getParsedWorkbook() {
        return parsedWorkbook;
    }

    public void setParsedWorkbook(Optional<Workbook> parsedWorkbook) {
        this.parsedWorkbook = parsedWorkbook;
    }

    public List<RowWrapper> getWorkbookRows() {
        return workbookRows;
    }

    public List<String> getColumns() {
        return columns;
    }

    static public class RowWrapper {

        private Row wrapped;

        public RowWrapper(Row wrapped) {
            this.wrapped = wrapped;
        }

        public Cell getCell(Integer index) {
            return wrapped.getCell(index.intValue());
        }

    }

    static public class WorkbookParsingException extends RuntimeException {

        public WorkbookParsingException() {
            super("Wczytywanie pliku nie powiod³o siê!");
        }
    }

}
