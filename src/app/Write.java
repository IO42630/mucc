package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import app.Artifacts.MFile;

public class Write {

    /**
	 * writes <b>text</b> to file at <b>path</b>
	 * <p>
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
	 * append all elements of <b>pool</b> to StringBuilder and write to
	 * <b>/tmp/name</b>
	 * <p>
	 *
	 * @param pool
	 */
	public void textPool(String name, Map<Integer, File> pool) {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < pool.size(); i++) {
			text.append(i + " " + pool.get(i) + "\n");
		}
		textFile("/tmp/" + name, text);
	}


	/**
	 * append all elements of <b>md5Pool</b> to StringBuilder and write to
	 * <b>/tmp/name</b>
	 * <p>
	 *
	 * @param md5Pool
	 */
	public void textMd5Pool(String name, Map<Integer, MFile> md5Pool) {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < md5Pool.size(); i++) {
			text.append(i + " " + md5Pool.get(i).md5 + " " + md5Pool.get(i).file + "\n");
		}
		textFile("/tmp/" + name, text);
	}
}
