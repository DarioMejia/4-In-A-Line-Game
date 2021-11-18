package utility;

import options.*;

public class GameSettings {

    private GuiStyle guiStyle;
    private GameMode gameMode;
    private Color player1Color;
    private Color player2Color;

    private int numOfRows;
    private int numOfColumns;
    private int checkersInARow;

    /* Default values */
    public GameSettings() {
        guiStyle = GuiStyle.SYSTEM_STYLE;
        gameMode = GameMode.HUMAN_VS_HUMAN;
        player1Color = Color.RED;
        player2Color = Color.YELLOW;

        numOfRows = Constants.CONNECT_4_NUM_OF_ROWS;
        numOfColumns = Constants.CONNECT_4_NUM_OF_COLUMNS;
        checkersInARow = Constants.CONNECT_4_CHECKERS_IN_A_ROW;
    }

    public GameSettings(GameSettings otherGameParameters) {
        this.guiStyle = otherGameParameters.guiStyle;
        this.gameMode = otherGameParameters.gameMode;
        this.player1Color = otherGameParameters.player1Color;
        this.player2Color = otherGameParameters.player2Color;
        this.numOfRows = otherGameParameters.numOfRows;
        this.numOfColumns = otherGameParameters.numOfColumns;
        this.checkersInARow = otherGameParameters.checkersInARow;
    }

    public GameSettings(GuiStyle guiStyle, GameMode gameMode, Color player1Color, 
            Color player2Color, int numOfRows, int numOfColumns, int checkersInARow) {
        this.guiStyle = guiStyle;
        this.gameMode = gameMode;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.checkersInARow = checkersInARow;
    }

    public GameSettings(GuiStyle guiStyle, Color player1Color, Color player2Color) {
        this.guiStyle = guiStyle;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        gameMode = GameMode.HUMAN_VS_HUMAN;
        numOfRows = Constants.CONNECT_4_NUM_OF_ROWS;
        numOfColumns = Constants.CONNECT_4_NUM_OF_COLUMNS;
        checkersInARow = Constants.CONNECT_4_CHECKERS_IN_A_ROW;
    }

    public GuiStyle getGuiStyle() {
        return guiStyle;
    }

    public void setGuiStyle(GuiStyle guiStyle) {
        this.guiStyle = guiStyle;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Color getPlayer1Color() {
        return player1Color;
    }

    public void setPlayer1Color(Color player1Color) {
        this.player1Color = player1Color;
    }

    public Color getPlayer2Color() {
        return player2Color;
    }

    public void setPlayer2Color(Color player2Color) {
        this.player2Color = player2Color;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public void setNumOfColumns(int numOfColumns) {
        this.numOfColumns = numOfColumns;
    }

    public int getCheckersInARow() {
        return checkersInARow;
    }

    public void setCheckersInARow(int checkersInARow) {
        this.checkersInARow = checkersInARow;
    }

}
