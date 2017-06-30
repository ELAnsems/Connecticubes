package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Algorithms.*;

import java.io.*;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class ConnetiCubes extends ApplicationAdapter {

	//Input variables
	public int numberOfCubes = 6;
	public int numberOfTilesWidth = 2;
	public int numberOfTilesHeight = 3;

	public int hintIntervalThreshold = 6;
	public static int[] scoreTurns = {1000, 20, 15, 13, 8};

	//LibGDX variables
	public Stage stage;
	SpriteBatch batch;
	OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;

	//Game variables
	Cube[] cubes = new Cube[numberOfCubes];
	public Tile[][] tiles = new Tile[numberOfTilesWidth][numberOfTilesHeight];
	String[][] solution = new String[numberOfTilesWidth][numberOfTilesHeight];
	public boolean giveHint = false;
	public int roundsPlayedSinceLastHint = 0;
	Image[] players = new Image[4];
	boolean[] playerActive = new boolean[4];
	int activePlayer = 0;

	public static int cubesCorrect = 0;
	public static int turnsTaken = 0;
	public static int[] turnsTakenPlayers = new int[4];


	String[] colors = {"Red", "Blue", "Green", "Yellow", "Orange"};
	static String[] allCodes = new String[299];
	public static int currentCode = 0;
	String[] characters = {"A", "B", "C", "D", "E", "F"};

	//UI variables
	public Image correct;
	public static Image[] starsCorrect;
	Texture[] cubeTextures= new Texture[numberOfCubes];
	Texture[] playerTextures = new Texture[4];
	int size = 150;
	int screenWidth;
	int screenHeight;
	private ImageButton checkButton;
	public int uiLineHeight = 5;
	BitmapFont  myFont;

	//helper variables
	int i;
	int j;
	private transient Reader reader;
	private transient Writer writer;

	//Algorithm variables
	private static final String ALG_SIMPLE = "simple";
	private static final String ALG_KNUTH = "knuth";
	private static final String ALG_EXP_SIZE = "exp_size";
	private transient String alphabet = "ABCDEF";
	private transient int length = numberOfCubes;
	private transient boolean uniqueChars = true;
	private transient int maxRounds = 9999;
	private transient String alg = ALG_KNUTH;
	private transient int precalcLevels = 1;
	private final CubeGame cubeGame = new CubeGame(alphabet, length, uniqueChars);
	public static Game game;
	public static Game opportunityCalculator_game;
	public int lowestOpportunity = 720;
	public String finalCode;
	private CubeGame opportunityCalculator_cubeGame;// = new CubeGame(alphabet, length, uniqueChars, cubeGame.finalCode);
	public static int hintSteps=0;
	public static int opportunityCalculator_opportunity=720;

	private final boolean COMMAND_LINE_PLAY = false;
	@Override
	public void create () {

		setupCameraview();

		assignCode();
		//generateCode(); old version of solution generator
		setupTiles();
		setupCubes();
		setupUI();
		playGame();
		shapeRenderer = new ShapeRenderer();
		playRound(tiles, cubes, correct);
		turnsTaken--;
	}

	//Algorithm methods

	private void playGame()
	{
		try {
			reader = new InputStreamReader(System.in, "UTF-8");
			writer = new OutputStreamWriter(System.out, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final AlgorithmFactory factory = createFactory(cubeGame);
		final Algorithm algorithm = factory.getAlgorithm();
		final Player player = new ReaderWriterPlayer(cubeGame, reader, writer);
		final GuessCalculator calc = new GuessCalculator(cubeGame, factory, precalcLevels);
		game = new Game(cubeGame, algorithm, maxRounds, player, calc);

		finalCode = cubeGame.finalCode;
		opportunityCalculator_cubeGame = new CubeGame(alphabet, length, uniqueChars, finalCode);
		final AlgorithmFactory opportunityCalculator_factory = createFactory(opportunityCalculator_cubeGame);
		final Algorithm opportunityCalculator_algorithm = opportunityCalculator_factory.getAlgorithm();
		final Player opportunityCalculator_player = new ReaderWriterPlayer(opportunityCalculator_cubeGame, reader, writer);
		final GuessCalculator opportunityCalculator_calc = new GuessCalculator(opportunityCalculator_cubeGame, opportunityCalculator_factory, precalcLevels);
		opportunityCalculator_game = new Game(opportunityCalculator_cubeGame, opportunityCalculator_algorithm, maxRounds, opportunityCalculator_player, opportunityCalculator_calc);




		if(COMMAND_LINE_PLAY == true){
			game.play();
			opportunityCalculator_game.play();
		}
		else{
			game.startPlayer();
			opportunityCalculator_game.startPlayer();
		}

	}

	private void resetopportunityCalculator_Game(){
		opportunityCalculator_cubeGame = new CubeGame(alphabet, length, uniqueChars, finalCode);
		final AlgorithmFactory opportunityCalculator_factory = createFactory(opportunityCalculator_cubeGame);
		final Algorithm opportunityCalculator_algorithm = opportunityCalculator_factory.getAlgorithm();
		final Player opportunityCalculator_player = new ReaderWriterPlayer(opportunityCalculator_cubeGame, reader, writer);
		final GuessCalculator opportunityCalculator_calc = new GuessCalculator(opportunityCalculator_cubeGame, opportunityCalculator_factory, precalcLevels);
		opportunityCalculator_game = new Game(opportunityCalculator_cubeGame, opportunityCalculator_algorithm, maxRounds, opportunityCalculator_player, opportunityCalculator_calc);

	}

	private void reset(){
		activePlayer = 0;
		cubesCorrect = 0;
		turnsTaken = 0;
		playerActive[0] = true;
		playerActive[1] = false;
		playerActive[2] = false;
		playerActive[3] = false;
		turnsTakenPlayers[0] = 0;
		turnsTakenPlayers[1] = 0;
		turnsTakenPlayers[2] = 0;
		turnsTakenPlayers[3] = 0;
		giveHint = false;
	}

	private AlgorithmFactory createFactory(final CubeGame cubeGame)
	{
		AlgorithmFactory factory;
		if (alg.equals(ALG_KNUTH)){
			factory = new KnuthAlgorithmFactory(cubeGame);
		}
		else if (alg.equals(ALG_EXP_SIZE)){
			factory = new ExpectedSizeAlgorithmFactory(cubeGame);
		}
		else if (alg.equals(ALG_SIMPLE)){
			factory = new SimpleAlgorithmFactory(cubeGame);
		}
		else{
			throw new CubeGameException();
		}
		return factory;
	}


	public void assignCode(){
		String[] theCode = this.cubeGame.finalCode.split("(?!^)");


		for(i =0; i<solution.length; i++) {
			for (j = 0; j < solution[i].length; j++) {
				solution[i][j] = theCode[(tiles.length*j)+i];
				Gdx.app.log("CCUBES", "solution[" + i + "][" + j + "] = theCode[" + ((tiles.length*j)+i) + "] = " + theCode[((tiles.length*j)+i)]);

			}
		}
	}


	public void generateCode(){

		Random rand = new Random();

		for(i =0; i<solution.length; i++) {
			for (j = 0; j < solution[i].length; j++) {
				solution[i][j]="x";
			}
		}
		if(characters.length<=(tiles.length * tiles[0].length)) {
			for (i = 0; i < characters.length; i++) {
				int randomNum = rand.nextInt(((tiles.length * tiles[0].length) - 1) + 1) + 1;
				int xPos = (randomNum - 1) % tiles.length;
				int yPos = (randomNum - 1) % tiles[0].length;
				while (!solution[xPos][yPos].equals("x")) {
					randomNum = rand.nextInt(((tiles.length * tiles[0].length) - 1) + 1) + 1;
					xPos = (randomNum - 1) % tiles.length;
					yPos = (randomNum - 1) % tiles[0].length;
				}
				Gdx.app.log("CCUBES", "store " + characters[i] + " in " + xPos + ", " + yPos + ". randomNum was " + randomNum);

				solution[xPos][yPos] = characters[i];
			}
		}
		else{
		//	Gdx.app.log("CCUBES", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!more blocks than positions");

		}
	}


	static  public int playRound(Tile[][] tls, Cube[] cbs, Image correct){
		turnsTaken++;
		String theCode="";
		String[] arrayCode= new String[tls.length*tls[0].length];
		int j,i=0;
		for (j = 0; j < tls[0].length; j++) {
			for (i = 0; i < tls.length; i++) {


				if (tls[i][j].cube == null) {
					arrayCode[(tls.length*j)+i]="0";
				}
				else {
					arrayCode[(tls.length*j)+i]=tls[i][j].cube.getName();
				}
			}
		}
		for (j = 0; j < arrayCode.length; j++) {
			theCode+=arrayCode[j];

		}
	//	Gdx.app.log("CCUBES", "playRound theCode to be checked = "+theCode );
		allCodes[currentCode]= theCode;
		Score score = game.playSingleRound(theCode);

		String allCodesString = "";
		if(currentCode>1) {
			for(int x = 0; x<allCodes.length; x++){
				if(allCodes[x]!=null) {
					if (!allCodes[x].equals("000000") && !allCodes[x].equals("")) {
						allCodesString += allCodes[x] + ",";
					}
				}
			}
			Gdx.app.log("Algorithm", "Codes tried so far = " + allCodesString);
			for (int z = 1; z >= 0; z--) {
				//Gdx.app.log("CCUBES", "playRound opportunityCalculator_game theCode to be checked = " + allCodes[currentCode - z]);
				opportunityCalculator_game.playSingleRound(allCodes[currentCode - z]);
			}
		}


		currentCode++;


		ConnetiCubes.cubesCorrect = score.getBulls();
		if(score.getBulls()==cbs.length){
			correct.setVisible(true);
			for(int x = 0; x<5; x++){
				if(turnsTaken<=scoreTurns[x]) {
					starsCorrect[x].setVisible(true);
				}
			}

			Gdx.app.log("CCUBES", "YOU WIN!" );
			return -1;
		}
		else{
			return game.getRoundsPlayed();
		}


	}
	//--------------------------------------------- cube methods -----------------------------------

	public void setupUI(){

		myFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

		setupCheckButton();
		setupPlayers();


		Texture correctTexture = new Texture(Gdx.files.internal("correct.png"));

		correct = new Image(correctTexture);

		correct.setX((Gdx.graphics.getWidth()/2) - (correct.getWidth()/2));
		correct.setY((Gdx.graphics.getHeight()/2) - (correct.getHeight()/2));

		starsCorrect = new Image[5];
		Texture starsTexture = new Texture(Gdx.files.internal("starGraphic.png"));
		stage.addActor(correct); //Add the button to the stage to perform rendering and take input.


		for(int i = 0; i<5; i++){
			starsCorrect[i] = new Image(starsTexture);
			starsCorrect[i].setY((correct.getY())+50);
			starsCorrect[i].setX(correct.getX()+69 +(75*i));
			starsCorrect[i].setVisible(false);
			stage.addActor(starsCorrect[i]); //Add the button to the stage to perform rendering and take input.
		}

		correct.setVisible(false);

	}

	public void setupPlayers(){
		playerTextures[0] = new Texture(Gdx.files.internal("redPlayerActive.png"));
		playerTextures[1] = new Texture(Gdx.files.internal("bluePlayer.png"));
		playerTextures[2] = new Texture(Gdx.files.internal("greenPlayer.png"));
		playerTextures[3] = new Texture(Gdx.files.internal("yellowPlayer.png"));
		playerActive[0]=true;
		playerActive[1]=false;
		playerActive[2]=false;
		playerActive[3]=false;
		players[0] = new Image(playerTextures[0]);
		players[1] = new Image(playerTextures[1]);
		players[2] = new Image(playerTextures[2]);
		players[3] = new Image(playerTextures[3]);
		players[0].setX(Gdx.graphics.getWidth() - players[0].getWidth());
		players[3].setX(Gdx.graphics.getWidth() - players[3].getWidth());
		players[2].setY((Gdx.graphics.getHeight() - players[2].getHeight())-(uiLineHeight+checkButton.getHeight()));
		players[3].setY((Gdx.graphics.getHeight() - players[3].getHeight())-(uiLineHeight+checkButton.getHeight()));
		for(int i = 0; i<4; i++) {
			players[i].setName(i+"");
			stage.addActor(players[i]); //Add the button to the stage to perform rendering and take input.

			players[i].addListener(new ClickListener() {

				public void activatePlayer(int i){
					playerActive[i] = true;
					playerTextures[i].dispose();
					String color = "red";
					if(i==1){color = "blue";}
					if(i==2){color = "green";}
					if(i==3){color = "yellow";}
					activePlayer=i;
					playerTextures[i] = new Texture(color+"PlayerActive.png");
					players[i].setDrawable(new SpriteDrawable(new Sprite(playerTextures[i])));
				}

				public void deactivatePlayer(int i){
					playerActive[i] = false;
					playerTextures[i].dispose();
					String color = "red";
					if(i==1){color = "blue";}
					if(i==2){color = "green";}
					if(i==3){color = "yellow";}
					playerTextures[i] = new Texture(color+"Player.png");
					players[i].setDrawable(new SpriteDrawable(new Sprite(playerTextures[i])));

				}

				@Override
				public void clicked(InputEvent event, float x, float y) {
					Gdx.app.log("CCUBES", "clicked redplayer at " + event.getTarget() + ", x = " + x + ", y = " + y);
					int a = parseInt(event.getTarget().getName());
					for(int i = 0; i<4; i++) {
						if(a==i){
							activatePlayer(i);
						}
						else{
							deactivatePlayer(i);
						}
					}
				}
			});
		}
	}

	public void setupCheckButton(){
		Texture myTextureUP = new Texture(Gdx.files.internal("checkbutton.png"));
		TextureRegion myTextureRegionUP = new TextureRegion(myTextureUP);
		TextureRegionDrawable myTexRegionDrawableUP = new TextureRegionDrawable(myTextureRegionUP);
		Texture myTextureDOWN = new Texture(Gdx.files.internal("checkbuttonpressed.png"));
		TextureRegion myTextureRegionDOWN = new TextureRegion(myTextureDOWN);
		TextureRegionDrawable myTexRegionDrawableDOWN = new TextureRegionDrawable(myTextureRegionDOWN);
		checkButton = new ImageButton(myTexRegionDrawableUP, myTexRegionDrawableDOWN);
		//myTextureDOWN.dispose();
		//myTextureUP.dispose();
		stage.addActor(checkButton); //Add the button to the stage to perform rendering and take input.
		checkButton.setWidth(Math.round(checkButton.getWidth()*0.8));
		checkButton.setHeight(Math.round(checkButton.getHeight()*0.8));
		checkButton.setX(Gdx.graphics.getWidth()-checkButton.getWidth());
		checkButton.setY((Gdx.graphics.getHeight())-(checkButton.getHeight()));

		checkButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				//Gdx.app.log("CCUBES", "clicked checkButton at " + event.getTarget()+ ", x = "+x+ ", y = "+y);


				resetopportunityCalculator_Game();//opportunityCalculator_game code
				int roundsPlayed = ConnetiCubes.playRound(tiles, cubes, correct);
				//Gdx.app.log("CCUBES", "roundsPlayed = "+roundsPlayed+ ", roundsPlayedSinceLastHint = "+roundsPlayedSinceLastHint);


				if((roundsPlayed-roundsPlayedSinceLastHint)>=hintIntervalThreshold){
					//Gdx.app.log("CCUBES", "giveHint=true");
					roundsPlayedSinceLastHint=roundsPlayed;
					//giveHint=true;
				}
			//	Gdx.app.log("CCUBES", "lowestOpportunity  = "+lowestOpportunity);
				Gdx.app.log("CCUBES", "opportunityCalculator_opportunity  = "+opportunityCalculator_opportunity);
			//	Gdx.app.log("CCUBES", "opportunityCalculator_game.getPossibleCodesSize()  = "+opportunityCalculator_game.getPossibleCodesSize()+"          ]]]]]]]]]]]]]]]");

				if(opportunityCalculator_game.getPossibleCodesSize()<lowestOpportunity && !giveHint){
					lowestOpportunity = opportunityCalculator_game.getPossibleCodesSize();
					hintSteps=0;
				//	Gdx.app.log("CCUBES", "hintSteps  = 0          ]]]]]]]]]]]]]]]");
				}
				else{
					hintSteps++;
					Gdx.app.log("Algorithm", "hintThreshold increased to = "+hintSteps );
				}

				if(hintSteps>=4){
					giveHint=true;
					Gdx.app.log("CCUBES", "Players are not progressing and require a hint.");
				}

			}

		});
	}


	public void setupCameraview(){
		camera = new OrthographicCamera();
		batch = new SpriteBatch();

		switch(Gdx.app.getType()) {
			case Android:
				size = 180;
				camera.setToOrtho(false, (2560/4)*3,(1190));
				//ExtendViewport viewport = new ExtendViewport(1000,560,camera);
				//FitViewport fViewPort = new FitViewport((2560/4)*3,(1190),camera);
				//FitViewport fViewPort = new FitViewport((2560/4)*3,(1190),camera);//s6
				camera.setToOrtho(false, 1280,800);
				FitViewport fViewPort = new FitViewport(1280,800,camera);//tablet
				stage = new Stage(fViewPort, batch);
				break;
			case Desktop:
				size = 100;
				camera.setToOrtho(false, 1000,560);
				ExtendViewport eViewport = new ExtendViewport(1000,560,camera);
				//FitViewport viewport = new FitViewport(1280,800,camera);
				stage = new Stage(eViewport, batch);
				break;
		}

		Gdx.input.setInputProcessor(stage);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
	}

	public void setupTiles(){

		for(i =0; i<tiles.length; i++){
			for(j =0; j<tiles[i].length; j++){
				Texture tempTex =new Texture("hole.png");
				tiles[i][j] = new Tile(tempTex);
				//tempTex.dispose();
				stage.addActor(tiles[i][j]);
				tiles[i][j].setName("tile_i_"+i+"_j_"+j);
				tiles[i][j].setX(((screenWidth/2)-size)+(i*size));
				tiles[i][j].setY((((screenHeight/2))-(j*size)));
				tiles[i][j].setWidth(size);
				tiles[i][j].setHeight(size);
			}
		}
	}

	public void setupCubes(){
		for(i =0; i<cubes.length; i++){
			//cubes[i] = new Cube(new Texture("cube"+colors[i]+".png"));
			cubeTextures[i] =new Texture(characters[i]+".png");
			cubes[i] = new Cube(cubeTextures[i]);
			//tempTex.dispose();
			stage.addActor(cubes[i]);
			cubes[i].setOriginalZIndex(cubes[i].getZIndex());
			cubes[i].setName(characters[i]);
			cubes[i].setY((i%4)*size);
			cubes[i].setX(100+((Math.round(i/4))*size));
			cubes[i].setWidth(size);
			cubes[i].setHeight(size);
			for(int x =0; x<solution.length; x++){
				for(int y =0;y<solution[0].length; y++){

					if(solution[x][y].equals(characters[i])){

						cubes[i].setCorrectTile(tiles[x][y]);
					}
				}
			}

			cubes[i].addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					//	Gdx.app.log("CCUBES", "clicked " + event.getTarget()+ ", x = "+x+ ", y = "+y);


					//do stuff to a different actor on the stage
					/*
					for (Actor actor : event.getTarget().getStage().getActors()) {
						if(actor.getName() == "testimg"){
							actor.setX(40);
						//	event.getTarget().setX(event.getTarget().getX()+40);
						}
					}*/


				};

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					//Gdx.app.log("CCUBES", "touchDown " + event.getTarget()+ ", event.getTarget().getX() = "+event.getTarget().getX() + ", event.getTarget().getY()  = "+event.getTarget().getY() + ", x = "+x+ ", y = "+y);
					((Cube)event.getTarget()).isDown=true;
					((Cube)event.getTarget()).lastTouch.set(event.getStageX(), event.getStageY());
					((Cube)event.getTarget()).color=colors[activePlayer];
					((Cube)event.getTarget()).setZIndex(99);

					boolean isLeastActivePlayerSelected = false;

					if(turnsTakenPlayers[activePlayer]<=turnsTakenPlayers[0] &&
							turnsTakenPlayers[activePlayer]<=turnsTakenPlayers[1] &&
							turnsTakenPlayers[activePlayer]<=turnsTakenPlayers[2] &&
							turnsTakenPlayers[activePlayer]<=turnsTakenPlayers[3]){
						isLeastActivePlayerSelected = true;

						//Gdx.app.log("CCUBES", "isLeastActivePlayerSelected=true");
					}


					if(giveHint==true && isLeastActivePlayerSelected){
						Gdx.app.log("CCUBES", "The least active player wants to make a move, a hint can be presented!");
						//give a hint
					//	Gdx.app.log("CCUBES", "Hint incoming!" +((Cube)event.getTarget()).correctChar);
						((Cube)event.getTarget()).correctTile.hintColor=colors[activePlayer];
						((Cube)event.getTarget()).correctTile.setZIndex(cubes.length-1);
						//tls[0][0].color="RED";
						//tls[0][0].setZIndex(cubes.length);
						giveHint=false;
						hintSteps=0;
					}



					if(((Cube)event.getTarget()).connectedTile !=null){
						//Gdx.app.log("CCUBES", "remove cube "+((Cube)event.getTarget()).getName()+" from tile "+((Cube)event.getTarget()).connectedTile.getName());


						//player makes a valid move: drops cube on a tile that it was not on before
					//	Gdx.app.log("CCUBES", "Cube is on a tile: turnsTakenPlayers++");
						turnsTakenPlayers[activePlayer]++;
						((Cube)event.getTarget()).lastConnectedTile=((Cube)event.getTarget()).connectedTile;
						((Cube)event.getTarget()).connectedTile.removeCube();
						((Cube)event.getTarget()).connectedTile=null;
					}


					((Cube)event.getTarget()).playerPickedUp=activePlayer;
					return super.touchDown(event, x, y, pointer, button);
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					//	Gdx.app.log("CCUBES", "touchUp " + event.getTarget()+ ", event.getTarget().getX() = "+event.getTarget().getX() + ", event.getTarget().getY()  = "+event.getTarget().getY() + ", x = "+x+ ", y = "+y);
					((Cube)event.getTarget()).isDragging=false;
					((Cube)event.getTarget()).isDown=false;
					((Cube)event.getTarget()).color="";
					((Cube)event.getTarget()).setZIndex(cubes[cubes.length-1].originalZIndex);

					for(i =0; i<tiles.length; i++){
						for(j =0; j<tiles[i].length; j++){
							tiles[i][j].hintColor="";
							if(  ((event.getTarget().getX()+size/3) >tiles[i][j].getX() &&
									(event.getTarget().getX()+size/3) <tiles[i][j].getX()+size) &&
									((event.getTarget().getY()+size/3) >tiles[i][j].getY() &&
											(event.getTarget().getY()+size/3) <tiles[i][j].getY()+size)
									){
								if(tiles[i][j].cube!=null) {
									//	Gdx.app.log("CCUBES", "tile "+tiles[i][j].getName()+" already has a cube. remove cube "+ (tiles[i][j].cube).getName()+" and set cube "+ (event.getTarget()).getName());

									//player makes a valid move: drops cube on a tile that it was not on before, tile did have another cube
								//	Gdx.app.log("CCUBES", "player makes a valid move: drops cube on a tile that it was not on before, tile did have another cube: turnsTakenPlayers++");
									turnsTakenPlayers[((Cube)event.getTarget()).playerPickedUp]++;

									tiles[i][j].cube.setX(players[0].getWidth());
									tiles[i][j].cube.setY(0);
									tiles[i][j].cube.connectedTile = null;
									tiles[i][j].removeCube();
								}
								else{

									if(((Cube)event.getTarget()).lastConnectedTile!=null) {
										if (tiles[i][j] != ((Cube) event.getTarget()).lastConnectedTile) {
										//	Gdx.app.log("CCUBES", "1 player makes a valid move: drops cube on a tile that it was not on before: turnsTakenPlayers++");
											//cube came from an existing tile, player makes a valid move: drops cube on a tile that it was not on before
										//	turnsTakenPlayers[activePlayer]++;
										} else {
											//Gdx.app.log("CCUBES", "player dropped cube on the same tile it already was on when it was picked up: turnsTakenPlayers--");
											//player dropped cube on the same tile it already was on when it was picked up
											turnsTakenPlayers[((Cube)event.getTarget()).playerPickedUp]--;
										}
									}
									else{
										//cube came from field, not an existing tile. player makes a valid move: drops cube on a tile that it was not on before
										//Gdx.app.log("CCUBES", "2 player makes a valid move: drops cube on a tile that it was not on before: turnsTakenPlayers++");
										turnsTakenPlayers[((Cube)event.getTarget()).playerPickedUp]++;
									}

								}

								event.getTarget().setX(tiles[i][j].getX());
								event.getTarget().setY(tiles[i][j].getY());
								tiles[i][j].cube = (Cube) event.getTarget();
								((Cube) event.getTarget()).connectedTile = tiles[i][j];
								i=tiles.length;
								break;
							}
						}
					}
					for(int p=0;p<players.length;p++){
						//Gdx.app.log("CCUBES", "turnsTakenPlayers["+p+"] = " + turnsTakenPlayers[p]);
					}

					((Cube)event.getTarget()).lastConnectedTile=null;

					printCurrentSetup();
					checkSolution();


					Gdx.app.log("CCUBES", "Number of possible solutions for all trials = " + game.getPossibleCodesSize());
					Gdx.app.log("CCUBES", "Number of possible solutions for last 2 trials= " + opportunityCalculator_game.getPossibleCodesSize());

					opportunityCalculator_opportunity = opportunityCalculator_game.getPossibleCodesSize();


					super.touchUp(event, x, y, pointer, button);
				}

				public void checkSolution(){

					boolean solutionFound = true;
					for (j = 0; j < tiles[0].length; j++) {
						for (i = 0; i < tiles.length; i++) {

							if(tiles[i][j].cube==null){
								if(!solution[i][j].equals("x")) {
									solutionFound = false;
								}
							}
							else if(!tiles[i][j].cube.getName().equals(solution[i][j])){
								solutionFound=false;
							}

						}
					}
					if(solutionFound) {
						Gdx.app.log("CCUBES", "SOLUTION FOUND, YOU WON!");
					}
				}

				public void printCurrentSetup(){
					Gdx.app.log("CCUBES","--------------------------");
					String message = "";
					for (j = 0; j < tiles[0].length; j++) {
						for(i =0; i<tiles.length; i++) {
							message+=i+","+j+":";
							if(tiles[i][j].cube!=null){
								message+=tiles[i][j].cube.getName()+" ";
							}
							else{
								message+="x ";
							}
						}
						Gdx.app.log("CCUBES", message);
						message="";
					}
					//Gdx.app.log("CCUBES","-SOLUTION:");
					message = "";
					for (j = 0; j < solution[0].length; j++) {
						for(i =0; i<solution.length; i++) {
							message+=i+","+j+":"+solution[i][j]+" ";
						}
					//	Gdx.app.log("CCUBES", message);
						message="";
					}


				}

				@Override
				public void touchDragged(InputEvent event, float x, float y, int pointer) {
					((Cube)event.getTarget()).isDragging=true;

					Vector2 newTouch = new Vector2(event.getStageX(), event.getStageY());
					Vector2 delta = newTouch.cpy().sub(((Cube)event.getTarget()).lastTouch);
					//Gdx.app.log("CCUBES", "touchDragged " + event.getTarget()+ ", event.getTarget().getX() = "+event.getTarget().getX() + ", event.getTarget().getY()  = "+event.getTarget().getY() + ",    x = "+x+ ", y = "+y+ ",    event.getStageX() = "+event.getStageX()+ ", event.getStageY() = "+event.getStageY()+",    delta.x = "+delta.x+ ", delta.y = "+delta.y);

					float newPositionX = event.getTarget().getX()+delta.x;
					float newPositionY = event.getTarget().getY()+delta.y;

					if(newPositionX>((screenWidth-size)-players[0].getWidth())){
						newPositionX=((screenWidth-size)-players[0].getWidth());
					}
					if(newPositionX<players[0].getWidth()){
						newPositionX=players[0].getWidth();
					}

					if(newPositionY>((screenHeight-size)-(uiLineHeight+checkButton.getHeight()))){
						newPositionY=((screenHeight-size)-(uiLineHeight+checkButton.getHeight()));

					}
					if(newPositionY<0){
						newPositionY=0;
					}

					event.getTarget().setX(newPositionX);
					event.getTarget().setY(newPositionY);

					((Cube)event.getTarget()).lastTouch = newTouch;

					super.touchDragged(event, x, y, pointer);
				}
			});

		}
	}


	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect(0, this.screenHeight-(checkButton.getHeight()+uiLineHeight), this.screenWidth, checkButton.getHeight()+uiLineHeight);
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		shapeRenderer.rect(0, this.screenHeight-checkButton.getHeight(), this.screenWidth, checkButton.getHeight());
		shapeRenderer.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		batch.begin();
		batch.enableBlending();

		myFont.draw(batch, cubesCorrect+" cubes correct       "+turnsTaken+" turns taken", 80, this.screenHeight-checkButton.getHeight()+43);

		batch.end();

	}

	@Override
	public void dispose () {
	}
}
