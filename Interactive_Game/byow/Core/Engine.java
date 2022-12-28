package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.File;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer(); //remove for ag
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    Player avatar;
    Player avatar2;
    public long seed;
    public Random random;
    String commands = "";
    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    public static final File CWD = new File(System.getProperty("user.dir"));
    TETile floor;
    TETile wall;
    TETile nothing;
    ArrayList<Room> roomAList;
    int flag;
    int score1;
    int score2;


    public void interactWithKeyboard() {
        InputSource typed = new KeyboardInputSource();
        String inputSeed = "";
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        lore1();
        while (typed.possibleNextInput()) {
            char initial = typed.getNextKey();
            if (initial == ':') {
                char next = typed.getNextKey();
                if (next == 'Q') {
                    saveWorld();
                    //     System.exit(0);
                }
            }

            switch (initial) {
//                case 'M'->{
//                    lore1(); To be worked upon when we make the menu
//                }
                case 'B' -> {
                    lore2();
                }
                case 'M' -> {
                    menu();
                }
                case 'N' -> {
                    initialDisplay();
                    char first = typed.getNextKey();
                    inputSeed = inputSeed + first;
                    while (first != 'S') {
                        constantDisplay(inputSeed);
                        first = typed.getNextKey();
                        if (first != 'S') {
                            inputSeed = inputSeed + first;
                        }
                    }
                    seed = Long.parseLong(inputSeed);
                    createBasics(finalWorldFrame);
                }
                /** check if the switch implementation is properly being replaced by ifs*/
                case 'L' -> finalWorldFrame = loadWorld();
                case 'R' -> finalWorldFrame = primary1();
                case 'W' -> {
                    avatarMove("w", finalWorldFrame);
                    displayScore(score1, score2);
                    commands = commands + 'W';
                }
                case 'A' -> {
                    avatarMove("a", finalWorldFrame);
                    displayScore(score1,score2);
                    commands = commands + 'A';
                }
                case 'S' -> {
                    avatarMove("s", finalWorldFrame);
                    displayScore(score1,score2);
                    commands = commands + 'S';
                }
                case 'D' -> {
                    avatarMove("d", finalWorldFrame);
                    displayScore(score1,score2);
                    commands = commands + 'D';
                }
                case 'U' -> {
                    avatarMove("u", finalWorldFrame);
                    displayScore(score1,score2);
                    commands = commands + 'U';
                }
                case 'H' -> {
                    avatarMove("h", finalWorldFrame);
                    displayScore(score1,score2);
                    commands = commands + 'H';
                }
                case 'J' -> {
                    avatarMove("j", finalWorldFrame);
                    displayScore(score1, score2);
                    commands = commands + 'J';
                }
                case 'K' -> {
                    avatarMove("k", finalWorldFrame);
                    displayScore(score1,score2);
                    commands = commands + 'K';
                }
                default -> {
                }
            }
            mousePointer();
        }
    }

    public void EndDisplay() {
        StdDraw.setPenColor(Color.WHITE);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        String Message1 = "Player @ Wins the game!";

        String Message2 = "Player $ Wins the game!";
        if (score1 == 3) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text(0.4, 0.8, Message1);
        }
        else if (score2 == 3) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text(0.4, 0.4, Message2);
        }
        StdDraw.show();
    }
    public void lore1(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 10);
        StdDraw.setFont(capsFont);
        String initialMessage = "Legend has it that 10 mystical flowers exist somewhere far far away.";
        StdDraw.text(0.4, 0.8, initialMessage);
        String sMessage = "These flowers have the power to make you immortal. Press b to continue";
        StdDraw.text(0.4, 0.4, sMessage);
        StdDraw.show();
    }

    public void lore2(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        String initialMessage = "You must embark on a journey to collect 5 mystical flower. But wait! you're not alone.";
        StdDraw.text(0.4, 0.8, initialMessage);
        String sMessage = "First to collect 5 flowers wins. Use WASD for @ and UHJK for $. Press M to continue";
        StdDraw.text(0.4, 0.4, sMessage);
        StdDraw.show();
    }
    public void menu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        String initialMessage = "New Game. (Press N)";
        StdDraw.text(0.4, 0.4, initialMessage);
        String sMessage = "Load Game. (Press L)";
        StdDraw.text(0.4, 0.5, sMessage);
        String ReplayMessage = "Replay Recent Game. (Press R)";
        StdDraw.text(0.4, 0.6, ReplayMessage);
        String stringMessage = "Quit Game. (Press :Q)";
        StdDraw.text(0.4, 0.7, stringMessage);
        StdDraw.show();
    }

    public void initialDisplay() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        String initialMessage = "Please enter a seed value.";
        StdDraw.text(0.4, 0.8, initialMessage);
        String sMessage = "Enter 's' after entering the seed";
        StdDraw.text(0.4, 0.4, sMessage);
        StdDraw.show();
    }

    public void constantDisplay(String numbers) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        String initialMessage = "Please enter a seed value.";
        StdDraw.text(0.4, 0.8, initialMessage);
        String sMessage = "Enter 's' after entering the seed";
        StdDraw.text(0.4, 0.4, sMessage);
        StdDraw.text(0.4, 0.6, numbers);
        StdDraw.show();
    }

    public void mousePointer() {
        int pointX = (int) StdDraw.mouseX();
        int pointY = (int) StdDraw.mouseY();
        TETile pointTile = finalWorldFrame[pointX][pointY];
        if (pointTile == floor) {
            if (pointTile.description().equals("floor")) {
                displayTile("normal floor.");
            }
            else if (pointTile.description().equals("sand")) {
                displayTile("sand.");
            }
            else if (pointTile.description().equals("grass")) {
                displayTile("grass");
            }
        }
        else if (pointTile == wall) {
            if (pointTile.description().equals("wall")) {
                displayTile("wall");
            }
            else if (pointTile.description().equals("tree")) {
                displayTile("tree");
            }
            else if (pointTile.description().equals("mountain")) {
                displayTile("mountain");
            }
        }
        else if (pointTile == nothing) {
            if (pointTile.description().equals("nothing")) {
                displayTile("black hole");
            }
            else if (pointTile.description().equals("water")) {
                displayTile("water");
            }
            else if (pointTile.description().equals("tree")) {
                displayTile("out of bounds tree");
            }
        }
        StdDraw.show();
    }
    public void displayScore(int s1, int s2){ //Score fun
        StdDraw.setPenColor(Color.WHITE);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        StdDraw.text(10.0,28.0, "Player @ score: " + s1);
        if (s1 == 3) {
            StdDraw.text(10.0,32.0, "Come on you got this! CS 61BL never give in!");
        }
        if (s1 == 4) {
            StdDraw.text(10.0,32.0, "You're almost there warrior, don't look back now!");
        }
        StdDraw.text(65.0,28.0, "Player $ score: "+ s2);
        if (s2 == 3) {
            StdDraw.text(10.0,32.0, "Come on you got this! CS 61BL never give in!");
        }
        if (s2 == 4) {
            StdDraw.text(10.0,32.0, "You're almost there warrior, don't look back now!");
        }
        StdDraw.show();
    }

    public void displayTile(String message) {
        StdDraw.setPenColor(Color.RED);
        Font capsFont = new Font("TimesRoman", Font.BOLD, 20);
        StdDraw.setFont(capsFont);
        StdDraw.text(10.0, 1.0, message);
        StdDraw.show();
    }

    /*0
    For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {

//        InputSource inputstring = new StringInputDevice(input);
//        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        String temp = input;
        String seedString = "";
        input = input.toLowerCase();
        int i = 1;
        if (input.charAt(0) == 'n') {
            while (input.charAt(i) != 's') {
                seedString = seedString + input.charAt(i);
                i++;
            }
        }
        temp = input.substring(i + 1);
        if (!seedString.equals("")) {
            seed = Long.parseLong(seedString);
        }
        createBasics(finalWorldFrame);
        while (!temp.equals("")) {
            String check = "" + temp.charAt(0);
            if (temp.length() > 1 && temp.substring(0, 2).equals(":q")) {
                saveWorld();
                //    System.exit(0);
            } else if (check.equals("l")) {
                finalWorldFrame = loadWorld();
            } else {
                commands = commands + check;
                /** implement a character movement method */
            }
            temp = temp.substring(1);
        }
        return finalWorldFrame;
    }

    public void createBasics(TETile[][] tile) {
        TERenderer ter = new TERenderer(); //REMOVE FOR AG
        ter.initialize(WIDTH, HEIGHT); //REMOVE FOR AG
        random = new Random(seed);
        int environment = RandomUtils.uniform(random, 1, 4);
        //int environment = 1;
        if (environment==1){
            floor = Tileset.FLOOR;
            wall = Tileset.WALL;
            nothing = Tileset.NOTHING;
        }
        else if (environment==2){
            floor = Tileset.SAND;
            wall = Tileset.WATERCLOSE;
            nothing = Tileset.WATER;
        }
        else if (environment==3){
            floor = Tileset.GRASS;
            wall = Tileset.MOUNTAIN;
            nothing = Tileset.TREE;
        }
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                finalWorldFrame[x][y] = nothing;
            }
        }

        int roomba = RandomUtils.uniform(random, 15, 23);
        roomAList = new ArrayList<Room>(roomba);

        for (int i = 0; i < roomba; i++) {

            int roomHt = 2 + RandomUtils.uniform(random, 2, 5);
            int roomWd = 3 + RandomUtils.uniform(random, 0, 4);
            int roomY = RandomUtils.uniform(random, 3, HEIGHT - HEIGHT / 5);
            int roomX = RandomUtils.uniform(random, 1, WIDTH - 3);

            roomAList.add(new Room(roomX, roomY, roomHt, roomWd));

            Room currentRoom = roomAList.get(i);
            int trackX = currentRoom.getX();
            int trackY = currentRoom.getY();
            for (int p = trackX; p < trackX + currentRoom.width; p++) {
                if (p + 3 >= WIDTH) {
                    continue;
                }
                for (int q = trackY; q < trackY + currentRoom.height; q++) {
                    if (q + 2 >= HEIGHT) {
                        continue;
                    }
                    finalWorldFrame[p][q] = floor;
                }
            }
        }
        for (int i = 0; i < roomba; i++) {
            Room buildTo = roomAList.get(i);
            int initX = buildTo.x;
            int initY = buildTo.y;
            Room initialRoom = buildTo;
            int dist = 9999999;
            for (int j = i + 1; j < roomba; j++) {
                int distX = initX - roomAList.get(j).x;
                int distY = initY - roomAList.get(j).y;
                double pythagStore = Math.pow(distX, 2) + Math.pow(distY, 2);
                if (pythagStore < dist) {
                    buildTo = roomAList.get(j);
                    dist = (int) pythagStore;
                }
            }
            int destX = buildTo.getX();
            int destY = buildTo.getY();
            if (initY > destY) {
                for (int k = destY; k <= initY; k++) {
                    finalWorldFrame[destX][k] = floor;
                }
            }
            if (initY < destY) {
                for (int k = initY; k <= destY; k++) {
                    finalWorldFrame[destX][k] = floor;
                }
            }
            if (initX > destX) {
                for (int k = destX; k <= initX; k++) {
                    finalWorldFrame[k][initY] = floor;
                }
            } else if (initX < destX) {
                for (int k = initX; k <= destX; k++) {
                    finalWorldFrame[k][initY] = floor;
                }
            }
        }
        for (int m = 0; m < HEIGHT; m++) {
            for (int n = 0; n < WIDTH; n++) {
                if (finalWorldFrame[n][m].equals(floor)) {
                    for (int adjacentX = m - 1; adjacentX <= m + 1; adjacentX += 1) {
                        for (int adjacentY = n - 1; adjacentY <= n + 1; adjacentY += 1) {
                            TETile currTile = finalWorldFrame[adjacentY][adjacentX];
                            if (currTile.equals(nothing)) {
                                finalWorldFrame[adjacentY][adjacentX] = wall;
                            }
                        }
                    }
                }
            }
        }
        createCoins();
        /** make function to track if a player reaches 5 points, then terminate and give an output screen which says "player @/$ wins!" */
//        for (int p = 0; p < WIDTH; p++) {
//            for (int q = 0; q < HEIGHT; q++) {
//                if (finalWorldFrame[p][q].equals(Tileset.NOTHING)) {
//                    finalWorldFrame[p][q] = Tileset.FLOOR;
//                }
//            }
//        }
        int avatarStart = RandomUtils.uniform(random, 8);
        Room spawnPoint = roomAList.get(avatarStart);
        avatar = new Player(spawnPoint.getX(), spawnPoint.getY());
        finalWorldFrame[spawnPoint.getX()][spawnPoint.getY()] = Tileset.AVATAR;
        // create second Avatar
        int avatarStart2 = RandomUtils.uniform(random,8);
        Room spawnPoint2 = roomAList.get(avatarStart2);
        avatar2 = new Player(spawnPoint2.getX(), spawnPoint2.getY());
        finalWorldFrame[spawnPoint2.getX()][spawnPoint2.getY()] = Tileset.AVATAR2;
        ter.renderFrame(finalWorldFrame); //IMPORTANT MISSING STATEMENT - uncomment
    }

    public void avatarMove(String wasd, TETile[][] finalWorldFrame) {
        switch (wasd) {
            case "w":
                int moveUpY = avatar.getY() + 1;
                if (finalWorldFrame[avatar.getX()][moveUpY] != wall) {
                    if (finalWorldFrame[avatar.getX()][moveUpY].equals(Tileset.FLOWER)){
                        score1++;
                    }
                    finalWorldFrame[avatar.getX()][moveUpY] = Tileset.AVATAR; //moving up
                    finalWorldFrame[avatar.getX()][moveUpY - 1] = floor; //reassigning prev variable to floor
                    avatar.setY(moveUpY);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "s":
                int moveDownY = avatar.getY() - 1;
                if (finalWorldFrame[avatar.getX()][moveDownY] != wall) {
                    if (finalWorldFrame[avatar.getX()][moveDownY].equals(Tileset.FLOWER)){
                        score1++;
                    }
                    finalWorldFrame[avatar.getX()][moveDownY] = Tileset.AVATAR; //moving up
                    finalWorldFrame[avatar.getX()][moveDownY + 1] = floor; //reassigning prev variable to floor
                    avatar.setY(moveDownY);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "a":
                int moveLeft = avatar.getX() - 1;
                if (finalWorldFrame[moveLeft][avatar.getY()] != wall) {
                    if (finalWorldFrame[moveLeft][avatar.getY()].equals(Tileset.FLOWER)){
                        score1++;
                    }
                    finalWorldFrame[moveLeft][avatar.getY()] = Tileset.AVATAR;
                    finalWorldFrame[moveLeft + 1][avatar.getY()] = floor;
                    avatar.setX(moveLeft);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "d":
                int moveRight = avatar.getX() + 1;
                if (finalWorldFrame[moveRight][avatar.getY()] != wall) {
                    if (finalWorldFrame[moveRight][avatar.getY()].equals(Tileset.FLOWER)){
                        score1++;
                    }
                    finalWorldFrame[moveRight][avatar.getY()] = Tileset.AVATAR;
                    finalWorldFrame[moveRight - 1][avatar.getY()] = floor;
                    avatar.setX(moveRight);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "u":
                int moveUpY2 = avatar2.getY() + 1;
                if (finalWorldFrame[avatar2.getX()][moveUpY2] != wall) {
                    if (finalWorldFrame[avatar.getX()][moveUpY2].equals(Tileset.FLOWER)){
                        score2++;
                    }
                    finalWorldFrame[avatar2.getX()][moveUpY2] = Tileset.AVATAR2; //moving up
                    finalWorldFrame[avatar2.getX()][moveUpY2 - 1] = floor; //reassigning prev variable to floor
                    avatar2.setY(moveUpY2);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "j":
                int moveDownY2 = avatar2.getY() - 1;
                if (finalWorldFrame[avatar2.getX()][moveDownY2] != wall) {
                    if (finalWorldFrame[avatar.getX()][moveDownY2].equals(Tileset.FLOWER)){
                        score2++;
                    }
                    finalWorldFrame[avatar2.getX()][moveDownY2] = Tileset.AVATAR2; //moving up
                    finalWorldFrame[avatar2.getX()][moveDownY2 + 1] = floor; //reassigning prev variable to floor
                    avatar2.setY(moveDownY2);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "h":
                int moveLeft2 = avatar2.getX() - 1;
                if (finalWorldFrame[moveLeft2][avatar2.getY()] != wall) {
                    if (finalWorldFrame[moveLeft2][avatar2.getY()].equals(Tileset.FLOWER)){
                        score2++;
                    }
                    finalWorldFrame[moveLeft2][avatar2.getY()] = Tileset.AVATAR2;
                    finalWorldFrame[moveLeft2 + 1][avatar2.getY()] = floor;
                    avatar2.setX(moveLeft2);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            case "k":
                int moveRight2 = avatar2.getX() + 1;
                if (finalWorldFrame[moveRight2][avatar2.getY()] != wall) {
                    if (finalWorldFrame[moveRight2][avatar2.getY()].equals(Tileset.FLOWER)){
                        score2++;
                    }
                    finalWorldFrame[moveRight2][avatar2.getY()] = Tileset.AVATAR2;
                    finalWorldFrame[moveRight2 - 1][avatar2.getY()] = floor;
                    avatar2.setX(moveRight2);
                    displayScore(score1,score2);
                    ter.renderFrame(finalWorldFrame);
                }
                break;
            default: break;
        }
    }

    public void saveWorld() {
        File currentWorld = Utils.join(CWD, "World.txt");
        String content = String.valueOf(seed);
        Utils.writeContents(currentWorld, content);
        File command = Utils.join(CWD, "command.txt");
        String commandString = String.valueOf(commands);
        Utils.writeContents(command, commandString);
    }

    public TETile[][] loadWorld() {
        File currentWorld = Utils.join(CWD, "World.txt");
        String currentSeed = Utils.readContentsAsString(currentWorld);
        File command = Utils.join(CWD, "command.txt");
        String currentCommands = Utils.readContentsAsString(command);
        if (currentCommands.equals("")) {
            // System.exit(0);
        }
        seed = Long.parseLong(currentSeed);
        return interactWithInputString(currentCommands);
    }

    public TETile[][] primary1() {
        File currentWorld = Utils.join(CWD, "World.txt");
        String currentSeed = Utils.readContentsAsString(currentWorld);
        File command = Utils.join(CWD, "command.txt");
        String currentCommands = Utils.readContentsAsString(command);
        if (currentCommands.equals("")) {
            // System.exit(0);
        }
        currentCommands = currentCommands.toLowerCase();
        seed = Long.parseLong(currentSeed);
        createBasics(finalWorldFrame);

        while (!currentCommands.equals("")) {
            String check = "" + currentCommands.charAt(0);
            commands = commands + check;
            StdDraw.pause(1000);
            avatarMove(check, finalWorldFrame);
            currentCommands = currentCommands.substring(1);
        }
        return finalWorldFrame;
    }

    public void createCoins() {
        int coinNumber = 10;
        flag = 0;
        do {
            int x = RandomUtils.uniform(random, 3, 78);
            int y = RandomUtils.uniform(random, 3,27);
            if (finalWorldFrame[x][y].equals(floor)){
                finalWorldFrame[x][y]=Tileset.FLOWER;
                flag++;
            }

        } while (flag < coinNumber);

//        for (int i=0;i<coinNumber;i++) {
//            int coinSpawn = RandomUtils.uniform(random, 8);
//            Room spawnPointCoin = roomAList.get(coinSpawn);
//            Player coin = new Player(spawnPointCoin.getX(), spawnPointCoin.getY());
//            finalWorldFrame[spawnPointCoin.getX()][spawnPointCoin.getY()] = Tileset.FLOWER;
//        }

        ter.renderFrame(finalWorldFrame);
    }

    public class Player {
        int x;
        int y;


        public Player(int xVar, int yVar) {
            x = xVar;
            y = yVar;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
    public class Room {
        int height;
        int width;
        //Location currSpot;
        Random rand;
        int x;
        int y;

        public Room(int _x, int _y, int _height, int _width) {
            x = _x;
            y = _y;
            height = _height;
            width = _width;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public int getRoomHeight(){ return height; }
        public int getRoomWidth(){ return width;}

    }
}




