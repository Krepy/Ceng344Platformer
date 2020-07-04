package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform extends Entity {
	
	public Platform(int x, int y) {
		color = Color.DARKGRAY;
		width = 60;
		height = 60;
		
		entity = new Rectangle(this.width, this.height);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(this.color);
		entity.getProperties().put("alive", true);
	}
}
