package app.routines;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.Artifacts;
import app.Artifacts.MFile;
import app.Execute;

public class DeleteDuplicates {

    private final Execute x;


    public DeleteDuplicates() {
        this.x = new Execute();
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
