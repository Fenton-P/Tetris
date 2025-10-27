import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Piece {
    private int piece;
    private Color color;
    private Color secondaryColor;
    private static Color[] colors = {new Color(0, 0, 150), new Color(150, 150, 0), new Color(150, 150, 0), new Color(0, 150, 0), new Color(150, 0, 0), new Color(150, 0, 0), new Color(150, 75, 0)};
    private static Color[] secondaryColors = {Color.blue, Color.yellow, Color.yellow, Color.green, Color.red, Color.red, Color.orange};
    private int[] position = new int[2];
    private static final String[] piecesFile = toArray("pieces.txt");
    
    public Piece() {
        piece = (int) (Math.random() * 7);
        color = colors[piece];
        secondaryColor = secondaryColors[piece];
    }
    public Piece(int pPiece) {
        piece = pPiece;
        color = colors[piece];
        secondaryColor = secondaryColors[piece];
    }
    public Piece(int x, int y) {
        piece = (int) (Math.random() * 7);
        color = colors[piece];
        secondaryColor = secondaryColors[piece];
        position = new int[2];
        position[0] = x;
        position[1] = y;
    }
    public Piece(int pPiece, int x, int y) {
        position = new int[2];
        position[0] = x;
        position[1] = y;
        piece = pPiece;
        color = colors[piece];
        secondaryColor = secondaryColors[piece];
    }
    public Piece(String inputs) {
        position = new int[2];
        piece = (int) (Math.random() * 7);
        color = colors[piece];
        secondaryColor = secondaryColors[piece];
        String[] inputList = inputs.split(",");
        String[][] inputListMain = new String[inputList.length][2];
        for(int i = 0;i<inputList.length;i++) {
            inputListMain[i] = inputList[i].split(":");
            switch(inputListMain[i][0]) {
                case "x":
                    try {
                        position[0] = Integer.parseInt(inputListMain[i][1]);
                    } catch(NumberFormatException e) {
                        switch(inputListMain[i][1]) {
                            case "centered":
                                position[0] = (Integer.parseInt(inputListMain[i][2]) - getPiece()[0].length * 20) / 2 + Integer.parseInt(inputListMain[i][3]); 
                                break;
                            case "dropping":
                                position[0] = 180;
                        }
                    }
                    break;
                case "y":
                    try {
                        position[1] = Integer.parseInt(inputListMain[i][1]);
                    } catch(NumberFormatException e) {}
                    break;
            }
        }
    }
    public int[] getPosition() {
        return position;
    }
    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
    }
    public void setPiece(int pPiece) {
        piece = pPiece;
        color = colors[piece%7];
        secondaryColor = secondaryColors[piece%7];
    }
    public int getPieceNum() {
        return piece;
    }
    public int getYOffset(int index) {
        int[][] piecesListed = getPiece();
        int offset = 0;
        for(int i = 0;i<piecesListed.length;i++) {
            for(int j = 0;j<piecesListed[i].length;j++) {
                if(piecesListed[i][j]==1) {
                    offset++;
                }
                if(offset-1 == index) {
                    return i;
                }
            }
        }
        return -1;
    }
    public int getXOffset(int index) {
        int[][] piecesListed = getPiece();
        int offset = 0;
        for(int i = 0;i<piecesListed.length;i++) {
            for(int j = 0;j<piecesListed[i].length;j++) {
                if(piecesListed[i][j]==1) {
                    offset++;
                }
                if(offset-1 == index) {
                    return j;
                }
            }
        }
        return -1;
    }
    public int[][] getPiece() {
        int currentCount = 0;
        int size1 = 0;
        int end = 0;
        for(int i = 0;i<piecesFile.length;i++) {
            if(piecesFile[i].equals("{")) {
                currentCount++;
            }
            if(currentCount==piece+1) {
                currentCount = i;
                break;
            }
        }
        
        for(int i = currentCount + 1;i<piecesFile.length;i++) {
            if(piecesFile[i].charAt(piecesFile[i].length()-1) == '}') {
                size1 = piecesFile[i].length() - 1;
                end = i;
                break;
            }
        }
        
        int[][] returnVal = new int[end - currentCount][size1];
        for(int i = currentCount + 1;i<end+1;i++) {
            for(int j = 0;j<size1;j++) {
                returnVal[i-currentCount-1][j] = Integer.parseInt(""+piecesFile[i].charAt(j));
            }
        }
        return returnVal;
    }
    public static String[] fromListToArr(ArrayList<String> arr) {
        String[] returnVal = new String[arr.size()];
        for(int i = 0;i<arr.size();i++) {
            returnVal[i] = arr.get(i);
        }
        return returnVal;
    }
    public static String[] toArray(String fileName) {
        String[] returnVal;
        ArrayList<String> temp = new ArrayList<String>();
        File inputFile;
        Scanner input = null;
        try {
            inputFile = new File(fileName);
            input = new Scanner(inputFile);
        } catch(FileNotFoundException e) {}
        while(input.hasNextLine()) {
            temp.add(input.nextLine());
        }
        returnVal = fromListToArr(temp);
        return returnVal;
    }
    public Color getColor() {
        return color;
    }
    public Color getSecondaryColor() {
        return secondaryColor;
    }
}
