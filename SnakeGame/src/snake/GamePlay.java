package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Set<Coord> global;
    private int score=0;
    static {
    	global= new HashSet<Coord>();
    	for(int i=25;i<=850;i+=25) {
    		for(int j=100;j<=600;j+=25) {
    			global.add(new Coord(i,j));
    		}
    	}
    }
	private int[] snakeXlength = new int[750];
	private int[] snakeYlength = new int[750];
	private int moves = 0;

	private int enemyXlocation;
	private int enemyYlocation;

	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;

	private ImageIcon rightMouth;
	private ImageIcon leftMouth;
	private ImageIcon downMouth;
	private ImageIcon upMouth;

	private ImageIcon enemyImage;
	private boolean isEnemyPresent = false;

	private int lengthOfSnake = 3;
	private ImageIcon snakeImage;

	private Timer timer;
	private int delay = 100;

	private ImageIcon titleImage;

	public GamePlay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g) {

		if (moves == 0) {
			snakeXlength[2] = 50;
			snakeXlength[1] = 75;
			snakeXlength[0] = 100;
			snakeYlength[2] = 100;
			snakeYlength[1] = 100;
			snakeYlength[0] = 100;

		}
		// draw title image border
		g.setColor(Color.white);
		g.drawRect(24, 10, 851, 55);

		// draw the title image
		titleImage = new ImageIcon("snaketitle.jpeg");
		titleImage.paintIcon(this, g, 25, 11);

		// draw border for gameplay
		g.setColor(Color.WHITE);
		g.drawRect(24, 74, 851, 577);

		// draw background for gameplay
		g.setColor(Color.black);
		g.fillRect(25, 75, 850, 575);
		
		g.setColor(Color.WHITE);
		g.setFont(new java.awt.Font("Comic Sans MS", Font.BOLD, 35));
		g.drawString(Integer.toString(score), 700, 50);

		if (!isEnemyPresent) {
			
			generateRandomEnemyposition();
			for(int i=0;i<lengthOfSnake;i++)
			   {
				   global.add(new Coord(snakeXlength[i],snakeYlength[i]));
			   }

			enemyImage = new ImageIcon("enemy.jpeg");
			enemyImage.paintIcon(this, g, enemyXlocation, enemyYlocation);
			isEnemyPresent=true;
			
		} else {
			enemyImage = new ImageIcon("enemy.jpeg");
			enemyImage.paintIcon(this, g, enemyXlocation, enemyYlocation);
			if (this.getXlocationOfSnakeHead() == enemyXlocation && this.getYloationOfSnakeHead() == enemyYlocation) {
				isEnemyPresent = false;
				lengthOfSnake++;
				score++;
			}
		}
		
		for(int i=2;i<lengthOfSnake;i++) {
			if(this.getXlocationOfSnakeHead()==snakeXlength[i]&&this.getYloationOfSnakeHead()==snakeYlength[i])
				System.exit(0);
		}
		

		rightMouth = new ImageIcon("rightmouth.png");
		rightMouth.paintIcon(this, g, snakeXlength[0], snakeYlength[0]);

		for (int a = 0; a < lengthOfSnake; a++) {
			if (a == 0 && right) {
				rightMouth = new ImageIcon("rightmouth.png");
				rightMouth.paintIcon(this, g, snakeXlength[a], snakeYlength[a]);
			}
			if (a == 0 && left) {
				leftMouth = new ImageIcon("leftmouth.jpeg");
				leftMouth.paintIcon(this, g, snakeXlength[a], snakeYlength[a]);
			}
			if (a == 0 && up) {
				upMouth = new ImageIcon("upmouth.jpeg");
				upMouth.paintIcon(this, g, snakeXlength[a], snakeYlength[a]);
			}
			if (a == 0 && down) {
				downMouth = new ImageIcon("downmouth.jpeg");
				downMouth.paintIcon(this, g, snakeXlength[a], snakeYlength[a]);
			}
			if (a != 0) {
				snakeImage = new ImageIcon("snakeimage.jpeg");
				snakeImage.paintIcon(this, g, snakeXlength[a], snakeYlength[a]);
			}

		}
		g.dispose();
	}

	private int getYloationOfSnakeHead() {
		return this.snakeYlength[0];
	}

	private int getXlocationOfSnakeHead() {
		return this.snakeXlength[0];
	}

	private void generateRandomEnemyposition() {
	
	   for(int i=0;i<lengthOfSnake;i++)
	   {
		   global.remove(new Coord(snakeXlength[i],snakeYlength[i]));
	   }
		
	   
		Random rand = new Random();
		Iterator<Coord> i=global.iterator();
		
		Coord itr = null; 
		for(int j=0;j<rand.nextInt(global.size());j++) {
			itr = i.next();
		}
		enemyXlocation=itr.getX();
		enemyYlocation=itr.getY();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (right) {
			for (int r = lengthOfSnake - 1; r >= 0; r--) {
				snakeYlength[r + 1] = snakeYlength[r];
			}
			for (int r = lengthOfSnake; r >= 0; r--) {
				if (r == 0) {
					snakeXlength[r] = snakeXlength[r] + 25;
				} else {
					snakeXlength[r] = snakeXlength[r - 1];
				}
				if (snakeXlength[r] > 850) {
					snakeXlength[r] = 25;
				}
			}
			repaint();
		}
		if (left) {
			for (int r = lengthOfSnake - 1; r >= 0; r--) {
				snakeYlength[r + 1] = snakeYlength[r];
			}
			for (int r = lengthOfSnake; r >= 0; r--) {
				if (r == 0) {
					snakeXlength[r] = snakeXlength[r] - 25;
				} else {
					snakeXlength[r] = snakeXlength[r - 1];
				}
				if (snakeXlength[r] < 25) {
					snakeXlength[r] = 850;
				}
			}
			repaint();

		}
		if (up) {
			for (int r = lengthOfSnake - 1; r >= 0; r--) {
				snakeXlength[r + 1] = snakeXlength[r];
			}
			for (int r = lengthOfSnake; r >= 0; r--) {
				if (r == 0) {
					snakeYlength[r] = snakeYlength[r] - 25;
				} else {
					snakeYlength[r] = snakeYlength[r - 1];
				}
				if (snakeYlength[r] < 100) {
					snakeYlength[r] = 600;
				}
			}
			repaint();

		}
		if (down) {
			for (int r = lengthOfSnake - 1; r >= 0; r--) {
				snakeXlength[r + 1] = snakeXlength[r];
			}
			for (int r = lengthOfSnake; r >= 0; r--) {
				if (r == 0) {
					snakeYlength[r] = snakeYlength[r] + 25;
				} else {
					snakeYlength[r] = snakeYlength[r - 1];
				}
				if (snakeYlength[r] > 600) {
					snakeYlength[r] = 100;
				}
			}
			repaint();

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moves++;
			right = true;
			if (!left)
				right = true;
			else {
				right = false;
				left = true;
			}
			up = down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			moves++;
			left = true;
			if (!right)
				left = true;
			else {
				right = true;
				left = false;
			}
			up = down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			moves++;
			up = true;
			if (!down)
				up = true;
			else {
				down = true;
				up = false;
			}
			left = right = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			moves++;
			down = true;
			if (!up)
				down = true;
			else {
				down = false;
				up = true;
			}
			left = right = false;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
