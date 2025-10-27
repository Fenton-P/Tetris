import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.Scanner;

/**
TODO:
    -> Fix Bad Controls
    -> Add sound?
**/

public class MyProgram extends JComponent {
    public static Piece nextPieceDisplay;
    public static int maxSpeed = 100; // in milliseconds
    public static int currentSpeed = 550;
    public static int frames = 0;
    public static Timer timer = null;
    public static ArrayList<JPanel> display = null;
    public static JFrame mainFrame = new JFrame();
    public static Piece currentPiece = new Piece("x:dropping,y:-40");
    public static ArrayList<JPanel> currentPieceList = draw(currentPiece);
    public static boolean first = false;
    public static boolean doOnce = true;
    public static File inputFile;
    public static Scanner input = null;
    public static int rotateVal = 0;
    public static int score = 0;
    public static String highestScore = "";
    public static JLabel scoreVisual;
    public static void checkAndClear(JPanel[][] board) {
        boolean check = true;
        while(check) {
            for(int k = board.length-1;k>-1;k--) {
                check = true;
                for(int i = 0;i<board[k].length;i++) {
                    if(board[k][i]==null) {
                        check = false;
                    }
                }
                if(check) {
                    score+=50;
                    scoreVisual.setText(score + "");
                    for(int i = k;i>0;i--) {
                        for(int j = 0;j<board[i].length;j++) {
                            if(board[i-1][j]!=null) {
                                Rectangle tempBounds = board[i-1][j].getBounds();
                                board[i-1][j].setBounds(tempBounds.x, tempBounds.y+20, 20, 20);
                                if(board[i][j]!=null) {
                                    mainFrame.remove(board[i][j]);
                                }
                                board[i][j] = board[i-1][j];
                                if(board[i][j]!=null) {
                                    mainFrame.add(board[i][j]);
                                }
                            } else {
                                if(board[i][j] != null) {
                                    mainFrame.remove(board[i][j]);
                                }
                                board[i][j] = null;
                            }
                        }
                    }
                    for(int i = 0;i<board[0].length;i++) {
                        if(board[0][i]!=null) {
                            mainFrame.remove(board[0][i]);
                        }
                        board[0][i] = null;
                    }
                    mainFrame.repaint();
                    for(int i = 0;i<board.length;i++) {
                        for(int j = 0;j<board[i].length;j++) {
                            if(board[i][j]!=null) {
                                mainFrame.add(board[i][j]);
                            }
                        }
                    }
                }
            }
        }
    }
    public static boolean onGround() {
        int boardy = (currentPiece.getPosition()[1]) / 20;
        int[][] tempPiece = currentPiece.getPiece();
        return tempPiece.length * 20 + boardy * 20 > 380;
    }
    public static boolean adjacent(JPanel[][] board, String direction) {
        boolean currentStanding = false;
        if(direction.equals("left")) {
            int boardx = (currentPiece.getPosition()[0]-100) / 20;
            int boardy = (currentPiece.getPosition()[1]) / 20;
            int[][] piecesArray = currentPiece.getPiece();
            for(int i = 0;i<piecesArray.length;i++) {
                for(int j = 0;j<piecesArray[i].length;j++) {
                    if(piecesArray[i][j]==1&&boardx!=0) {
                        currentStanding = true;
                    }
                }
            }
            for(int i = 0;i<piecesArray.length;i++) {
                for(int j = 0;j<piecesArray[i].length;j++) {
                    if(currentStanding && board[i + boardy][j + boardx - 1] != null) {
                        currentStanding = false;
                    }
                }
            }
        } else {
            int boardx = (currentPiece.getPosition()[0]-100) / 20;
            int boardy = (currentPiece.getPosition()[1]) / 20;
            int[][] piecesArray = currentPiece.getPiece();
            for(int i = 0;i<piecesArray.length;i++) {
                for(int j = 0;j<piecesArray[i].length;j++) {
                    if(piecesArray[i][j]==1&&boardx+piecesArray[i].length-1!=9) {
                        currentStanding = true;
                    }
                }
            }
            for(int i = 0;i<piecesArray.length;i++) {
                for(int j = 0;j<piecesArray[i].length;j++) {
                    if(currentStanding && board[i + boardy][j + boardx + 1] != null) {
                        currentStanding = false;
                    }
                }
            }
        }
        return currentStanding;
    }
    public static boolean touching(JPanel[][] board) {
        int boardx = (currentPiece.getPosition()[0]-100) / 20;
        int boardy = (currentPiece.getPosition()[1]) / 20;
        int[][] piecesArray = currentPiece.getPiece();
        for(int i = 0;i<piecesArray.length;i++) {
            for(int j = 0;j<piecesArray[i].length;j++) {
                if(boardy + i + 1<20&&board[boardy + i + 1][boardx + j]!=null&&piecesArray[i][j]==1) {
                 return true;
                }
            }
        }
        return false;
    }
    public static ArrayList<JPanel> draw(Piece piece) {
        int[][] piecePosition = piece.getPiece();
        ArrayList<JPanel> panelList = new ArrayList<JPanel>();
        for(int i = 0;i<piecePosition.length;i++) {
            for(int j = 0;j<piecePosition[i].length;j++) {
                if(piecePosition[i][j]==1) {
                    JPanel temp = new JPanel();
                    temp.setBounds(j*20+piece.getPosition()[0], i*20+piece.getPosition()[1], 20, 20);
                    temp.setBackground(piece.getColor());
                    panelList.add(temp);
                }
            }
        }
        return panelList;
    }
    public static void updatePiece(ArrayList<JPanel> piece, String action, JPanel[][] board) {
        switch(action) {
            case "down":
                frames++;
                currentPiece.setPosition(currentPiece.getPosition()[0], currentPiece.getPosition()[1]+20);
                for(int i = 0;i<piece.size();i++) {
                    piece.get(i).setBounds(piece.get(i).getBounds().x, currentPiece.getPosition()[1] + currentPiece.getYOffset(i)*20, 20, 20);
                }
                break;
            case "left":
                if(currentPiece.getPosition()[1]>-20&&adjacent(board, "left")) {
                    currentPiece.setPosition(currentPiece.getPosition()[0] - 20, currentPiece.getPosition()[1]);
                    for(int i = 0;i<piece.size();i++) {
                        piece.get(i).setBounds(currentPiece.getPosition()[0] + currentPiece.getXOffset(i)*20, currentPiece.getPosition()[1] + currentPiece.getYOffset(i)*20, 20, 20);
                    }
                }
                break;
            case "right":
                if(currentPiece.getPosition()[1]>-20&&adjacent(board, "right")) {
                    currentPiece.setPosition(currentPiece.getPosition()[0] + 20, currentPiece.getPosition()[1]);
                    for(int i = 0;i<piece.size();i++) {
                        piece.get(i).setBounds(currentPiece.getPosition()[0] + currentPiece.getXOffset(i)*20, currentPiece.getPosition()[1] + currentPiece.getYOffset(i)*20, 20, 20);
                    }
                }
                break;
            case "rotate":
                if(currentPiece.getPosition()[1]>-20) {
                    boolean works = true;
                    Piece tempPiece = new Piece();
                    tempPiece.setPosition(currentPiece.getPosition()[0], currentPiece.getPosition()[1]);
                    tempPiece.setPiece(currentPiece.getPieceNum()-rotateVal*7);
                    tempPiece.setPiece(tempPiece.getPieceNum()+((rotateVal+1)%4) * 7);
                    int boardx = (tempPiece.getPosition()[0]-100) / 20;
                    int boardy = (tempPiece.getPosition()[1]) / 20;
                    int[][] tempPiecesPiece = tempPiece.getPiece();
                    for(int i = 0;i<tempPiecesPiece.length;i++) {
                        for(int j = 0;j<tempPiecesPiece[i].length;j++) {
                            if(i+boardy>19||j+boardx>9||(tempPiecesPiece[i][j] == 1 && board[i + boardy][j + boardx] != null)) {
                                works = false;
                            }
                        }
                    }
                    if(works) {
                        for(int i = 0;i<currentPieceList.size();i++) {
                            mainFrame.remove(currentPieceList.get(i));
                        }
                        mainFrame.repaint();
                        currentPiece.setPiece(currentPiece.getPieceNum()-rotateVal*7);
                        rotateVal++;
                        rotateVal %= 4;
                        currentPiece.setPiece(currentPiece.getPieceNum()+rotateVal * 7);
                        currentPieceList = draw(currentPiece);
                        for(int i = 0;i<currentPieceList.size();i++) {
                            JPanel tempPanel = new JPanel();
                            tempPanel.setBounds(5, 5, 10, 10);
                            tempPanel.setBackground(currentPiece.getSecondaryColor());
                            currentPieceList.get(i).add(tempPanel);
                            mainFrame.add(currentPieceList.get(i));
                        }
                    }
                }
                break;
        }
    }
    public static void getNextPiece() {
        if(display!=null) {
            for(int i = 0;i<display.size();i++) {
                mainFrame.remove(display.get(i));
            }
        }
        mainFrame.repaint();
        nextPieceDisplay = new Piece("x:centered:100:300,y:50");
        display = draw(nextPieceDisplay);
        for(int i = 0;i<display.size();i++) {
            JPanel tempPanel = new JPanel();
            tempPanel.setBounds(5, 5, 10, 10);
            tempPanel.setBackground(nextPieceDisplay.getSecondaryColor());
            display.get(i).add(tempPanel);
            mainFrame.add(display.get(i));
        }
    }
    public static void main(String[] args) {
        //Read High Score File
        try {
            inputFile = new File("highscore.txt");
            input = new Scanner(inputFile);
            
        } catch(FileNotFoundException e) {}
        highestScore = input.nextLine();
        
        //Adding Graphics
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 100, 400);
        leftPanel.setBackground(Color.black);
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(300, 0, 100, 400);
        rightPanel.setBackground(Color.black);
        JLabel nextPiece = new JLabel("Next:");
        nextPiece.setFont(new Font("Roboto", Font.PLAIN, 30));
        nextPiece.setForeground(Color.white);
        JLabel scoreText = new JLabel("Score:");
        scoreText.setFont(new Font("Roboto", Font.PLAIN, 30));
        scoreText.setForeground(Color.white);
        scoreVisual = new JLabel(score+"");
        scoreVisual.setFont(new Font("Roboto", Font.PLAIN, 30));
        scoreVisual.setForeground(Color.white);
        JLabel highScoreText = new JLabel("High:");
        highScoreText.setFont(new Font("Roboto", Font.PLAIN, 30));
        highScoreText.setForeground(Color.white);
        JLabel highScore = new JLabel(highestScore);
        highScore.setFont(new Font("Roboto", Font.PLAIN, 30));
        highScore.setForeground(Color.white);
        JPanel gameOver = new JPanel();
        gameOver.setBounds(0, 0, 400, 400);
        gameOver.setBackground(Color.black);
        JLabel gameOverText = new JLabel("Game Over");
        gameOverText.setFont(new Font("Roboto", Font.PLAIN, 30));
        gameOverText.setForeground(Color.red);
        gameOverText.setBounds(50, 100, 400, 150);
        gameOver.add(gameOverText);
        highScoreText.setBounds(10, 0, 100, 45);
        mainFrame.add(highScoreText);
        highScore.setBounds(1, 35, 100, 45);
        mainFrame.add(highScore);
        mainFrame.add(leftPanel);
        
        //Display The Next Available Piece
        getNextPiece();
        
        //Definitions
        for(int i = 0;i<currentPieceList.size();i++) {
            JPanel tempPanel = new JPanel();
            tempPanel.setBounds(5, 5, 10, 10);
            tempPanel.setBackground(currentPiece.getSecondaryColor());
            currentPieceList.get(i).add(tempPanel);
            mainFrame.add(currentPieceList.get(i));
        }
        JPanel[][] board = new JPanel[20][10];
        
        MyProgram handler = new MyProgram();
        
        //Left Arrow Key
        Action moveLeft = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updatePiece(currentPieceList, "left", board);
            }
        };
        
        handler.getActionMap().put("moveLeft", moveLeft);
        
        handler.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        
        //Right Arrow Key
        Action moveRight = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updatePiece(currentPieceList, "right", board);
            }
        };
        
        handler.getActionMap().put("moveRight", moveRight);
        
        handler.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        
        //R Key
        Action rotate = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updatePiece(currentPieceList, "rotate", board);
            }
        };
        
        handler.getActionMap().put("rotate", rotate);
        
        handler.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotate");
        
        //Up Arrow Key
        handler.getInputMap().put(KeyStroke.getKeyStroke("UP"), "rotate");
        
        //Down Arrow Key
        Action quickDown = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    score++;
                    scoreVisual.setText(score+"");
                    if(first&&(onGround()||(touching(board)))) {
                        int boardx = (currentPiece.getPosition()[0]-100) / 20;
                        int boardy = (currentPiece.getPosition()[1]) / 20;
                        int[][] tempPiece = currentPiece.getPiece();
                        int indexer = 0;
                        for(int i = 0;i<tempPiece.length;i++) {
                            for(int j = 0;j<tempPiece[i].length;j++) {
                                if(tempPiece[i][j]==1) {
                                    board[i + boardy][j + boardx] = currentPieceList.get(indexer);
                                    indexer++;
                                }
                            }
                        }
                        rotateVal = 0;
                        currentPiece = new Piece("x:dropping,y:-40");
                        currentPiece.setPiece(nextPieceDisplay.getPieceNum());
                        mainFrame.remove(rightPanel);
                        getNextPiece();
                        mainFrame.add(rightPanel);
                        currentPieceList = draw(currentPiece);
                        for(int i = 0;i<currentPieceList.size();i++) {
                            JPanel tempPanel = new JPanel();
                            tempPanel.setBounds(5, 5, 10, 10);
                            tempPanel.setBackground(currentPiece.getSecondaryColor());
                            currentPieceList.get(i).add(tempPanel);
                            mainFrame.add(currentPieceList.get(i));
                        }
                    }
                    first = true;
                    updatePiece(currentPieceList, "down", board);
                    checkAndClear(board);
                } catch (ArrayIndexOutOfBoundsException exception) {
                    mainFrame.remove(rightPanel);
                    mainFrame.remove(leftPanel);
                    mainFrame.remove(scoreVisual);
                    mainFrame.remove(scoreText);
                    for(JPanel[] i : board) {
                        for(JPanel j : i) {
                            if(j!=null) {
                                mainFrame.remove(j);
                            }
                        }
                    } 
                    for(int i = 0;i<currentPieceList.size();i++) {
                        mainFrame.remove(currentPieceList.get(i));
                    }
                    for(int i = 0;i<display.size();i++) {
                        mainFrame.remove(display.get(i));
                    }
                    mainFrame.repaint();
                    mainFrame.add(gameOver);
                    if(doOnce) {
                        int highestScoreInt = Integer.parseInt(highestScore);
                        if(highestScoreInt<score) {
                            try {
                                FileWriter writer = new FileWriter("highscore.txt");
                                writer.write(score + "");
                                writer.close();
                            } catch(IOException error) {System.out.println("Couldn't Edit High Score");}
                        }
                        doOnce = !doOnce;
                    }
                }
            }
        };
        
        handler.getActionMap().put("quickDown", quickDown);
        
        handler.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "quickDown");
        
        //Game Timer
        timer = new Timer(currentSpeed, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    if(frames%99==0&&currentSpeed-50>=maxSpeed) {
                        currentSpeed-=50;
                        timer.setDelay(currentSpeed);
                        timer.restart();
                    }
                    if(first&&(onGround()||(touching(board)))) {
                        int boardx = (currentPiece.getPosition()[0]-100) / 20;
                        int boardy = (currentPiece.getPosition()[1]) / 20;
                        int[][] tempPiece = currentPiece.getPiece();
                        int indexer = 0;
                        for(int i = 0;i<tempPiece.length;i++) {
                            for(int j = 0;j<tempPiece[i].length;j++) {
                                if(tempPiece[i][j]==1) {
                                    board[i + boardy][j + boardx] = currentPieceList.get(indexer);
                                    indexer++;
                                }
                            }
                        }
                        rotateVal = 0;
                        currentPiece = new Piece("x:dropping,y:-40");
                        currentPiece.setPiece(nextPieceDisplay.getPieceNum());
                        mainFrame.remove(rightPanel);
                        getNextPiece();
                        mainFrame.add(rightPanel);
                        currentPieceList = draw(currentPiece);
                        for(int i = 0;i<currentPieceList.size();i++) {
                            JPanel tempPanel = new JPanel();
                            tempPanel.setBounds(5, 5, 10, 10);
                            tempPanel.setBackground(currentPiece.getSecondaryColor());
                            currentPieceList.get(i).add(tempPanel);
                            mainFrame.add(currentPieceList.get(i));
                        }
                    }
                    first = true;
                    updatePiece(currentPieceList, "down", board);
                    checkAndClear(board);
                } catch (ArrayIndexOutOfBoundsException exception) {
                    mainFrame.remove(rightPanel);
                    mainFrame.remove(leftPanel);
                    mainFrame.remove(scoreVisual);
                    mainFrame.remove(scoreText);
                    mainFrame.remove(highScoreText);
                    mainFrame.remove(highScore);
                    for(JPanel[] i : board) {
                        for(JPanel j : i) {
                            if(j!=null) {
                                mainFrame.remove(j);
                            }
                        }
                    } 
                    for(int i = 0;i<currentPieceList.size();i++) {
                        mainFrame.remove(currentPieceList.get(i));
                    }
                    for(int i = 0;i<display.size();i++) {
                        mainFrame.remove(display.get(i));
                    }
                    mainFrame.repaint();
                    mainFrame.add(gameOver);
                    if(doOnce) {
                        int highestScoreInt = Integer.parseInt(highestScore);
                        if(highestScoreInt<score) {
                            try {
                                FileWriter writer = new FileWriter("highscore.txt");
                                writer.write(score + "");
                                writer.close();
                            } catch(IOException error) {System.out.println("Couldn't Edit High Score");}
                        }
                        doOnce = !doOnce;
                    }
                }
            }
        });
        timer.start();

        rightPanel.add(nextPiece);
        scoreText.setBounds(305, 150, 100, 100);
        scoreVisual.setBounds(305, 200, 100, 100);
        mainFrame.add(scoreText);
        mainFrame.add(scoreVisual);
        mainFrame.add(rightPanel);
        mainFrame.add(handler);
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }
}
