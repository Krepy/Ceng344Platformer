package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class levelEnd extends Entity {
	
	public levelEnd(int x, int y) {
		color = Color.YELLOW;
		width = 40;
		height = 80;
		
		entity = new Rectangle(width, height);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		entity.getProperties().put("alive", true);
	}

}
