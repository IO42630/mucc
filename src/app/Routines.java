package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.Artifacts.MFile;

class Routines {

    private final Execute x;


    public Routines() {
        this.x = new Execute();
    }


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
        x.execute(new String[]{System.getProperty("user.dir") + "/src/app/toFile.sh", "find", srcdir, "/tmp/find"});
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

    /**
     * Calculate md5 for each file in <b>pool</b> .
     *
     * @param pool Map< Integer, File />
     * @return Map<                Integer               ,                               MFile                               />
     */
    public Map<Integer, MFile> md5Pool(Map<Integer, File> pool) {
        Map<Integer, MFile> md5Pool = new HashMap<>();
        for (int i = 0; i < pool.size(); i++) {
            File file = pool.get(i);
            md5Pool.put(i, new Artifacts().getMFile(file));
        }
        return md5Pool;
    }

    /**
     * @param md5Pool Map< Integer, MFile /> , a map containing files and their md5.
     * @return Map<        Integer       ,               MFile               /> of duplicates contained in <b>md5Pool</b>
     */
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
