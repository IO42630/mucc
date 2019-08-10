package app;

import app.routines.DeleteDuplicates;
import app.routines.FilePool;
import app.routines.RetrieveSubFiles;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import app.Artifacts.MFile;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

/***
 * Controller class for JavaFX. Contains the application logic.
 */
public class Controller {

    private Map<Integer, MFile> doubles;
    private Map<Integer, File> dupblicate_base_pool;
    private Map<Integer, File> pdf_base_pool;


    // Delete Duplicates
    // ----------------------------------------------------------------------------------------------------------------

    @FXML
    protected Text loadDirState;

    @FXML
    protected Text calcMd5State;

    @FXML
    protected Text sortFileState;

    @FXML
    protected Text findDuplicateState;

    @FXML
    protected Text delDuplicateState;

    @FXML
    protected Text fileNrCount;

    @FXML
    protected Text doubleNrCount;

    @FXML
    protected TextField directoryField;

    @FXML
    protected void openDir() {
        Window stage = loadDirState.getScene().getWindow();


        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory.");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = directoryChooser.showDialog(stage);
        if (dir != null) {
            directoryField.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    protected void loadDuplicateDir() {

        Task<Void> loadDirTask = new Task<Void>() {
            @Override
            public Void call() {

                loadDirState.setText("__");
                calcMd5State.setText("__");
                sortFileState.setText("__");
                findDuplicateState.setText("__");
                delDuplicateState.setText("__");
                fileNrCount.setText("__");
                doubleNrCount.setText("__");



                Path path = Paths.get(directoryField.getText());

                if (!Files.isDirectory(path)) {
                    loadDirState.setFill(Color.RED);
                    loadDirState.setText("ERROR.");

                } else {

                    Map<Integer, File> pool = new FilePool().loadPool(directoryField.getText(), "file");
                    new Write().textPool("pool", pool);
                    loadDirState.setFill(Color.GREEN);
                    loadDirState.setText("OK.");
                    fileNrCount.setText("" + pool.size());

                    Map<Integer, MFile> md5Pool = new DeleteDuplicates().md5Pool(pool);
                    new Write().textMd5Pool("md5Pool", md5Pool);
                    calcMd5State.setFill(Color.GREEN);
                    calcMd5State.setText("OK.");

                    Map<Integer, MFile> qsMd5Pool = new QuicksortMd5().quicksortMd5(md5Pool);
                    new Write().textMd5Pool("qsMd5Pool", qsMd5Pool);
                    sortFileState.setFill(Color.GREEN);
                    sortFileState.setText("OK.");

                    doubles = new DeleteDuplicates().doubles(qsMd5Pool);
                    new Write().textMd5Pool("doubles", doubles);
                    findDuplicateState.setFill(Color.GREEN);
                    findDuplicateState.setText("OK.");
                    doubleNrCount.setText("" + doubles.size());

                }
                return null;
            }
        };
        new Thread(loadDirTask).start();
    }


    @FXML
    protected void deleteDuplicates() {

        Task<Void> delDuplicateTask = new Task<Void>() {
            @Override
            public Void call() {

                for (int i = 0; i < doubles.size(); i++) {
                    new Execute().execute(new String[]{"rm", doubles.get(i).file.getAbsolutePath()});
                }
                delDuplicateState.setFill(Color.GREEN);
                delDuplicateState.setText("OK.");
                return null;
            }
        };
        new Thread(delDuplicateTask).start();
    }

    @FXML
    protected void loadBaseFiles() {
    }


    // Retrieve Sub-Files
    // ----------------------------------------------------------------------------------------------------------------

    @FXML
    protected Text loadPdfState;

    @FXML
    protected Text splitPdfState;

    @FXML
    protected Text baseFileCount;

    @FXML
    protected Text subFileCount;

    @FXML
    protected void loadBaseDir() {
        Task<Void> loadDirTask = new Task<Void>() {
            @Override
            public Void call() {

                loadPdfState.setText("__");
                splitPdfState.setText("__");
                baseFileCount.setText("__");
                subFileCount.setText("__");

                Path path = Paths.get(directoryField.getText());

                if (!Files.isDirectory(path)) {
                    loadPdfState.setFill(Color.RED);
                    loadPdfState.setText("ERROR.");
                } else {

                    pdf_base_pool = new FilePool().loadPool(directoryField.getText(), "file");
                    loadPdfState.setFill(Color.GREEN);
                    loadPdfState.setText("OK.");
                    baseFileCount.setText("" + pdf_base_pool.size());
                }
                return null;
            }
        };
        new Thread(loadDirTask).start();
    }

    @FXML
    protected void splitPdf() {

        Task<Void> splitPdfTask = new Task<Void>() {
            @Override
            public Void call() {

                int list_size = new RetrieveSubFiles().pdf_method(pdf_base_pool);
                splitPdfState.setFill(Color.GREEN);
                splitPdfState.setText("OK.");
                subFileCount.setText("" + list_size);
                return null;
            }
        };
        new Thread(splitPdfTask).start();
    }

}
