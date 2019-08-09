package app;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;


import java.awt.event.ActionEvent;
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

    Map<Integer, MFile> doubles;


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
    protected Text fileNr;

    @FXML
    protected Text doubleNr;

    @FXML
    protected TextField directoryField;


    @FXML
    protected void openDir(){
        Window stage = loadDirState.getScene().getWindow();


        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory.");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        TextArea textArea = new TextArea();
        textArea.setMinHeight(70);

        File dir = directoryChooser.showDialog(stage);
        if (dir != null){
            directoryField.setText(dir.getAbsolutePath());
        }else{
            //textArea.setText(null);
        }
    }


    @FXML
    protected void loadDir() {

        Task<Void> loadDirTask = new Task<Void>() {
            @Override
            public Void call() {

                loadDirState.setText("");
                calcMd5State.setText("");
                sortFileState.setText("");
                findDuplicateState.setText("");
                delDuplicateState.setText("");
                fileNr.setText("Number of Files:");
                doubleNr.setText("Number of Duplicates:");


                Path path = Paths.get(directoryField.getText());

                if (!Files.isDirectory(path)) {
                    loadDirState.setText("ERROR.");

                } else {

                    Map<Integer, File> pool = new Routines().loadPool(directoryField.getText(), "file");
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
                    findDuplicateState.setText("OK.");
                    doubleNr.setText("Number of Duplicates:  " + doubles.size());

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
            public Void call()  {

                for (int i = 0; i < doubles.size(); i++) {
                    new Execute().execute(new String[]{"rm", doubles.get(i).file.getAbsolutePath()});

                }
                delDuplicateState.setText("OK.");
                return null;
            }
        };
        new Thread(delDuplicateTask).start();
    }


}
