package net.oaster2000.sin3d;

import java.awt.event.KeyEvent;

import net.oaster2000.sin3d.input.Controller;
import net.oaster2000.sin3d.input.InputHandler;

public class Game {
	
	public int time;
	public Controller controls;
	
	public Game() {
		controls = new Controller();
	}
	
	public void tick(InputHandler input) {
		time++;
		boolean forward = input.key[KeyEvent.VK_W];
		boolean back = input.key[KeyEvent.VK_S];
		boolean left = input.key[KeyEvent.VK_A];
		boolean right = input.key[KeyEvent.VK_D];
		
		controls.tick(forward, back, left, right);
	}
}
