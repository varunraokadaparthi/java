package Minesweeper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GamePlay extends JPanel implements MouseListener, ActionListener {

	private static int length = 250;
	private static int width = 300;
	private static int noOfRows = width / 25 - 2;
	private static int noOfCols = length / 25;
	private static int noOfBombs = 30;
	private static int flagCount = 0;
	private static int noOfCells = noOfCols * noOfRows;
	private static int noOfCellsOpened = 0;
	private static Cell[][] cells;

	private ImageIcon unOpened = resizeImageIcon(new ImageIcon("unopened.png"));
	private ImageIcon flag = resizeImageIcon(new ImageIcon("flag.png"));
	private ImageIcon opened;
	private ImageIcon win = new ImageIcon("WIN.png");
	private ImageIcon bombExplode = resizeImageIcon(new ImageIcon("clickedbomb.png"));
	private ImageIcon bomb = resizeImageIcon(new ImageIcon("bomb.png"));

	public GamePlay() {
		addMouseListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}

	static {
		// Initialize cells and place bombs
		cells = new Cell[noOfRows][noOfCols];
		for (int i = 0; i < noOfRows; i++) {
			for (int j = 0; j < noOfCols; j++) {
				cells[i][j] = new Cell(false, false, false);
			}
		}
		for (int i = 0; i < noOfBombs; i++) {
			randomBomb();
		}
		System.out.println("Neighbouring bombs count of each cell");
		for (int i = 0; i < noOfCols; i++) {
			for (int j = 0; j < noOfRows; j++) {
				int neighbouringBombCount = 0;
				for (int l = i - 1; l <= i + 1; l++) {
					for (int m = j - 1; m <= j + 1; m++) {
						if ((l != i && m != j) && isNotOutOfbounds(l, m)) {
							if (cells[l][m].isBomb()) {
								neighbouringBombCount++;
							}
						}
					}
				}
				cells[i][j].setNeighbourBombCount(neighbouringBombCount);
				System.out.print(cells[i][j].getNeighbourBombCount());
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("bombs location");
		for (int i = 0; i < noOfRows; i++) {
			for (int j = 0; j < noOfCols; j++) {
				System.out.print(cells[i][j].isBomb() ? 1 : 0);
			}
			System.out.println();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < noOfRows; i++) {
			for (int j = 0; j < noOfCols; j++) {
				if (noOfCells - noOfCellsOpened == noOfBombs) {
					win.paintIcon(this, g, 0, 0);
				} else if (!cells[i][j].isOpened()) {
					unOpened.paintIcon(this, g, i * 25, (j + 2) * 25);
				} else {
					if (!cells[i][j].isBomb()) {
						int neighbouringBombCount = cells[i][j].getNeighbourBombCount();
						System.out.println("neighbours of " + i + "," + j + ": " + neighbouringBombCount);
						if (neighbouringBombCount > 0) {
							opened = resizeImageIcon(new ImageIcon(neighbouringBombCount + ".png"));
							opened.paintIcon(this, g, i * 25, (j + 2) * 25);
						} else if (neighbouringBombCount == 0) {
							g.setColor(Color.GRAY);
							g.drawRect(i * 25, (j + 2) * 25, 25, 25);
						}
					} else {
						bombExplode.paintIcon(this, g, i * 25, (j + 2) * 25);
					}
				}
				if (cells[i][j].isFlag()) {
					flag.paintIcon(this, g, i * 25, (j + 2) * 25);
				}
			}

		}
	}

	private void flood(int i, int j) {
		System.out.println("i:" + i + "j:" + j);
		cells[i][j].setOpened(true);
		repaint();
		if (canFloodHere(i - 1, j))
			flood(i - 1, j);
		if (canFloodHere(i + 1, j))
			flood(i + 1, j);
		if (canFloodHere(i, j - 1))
			flood(i, j - 1);
		if (canFloodHere(i, j + 1))
			flood(i, j + 1);
	}

	private boolean canFloodHere(int i, int j) {
		if (isNotOutOfbounds(i, j) && !cells[i][j].isOpened() && !cells[i][j].isBomb()
				&& cells[i][j].getNeighbourBombCount() == 0) {
			for (int l = i - 1; l <= i + 1; l++) {
				for (int m = j - 1; m <= j + 1; m++) {
					if (!(l == i && m == j)) {
						if (isNotOutOfbounds(l, m) && cells[l][m].isBomb()) {
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private static boolean isNotOutOfbounds(int i, int j) {
		return (i >= 0 && i < noOfCols && j >= 0 && j < noOfRows);
	}

	private static void randomBomb() {
		Random rand = new Random();
		cells[rand.nextInt(noOfRows)][rand.nextInt(noOfCols)].setBomb(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int i = e.getX() / 25;
		int j = e.getY() / 25 - 2;
		boolean isFlag = false;
		try {
			isFlag = cells[i][j].isFlag();
		} catch (Exception e1) {
			System.out.println("Out of bounds");
		}

		if (e.getButton() == MouseEvent.BUTTON1 && !isFlag) {
			try {
				noOfCellsOpened++;
				cells[i][j].setOpened(true);
				System.out.println("Left mouse Button clicked");
				System.out.println("x:" + i + "y:" + j);
				if (cells[i][j].getNeighbourBombCount() == 0)
					flood(i, j);
			} catch (Exception e1) {
				System.out.println("Out of bounds");
			}
		} else if (e.getButton() == MouseEvent.BUTTON3 && flagCount <= noOfBombs && !cells[i][j].isOpened()) {
			try {
				Cell cell = cells[i][j];
				cell.setFlag(!cell.isFlag());
				System.out.println("right mouse button clicked");
				System.out.println("x:" + i + "y:" + j);
				flagCount += cell.isFlag() ? +1 : -1;
			} catch (Exception e1) {
				System.out.println("Out of bounds");
			}
		}
		repaint();
		System.out.println("repainted");
	}

	public ImageIcon resizeImageIcon(ImageIcon imageIcon) {
		BufferedImage bufferedImage = new BufferedImage(25, 25, BufferedImage.TRANSLUCENT);

		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.drawImage(imageIcon.getImage(), 0, 0, 25, 25, null);
		graphics2D.dispose();

		return new ImageIcon(bufferedImage, imageIcon.getDescription());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
