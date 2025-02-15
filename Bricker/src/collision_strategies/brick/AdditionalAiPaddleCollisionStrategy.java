package collision_strategies.brick;

import collision_strategies.CollisionStrategy;
import collision_strategies.paddle.RemovePaddleColisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import game_objects.Paddle;
import paddle_movement_strategies.AiMovementStrategy;

import java.util.Random;

public class AdditionalAiPaddleCollisionStrategy extends RemoveBrickCollisionStrategy implements CollisionStrategy {
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 20;
    private static final int CHANCE_FOR_AI_HELP = 70;
    private static final int FULL_PERCENT = 100;
    private final int PADDLE_PADDING = (int)PADDLE_HEIGHT + 40;
    private final int WIDTH_TO_CENTER = 2;
    private final int AI_HELPER_COLLISIONS = 2;
    private final int PADDLE_Y = (int)PADDLE_HEIGHT / WIDTH_TO_CENTER + PADDLE_PADDING;
    private final GameObjectCollection gameObjectCollections;
    private ImageReader imageReader;
    private Vector2 windowDimensions;
    private Random rnd = new Random();

    public AdditionalAiPaddleCollisionStrategy(GameObjectCollection gameObjectCollections, Counter bricksCounter,
                                               ImageReader imageReader, Vector2 windowDimensions){
        super(gameObjectCollections, bricksCounter);
        this.gameObjectCollections = gameObjectCollections;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if(rnd.nextInt(FULL_PERCENT) <= CHANCE_FOR_AI_HELP){
        addAiPaddle();
        }
    }

    private Paddle addAiPaddle() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Counter aiCounter = new Counter();
        aiCounter.increaseBy(AI_HELPER_COLLISIONS);

        Paddle aiPaddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                windowDimensions,
                new AiMovementStrategy(gameObjectCollections),
                new RemovePaddleColisionStrategy(gameObjectCollections, aiCounter)
        );
        aiPaddle.setCenter( new Vector2(windowDimensions.x() / WIDTH_TO_CENTER, windowDimensions.y() - PADDLE_Y));
        this.gameObjectCollections.addGameObject(aiPaddle);
        return aiPaddle;
    }
}

