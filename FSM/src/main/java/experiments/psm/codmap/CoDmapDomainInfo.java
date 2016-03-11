package experiments.psm.codmap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import experiments.psm.DomainInfo;
import experiments.psm.file.FileMultiAgentProblem;

public class CoDmapDomainInfo implements DomainInfo {

    private File[] problemDirs;

    private String domainName;

    public CoDmapDomainInfo(String domainName, String domainDir) {
        this.domainName = domainName;
        problemDirs = sort(getSubdirs(domainDir));
    }

    public File[] getSubdirs(String domainDir) {
        File[] files = new File(domainDir).listFiles();
        List<File> subdirs = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                subdirs.add(file);
            }
        }
        return subdirs.toArray(new File[0]);
    }

    private File[] sort(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return sizeof(o1) - sizeof(o2);
            }
        });
        return files;
    }

    private int sizeof(File directory) {
        int size = 0;
        for (File file : directory.listFiles()) {
            size += file.length();
        }
        return size;
    }

    @Override
    public FileMultiAgentProblem createProblem(int problemNo) throws Exception {
        System.out.println("problemDir: " + problemDirs[problemNo]);
        return new FileMultiAgentProblem( problemDirs[problemNo] );
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public int getProblemCount() {
        return problemDirs.length;
    }

}
