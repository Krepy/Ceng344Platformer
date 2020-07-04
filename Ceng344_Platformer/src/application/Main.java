package application;
	
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>(); // List of keys and if they are pressed or not
	private ArrayList<Node> platforms = new ArrayList<Node>(); // List of platforms
	private ArrayList<Node> spikes = new ArrayList<Node>(); // List of spikes
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>(); // List of enemies
	
	private Pane app = new Pane(); // Main application
	private Pane game = new Pane(); // Game layer
	private Pane ui = new Pane(); // Ui layer
	
	private Text hp;
	private Text gO;
	private Text finish;
	
	private Button Restart;
	private Button Exit;
	private Button Start;
	
	private Enemy enemy;
	private Player player = new Player(0, 0);
	private levelEnd End;
	
	private boolean canJump = true;
	private boolean running = true;
	private boolean gameOver = false;
	private boolean fin = false;
	private boolean startu = false;
	
	private int levelWidth;
	private int currentLevel = 0;
	private int levels = 2; // Level Count - 1

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		startScreen();
		
		Scene scene = new Scene(app);
		
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		
		primaryStage.setTitle("Ceng344 Project - Platformer");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Frame timer
		AnimationTimer timer = new AnimationTimer() {
			int frameCount = 0;
			public void handle(long now) {
				if(!startu) {
					if(frameCount % 2 == 0) {
						if(!fin) {
							if(!gameOver) {
				                if (running) {
				                    update();
				                }
								
							} else
								hp.setText("HP: 0");
						}
					}
					frameCount++;
				}
				
			}
		};
		
		timer.start();
	}
	
	private void startScreen() {
		Rectangle background = new Rectangle(1280, 720);
		
		// Texts
		Text intro = new Text(260, 470, "You have three hp. You lose hp if you fall down, hit spikes or enemies!");
		intro.setFont(Font.font ("Verdana", 20));
		intro.setFill(Color.WHITE);
		
		Text intro2 = new Text(560, 380, "Good Luck!");
		intro2.setFont(Font.font ("Verdana", 20));
		intro2.setFill(Color.WHITE);
		
		Text endu =  new Text(1020, 90, "End of Level ->");
		endu.setFont(Font.font ("Verdana", 20));
		endu.setFill(Color.WHITE);
		
		Text spikeu =  new Text(1080, 165, "Spikes ->");
		spikeu.setFont(Font.font ("Verdana", 20));
		spikeu.setFill(Color.WHITE);
		
		Text enemu =  new Text(1060, 230, "Enemies ->");
		enemu.setFont(Font.font ("Verdana", 20));
		enemu.setFill(Color.WHITE);
		
		ui.getChildren().add(endu);
		ui.getChildren().add(spikeu);
		ui.getChildren().add(enemu);
		ui.getChildren().add(intro);
		ui.getChildren().add(intro2);
		
		// Legends
		ui.getChildren().add(new levelEnd(1200, 40).entity);
		ui.getChildren().add(new Spike(1195, 140).entity);
		ui.getChildren().add(new Enemy(1200, 200).entity);
		
		// Start Button
		Start = new Button("Start");
    	Start.setLayoutX(600);
    	Start.setLayoutY(400);
    	
    	ui.getChildren().add(Start);
    	
    	app.getChildren().addAll(background, game, ui);
    	
		startu = true;
    	
    	Start.setOnAction(event -> {
    		startu = false;
    		clearu();
    		Content();
        });
    	
	}
	
	private void Content() {
		Rectangle background = new Rectangle(1280, 720);
		
		levelWidth = Levels.LEVELS[currentLevel][0].length() * 60;
		
		//Entities
		Node A;
		for (int i = 0; i < Levels.LEVELS[currentLevel].length; i++) {
			String row = Levels.LEVELS[currentLevel][i];
			
			for(int j = 0; j < row.length(); j++) {
				switch(row.charAt(j)) {
					case '0':
						break;
					case '1': // Platforms
						A = new application.Platform(j*60, i*60).entity;
						game.getChildren().add(A);
						platforms.add(A);
						break;
					case 'E': // Level Ends
						End = new levelEnd(j*60, i*60-20);
						game.getChildren().add(End.entity);
						break;
					case '#': // Spikes
						A = new Spike(j*60+5, i*60+20).entity;
						game.getChildren().add(A);
						spikes.add(A);
						break;
					case 'P': // Player
						if(currentLevel == 0)
							player = new Player(j*60, i*60);
						else {
							player.entity.setTranslateX(j*60);
							player.entity.setTranslateY(i*60);
						}
						game.getChildren().add(player.entity);
						break;
					case 'V': // Enemies
						enemy = new Enemy(j*60+5, i*60+19);
						game.getChildren().add(enemy.entity);
						enemies.add(enemy);
						break;
				}
			}
		}
		
		// HP Text
		hp = new Text(10, 40, "HP: " + player.hp);
		hp.setFont(Font.font ("Verdana", 20));
		hp.setFill(Color.WHITE);
		ui.getChildren().add(hp);
		
		// GameOver Text
		gO = new Text(600, 40, "Game Over!");
		gO.setFont(Font.font ("Verdana", 20));
		gO.setFill(Color.RED);
		
    	// Finish Text
    	finish = new Text(450, 40, "You have successfully completed the game!");
    	finish.setFont(Font.font ("Verdana", 20));
    	finish.setFill(Color.GREEN);
		
		// Restart Button
    	Restart = new Button("Restart");
    	Restart.setLayoutX(590);
    	Restart.setLayoutY(90);
    	
    	// Exit Button
    	Exit = new Button("Exit");
    	Exit.setLayoutX(700);
    	Exit.setLayoutY(90);
		
		// Camera
		player.entity.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			
			if(offset > 640 && offset < levelWidth - 640) {
				game.setLayoutX(-(offset - 640));
			}
		});
		
		// Mount game and ui layer to main application, add background
		app.getChildren().addAll(background, game, ui);		
	}
	
	private void update() {
		// Input processing
        if ((isPressed(KeyCode.SPACE) || isPressed(KeyCode.W)) && player.entity.getTranslateY() >= 5) {
            jumpPlayer();
        }

        if (isPressed(KeyCode.A) && player.entity.getTranslateX() >= 5) {
            movePlayerX(-5);
        }

        if (isPressed(KeyCode.D) && player.entity.getTranslateX() + 40 <= levelWidth - 5) {
            movePlayerX(5);
        }
        
        // Falling down velocity, minus 1 every update
        if (player.getYVelocity() < 10) {
            player.addYVelocity(1);
        }

        // Falls down if velo.y < 10
        movePlayerY((int)player.getYVelocity());
        
        // Enemy movement
        for(Enemy X : enemies) {
        	moveEnemy(X);
        }
    }
	
	private void moveEnemy(Enemy X) {
		boolean drop = true;
		Rectangle dump;
		
		if(X.entity.getTranslateX() >= 5 || X.entity.getTranslateY() <= levelWidth - 5) {
			for(int i = 0; i < 5; i++) {
				// Check if movement blocked by platforms
	    		for (Node A : platforms) {
	                if (X.entity.getBoundsInParent().intersects(A.getBoundsInParent())) {
	                    if (X.movingRight) {
	                        if (X.entity.getTranslateX() + 40 == A.getTranslateX()) {
	                            X.entity.setTranslateX(X.entity.getTranslateX()-1);
	                            X.movingRight = false;
	                            return;
	                        }
	                    }
	                    else {
	                        if (X.entity.getTranslateX() == A.getTranslateX() + 60) {
	                            X.entity.setTranslateX(X.entity.getTranslateX()+1);
	                            X.movingRight = true;
	                            return;
	                        }
	                    }
	                }
	                
	                // Check if enemy falls down
		    		if(X.movingRight) {
		    			dump = new Rectangle((int)X.entity.getTranslateX() + 46, (int)X.entity.getTranslateY() + 45, 5, 5);
		    			if(dump.getBoundsInParent().intersects(A.getBoundsInParent()))
		    					drop = false;
		    		} else {
		    			dump = new Rectangle((int)X.entity.getTranslateX() - 6, (int)X.entity.getTranslateY() + 45, 5, 5);
		    			if(dump.getBoundsInParent().intersects(A.getBoundsInParent()))
		    					drop = false;
		    		}
	            }
	    		// If enemy is gonna fall down change the movement direction
	    		if(drop) {
	    			if(X.movingRight)
	    				X.movingRight = false;
	    			else
	    				X.movingRight = true;
	    			return;
	    		}
	    		
	    		// Move enemy by 1 in the correct direction
	    		X.entity.setTranslateX(X.entity.getTranslateX() + (X.movingRight ? 1 : -1));
	    	}
		}
	}
	
	 
	private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
        	// Check if movement blocked by platforms
            for (Node A : platforms) {
                if (player.entity.getBoundsInParent().intersects(A.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.entity.getTranslateX() + 40 == A.getTranslateX()) {
                            player.entity.setTranslateX(player.entity.getTranslateX()-1);
                            return;
                        }
                    }
                    else {
                        if (player.entity.getTranslateX() == A.getTranslateX() + 60) {
                            player.entity.setTranslateX(player.entity.getTranslateX()+1);
                            return;
                        }
                    }
                }
            }
            // Move player by 1 in the correct direction
            player.entity.setTranslateX(player.entity.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
	

    private void movePlayerY(int value) {
    	canJump = false;
        boolean movingDown = value > 0;
        
        for (int i = 0; i < Math.abs(value); i++) {
        	// Check if player ends the level
        	if(player.entity.getBoundsInParent().intersects(End.entity.getBoundsInParent())) {
        		nextLevel();
        	}
        	// Check if movement blocked by platforms
            for (Node A : platforms) {
                if (player.entity.getBoundsInParent().intersects(A.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.entity.getTranslateY() + 40 == A.getTranslateY()) {
                            player.entity.setTranslateY(player.entity.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.entity.getTranslateY() == A.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            
            // Check collision with spikes
            for (Node A : spikes) {
            	if(player.entity.getBoundsInParent().intersects(A.getBoundsInParent())) {
            		ohUh();
            		return;
            	}
            }
            
            // Check collision with enemies
            for (Enemy A : enemies) {
            	if(player.entity.getBoundsInParent().intersects(A.entity.getBoundsInParent())) {
            		ohUh();
            		return;
            	}
            }
            // Check if player falls down the holes
            
        	if(player.entity.getTranslateY() >= 680) {
        		ohUh();
        	} else
                // Move player by 1 in the correct direction
        		player.entity.setTranslateY(player.entity.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    

    private void jumpPlayer() {
    	
        if (canJump) {
        	// If player tries jumping after jumping to a higher platform, reset velocity
        	while(player.getYVelocity() < 10)
        		player.addYVelocity(1);
        	
        	// Add velocity to make the player go up
        	player.addYVelocity(-30);
            canJump = false;
        }
    }
    

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
    
    private void ohUh() {
    	// Lose HP
    	player.uhOh();
    	
    	// Game Over if player hp is 0
		if(player.isDead()) {
			gameOver();
		// Restart the level if not
		} else {
			hp.setText("HP: " + player.hp);
    		player.entity.setTranslateY(600);
    		player.entity.setTranslateX(0);
    		game.setLayoutX(0);
    	}
    }
    
    private void nextLevel() {
    	currentLevel++;
    	
    	// Finish screen
    	if(levels < currentLevel) {
    		ui.getChildren().clear();
    		ui.getChildren().add(Exit);
    		ui.getChildren().add(Restart);
    		ui.getChildren().add(finish);
    		fin = true;
    		Restart.setOnAction(event -> { 
    			restartu();
            });
    		Exit.setOnAction(event -> {
        		javafx.application.Platform.exit();
            });
    	} else { // Next level if there is one
    		clearu();
        	game.setLayoutX(0);
        	Content();
    	}
    }
    
    private void gameOver() {
    	// Game Over screen, Restart the game or Exit
    	ui.getChildren().clear();
    	ui.getChildren().add(Restart);
    	ui.getChildren().add(Exit);
    	ui.getChildren().add(gO);
    	gameOver = true;
    	Restart.setOnAction(event -> {
    		restartu();
        });
		Exit.setOnAction(event -> {
    		javafx.application.Platform.exit();
        });
    }
    
    private void clearu() {
    	// Clear screen
		game.getChildren().clear();
    	app.getChildren().clear();
    	ui.getChildren().clear();
    	platforms.clear();
    	spikes.clear();
    	enemies.clear();
    }
    
    private void restartu() {
		clearu();
		game.setLayoutX(0);
    	currentLevel = 0;
    	Content();
    	gameOver = false;
    	fin = false;
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}
