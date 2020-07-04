package application;

import javafx.scene.paint.Color;

public class Enemy extends Player {

	public boolean movingRight = true;
	
	public Enemy(int x, int y) {
		super(x, y);
		this.color = Color.PINK;
		this.hp = 1;
		
		entity.setFill(color);
	}

}
