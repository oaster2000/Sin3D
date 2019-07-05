package net.oaster2000.sin3d;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import net.oaster2000.sin3d.graphics.Screen;
import net.oaster2000.sin3d.input.Controller;
import net.oaster2000.sin3d.input.InputHandler;

public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final String TITLE = "SIN 3D v0.1";
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 576;

	private Thread thread;
	private Screen screen;
	private BufferedImage img;
	private Game game;
	private Boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int oldX = 0;
	
	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(WIDTH, HEIGHT);
		game = new Game();
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;
		
		while(running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			requestFocus();
			
			while(unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if(tickCount % 60 == 0) {
					System.out.println(frames + " FPS");
					previousTime += 1000;
					frames = 0;
				}
			}
			
			if(ticked) {
				render();
				frames++;
			}
			render();
			frames++;
			
			newX = InputHandler.mouseX;
			if(newX > oldX) {
				Controller.turnRight = true;
			}
			if(newX < oldX) {
				Controller.turnLeft = true;
			}
			if(newX == oldX) {
				Controller.turnRight = false;
				Controller.turnLeft = false;
			}
			oldX = newX;
		}
	}

	private void tick() {
		game.tick(input);
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.render(game);
		
		for(int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g =bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
		
	}

	public static void main(String[] args) {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
		Display game = new Display();
		JFrame window = new JFrame();
		window.add(game);
		window.pack();
		window.getContentPane().setCursor(blank);
		window.setTitle(TITLE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(WIDTH, HEIGHT);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
		
		game.start();
	}

}
