package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import app.Artifacts.MFile;

public class Write {

    /**
     * Write <b>text</b> to file at <b>path</b> .
     *
     * @param path <i>String</i>
     * @param text <i>StringBuilder</i>
     */
    public void textFile(String path, StringBuilder text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
            bw.write(text.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Append all elements of <b>pool</b> to <i>StringBuilder</i>
     * and write to /tmp/<b>name</b> .
     *
     * @param name <i>String</i>
     * @param pool <i>Map< Integer, File /></i>
     */
    public void textPool(String name, Map<Integer, File> pool) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < pool.size(); i++) {
            text.append(i + " " + pool.get(i) + "\n");
        }
        textFile("/tmp/" + name, text);
    }


    /**
     * Append all elements of <b>pool</b> to <i>StringBuilder</i>
     * and write to /tmp/<b>name</b> .
     *
     * @param name    <i>String</i>
     * @param md5Pool <i>Map< Integer, MFile /></i>
     */
    public void textMd5Pool(String name, Map<Integer, MFile> md5Pool) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < md5Pool.size(); i++) {
            text.append(i + " " + md5Pool.get(i).md5 + " " + md5Pool.get(i).file + "\n");
        }
        textFile("/tmp/" + name, text);
    }
}
