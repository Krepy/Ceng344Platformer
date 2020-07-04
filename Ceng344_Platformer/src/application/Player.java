package application;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Entity {
	
	public int hp;
	private Point2D Velo;

	public Player(int x, int y) {
		color = Color.BLUE;
		width = 40;
		height = 40;
		
		entity = new Rectangle(width, height);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		entity.getProperties().put("alive", true);
		
		this.hp = 3;
		this.Velo = new Point2D(0, 0);
	}
	
	public void uhOh( ) {
		this.hp--;
	}
	
	public boolean isDead() {
		if(this.hp <= 0)
			return true;
		return false;
	}

	public void addYVelocity(double x) {
		this.Velo = this.Velo.add(0, x);
	}
	
	public void addXVelocity(double x) {
		this.Velo = this.Velo.add(x, 0);
	}
	
	public double getYVelocity() {
		return this.Velo.getY();
	}
	
	public double getXVelocity() {
		return this.Velo.getX();
	}
	
}
