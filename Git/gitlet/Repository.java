package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;


/**
 * Represents a gitlet repository.
 * does at a high level.
 * @author Keshav Singhal
 */
public class Repository {

    /** Just creating directories. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** Branches directory. */
    public static final File GITLET_BRANCHES =
            Utils.join(GITLET_DIR, "Branches");
    /** Main file. */
    public static final File GITLET_MAIN =
            Utils.join(GITLET_BRANCHES, "main.txt");
    /** Stage directory. */
    public static final File GITLET_STAGE = Utils.join(GITLET_DIR, "stage");
    /** Commits directory. */
    public static final File GITLET_COMMITS = Utils.join(GITLET_DIR, "Commits");
    /** Blobs directory. */
    public static final File GITLET_BLOBS =
            Utils.join(GITLET_DIR, "Blobs");
    /** Head file. */
    public static final File GITLET_HEAD =
            Utils.join(GITLET_BRANCHES, "Head.txt");
    private static final int SECOND_PARENT_INDEX = 41;
    private static final int FIRST_PARENT_END = 40;
    private static final int LOWER_PARENT_LENGTH = 50;
    private static final int LOWER_PARENT_START = 0;
    private Stage stagingArea;
    private String headFile = "main";


    public Repository() {
        String headPath = GITLET_HEAD.getPath();
        File checkFile = new File(headPath);
        if (checkFile.exists()) {
            headFile = Utils.readContentsAsString(checkFile);
        }
        String stagePath = Utils.join(GITLET_STAGE, "stage.txt").getPath();
        File stageFile = new File(stagePath);
        if (stageFile.exists()) {
            stagingArea = Utils.readObject(stageFile, Stage.class);
        }
    }
    /**
     * (Make sure it is also removed from the staged from removal).
     * See if the Current blob can be assigned like this
     * Also create a exit() method if possible */

    public void init() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            GITLET_BLOBS.mkdir();
            GITLET_BRANCHES.mkdir();
            GITLET_COMMITS.mkdir();
            GITLET_STAGE.mkdir();

            Commit firstCommit = new Commit();
            String firstCommitID = firstCommit.getID();
            File first = Utils.join(GITLET_COMMITS, firstCommitID + ".txt");
            Utils.writeObject(first, firstCommit);
            Utils.writeContents(GITLET_MAIN, firstCommitID);
            Utils.writeContents(GITLET_HEAD, "main");

            stagingArea = new Stage();
            String stagePath = Utils.join(GITLET_STAGE, "stage.txt").getPath();
            File stageFile = new File(stagePath);
            Utils.writeObject(stageFile, stagingArea);
        } else {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory."
            );
        }
    }
    public void add(String[] args) {
        initializedDirectory();
        File stageAdd = new File(args[1]);
        String filename = args[1];
        if (!stageAdd.exists()) {
            System.out.print("File does not exist.");
            System.exit(0);
        }
        Commit recentCommit = latestCommit();
        byte[] blobArray = Utils.readContents(stageAdd);
        String blobID = Utils.sha1(blobArray);

        if (recentCommit.checkID(filename)) {
            if (recentCommit.getIdMap().get(filename).equals(blobID)) {
                if (stagingArea.getRemovePath().contains(filename)) {
                    stagingArea.getRemovePath().remove(filename);
                    Utils.writeObject(Utils.join(GITLET_STAGE,
                            "stage.txt"), stagingArea
                    );
                }
                return;
            }
        }

        if (stagingArea.getRemovePath().contains(filename)) {
            stagingArea.getRemovePath().remove(filename);
        }
        File blobFile = Utils.join(GITLET_BLOBS, createTxt(blobID));
        Utils.writeContents(blobFile, blobArray);
        stagingArea.addToStage(filename, blobID);
        Utils.writeObject(Utils.join(GITLET_STAGE, "stage.txt"), stagingArea);
    }


    public Commit latestCommit() {
        String commitFileName =
                Utils.join(GITLET_BRANCHES,
                createTxt(Utils.readContentsAsString(GITLET_HEAD))).getPath();
        File commitFile = new File(commitFileName);
        String currentCommitID = Utils.readContentsAsString(commitFile);
        String currentCommitPath =
                Utils.join(GITLET_COMMITS, currentCommitID + ".txt").getPath();
        File currentCommitFile = new File(currentCommitPath);
        return Utils.readObject(currentCommitFile, Commit.class);
    }
    public static boolean fileCheck(String fileID, File[] fileArray) {
        if (fileArray.length != 0) {
            for (File check : fileArray) {
                if (fileID.equals(check.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void commit(String[] args) {
        initializedDirectory();
        String message = args[1];
        String secondParent = "";
        String finalParent = "";
        if ((getStagingArea().getAdd().size() == 0)
                && (getStagingArea().getRemovePath().size() == 0)) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit recentCommit = latestCommit();

        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else if (args[0].equals("Merge-Commit")) {
            secondParent = args[2];
        }

        HashMap<String, String> commitMap = recentCommit.getIdMap();
        for (String filePath : getStagingArea().getAdd().keySet()) {
            if (commitMap.containsKey(filePath)) {
                commitMap.replace(filePath,
                        getStagingArea().getAdd().get(filePath)
                );
            } else {
                commitMap.put(filePath,
                        getStagingArea().getAdd().get(filePath)
                );
            }
        }
        for (String removeFile : getStagingArea().getRemovePath()) {
            if (commitMap.containsKey(removeFile)) {
                commitMap.remove(removeFile);
            }
        }

        if (secondParent.isEmpty()) {
            finalParent = recentCommit.getID();
        } else {
            finalParent = recentCommit.getID() + " " + secondParent;
        }
        Commit makeCommit = new Commit(finalParent, message, commitMap);
        File topFile = Utils.join
                (GITLET_BRANCHES,
                createTxt(Utils.readContentsAsString(GITLET_HEAD)));
        Utils.writeContents(topFile, makeCommit.getID());
        File commitFile = Utils.join
                (GITLET_COMMITS, makeCommit.getID() + ".txt");
        Utils.writeObject(commitFile, makeCommit);
        getStagingArea().clean();
    }
/**        File FreshStagingFile = Utils.join(GITLET_STAGE, "stage.txt").
        Utils.writeObject(FreshStagingFile, StagingArea);  */

    public void checkoutHead(String[] args) throws IOException {
        initializedDirectory();
        Commit recentCommit = latestCommit();
        String fileName = args[2];
        if (!recentCommit.checkID(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String fileID = createTxt(recentCommit.getIdMap().get(fileName));
        File contentFile = new File(GITLET_BLOBS, fileID);
        byte[] checkoutContent = Utils.readContents(contentFile);
        File checkoutOverwritten = Utils.join(CWD, fileName);
        Utils.writeContents(checkoutOverwritten, checkoutContent);
    }

    public void checkoutID(String[] args) {
        initializedDirectory();
        Commit recentCommit = latestCommit();
        String fileID = args[1];
        String fileName = args[3];
        File committedFile = null;
        for (File x : GITLET_COMMITS.listFiles()) {
            if (x.getName().startsWith(fileID)) {
                committedFile = x;
            }
        }

        if (committedFile == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!args[0].equals("checkout-merge")) {
            if (!recentCommit.checkID(fileName)) {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }
        }

        String checkoutFinalID = committedFile.getName();
        Commit checkoutCommit = Utils.readObject
                (Utils.join(GITLET_COMMITS, checkoutFinalID), Commit.class);
        File commitFile = Utils.join
                (GITLET_BLOBS,
                createTxt(checkoutCommit.getIdMap().get(fileName)));
        byte[] checkoutContent = Utils.readContents(commitFile);
        File checkoutOverwritten = new File(CWD, fileName);
        Utils.writeContents(checkoutOverwritten, checkoutContent);
    }

    public void checkoutBranch(String[] args) {
        initializedDirectory();
        String branchName = createTxt(args[1]);
        String branchToHead = args[1];
        File[] branches = GITLET_BRANCHES.listFiles();
        File checkBranch = null;

        for (File name : branches) {
            if (branchName.equals(name.getName())) {
                checkBranch = name;
            }
        }
        if (checkBranch == null) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        if (createTxt
            (Utils.readContentsAsString(GITLET_HEAD)).equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
        }

        String headBranch = createTxt(Utils.readContentsAsString(checkBranch));
        File branchNewFile = Utils.join(GITLET_COMMITS, headBranch);
        Commit headBranchCommit = Utils.readObject(branchNewFile, Commit.class);
        Commit recentCommit = latestCommit();
        for (String fileName : headBranchCommit.getIdMap().keySet()) {
            File check = new File(fileName);
            if (check.exists()) {
                if (!(recentCommit.getIdMap().containsKey(fileName))
                        && !(getStagingArea().getAdd().containsKey(fileName))
                        && !(getStagingArea().getRemovePath().
                        contains(fileName))) {
                    System.out.println("There is an untracked file "
                            + "in the way; delete it, "
                            + "or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        for (String fileName : recentCommit.getIdMap().keySet()) {
            if (!headBranchCommit.getIdMap().containsKey(fileName)) {
                File toDelete = new File(fileName);
                toDelete.delete();
            }
        }
        for (String fileName : headBranchCommit.getIdMap().keySet()) {
            String fileID = createTxt
                (headBranchCommit.getIdMap().get(fileName));
            File contentFile = new File(GITLET_BLOBS, fileID);
            byte[] checkoutContent = Utils.readContents(contentFile);
            File checkoutOverwritten = Utils.join(CWD, fileName);
            Utils.writeContents(checkoutOverwritten, checkoutContent);
        }
        stagingArea.clean();
        headFile = branchToHead;
        Utils.writeContents(GITLET_HEAD, headFile);
    }

    public void log(String[] args) {
        initializedDirectory();
        Commit recentCommit = latestCommit();
        while (recentCommit != null) {
            System.out.println("===");
            System.out.println("commit " + recentCommit.getID());
            System.out.println("Date: " + recentCommit.getTimeStamp());
            System.out.println(recentCommit.getMessage());
            System.out.println();
            if (recentCommit.getParent() != null) {
                recentCommit = getParentCommit(recentCommit);
            } else {
                break;
            }
        }
    }

    public String createTxt(String name) {
        return name + ".txt";
    }

    public Stage getStagingArea() {
        return stagingArea;
    }

    public Commit getParentCommit(Commit current) {
        String parentID = createTxt(
                current.getParent().substring(
                        LOWER_PARENT_START, FIRST_PARENT_END)
        );
        File parent = Utils.join(GITLET_COMMITS, parentID);
        Commit parentCommit = Utils.readObject(parent, Commit.class);
        return parentCommit;
    }

    public Commit getParentCommit2(Commit current) {
        if (current.getParent().length() > LOWER_PARENT_LENGTH) {
            String mergeParentID = createTxt
                    (current.getParent().substring(SECOND_PARENT_INDEX));
            File parent = Utils.join(GITLET_COMMITS, mergeParentID);
            Commit parentCommit2 = Utils.readObject(parent, Commit.class);
            return parentCommit2;
        }
        return null;
    }

    public void rm(String[] args) {
        initializedDirectory();
        String filename = args[1];
        File removeFile = new File(filename);
        Commit recentCommit = latestCommit();
        if (!args[0].equals("rm-merge")) {
            if (!(getStagingArea().getAdd().containsKey(filename)
                || recentCommit.getIdMap().containsKey(filename))) {
                System.out.println("No reason to remove the file.");
            }
        }

        if (getStagingArea().getAdd().containsKey(filename)) {
            getStagingArea().getAdd().remove(filename);
        }
        if (recentCommit.getIdMap().containsKey(filename)) {
            stagingArea.removalStage(filename);
            if (removeFile.exists()) {
                removeFile.delete();
            }
        }
        File freshStagingFile = Utils.join(GITLET_STAGE, "stage.txt");
        Utils.writeObject(freshStagingFile, stagingArea);
    }

    public File removeFile(String fileName, File[] checkArray) {
        for (File check : checkArray) {
            if (fileName.equals(check.getName())) {
                return check;
            }
        }
        return null;
    }

    public void rmBranch(String[] args) {
        initializedDirectory();
        String branchName = args[1];
        if (!fileCheck(createTxt(branchName), GITLET_BRANCHES.listFiles())) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(Utils.readContentsAsString(GITLET_HEAD))) {
            System.out.println("Cannot remove the current branch.");
        }
        File toRemove =
            removeFile(createTxt(branchName), GITLET_BRANCHES.listFiles());
        toRemove.delete();
    }

    public void status(String[] args) {
        initializedDirectory();
        System.out.println("=== Branches ===");
        File[] branchesFiles = GITLET_BRANCHES.listFiles();
        for (File i : branchesFiles) {
            if (i.getName().startsWith
                (Utils.readContentsAsString(GITLET_HEAD))) {
                System.out.println("*"
                        + Utils.readContentsAsString(GITLET_HEAD));
            } else if (
                    !i.getName().startsWith("Head")) {
                System.out.println(
                        i.getName().substring(0, (i.getName().length() - 4))
                );
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String filename : stagedArray()) {
            System.out.println(filename);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removedFile : getStagingArea().getRemovePath()) {
            System.out.println(removedFile);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public ArrayList<String> stagedArray() {
        ArrayList<String> stagedFiles = new ArrayList<>();
        for (String addFiles : stagingArea.getAdd().keySet()) {
            stagedFiles.add(addFiles);
        }
        stagedFiles.sort(String::compareTo);
        return stagedFiles;
    }

    public void globalLog(String[] args) {
        initializedDirectory();
        File[] commitList = GITLET_COMMITS.listFiles();
        for (File file : commitList) {
            Commit logCommit = Utils.readObject(file, Commit.class);
            System.out.println("===");
            System.out.println("commit " + logCommit.getID());
            System.out.println("Date: " + logCommit.getTimeStamp());
            System.out.println(logCommit.getMessage());
            System.out.println();
        }
    }

    public void find(String[] args) {
        initializedDirectory();
        File[] commitList = GITLET_COMMITS.listFiles();
        String compareMessage = args[1];
        int count = 0;
        for (File file : commitList) {
            Commit findCommit = Utils.readObject(file, Commit.class);
            if (findCommit.getMessage().equals(compareMessage)) {
                count += 1;
                System.out.println(findCommit.getID());
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void reset(String[] args) {
        initializedDirectory();
        String fileID = args[1];
        File committedFile = null;
        for (File x : GITLET_COMMITS.listFiles()) {
            if (x.getName().startsWith(fileID)) {
                committedFile = x;
            }
        }
        if (committedFile == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        String checkoutFinalID = committedFile.getName();
        Commit checkoutCommit = Utils.readObject
            (Utils.join(GITLET_COMMITS, checkoutFinalID), Commit.class);
        Commit recentCommit = latestCommit();
        for (String fileName : checkoutCommit.getIdMap().keySet()) {
            File check = new File(fileName);
            if (check.exists()) {
                if ((!recentCommit.getIdMap().containsKey(fileName))
                        && (!getStagingArea().getAdd().
                        containsKey(fileName))
                        && (!getStagingArea().getRemovePath().
                        contains(fileName))) {
                    System.out.println("There is an untracked file in the way;"
                            + " delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        for (String fileName : recentCommit.getIdMap().keySet()) {
            if (!checkoutCommit.getIdMap().containsKey(fileName)) {
                File toDelete = new File(fileName);
                toDelete.delete();
            }
        }
        for (String fileName : checkoutCommit.getIdMap().keySet()) {
            String fileCheckoutID =
                createTxt(checkoutCommit.getIdMap().get(fileName));
            File contentFile = new File(GITLET_BLOBS, fileCheckoutID);
            byte[] checkoutContent = Utils.readContents(contentFile);
            File checkoutOverwritten = Utils.join(CWD, fileName);
            Utils.writeContents(checkoutOverwritten, checkoutContent);
        }
        getStagingArea().clean();
        File topFile = Utils.join
            (GITLET_BRANCHES, createTxt
                    (Utils.readContentsAsString(GITLET_HEAD)));
        Utils.writeContents(topFile, checkoutCommit.getID());
    }

    public void branch(String[] args) {
        initializedDirectory();
        Commit recentCommit = latestCommit();
        String recentCommitID = recentCommit.getID();
        String branchName = createTxt(args[1]);
        String branchPath = Utils.join(GITLET_BRANCHES, branchName).getPath();
        File secondBranch = new File(branchPath);
        if (secondBranch.exists()) {
            System.out.println("A branch with that name already exists.");
        } else {
            Utils.writeContents(secondBranch, recentCommitID);
        }
    }
    public void merge(String[] args) {
        initializedDirectory();
        String branchName = args[1];
        String branchFile = createTxt(branchName);
        File branchPointer =
            removeFile(branchFile, GITLET_BRANCHES.listFiles());
        mergeBase2(branchPointer);
        String branchCommitID = Utils.readContentsAsString(branchPointer);
        File commitFile =
            removeFile(createTxt(branchCommitID), GITLET_COMMITS.listFiles());
        Commit branchCommit = Utils.readObject(commitFile, Commit.class);
        Commit recentCommit = latestCommit();

        mergeBase1();
        mergeBase3(branchName);
        mergeBase6(recentCommit, branchCommit);

        Commit splitPoint = createSplitPoint(recentCommit, branchCommit);
        mergeBase4(splitPoint, branchCommit);
        mergeBase5(splitPoint, recentCommit, branchName);

        for (String fileCheck : splitPoint.getIdMap().keySet()) {
            if (testModify(splitPoint, branchCommit, fileCheck)) {
                if (!testModify(splitPoint, recentCommit, fileCheck)) {
                    String[] commandsCheckout =
                    {"checkout-merge", branchCommit.getID(), "--", fileCheck};
                    checkoutID(commandsCheckout);
                    String[] commandsAdd = {"add", fileCheck};
                    add(commandsAdd);
                }
            }
        }
        for (String fileName : branchCommit.getIdMap().keySet()) {
            if ((!splitPoint.getIdMap().containsKey(fileName))
                    && (!recentCommit.getIdMap().containsKey(fileName))) {
                String[] commandsCheckout
                = {"checkout-merge", branchCommit.getID(), "--", fileName};
                checkoutID(commandsCheckout);
                String[] commandsAdd = {"add", fileName};
                add(commandsAdd);
            }
        }
        for (String fileTest : splitPoint.getIdMap().keySet()) {
            if ((!testModify(splitPoint, recentCommit, fileTest))
                    && (!branchCommit.getIdMap().containsKey(fileTest))
                    && (recentCommit.getIdMap().containsKey(fileTest))) {
                String[] rmCommands = {"rm-merge", fileTest};
                rm(rmCommands);
            }
        }
        boolean check = mergeConflict(recentCommit, branchCommit, splitPoint);
        String branchHeadName = Utils.readContentsAsString(GITLET_HEAD);
        String[] commandsMerged = {"Merge-Commit", "Merged "
                + branchName + " into "
                + branchHeadName + ".", branchCommit.getID()};
        commit(commandsMerged);
        if (check) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    public void mergeBase1() {
        if (getStagingArea().getAdd().size() != 0
            || getStagingArea().getRemovePath().size() != 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }

    public void mergeBase2(File check) {
        if (check == null) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }

    public void mergeBase3(String name) {
        if (Utils.readContentsAsString(GITLET_HEAD).
            equals(name)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    public Commit createSplitPoint(Commit headBranch, Commit otherBranch) {

/** if (HeadBranch.getID().equals(OtherBranch.getID())) {.
 *          return HeadBranch;  }
 *          if (HeadBranch.getParent() != null) {
 *          HeadBranch = getParentCommit(HeadBranch);
 *          }
 *          if (OtherBranch.getParent() != null) {
 *          OtherBranch = getParentCommit(OtherBranch);}}   */
        if (otherBranch.getParent().isEmpty()) {
            return otherBranch;
        }
        HashSet<String> commitAncestors = new HashSet<>();
        while (headBranch.getParent() != null) {
            commitAncestors.add(headBranch.getID());
            if (headBranch.getParent().length() > LOWER_PARENT_LENGTH) {
                commitAncestors.add(
                        headBranch.getParent().substring(SECOND_PARENT_INDEX)
                );
            }
            headBranch = getParentCommit(headBranch);
        }
        while (otherBranch.getParent() != null) {
            if (commitAncestors.contains(otherBranch.getID())) {
                return otherBranch;
            }
            if (otherBranch.getParent().length() > LOWER_PARENT_LENGTH) {
                if (commitAncestors.contains
                    (otherBranch.getParent().substring(SECOND_PARENT_INDEX))) {
                    return getParentCommit2(otherBranch);
                }
            }
            otherBranch = getParentCommit(otherBranch);
        }
        return otherBranch;
    }

    public void mergeBase4(Commit check, Commit given) {
        if (check.getID().equals(given.getID())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        }
    }

    public void mergeBase5(Commit check, Commit current, String branch) {
        if (check.getID().equals(current.getID())) {
            String[] commands = {"checkout", branch};
            checkoutBranch(commands);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
    }

    public void mergeBase6(Commit current, Commit branch) {
        for (String fileName : branch.getIdMap().keySet()) {
            File check = new File(fileName);
            if (check.exists()) {
                if ((!current.getIdMap().
                        containsKey(fileName))
                        && (!getStagingArea().getAdd().containsKey(fileName))
                        && (!getStagingArea().getRemovePath().
                            contains(fileName))) {
                    System.out.println("There is an "
                            + "untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
    }

    public boolean testModify(Commit current, Commit point, String fileName) {
        if (point.getIdMap().containsKey(fileName)) {
            if (!current.getIdMap().get(fileName).
                equals(point.getIdMap().get(fileName))) {
                return true;
            }
        }
        return false;
    }

    public boolean mergeConflict(Commit current, Commit other, Commit split) {
        boolean encountered = false;
        for (String checkFile : other.getIdMap().keySet()) {

            if ((testModify(other, current, checkFile))
                    && (testModify(split, current, checkFile))
                    && (testModify(other, split, checkFile))) {
                encountered = true;
                File otherBlob = removeFile
                    (createTxt(other.getIdMap().get(checkFile)),
                            GITLET_BLOBS.listFiles());
                String otherContent = Utils.readContentsAsString(otherBlob);
                File currentBlob = removeFile(createTxt
                    (current.getIdMap().get(checkFile)),
                        GITLET_BLOBS.listFiles());
                String currentContent = Utils.readContentsAsString(currentBlob);
                String finalContent = "<<<<<<< HEAD\n"
                    + currentContent + "=======\n"
                    + otherContent + ">>>>>>>\n";
                File mergedFile = new File(checkFile);
                Utils.writeContents(mergedFile, finalContent);
                String[] commandsAdd = {"add", checkFile};
                add(commandsAdd);
            } else if ((!current.getIdMap().containsKey(checkFile))
                    && (testModify(other, split, checkFile))) {
                encountered = true;
                File otherBlob2 = removeFile(createTxt
                    (other.getIdMap().get(checkFile)),
                        GITLET_BLOBS.listFiles());
                String otherContent2 = Utils.readContentsAsString(otherBlob2);
                String finalContent2 = "<<<<<<< HEAD\n"
                        + "=======\n" + otherContent2
                        + ">>>>>>>\n";
                File mergedFile2 = new File(checkFile);
                Utils.writeContents(mergedFile2, finalContent2);
                String[] commandsAdd2 = {"add", checkFile};
                add(commandsAdd2);
            }
        }
        for (String fileName : current.getIdMap().keySet()) {
            if ((!other.getIdMap().containsKey(fileName))
                && (testModify(current, split, fileName))) {
                encountered = true;
                File currentBlob2 = removeFile
                        (createTxt(current.getIdMap().get(fileName)),
                                GITLET_BLOBS.listFiles());
                String currentContent2 =
                        Utils.readContentsAsString(currentBlob2);
                String finalContent = "<<<<<<< HEAD\n"
                        + currentContent2 + "=======\n"
                        + ">>>>>>>\n";
                File mergedFile = new File(fileName);
                Utils.writeContents(mergedFile, finalContent);
                String[] currentAddCommands = {"add", fileName};
                add(currentAddCommands);
            }
        }
        return encountered;
    }

    public void initializedDirectory() {
        if (!(GITLET_DIR.exists()
                && GITLET_BRANCHES.exists()
                && GITLET_COMMITS.exists()
                && GITLET_BRANCHES.exists()
                && GITLET_BLOBS.exists()
                && GITLET_HEAD.exists())) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
