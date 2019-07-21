package app;

import java.io.File;

public class Artifacts {

    private Tools tools = new Tools();

    /**
     * @param file <i>File</i>
     * @return file with md5 field
     */
    public MFile getMFile(File file) {
        MFile mfile = new MFile();
        mfile.file = file;
        mfile.md5 = tools.getMd5(file.getPath());
        return mfile;
    }

    /**
     *
     */
    public class MFile {
        public File file;
        public String md5;
    }

}
