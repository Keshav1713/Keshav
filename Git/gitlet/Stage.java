package gitlet;

import java.util.*;
import java.io.Serializable;
import java.io.File;

public class Stage implements Serializable {
    private ArrayList<String> removePath;
    private HashMap<String, String> add;
    private ArrayList<String> deletedFiles;

    public Stage() {
        removePath = new ArrayList<>();
        add = new HashMap<>();
        deletedFiles = new ArrayList<>();
    }

    public void addToStage(String file, String sha1) {
        add.put(file, sha1);
    }

    public void removalStage(String file) {
        removePath.add(file);
    }

    public void clean() {
        removePath = new ArrayList<>();
        add = new HashMap<>();
        File freshStagingFile = Utils.join
                (gitlet.Repository.GITLET_STAGE, "stage.txt");
        Utils.writeObject(freshStagingFile, this);
    }

    public void addDeletedFiles(String filename) {
        deletedFiles.add(filename);
    }

    public ArrayList<String> getRemovePath() {
        return removePath;
    }

    public HashMap<String, String> getAdd() {
        return add;
    }

    public ArrayList<String> getDeletedFiles() {
        deletedFiles.sort(String::compareTo);
        return deletedFiles;
    }
}
