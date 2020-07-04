package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Spike extends Entity {

	public Spike(int x, int y) {
		color = Color.RED;
		width = 50;
		height = 40;
		
		entity = new Rectangle(this.width, this.height);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(this.color);
		entity.getProperties().put("alive", true);
	}

}
