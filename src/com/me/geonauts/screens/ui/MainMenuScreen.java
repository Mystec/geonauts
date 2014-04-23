package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.geonauts.Geonauts;
import com.me.geonauts.screens.GameScreen;

/**
 * Resources used: -
 * http://libdgxtutorials.blogspot.com/2013/09/libgdx-tutorial-10-button-and-stage-2d.html 
 * https://github.com/libgdx/libgdx/wiki/Scene2d.ui
 * https://github.com/libgdx/libgdx/wiki/Scene2d
 */

public class MainMenuScreen extends AbstractScreen {
	/** GameScreen object where main gameplay takes place */
	private Screen gameScreen;
	//private Screen shopScreen;
	private Screen creditScreen;
	//private Screen optionsScreen;

	// Strings for mainmenu
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	private static final String TITLE = "G E O N A U T S";

	
	// Buttons
	private TextButton btnNewGame;
	//private TextButton btnShop;
	//private TextButton btnOptions;
	private TextButton btnCredits;
	private TextButton btnQuit;
	
	// Labels
	private Label lblTitle;
	private Label lblScore;
	
	private Table table;
	
	private int highscore  = 0;
	
	// Load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
			

	public MainMenuScreen(Geonauts game) {
		super(game);
		
		highscore = prefs.getInteger("highscore");

		font = new BitmapFont(
				Gdx.files.internal("fonts/fipps/fipps_big.fnt"),
				Gdx.files.internal("fonts/fipps/fipps_big.png"), false);

		// Table
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		// Skins
		Skin skin = new Skin(Gdx.files.internal("images/ui/uiskin.json"));
		
		// Load textures
		TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		// TextureRegions
		TextureRegion upRegion = uiAtlas.findRegion("buttonNormal");
		TextureRegion downRegion = uiAtlas.findRegion("buttonPressed");

		// Styles
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(upRegion);
		style.down = new TextureRegionDrawable(downRegion);
		style.font = new BitmapFont();

		// Labels
		lblTitle = new Label(TITLE, skin);
		lblScore = new Label("High score: " + highscore, skin);
		
		if(prefs.getInteger("games_played") == 0){
			btnNewGame = new TextButton("New Game", style);
		} else{
			btnNewGame = new TextButton("Continue", style);
		}
		btnNewGame.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				newGame();
			}
		});
		/*btnShop = new TextButton("Shop", style);
		btnShop.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				shop();
			}
		});*/
		/*btnOptions = new TextButton("Options", style);
		btnOptions.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				options();
			}
		});*/
		btnCredits = new TextButton("Credits", style);
		btnCredits.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				credit();
			}
		});
		btnQuit = new TextButton("Quit", style);
		btnQuit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				System.exit(0);
			}
		});	
	}

	public void render(float delta) {
		super.render(delta);
		
		//System.out.println(highscore);
		/**
		batch.begin();
			font.draw(batch, TITLE, stage.getWidth() / 2 - FONT_SIZE * TITLE.length(), stage.getHeight() - FONT_SIZE);
		batch.end();
		*/

	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		// Load preferences
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		highscore = prefs.getInteger("highscore");
		
		table.add(lblTitle);
		table.row();
		table.add(btnNewGame);
		//table.row();
		//table.add(btnShop);
		//table.row();
		//table.add(btnOptions);
		table.row();
		table.add(btnCredits);
		table.row();
		table.add(btnQuit);
		table.row();
		table.add(lblScore);
	}

	private void newGame() {
		//prefs.putInteger("games_played", 0);
		if(prefs.getInteger("games_played") == 0) {
			prefs.putInteger("Reload", 1);
			prefs.putInteger("Attack", 1);
			prefs.putInteger("Health", 1);
			prefs.putInteger("Moneyx", 1);
			prefs.putInteger("max targets", 1);
			prefs.putInteger("Money", 100);
			prefs.flush();
		}
		
		game.setScreen(game.getGameScreen());
	}
	
	/*private void shop(){
		shopScreen = new ShopScreen(game);
		game.setScreen(shopScreen);
	}*/
	
	private void credit(){
		creditScreen = new CreditScreen(game);
		game.setScreen(creditScreen);
	}
	
	/*private void options(){
		optionsScreen = new OptionsScreen(game);
		game.setScreen(optionsScreen);;
	}*/
}
