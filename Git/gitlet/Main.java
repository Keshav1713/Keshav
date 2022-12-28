package gitlet;

import java.io.File;
import java.io.IOException;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 * @author Keshav Raj Singhal */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains.
     * <COMMAND> <OPERAND1> <OPERAND2> */
    static final File CWD = new File(".");

    /** Main method caller.
     * @param args contains the commands */
    public static void main(String[] args) throws IOException {
        Repository methodCaller = new Repository();
        if (args.length == 0) {
            helperMethod();
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                methodCaller.init();
                break;
            case "add":
                methodCaller.add(args);
                break;
            case "commit":
                methodCaller.commit(args);
                break;
            case "log":
                methodCaller.log(args);
                break;
            case "checkout":
                if ((args[1].equals("--")) && (args.length == 3)) {
                    methodCaller.checkoutHead(args);
                } else if ((args.length == 4) && ((args[2].equals("--")))) {
                    methodCaller.checkoutID(args);
                } else if (args.length == 2) {
                    methodCaller.checkoutBranch(args);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "rm":
                methodCaller.rm(args);
                break;
            case "rm-branch":
                methodCaller.rmBranch(args);
                break;
            case "status":
                methodCaller.status(args);
                break;
            case "global-log":
                methodCaller.globalLog(args);
                break;
            case "find":
                methodCaller.find(args);
                break;
            case "branch":
                methodCaller.branch(args);
                break;
            case "reset":
                methodCaller.reset(args);
                break;
            case "merge":
                methodCaller.merge(args);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                break;
        } }

   /** For system exit. */
    public static void helperMethod() {
        System.out.println("Please enter a command.");
        System.exit(0);
    }
}
