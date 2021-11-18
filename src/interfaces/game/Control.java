package interfaces.game;

import java.awt.*;
import static java.awt.Cursor.HAND_CURSOR;
import java.awt.event.*;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.Border;
import logic.Board;
import options.*;
import utility.Constants;
import static utility.Constants.CONNECT_4_BOARD_IMG_PATH;
import static utility.Constants.CONNECT_4_CHECKERS_IN_A_ROW;
import static utility.Constants.DEFAULT_CONNECT_4_HEIGHT;
import static utility.Constants.DEFAULT_CONNECT_4_WIDTH;
import utility.GameSettings;
import utility.ResourceLoader;

public class Control {

    public static GameSettings gameSettings = new GameSettings();
    public static GameSettings newGameSettings = new GameSettings(gameSettings);

    static int NUM_OF_ROWS;
    static int NUM_OF_COLUMNS;
    static int CHECKERS_IN_A_ROW;

    static Board board;
    static JFrame frameMainWindow;

    static JPanel panelMain;
    static JPanel panelBoardNumbers;
    static JLayeredPane layeredGameBoard;

    static JButton[] buttons;

    static JLabel turnMessage;

    static java.awt.Color myColor = new java.awt.Color(33, 24, 4);
    static java.awt.Color dialogBg = new java.awt.Color(252, 177, 101);
    static Cursor hand = new Cursor(12);

    // Player 1 symbol: X. Plays first.
    // Player 2 symbol: O.
    // These Stack objects are used for the "Undo" and "Redo" functionalities.
    static Stack<Board> undoBoards = new Stack<>();
    static Stack<JLabel> undoCheckerLabels = new Stack<>();
    static Stack<Board> redoBoards = new Stack<>();
    static Stack<JLabel> redoCheckerLabels = new Stack<>();

    // Menu bars and items
    static JMenuBar menuBar;
    static JMenu fileMenu;
    static JMenuItem newGameItem;
    static JMenuItem undoItem;
    static JMenuItem redoItem;
    static JMenuItem saveGameItem;
    static JMenuItem restoreSavedGameItem;
    static JMenuItem loadNovelPositionItem;
    static JMenuItem exportToGifItem;
    static JMenuItem settingsItem;
    static JMenuItem exitItem;
    static JMenu helpMenu;
    static JMenuItem howToPlayItem;
    static JMenuItem aboutItem;

    static JButton undoButton;
    static JButton redoButton;
    static boolean pause;

    public Control() {

    }

    // Add the menu bars and items to the window.
    private static void AddMenus() {

        // Add the menu bar.
        menuBar = new JMenuBar();
        menuBar.setBackground(dialogBg);

        fileMenu = new JMenu("File");
        fileMenu.setCursor(hand);
        fileMenu.setFont(new Font("Arial", Font.BOLD, 12));
        newGameItem = new JMenuItem("New Game");
        newGameItem.setCursor(hand);
        newGameItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
        undoItem = new JMenuItem("Undo    Ctrl+Z");
        undoItem.setCursor(hand);
        undoItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
        redoItem = new JMenuItem("Redo    Ctrl+Y");
        redoItem.setCursor(hand);
        redoItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
        settingsItem = new JMenuItem("Settings");
        settingsItem.setCursor(hand);
        settingsItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
        exitItem = new JMenuItem("Exit");
        exitItem.setCursor(hand);
        exitItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));

        helpMenu = new JMenu("Help");
        helpMenu.setCursor(hand);
        helpMenu.setFont(new Font("Arial", Font.BOLD, 12));
        howToPlayItem = new JMenuItem("How to Play");
        howToPlayItem.setCursor(hand);
        howToPlayItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));
        aboutItem = new JMenuItem("About");
        aboutItem.setCursor(hand);
        aboutItem.setFont(new Font("Showcard Gothic", Font.PLAIN, 12));

        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        newGameItem.addActionListener(e -> createNewGame());

        undoItem.addActionListener(e -> undo());

        redoItem.addActionListener(e -> redo());

        settingsItem.addActionListener(e -> {
            SettingsWindow settings = new SettingsWindow();
            settings.setVisible(true);
        });

        exitItem.addActionListener(e -> System.exit(0));

        howToPlayItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "Click on the buttons or press 1-" + NUM_OF_COLUMNS + " on your keyboard to insert a new checker."
                + "\nTo win you must place " + CHECKERS_IN_A_ROW + " checkers in an row, horizontally, vertically or diagonally.",
                "How to Play", JOptionPane.INFORMATION_MESSAGE));

        aboutItem.addActionListener(e -> {
            JLabel label = new JLabel("<html><center> Created by: Carlos Mendoza & Dario Mejia<br>");
            JOptionPane.showMessageDialog(frameMainWindow, label, "About", JOptionPane.INFORMATION_MESSAGE);
        });

        fileMenu.add(newGameItem);
        fileMenu.add(undoItem);
        fileMenu.add(redoItem);
        fileMenu.add(settingsItem);
        fileMenu.add(exitItem);

        helpMenu.add(howToPlayItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        frameMainWindow.setJMenuBar(menuBar);
        frameMainWindow.getContentPane().setBackground(dialogBg);
        panelMain.setBackground(dialogBg);
        panelBoardNumbers.setBackground(dialogBg);

        // Make the board visible after adding the menus.
        frameMainWindow.setVisible(true);

    }

    // This is the main Connect-4 board.
    public static JLayeredPane createLayeredBoard() {
        layeredGameBoard = new JLayeredPane();

        ImageIcon imageBoard = null;
        if (gameSettings.getCheckersInARow() == CONNECT_4_CHECKERS_IN_A_ROW) {
            layeredGameBoard.setPreferredSize(new Dimension(DEFAULT_CONNECT_4_WIDTH, DEFAULT_CONNECT_4_HEIGHT));
            layeredGameBoard.setBorder(BorderFactory.createTitledBorder("4 in a Line"));
            ((javax.swing.border.TitledBorder) layeredGameBoard.getBorder()).setTitleFont(new Font("Showcard Gothic", Font.PLAIN, 10));
            imageBoard = new ImageIcon(ResourceLoader.load(CONNECT_4_BOARD_IMG_PATH));
        }

        JLabel imageBoardLabel = new JLabel(imageBoard);

        imageBoardLabel.setBounds(20, 20, imageBoard.getIconWidth(), imageBoard.getIconHeight());
        layeredGameBoard.add(imageBoardLabel, 0, 1);

        return layeredGameBoard;
    }

    private static void undo() {
        if (!undoBoards.isEmpty()) {
            // This is the "undo" implementation for "Human Vs Human" mode.
            if (gameSettings.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
                try {
                    board.setGameOver(false);

                    setAllButtonsEnabled(true);

                    if (frameMainWindow.getKeyListeners().length == 0) {
                        frameMainWindow.addKeyListener(gameKeyListener);
                    }

                    JLabel previousCheckerLabel = undoCheckerLabels.pop();

                    redoBoards.push(new Board(board));
                    redoCheckerLabels.push(previousCheckerLabel);

                    board = undoBoards.pop();
                    layeredGameBoard.remove(previousCheckerLabel);

                    turnMessage.setText("Turn: " + board.getTurn());
                    frameMainWindow.paint(frameMainWindow.getGraphics());
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("No move has been made yet!");
                    System.err.flush();
                }
            }

            if (undoBoards.isEmpty()) {
                undoItem.setEnabled(false);
                undoButton.setEnabled(false);
            }

            redoItem.setEnabled(true);
            redoButton.setEnabled(true);
            if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                redoButton.setVisible(true);
            }

            System.out.println("Turn: " + board.getTurn());
            Board.printBoard(board.getGameBoard());
        }
    }

    private static void redo() {
        if (!redoBoards.isEmpty()) {
            // This is the "redo" implementation for "Human Vs Human" mode.
            if (gameSettings.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
                try {
                    board.setGameOver(false);

                    setAllButtonsEnabled(true);

                    if (frameMainWindow.getKeyListeners().length == 0) {
                        frameMainWindow.addKeyListener(gameKeyListener);
                    }

                    JLabel redoCheckerLabel = redoCheckerLabels.pop();

                    undoBoards.push(new Board(board));
                    undoCheckerLabels.push(redoCheckerLabel);

                    board = new Board(redoBoards.pop());
                    layeredGameBoard.add(redoCheckerLabel, 0, 0);

                    turnMessage.setText("Turn: " + board.getTurn());
                    frameMainWindow.paint(frameMainWindow.getGraphics());

                    boolean isGameOver = board.checkForGameOver();
                    if (isGameOver) {
                        gameOver();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("There is no move to redo!");
                    System.err.flush();
                }
            }

            if (redoBoards.isEmpty()) {
                redoItem.setEnabled(false);
                redoButton.setEnabled(false);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    redoButton.setVisible(false);
                }
            }

            undoItem.setEnabled(true);
            undoButton.setEnabled(true);
            if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                undoButton.setVisible(true);
            }

            System.out.println("Turn: " + board.getTurn());
            Board.printBoard(board.getGameBoard());
        }
    }
    public static KeyListener gameKeyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            String keyText = KeyEvent.getKeyText(e.getKeyCode());

            for (int i = 0; i < gameSettings.getNumOfColumns(); i++) {
                if (keyText.equals(i + 1 + "")) {
                    undoBoards.push(new Board(board));
                    makeMove(i);

                    if (!board.isOverflow()) {
                        boolean isGameOver = game();
                    }
                    break;
                }
            }
            if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)
                    && (e.getKeyCode() == KeyEvent.VK_Z)) {
                undo();
            } else if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)
                    && (e.getKeyCode() == KeyEvent.VK_Y)) {
                redo();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };

    // To be called when the game starts for the first time
    // or a new game starts.
    public static void createNewGame() {
        gameSettings = new GameSettings(newGameSettings);

        NUM_OF_ROWS = gameSettings.getNumOfRows();
        NUM_OF_COLUMNS = gameSettings.getNumOfColumns();
        CHECKERS_IN_A_ROW = gameSettings.getCheckersInARow();

        buttons = new JButton[NUM_OF_COLUMNS];
        for (int i = 0; i < NUM_OF_COLUMNS; i++) {
            buttons[i] = new JButton(i + 1 + "");
            buttons[i].setFont(new Font("Showcard gothic", Font.PLAIN, 25));
            buttons[i].setBackground(dialogBg);
            buttons[i].setFocusable(false);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorder(new RoundedBorder(8)); //10 is the radius
            buttons[i].setForeground(myColor);
            buttons[i].setCursor(hand);
        }

        configureGuiStyle();

        setAllButtonsEnabled(true);

        board = new Board(gameSettings.getNumOfRows(), gameSettings.getNumOfColumns(), gameSettings.getCheckersInARow());

        undoBoards.clear();
        undoCheckerLabels.clear();

        redoBoards.clear();
        redoCheckerLabels.clear();

        if (frameMainWindow != null) {
            frameMainWindow.dispose();
        }
        if (gameSettings.getCheckersInARow() == CONNECT_4_CHECKERS_IN_A_ROW) {
            frameMainWindow = new JFrame("4 In A Line!");
            // make the main window appear on the center
            centerWindow(frameMainWindow, DEFAULT_CONNECT_4_WIDTH, DEFAULT_CONNECT_4_HEIGHT);
        }
        Component compMainWindowContents = createContentComponents();
        frameMainWindow.getContentPane().add(compMainWindowContents, BorderLayout.CENTER);

        frameMainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        if (frameMainWindow.getKeyListeners().length == 0) {
            frameMainWindow.addKeyListener(gameKeyListener);
        }

        frameMainWindow.setFocusable(true);

        // show window
        frameMainWindow.pack();
        // Makes the board visible before adding menus.
        // frameMainWindow.setVisible(true);

        // Add the turn label.
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        frameMainWindow.add(tools, BorderLayout.PAGE_END);
        turnMessage = new JLabel("Turn: " + board.getTurn());
        turnMessage.setFont(new Font("Showcard gothic", Font.PLAIN, 10));
        tools.add(turnMessage);

        undoButton = new JButton("<<");
        undoButton.setCursor(hand);
        undoButton.setFont(new Font("Showcard gothic", Font.PLAIN, 10));
        JButton pauseButton = new JButton("Pause");
        pauseButton.setCursor(hand);
        pauseButton.setFont(new Font("Showcard gothic", Font.PLAIN, 10));
        JButton startButton = new JButton("Resume");
        startButton.setCursor(hand);
        startButton.setFont(new Font("Showcard gothic", Font.PLAIN, 10));
        redoButton = new JButton(">>");
        redoButton.setCursor(hand);
        redoButton.setFont(new Font("Showcard gothic", Font.PLAIN, 10));
        JButton resetButton = new JButton("Reset");
        resetButton.setCursor(hand);
        resetButton.setFont(new Font("Showcard gothic", Font.PLAIN, 10));

        undoButton.setEnabled(false);
        if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
            undoButton.setVisible(false);
        }
        redoButton.setEnabled(false);
        if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
            redoButton.setVisible(false);
        }

        undoButton.addActionListener(e -> {
            if (!pause) {
                undo();
            }
        });

        pauseButton.addActionListener(e -> {
            if (!pause) {
                setAllButtonsEnabled(false);
                frameMainWindow.removeKeyListener(frameMainWindow.getKeyListeners()[0]);
                pause = true;
                undoButton.setEnabled(false);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    undoButton.setVisible(false);
                }
                pauseButton.setEnabled(false);
                redoButton.setEnabled(false);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    redoButton.setVisible(false);
                }
                startButton.setEnabled(true);
                resetButton.setEnabled(false);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    resetButton.setVisible(false);
                }
            }
        });

        redoButton.addActionListener(e -> {
            if (!pause) {
                redo();
            }
        });

        startButton.addActionListener(e -> {
            if (pause) {
                setAllButtonsEnabled(true);

                if (frameMainWindow.getKeyListeners().length == 0) {
                    frameMainWindow.addKeyListener(gameKeyListener);
                }

                pause = false;
                if (undoBoards.isEmpty()) {
                    undoButton.setEnabled(false);
                    if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                        undoButton.setVisible(false);
                    }
                } else {
                    undoButton.setEnabled(true);
                    if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                        undoButton.setVisible(true);
                    }
                }
                pauseButton.setEnabled(true);
                if (redoBoards.isEmpty()) {
                    redoButton.setEnabled(false);
                    if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                        redoButton.setVisible(false);
                    }
                } else {
                    redoButton.setEnabled(true);
                    if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                        redoButton.setVisible(true);
                    }
                }
                startButton.setEnabled(false);
                resetButton.setEnabled(true);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    resetButton.setVisible(true);
                }
            }
        });

        resetButton.addActionListener(e -> {
            if (!pause) {
                setAllButtonsEnabled(false);
                for (KeyListener keyListener : frameMainWindow.getKeyListeners()) {
                    frameMainWindow.removeKeyListener(keyListener);
                }
                pause = false;
                undoButton.setEnabled(false);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    undoButton.setVisible(false);
                }
                pauseButton.setEnabled(true);
                redoButton.setEnabled(false);
                if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                    redoButton.setVisible(false);
                }
                startButton.setEnabled(false);
                createNewGame();
            }
        });

        undoButton.setFocusable(false);
        pauseButton.setFocusable(false);
        redoButton.setFocusable(false);
        startButton.setFocusable(false);
        resetButton.setFocusable(false);

        startButton.setEnabled(false);

        tools.setLayout(new FlowLayout(FlowLayout.CENTER));
        tools.add(new JLabel(" "));
        tools.add(undoButton);
        tools.add(new JLabel(" "));
        tools.add(pauseButton);
        tools.add(new JLabel(" "));
        tools.add(startButton);
        tools.add(new JLabel(" "));
        tools.add(redoButton);
        tools.add(new JLabel(" "));
        tools.add(resetButton);

        AddMenus();

        System.out.println("Turn: " + board.getTurn());
        Board.printBoard(board.getGameBoard());

    }

    private static void configureGuiStyle() {
        try {
            if (gameSettings.getGuiStyle() == GuiStyle.SYSTEM_STYLE) {
                // Option 1
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else if (gameSettings.getGuiStyle() == GuiStyle.CROSS_PLATFORM_STYLE) {
                // Option 2
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } else if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                // Option 3
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
        } catch (Exception e1) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // It centers the window on screen.
    public static void centerWindow(Window frame, int width, int height) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dimension.getWidth() - frame.getWidth() - width) / 2;
        int y = (int) (dimension.getHeight() - frame.getHeight() - height) / 2;
        frame.setLocation(x, y);
    }

    // It finds which player plays next and makes a move on the board.
    public static void makeMove(int col) {
        board.setOverflow(false);

        int previousRow = board.getLastMove().getRow();
        int previousCol = board.getLastMove().getColumn();
        int previousLetter = board.getLastPlayer();

        if (board.getLastPlayer() == Constants.P2) {
            board.makeMove(col, Constants.P1);
        } else {
            board.makeMove(col, Constants.P2);
        }

        if (board.isOverflow()) {
            board.getLastMove().setRow(previousRow);
            board.getLastMove().setColumn(previousCol);
            board.setLastPlayer(previousLetter);

            undoBoards.pop();
        }

    }

    // It places a checker on the board.
    public static void placeChecker(options.Color color, int row, int col) {
        String colorString = String.valueOf(color).charAt(0) + String.valueOf(color).toLowerCase().substring(1);
        int xOffset = 75 * col;
        int yOffset = 75 * row;
        ImageIcon checkerIcon = new ImageIcon(ResourceLoader.load("images/" + colorString + ".png"));

        JLabel checkerLabel = new JLabel(checkerIcon);
        checkerLabel.setBounds(27 + xOffset, 27 + yOffset, checkerIcon.getIconWidth(), checkerIcon.getIconHeight());
        layeredGameBoard.add(checkerLabel, 0, 0);

        undoCheckerLabels.push(checkerLabel);
    }

    // Gets called after makeMove(int, col) is called.
    public static boolean game() {
        turnMessage.setText("Turn: " + board.getTurn());

        int row = board.getLastMove().getRow();
        int col = board.getLastMove().getColumn();
        int currentPlayer = board.getLastPlayer();

        if (currentPlayer == Constants.P1) {
            // It places a checker in the corresponding [row][col] of the GUI.
            placeChecker(gameSettings.getPlayer1Color(), row, col);
        }

        if (currentPlayer == Constants.P2) {
            // It places a checker in the corresponding [row][col] of the GUI.
            placeChecker(gameSettings.getPlayer2Color(), row, col);
        }

        System.out.println("Turn: " + board.getTurn());
        Board.printBoard(board.getGameBoard());

        boolean isGameOver = board.checkForGameOver();
        if (isGameOver) {
            gameOver();
        } else {
            undoButton.setEnabled(true);
            if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
                undoButton.setVisible(true);
            }
            undoItem.setEnabled(true);
        }

        redoBoards.clear();
        redoCheckerLabels.clear();
        redoButton.setEnabled(false);
        if (gameSettings.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
            redoButton.setVisible(false);
        }
        redoItem.setEnabled(false);

        return isGameOver;
    }

    public static void setAllButtonsEnabled(boolean b) {
        if (b) {

            for (int i = 0; i < buttons.length; i++) {
                JButton button = buttons[i];
                int column = i;

                if (button.getActionListeners().length == 0) {
                    button.addActionListener(e -> {
                        undoBoards.push(new Board(board));
                        makeMove(column);

                        if (!board.isOverflow()) {
                            boolean isGameOver = game();
                        }
                        frameMainWindow.requestFocusInWindow();
                    });
                }
            }

        } else {

            for (JButton button : buttons) {
                for (ActionListener actionListener : button.getActionListeners()) {
                    button.removeActionListener(actionListener);
                }
            }

        }
    }

    /**
     * It returns a component to be drawn by main window. This function creates
     * the main window components. It calls the "actionListener" function, when
     * a click on a button is made.
     */
    public static Component createContentComponents() {

        // Create a panel to set up the board buttons.
        panelBoardNumbers = new JPanel();
        panelBoardNumbers.setLayout(new GridLayout(1, NUM_OF_COLUMNS, NUM_OF_ROWS, 4));
        panelBoardNumbers.setBorder(BorderFactory.createEmptyBorder(2, 22, 2, 22));

        for (JButton button : buttons) {
            panelBoardNumbers.add(button);
        }

        // main Connect-4 board creation
        layeredGameBoard = createLayeredBoard();

        // panel creation to store all the elements of the board
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        panelMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // add button and main board components to panelMain
        panelMain.add(panelBoardNumbers, BorderLayout.NORTH);
        panelMain.add(layeredGameBoard, BorderLayout.CENTER);

        frameMainWindow.setResizable(false);
        return panelMain;
    }

    // It gets called only of the game is over.
    // We can check if the game is over by calling the method "checkGameOver()"
    // of the class "Board".
    public static void gameOver() {
        board.setGameOver(true);

        int choice = 0;
        if (board.getWinner() == Constants.P1) {
            if (gameSettings.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
                choice = JOptionPane.showConfirmDialog(null,
                        "Player 1 wins! Start a new game?",
                        "Game Over", JOptionPane.YES_NO_OPTION);
            }
        } else if (board.getWinner() == Constants.P2) {
            if (gameSettings.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
                choice = JOptionPane.showConfirmDialog(null,
                        "Player 2 wins! Start a new game?",
                        "Game Over", JOptionPane.YES_NO_OPTION);
            }
        } else if (board.checkForDraw()) {
            choice = JOptionPane.showConfirmDialog(null,
                    "It's a draw! Start a new game?",
                    "Game Over", JOptionPane.YES_NO_OPTION);
        }

        // Disable buttons
        setAllButtonsEnabled(false);

        // Remove key listener
        for (KeyListener keyListener : frameMainWindow.getKeyListeners()) {
            frameMainWindow.removeKeyListener(keyListener);
        }

        if (choice == JOptionPane.YES_OPTION) {
            createNewGame();
        }

    }

    private static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
