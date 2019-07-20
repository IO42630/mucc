package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.Artifacts.MFile;

public class Routines {


    Execute x = null;

    Tools tools = new Tools();
    Write write = new Write();


    public Routines() {
        this.x = new Execute();

        this.tools = new Tools();
    }


    /**
     * [1] Write output of <b>find srcdir</b> to <b>/tmp/find</b> <br>
     * [2] Read <b>/tmp/find</b> into <b>List>String></b> <br>
     * [3] Add <b>List>String></b> entries to <b>Map>String,File></b> , where
     * <b>String</b> is an <b>int</b> key. <br>
     *
     * @param srcdir
     * @param type   file OR directory
     * @return filepool
     */
    public Map<Integer, File> loadPool(String srcdir, String type) {
        // [1]
        x.execute(new String[]{System.getProperty("user.dir") + "/src/app/toFile.sh", "find", srcdir, "/tmp/find"});
        // [2]
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/tmp/find"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // [3]
        Map<Integer, File> filepool = new HashMap<Integer, File>();
        int j = 0;
        for (int i = 0; i < lines.size(); i++) {
            File file = new File(lines.get(i));
            if (type == "directory" && file.isDirectory() || type == "file" && file.isFile()) {
                filepool.put(j, file);
                j++;
            }
        }
        return filepool;
    }


    public Map<Integer, MFile> md5Pool(Map<Integer, File> pool) {
        Map<Integer, MFile> md5Pool = new HashMap<>();
        for (int i = 0; i < pool.size(); i++) {
            File file = pool.get(i);
            md5Pool.put(i, new Artifacts().getMFile(file));
        }
        return md5Pool;
    }


    public Map<Integer, MFile> doubles(Map<Integer, MFile> md5Pool) {
        Map<Integer, MFile> doubles = new HashMap<>();
        int d = 0;
        for (int i = 0; i < md5Pool.size() - 1; i++) {
            MFile iF = md5Pool.get(i);
            MFile jF = md5Pool.get(i + 1);
            if (iF.md5.equals(jF.md5)) {
                if (iF.file.lastModified() >= jF.file.lastModified()) {
                    doubles.put(d, iF);
                    d++;
                } else {
                    doubles.put(d, jF);
                    d++;
                }
            }
        }
        return doubles;
    }

}
