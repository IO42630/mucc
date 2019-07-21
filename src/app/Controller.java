package app;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import app.Artifacts.MFile;

/***
 * Controller class for JavaFX. Contains the application logic.
 */
public class Controller {


    Map<Integer, MFile> doubles;


    @FXML
    protected Text loadDirState;

    @FXML
    protected Text calcMd5State;

    @FXML
    protected Text sortFileState;

    @FXML
    protected Text findDoubleState;

    @FXML
    protected Text delDoubleState;

    @FXML
    protected Text fileNr;

    @FXML
    protected Text doubleNr;

    @FXML
    protected TextField dir;


    @FXML
    protected void loadDir() {

        Task<Void> loadDirTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                loadDirState.setText("");
                calcMd5State.setText("");
                sortFileState.setText("");
                findDoubleState.setText("");
                delDoubleState.setText("");
                fileNr.setText("Number of Files:");
                doubleNr.setText("Number of Doubles:");


                Path path = Paths.get(dir.getText());

                if (!Files.isDirectory(path)) {
                    loadDirState.setText("ERROR.");

                } else {

                    Map<Integer, File> pool = new Routines().loadPool(dir.getText(), "file");
                    new Write().textPool("pool", pool);
                    loadDirState.setText("OK.");
                    fileNr.setText("Number of Files:  " + pool.size());

                    Map<Integer, MFile> md5Pool = new Routines().md5Pool(pool);
                    new Write().textMd5Pool("md5Pool", md5Pool);
                    calcMd5State.setText("OK.");

                    Map<Integer, MFile> qsMd5Pool = new QuicksortMd5().quicksortMd5(md5Pool);
                    new Write().textMd5Pool("qsMd5Pool", qsMd5Pool);
                    sortFileState.setText("OK.");

                    doubles = new Routines().doubles(qsMd5Pool);
                    new Write().textMd5Pool("doubles", doubles);
                    findDoubleState.setText("OK.");
                    doubleNr.setText("Number of Doubles:  " + doubles.size());

                }
                return null;
            }
        };
        new Thread(loadDirTask).start();
    }

    @FXML
    protected void deleteDoubles() {

        Task<Void> delDoubleTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                for (int i = 0; i < doubles.size(); i++) {
                    new Execute().execute(new String[]{"rm", doubles.get(i).file.getAbsolutePath()});

                }
                delDoubleState.setText("OK.");
                return null;
            }
        };
        new Thread(delDoubleTask).start();
    }


}
