package app.routines;

import app.Execute;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilePool {

    private final Execute x = new Execute();

    /**
     * [1] Write output of <b>find srcdir</b> to <b>/tmp/find</b> .<br>
     * [2] Read <b>/tmp/find</b> into <b>List< String /></b> .<br>
     * [3] Add <b>List< String /></b> entries to <b>Map>String,File></b> , where
     * <b>String</b> is an <b>int</b> key. <br>
     *
     * @param srcdir <i>String</i>
     * @param type   <i>String</i>  "file" OR "dir" , pick what type will be loaded.
     * @return filepool
     */
    public Map<Integer, File> loadPool(String srcdir, String type) {
        // [1]
        x.execute(new String[]{System.getProperty("user.dir") + "/src/app/shell/toFile.sh", "find", srcdir, "/tmp/find"});
        // [2]
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/tmp/find"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // [3]
        Map<Integer, File> filepool = new HashMap<>();
        int j = 0;
        for (String line : lines) {
            File file = new File(line);
            if (type == "dir" && file.isDirectory() || type == "file" && file.isFile()) {
                filepool.put(j, file);
                j++;
            }
        }
        return filepool;
    }
}
