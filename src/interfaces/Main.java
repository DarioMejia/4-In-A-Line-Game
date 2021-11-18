package interfaces;

import interfaces.game.Control;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main {

    JFrame frame;
    Balls balls;
    PanelListener panelListener;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }

                frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                balls = new Balls();
                balls.setBackground(Color.white);
                frame.add(balls);
                frame.setSize(570, 550);
                frame.setResizable(false);
                frame.requestFocus();
                frame.setLocationRelativeTo(null);
                frame.setUndecorated(true);
                frame.setVisible(true);
                
                panelListener = new PanelListener();
                balls.addMouseListener(panelListener);
                new Thread(new BounceEngine(balls)).start();

            }
        });
    }

    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

    public class Balls extends JPanel {

        public ArrayList<Ball> ballsUp;
        JLabel title = new JLabel("4 In A Line");
        BlinkLabel clickMessage = new BlinkLabel("Click to continue!");

        public Balls() {
            ballsUp = new ArrayList<>(25);
            title.setFont(new Font("Showcard Gothic", Font.PLAIN, 65));
            title.setOpaque(true);
            title.setBackground(Color.yellow);
            title.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            clickMessage.setFont(new Font("Showcard Gothic", Font.PLAIN, 15));
            clickMessage.setBlinking(true);
            this.add(title);
            this.add(clickMessage);

            for (int index = 0; index < 10 + random(90); index++) {
                ballsUp.add(new Ball(new Color(random(255), random(255), random(255))));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Ball ball : ballsUp) {
                ball.paint(g2d);
            }
            g2d.dispose();
        }

        public ArrayList<Ball> getBalls() {
            return ballsUp;
        }
    }

    public class BounceEngine implements Runnable {

        private Balls parent;
        final int MAX_Y = 225;

        public BounceEngine(Balls parent) {
            this.parent = parent;
        }

        @Override
        public void run() {

            int width = getParent().getWidth();
            int height = getParent().getHeight();

            int xT = 125;
            int yT = -20;  //label title start positions
            boolean checked = true;

            while (checked) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                }
                getParent().clickMessage.setLocation(135, -20);
                getParent().title.setLocation(xT, yT);
                yT += 10;
                if (yT > MAX_Y) {
                    checked = false;
                }
            }
            getParent().clickMessage.setLocation(200, 525);
            // Randomize the starting position...
            for (Ball ball : getParent().getBalls()) {
                int x = random(width);
                int y = random(height);

                Dimension size = ball.getSize();

                if (x + size.width > width) {
                    x = width - size.width;
                }
                if (y + size.height > height) {
                    y = height - size.height;
                }

                ball.setLocation(new Point(x, y));

            }

            while (getParent().isVisible()) {

                // Repaint the balls pen...
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        getParent().repaint();
                    }
                });

                // This is a little dangrous, as it's possible
                // for a repaint to occur while we're updating...
                for (Ball ball : getParent().getBalls()) {
                    move(ball);
                }

                // Some small delay...
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                }

            }

        }

        public Balls getParent() {
            return parent;
        }

        public void move(Ball ball) {

            Point p = ball.getLocation();
            Point speed = ball.getSpeed();
            Dimension size = ball.getSize();

            int vx = speed.x;
            int vy = speed.y;

            int x = p.x;
            int y = p.y;

            if (x + vx < 0 || x + size.width + vx > getParent().getWidth()) {
                vx *= -1;
            }
            if (y + vy < 0 || y + size.height + vy > getParent().getHeight()) {
                vy *= -1;
            }
            x += vx;
            y += vy;

            ball.setSpeed(new Point(vx, vy));
            ball.setLocation(new Point(x, y));

        }
    }

    public class BlinkLabel extends JLabel {

        private static final long serialVersionUID = 1L;

        private static final int BLINKING_RATE = 1000; // in ms

        private boolean blinkingOn = true;

        public BlinkLabel(String text) {
            super(text);
            Timer timer = new Timer(BLINKING_RATE, new TimerListener(this));
            timer.setInitialDelay(0);
            timer.start();
        }

        public void setBlinking(boolean flag) {
            this.blinkingOn = flag;
        }

        public boolean getBlinking(boolean flag) {
            return this.blinkingOn;
        }

        private class TimerListener implements ActionListener {

            private BlinkLabel bl;
            private Color bg;
            private Color fg;
            private boolean isForeground = true;

            public TimerListener(BlinkLabel bl) {
                this.bl = bl;
                fg = bl.getForeground();
                bg = bl.getBackground();
            }

            public void actionPerformed(ActionEvent e) {
                if (bl.blinkingOn) {
                    if (isForeground) {
                        bl.setForeground(fg);
                    } else {
                        bl.setForeground(bg);
                    }
                    isForeground = !isForeground;
                } else {
                    // here we want to make sure that the label is visible
                    // if the blinking is off.
                    if (isForeground) {
                        bl.setForeground(fg);
                        isForeground = false;
                    }
                }
            }

        }
    }

    public class Ball {

        private Color color;
        private Point location;
        private Dimension size;
        private Point speed;

        public Ball(Color color) {

            setColor(color);

            speed = new Point(10 - random(20), 10 - random(20));
            size = new Dimension(30, 30);

        }

        public Dimension getSize() {
            return size;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setLocation(Point location) {
            this.location = location;
        }

        public Color getColor() {
            return color;
        }

        public Point getLocation() {
            return location;
        }

        public Point getSpeed() {
            return speed;
        }

        public void setSpeed(Point speed) {
            this.speed = speed;
        }

        protected void paint(Graphics2D g2d) {

            Point p = getLocation();
            if (p != null) {
                g2d.setColor(getColor());
                Dimension size = getSize();
                g2d.fillOval(p.x, p.y, size.width, size.height);
            }

        }
    }

    private class PanelListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent event) {
            balls.setVisible(false);
            frame.dispose();
            Control control = new Control();
            control.createNewGame();
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
        }

    }

}
