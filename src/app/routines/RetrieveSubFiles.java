package app.routines;

import app.Execute;
import app.Tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetrieveSubFiles {

    private final Execute x;
    private final Tools tools;


    public RetrieveSubFiles() {
        this.x = new Execute();
        this.tools = new Tools();
    }





    /**
     * @param pool Map< Integer, File /> , a map containing files.
     * @return create subfiles  on disk.                                                                                                                                                                                                                                                            MFile                                                                                                                                                                                                                                                               /> of duplicates contained in <b>md5Pool</b>
     */
    public int pdf_method(Map<Integer, File> pool) {

        int files_created = 0;

        for (int i = 0; i < pool.size(); i++) {

            File file = pool.get(i);
            String f_path = file.getAbsolutePath();
            if (f_path.endsWith("pdf")) {
                // -n is for adding line numbers
                // -a is for accepting binary input
                // -T is for preserving tabs, this helps in some special cases.
                String[] cmd = new String[]{System.getProperty("user.dir") + "/src/app/shell/pipe.sh",
                                            "cat " + f_path,
                                            "grep -naT %PDF"};
                String pdf = tools.brToString(x.execute(cmd).output);
                // because -T was used, we now must use regex to extract the '1234:' tag
                String pdf_lines = tools.matchRegEx(pdf, "[0-9]+:");

                cmd = new String[]{System.getProperty("user.dir") + "/src/app/shell/pipe.sh",
                                   "cat " + f_path,
                                   "grep -naT %%EOF"};
                String eof = tools.brToString(x.execute(cmd).output);
                String eof_lines = tools.matchRegEx(eof, "[0-9]+:");

                // TODO because of PDF tags having 'error char' instad of line nums, the # of PDF tags < # EOF tags
                // TODO fix this by maybe making a grep of grep
                List<String> pdf_list = pdf_list(pdf_lines, eof_lines);
                int adf = 3;

                for (int j = 0; j < pdf_list.size(); j++) {
                    String sub_f_name = file.getName().split("\\.")[0] + "-sub" + j + ".pdf";
                    String[] split = pdf_list.get(j).split(":");

                    cmd = new String[]{System.getProperty("user.dir") + "/src/app/shell/fileCut.sh",
                                       f_path,
                                       split[0],
                                       split[1],
                                       file.getParent() + "/" + sub_f_name};
                    x.execute(cmd);
                }

                files_created += pdf_list.size();
                // x.execute(new String[]{"rm", f_path});

            } else {
                // Do nothing. File is either not a PDF,
                // or contains no usable %PDF and %%EOF sequences.
            }
        }
        return files_created;
    }


    /**
     * pdf_matrix contains rows for each subfile as hinted by <i>params</i>:<br>
     * [ %PDF line ; %%EOF line ; %PDF-version ; %%EOF ]
     *
     * @param pdf Record of lines containing the %PDF sequence.
     * @param eof Record of lines containing the %%EOF sequence.
     * @return pdf_matrix, see above.
     */
    public List<String> pdf_list(String pdf, String eof) {
        List<String> list = new ArrayList<String>();

        String[] pdf_list = pdf.split("\n");
        String[] eof_list = eof.split("\n");

        if (pdf_list.length == eof_list.length) {
            for (int j = 0; j < pdf_list.length; j++) {
                String pdf_tag_line = pdf_list[j].split(":")[0];
                String eof_tag_line = eof_list[j].split(":")[0];
                list.add(pdf_tag_line + ":" + eof_tag_line);
            }
        } else {
            throw new Error("Number of %PDF tags does not match the number %%EOF tags. Skipping this file.");
        }
        return list;
    }

}
