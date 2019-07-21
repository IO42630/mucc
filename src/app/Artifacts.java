package app;

import java.io.File;

public class Artifacts {

    Tools tools = new Tools();

    /**
     * @param file
     * @return
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
