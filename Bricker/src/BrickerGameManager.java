import collision_strategies.brick.BrickStrategyFactory;
import collision_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import game_objects.*;
import paddle_movement_strategies.UserMovementStrategy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickerGameManager extends GameManager{

    private static final float BALL_VELOCITY = 300;
    private static final int MAX_LOSES = 3;
    private final int WIDTH_TO_CENTER = 2;
    private final int PADDLE_HEIGHT = 20;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_PADDING = 20;
    private final int PADDLE_Y = PADDLE_HEIGHT / WIDTH_TO_CENTER + PADDLE_PADDING;
    private final int BORDER_THICKNESS = 20;
    private final int ROWS = 5, COLS = 8;
    private GameObject lastBall;
    private Paddle paddle;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter losesCounter;
    private List<GameObject> hearts;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Counter ballCounter;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);

    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.ballCounter = new Counter();

        // Creating paddle
        //addAiPaddle(inputListener);
        this.paddle = addUserPaddle(inputListener);

        // Creating walls
        addWalls();

        // Creating background
        addBackground(imageReader);

        // Add harts
        addHarts(imageReader);

        // Creating ball
        addBall();

        // Initializing bricks
        this.bricksCounter = new Counter();
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(
                this.bricksCounter,
                this.gameObjects(),
                this.soundReader,
                this.imageReader,
                this.windowDimensions,
                this.lastBall,
                this.ballCounter,
                this.paddle
        );
        initGameBricks(this.ROWS, this.COLS, brickStrategyFactory);

        //move it
        moveBall();

    }

    private void addHarts(ImageReader imageReader) {
        final float HEART_SIZE = 25;
        final float HEART_SIDE_PADDING = 5;
        this.hearts = new ArrayList<>();
        this.losesCounter = new Counter();
        losesCounter.increaseBy(MAX_LOSES);

        Renderable heartImage = imageReader.readImage("assets/heart.png", true);

        for(int i = 0; i < MAX_LOSES; i++){
            this.hearts.add(
                    new GameObject(
                    new Vector2(BORDER_THICKNESS + (HEART_SIZE + HEART_SIDE_PADDING) * i, this.windowDimensions.y() - HEART_SIZE - BORDER_THICKNESS),
                    new Vector2(HEART_SIZE, HEART_SIZE),
                    heartImage)
            );
            gameObjects().addGameObject(hearts.get(hearts.size() - 1), Layer.BACKGROUND);

        }

    }


    private void moveBall() {
        Random rnd = new Random();
        final float x_velocity = rnd.nextBoolean() ? BALL_VELOCITY : -BALL_VELOCITY;
        final float y_velocity = -BALL_VELOCITY;
        this.lastBall.setVelocity(new Vector2(x_velocity, y_velocity));


        // Delay for 1 second
        try {
            Thread.sleep(1000); // Sleep for 1000 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
        }
    }

    @Override
    public void update(float deltaTime) {
        CheckForGameEnd();
        super.update(deltaTime);

    }

    private void CheckForGameEnd() {
        String prompt = "";

        if(this.bricksCounter.value() <= 0){
            prompt = "You win!";
            this.bricksCounter.reset();
        }else if(this.ballCounter.value() == 0){
            losesCounter.increaseBy(-1);
            removeHeart();
            this.paddle.setCenter( new Vector2(windowDimensions.x() / WIDTH_TO_CENTER, windowDimensions.y() - PADDLE_Y));
            addBall();
            moveBall();

        }
        else if(this.losesCounter.value() == 0){
            prompt = "You lose!";
        }
        if(!prompt.isEmpty()){
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt)){
                windowController.resetGame();
            }else{
                windowController.closeWindow();
            }
        }
    }

    private void removeHeart() {
        gameObjects().removeGameObject(hearts.get(losesCounter.value()), Layer.BACKGROUND);
    }

    private void initGameBricks(int rows, int cols, BrickStrategyFactory brickStrategyFactory) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        final float BRICKS_SIDE_PADDING = 0;
        final float FILLED_SCREEN_PERCENT = 0.3f;
        final float BRICK_HORIZONTAL_PADDING = 2;
        final float BRICK_VERTICAL_PADDING = 2;
        final float BRICK_HEIGHT = ( ( (this.windowDimensions.y() * FILLED_SCREEN_PERCENT) - BORDER_THICKNESS) / rows ) - BRICK_VERTICAL_PADDING;
        final float BRICK_WIDTH = ( (this.windowDimensions.x() - ( 2 * BORDER_THICKNESS + 2 * BRICKS_SIDE_PADDING)) / cols ) - BRICK_HORIZONTAL_PADDING;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                CollisionStrategy collisionStrategy = brickStrategyFactory.getStrategy();
                bricksCounter.increaseBy(1);
                Brick brick = new Brick(
                        Vector2.ZERO,
                        new Vector2(BRICK_WIDTH, BRICK_HEIGHT),
                        brickImage,
                        collisionStrategy,
                        gameObjects()
                );
                Vector2 brickTopLeftCorner = new Vector2(BORDER_THICKNESS + j * (BRICK_WIDTH + BRICK_HORIZONTAL_PADDING),BORDER_THICKNESS + i * (BRICK_HEIGHT + BRICK_VERTICAL_PADDING));
                brick.setTopLeftCorner(brickTopLeftCorner);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }

    }



    private void addWalls() {
        final Color BORDER_COLOR = Color.CYAN;

        Vector2[] positions = {
                Vector2.ZERO, // Left wall
                Vector2.ZERO, // Top wall
                new Vector2(windowDimensions.x() - this.BORDER_THICKNESS, 0), // Right wall
        };

        Vector2[] sizes = {
                new Vector2(this.BORDER_THICKNESS, windowDimensions.y()), // Left wall
                new Vector2(windowDimensions.x(), this.BORDER_THICKNESS), // Top wall
                new Vector2(this.BORDER_THICKNESS, windowDimensions.y()), // Right wall
        };

        for (int i = 0; i < positions.length; i++) {
            GameObject wall = new GameObject(
                    positions[i],
                    sizes[i],
                    null // invisible
                    //new RectangleRenderable(BORDER_COLOR) // <- for color
            );

            gameObjects().addGameObject(wall);
        }
    }

    private void addBackground(ImageReader imageReader){
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);

        GameObject background = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                backgroundImage);

        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }



    private Paddle addUserPaddle(UserInputListener inputListener){

        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);

        Paddle userPaddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                windowDimensions,
                new UserMovementStrategy(inputListener),
                null
        );

        userPaddle.setCenter( new Vector2(windowDimensions.x() / WIDTH_TO_CENTER, (int)windowDimensions.y() - PADDLE_Y));
        this.gameObjects().addGameObject(userPaddle, Layer.DEFAULT);
        return userPaddle;
    }



    private void addBall(){
        final int BALL_SIZE = 20;
        this.ballCounter.increaseBy(1);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");

        Renderable ballImage = imageReader.readImage("assets/ball.png", true);

        this.lastBall = new Ball(
                windowDimensions.mult(1f / WIDTH_TO_CENTER),
                new Vector2(BALL_SIZE, BALL_SIZE),
                ballImage,
                collisionSound,
                windowDimensions,
                this.ballCounter,
                gameObjects()
        );
        gameObjects().addGameObject(lastBall);

    }

    public static void main(String[] args) {
        final Vector2 windowDimensions = new Vector2(700, 500);

        new BrickerGameManager(
                "Bricker",
                windowDimensions
        ).run();
    }
}