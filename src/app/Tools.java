package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

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


       public String brToString(BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        Object[] br_array = br.lines().toArray();
        for (int i = 0; i < br_array.length; i++) {
            sb.append(br_array[i].toString() + "\n");
        }
        return sb.toString();
    }

    /**
     *
     * @param input input String
     * @param regex pattern String
     * @return matches for pattern, separated by \n
     */
    public String matchRegEx(String input, String regex){

        Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(input);
            StringBuilder sb = new StringBuilder();
            while (m.find()){
                //
                sb.append(m.group()+"\n");
            }

        return sb.toString();
    }
}
