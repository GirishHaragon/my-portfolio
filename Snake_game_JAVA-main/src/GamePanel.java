import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int OBJECT_SIZE = 20;
	private static final int OBJECT_NUMBER = (WIDTH * HEIGHT) / OBJECT_SIZE;
	private static final int FPS = 140;
	// Coordinates
	private int[] x = new int[OBJECT_NUMBER];
	private int[] y = new int[OBJECT_NUMBER];
	private int bodyPart = 3;
	private int appleEaten = 0;
	private int appleX;
	private int appleY;
	private Direction direction = Direction.RIGHT;
	private boolean running = false;
	private Timer timer;
	private int highScore;

	public GamePanel() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	private void startGame() {
		running = true;
		timer = new Timer(FPS, this);
		generateApple();
		timer.start();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}

	private void draw(Graphics graphics) {
		if (running) {
			/*
			 * DRAW GRID
			 * 
			 * for (int i = 0; i < HEIGHT / OBJECT_SIZE; i++) { graphics.drawLine(i *
			 * OBJECT_SIZE, 0, i * OBJECT_SIZE, HEIGHT); graphics.drawLine(0, i *
			 * OBJECT_SIZE, WIDTH, i * OBJECT_SIZE); }
			 */
			graphics.setColor(Color.RED);
			graphics.fillRect(appleX, appleY, OBJECT_SIZE, OBJECT_SIZE);
			graphics.setColor(Color.WHITE);
			graphics.setFont(new Font("arial", Font.BOLD, 20));
			graphics.drawString("Score: " + appleEaten, 20, 20);
			for (int i = 0; i < bodyPart; i++) {
				if (i == 0) {
					graphics.setColor(Color.GREEN);
				} else {
					graphics.setColor(new Color((int) (Math.random() * 0x1000000)));
				}
				graphics.fillRect(x[i], y[i], OBJECT_SIZE, OBJECT_SIZE);
			}
		} else {
			gameOver(graphics);
		}
	}

	public void generateApple() {
		appleX = ThreadLocalRandom.current().nextInt(WIDTH / OBJECT_SIZE) * OBJECT_SIZE;
		appleY = ThreadLocalRandom.current().nextInt(HEIGHT / OBJECT_SIZE) * OBJECT_SIZE;
	}

	private void move() {
		for (int i = bodyPart; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
			case UP : y[0] = y[0] - OBJECT_SIZE;
			break;
			case DOWN : y[0] = y[0] + OBJECT_SIZE;
			break;
			case LEFT : x[0] = x[0] - OBJECT_SIZE;
			break;
			case RIGHT : x[0] = x[0] + OBJECT_SIZE;
			break;
		}
	}

	private void eatApple() {
		if (x[0] == appleX && y[0] == appleY) {
			generateApple();
			bodyPart++;
			appleEaten++;
			timer.setDelay(timer.getDelay() - 5);
		}
	}

	private void collision() {
		// bodyCollision
		for (int i = bodyPart; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
				break;
			}
		}
		// BorderCollision
		if (x[0] < 0) {
			running = false;
		} else if (x[0] > WIDTH - OBJECT_SIZE) {
			running = false;
		} else if (y[0] < 0) {
			running = false;
		} else if (y[0] > HEIGHT - OBJECT_SIZE) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	private void gameOver(Graphics graphics) {
		if (appleEaten > highScore) {
			highScore = appleEaten;
		}
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("arial", Font.BOLD, 30));
		graphics.drawString("Score: " + appleEaten, 250, 40);
		graphics.setFont(new Font("arial", Font.BOLD, 20));
		graphics.drawString("High Score: " + highScore, 240, 100);
		graphics.setFont(new Font("arial", Font.BOLD, 50));
		graphics.drawString("Game Over", 180, 250);
		graphics.setFont(new Font("arial", Font.BOLD, 20));
		graphics.drawString(" Press R to Restart or E to Exit", 165, 280);
		running = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			eatApple();
			collision();
		}
		repaint();
	}

	private class MyKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT :
					if (direction != Direction.RIGHT)
						direction = Direction.LEFT;
					break;
				case KeyEvent.VK_RIGHT :
					if (direction != Direction.LEFT)
						direction = Direction.RIGHT;
					break;
				case KeyEvent.VK_UP :
					if (direction != Direction.DOWN)
						direction = Direction.UP;
					break;
				case KeyEvent.VK_DOWN :
					if (direction != Direction.UP)
						direction = Direction.DOWN;
					break;
			}
			if (!running) {
				if (e.getKeyCode() == KeyEvent.VK_R) {
					bodyPart = 3;
					appleEaten = 0;
					direction = Direction.RIGHT;
					running = true;
					x = new int[OBJECT_NUMBER];
					y = new int[OBJECT_NUMBER];
					repaint();
					startGame();
				}
				if (e.getKeyCode() == KeyEvent.VK_E) {
					Window win = SwingUtilities.getWindowAncestor((Component) e.getSource());
					win.dispose();
				}
			}
		}

	}
}
