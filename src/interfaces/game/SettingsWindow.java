package interfaces.game;

import static interfaces.game.Control.hand;
import java.awt.Font;
import options.*;
import utility.GameSettings;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {

    public static int width = 450;
    public static int height = 275;
    private final JComboBox<String> gui_style_drop_down;
    private final JComboBox<String> player1_color_drop_down;
    private final JComboBox<String> player2_color_drop_down;
    private final JButton apply;
    private final JButton cancel;
    static java.awt.Color bgColor = new java.awt.Color(243, 121, 68);
    static java.awt.Color buttonsColor = new java.awt.Color(198, 70, 15);

    public SettingsWindow() {
        super("Settings");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);

        EventHandler handler = new EventHandler();

        GuiStyle selectedGuiStyle = Control.gameSettings.getGuiStyle();
        GameMode selectedMode = Control.gameSettings.getGameMode();
        Color selectedPlayer1Color = Control.gameSettings.getPlayer1Color();
        Color selectedPlayer2Color = Control.gameSettings.getPlayer2Color();
        int inARow = Control.gameSettings.getCheckersInARow();

        JLabel guiStyleLabel = new JLabel("GUI style");
        guiStyleLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 14));
        JLabel player1ColorLabel = new JLabel("Player 1 color");
        player1ColorLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 14));
        JLabel player2ColorLabel = new JLabel("Player 2 color");
        player2ColorLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 14));

        add(guiStyleLabel);
        add(player1ColorLabel);
        add(player2ColorLabel);

        gui_style_drop_down = new JComboBox<>();
        gui_style_drop_down.setCursor(hand);
        gui_style_drop_down.setFont(new Font("Showcard Gothic", Font.PLAIN, 10));
        gui_style_drop_down.addItem("System style");
        gui_style_drop_down.addItem("Cross-platform style");
        gui_style_drop_down.addItem("Nimbus style");

        if (selectedGuiStyle == GuiStyle.SYSTEM_STYLE) {
            gui_style_drop_down.setSelectedIndex(0);
        } else if (selectedGuiStyle == GuiStyle.CROSS_PLATFORM_STYLE) {
            gui_style_drop_down.setSelectedIndex(1);
        } else if (selectedGuiStyle == GuiStyle.NIMBUS_STYLE) {
            gui_style_drop_down.setSelectedIndex(2);
        }

        player1_color_drop_down = new JComboBox<>();
        player1_color_drop_down.setCursor(hand);
        player1_color_drop_down.setFont(new Font("Showcard Gothic", Font.PLAIN, 10));
        player1_color_drop_down.addItem("Red");
        player1_color_drop_down.addItem("Yellow");
        player1_color_drop_down.addItem("Black");
        player1_color_drop_down.addItem("Green");
        player1_color_drop_down.addItem("Blue");
        player1_color_drop_down.addItem("Purple");

        if (selectedPlayer1Color == Color.RED) {
            player1_color_drop_down.setSelectedIndex(0);
        } else if (selectedPlayer1Color == Color.YELLOW) {
            player1_color_drop_down.setSelectedIndex(1);
        } else if (selectedPlayer1Color == Color.BLACK) {
            player1_color_drop_down.setSelectedIndex(2);
        } else if (selectedPlayer1Color == Color.GREEN) {
            player1_color_drop_down.setSelectedIndex(3);
        } else if (selectedPlayer1Color == Color.BLUE) {
            player1_color_drop_down.setSelectedIndex(4);
        } else if (selectedPlayer1Color == Color.PURPLE) {
            player1_color_drop_down.setSelectedIndex(5);
        }

        player2_color_drop_down = new JComboBox<>();
        player2_color_drop_down.setCursor(hand);
        player2_color_drop_down.setFont(new Font("Showcard Gothic", Font.PLAIN, 10));
        player2_color_drop_down.addItem("Red");
        player2_color_drop_down.addItem("Yellow");
        player2_color_drop_down.addItem("Black");
        player2_color_drop_down.addItem("Green");
        player2_color_drop_down.addItem("Blue");
        player2_color_drop_down.addItem("Purple");

        if (selectedPlayer2Color == Color.RED) {
            player2_color_drop_down.setSelectedIndex(0);
        } else if (selectedPlayer2Color == Color.YELLOW) {
            player2_color_drop_down.setSelectedIndex(1);
        } else if (selectedPlayer2Color == Color.BLACK) {
            player2_color_drop_down.setSelectedIndex(2);
        } else if (selectedPlayer2Color == Color.GREEN) {
            player2_color_drop_down.setSelectedIndex(3);
        } else if (selectedPlayer2Color == Color.BLUE) {
            player2_color_drop_down.setSelectedIndex(4);
        } else if (selectedPlayer2Color == Color.PURPLE) {
            player2_color_drop_down.setSelectedIndex(5);
        }

        add(gui_style_drop_down);
        add(player1_color_drop_down);
        add(player2_color_drop_down);

        guiStyleLabel.setBounds(25, 45, 200, 25);
        player1ColorLabel.setBounds(25, 80, 200, 25);
        player2ColorLabel.setBounds(25, 115, 200, 25);

        gui_style_drop_down.setBounds(195, 45, 200, 25);
        player1_color_drop_down.setBounds(195, 80, 200, 25);
        player2_color_drop_down.setBounds(195, 115, 200, 25);

        apply = new JButton("Apply");
        apply.setCursor(hand);
        apply.setFont(new Font("Showcard gothic", Font.PLAIN, 15));
        cancel = new JButton("Cancel");
        cancel.setCursor(hand);
        cancel.setFont(new Font("Showcard gothic", Font.PLAIN, 15));
        add(apply);
        add(cancel);

        int distance = 50;
        apply.setBounds((width / 2) - 110 - (distance / 2), 185, 100, 30);
        apply.addActionListener(handler);
        cancel.setBounds((width / 2) - 10 + (distance / 2), 185, 100, 30);
        cancel.addActionListener(handler);
    }

    private class EventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {

            if (ev.getSource() == cancel) {
                dispose();
            } else if (ev.getSource() == apply) {
                try {

                    GuiStyle guiStyle
                            = GuiStyle.valueOf(gui_style_drop_down.getSelectedItem().toString().toUpperCase().replace("-", "_").replace(" ", "_"));
                    Color player1Color
                            = Color.valueOf(player1_color_drop_down.getSelectedItem().toString().toUpperCase());
                    Color player2Color
                            = Color.valueOf(player2_color_drop_down.getSelectedItem().toString().toUpperCase());

                    if (player1Color == player2Color) {
                        JOptionPane.showMessageDialog(null,
                                "Player 1 and Player 2 cannot have the same color of checkers!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Change game parameters based on settings.
                    Control.newGameSettings = new GameSettings(guiStyle, player1Color, player2Color);

                    JOptionPane.showMessageDialog(null,
                            "Game settings have been changed.\nThe changes will be applied in the next new game.",
                            "Notice", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception e) {
                    System.err.println("ERROR : " + e.getMessage());
                }

            }  // else if.

        }  // action performed.

    }  // inner class.

}  // class end.
