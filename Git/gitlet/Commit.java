package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a gitlet commit object.
 * does at a high level.
 *
 * @author Keshav Singhal
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private String parent;
    private String id;
    private String message;
    private String timeStamp;

    private HashMap<String, String> idMap = new HashMap<>();


    public Commit() {
        /** The message of this Commit. */
        this.message = "initial commit";

        this.parent = null;
        timeStamp =
                "Thu Jan 01 05:30:00 1970 -0800";
        this.id = generateID();
        idMap = new HashMap<>();
    }

    public Commit(
            String parent, String message, HashMap<String, String> map) {
        this.parent = parent;
        this.message = message;
        this.id = generateID();
        idMap = map;
        Date current = new Date();
        timeStamp = dateToTime(current);
    }

    public String dateToTime(Date time) {
        SimpleDateFormat formatted =
                new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        String timeStamp2 = formatted.format(time);
        return timeStamp2;
    }

    public static File removeFile(String fileID, File[] fileList) {
        for (File check : fileList) {
            if (fileID.equals(check.getName())) {
                return check;
            }
        }
        return null;
    }



    public String getPath(String sha1ID) {
        for (String check : idMap.keySet()) {
            if (idMap.get(check).equals(sha1ID)) {
                return check;
            }
        }
        return "";
    }

    public void setTimeStamp() {
        Date now = new Date();
        this.timeStamp = dateToTime(now);
    }

    private String generateID() {
        return Utils.sha1(Utils.serialize(this));
    }

    public String getID() {
        return this.id;
    }

    public void setID() {
        this.id = generateID();
    }

    public boolean checkID(String filename) {
        return getIdMap().get(filename) != null;
    }
    public String getParent() {
        return this.parent;
    }

    public void setParent(String parentName) {
        this.parent = parentName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage() {
        this.message = message;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public HashMap<String, String> getIdMap() {
        return this.idMap;
    }


}
