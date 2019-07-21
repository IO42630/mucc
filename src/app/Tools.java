package app;

import java.io.BufferedReader;
import java.io.IOException;

class Tools {

    private final Execute x;

    public Tools() {
        x = new Execute();
    }


    /**
     * @param path <i>String</i>
     * @return Md5 of File at <b>path</b>
     */
    public String getMd5(String path) {
        // output of md5sum is "md5 filepath"
        BufferedReader md5reader = x.execute(new String[]{"md5sum", path}).output;
        String md5 = null;
        try {
            md5 = md5reader.readLine().split(" ")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
