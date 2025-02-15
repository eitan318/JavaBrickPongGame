package collision_strategies.brick;

import collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import game_objects.Paddle;

import java.util.Random;

public class BrickStrategyFactory {

    private final Counter bricksCounter;
    private final GameObjectCollection gameObjectCollection;
    private final SoundReader soundReader;
    private final ImageReader imageReader;
    private final Random rnd = new Random();
    private Vector2 windowDimensions;
    private GameObject objToFollow;
    private Counter ballCounter;
    private Paddle paddleToTarget;


    public BrickStrategyFactory(Counter bricksCounter, GameObjectCollection gameObjectCollection,
                                SoundReader soundReader, ImageReader imageReader, Vector2 windowDimensions,
                                GameObject objToFollow, Counter ballCounter, Paddle paddleToTarget){
        this.bricksCounter = bricksCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.objToFollow = objToFollow;
        this.ballCounter = ballCounter;
        this.paddleToTarget = paddleToTarget;
    }

    public CollisionStrategy getStrategy() {

        CollisionStrategy[] collisionStrategies = {
                new RemoveBrickCollisionStrategy(
                        this.gameObjectCollection,
                        this.bricksCounter),
                new AdditionalBallsCollisionStrategy(
                        this.gameObjectCollection,
                        this.bricksCounter,
                        this.soundReader,
                        this.ballCounter,
                        this.imageReader,
                        windowDimensions
                ),
                new AdditionalAiPaddleCollisionStrategy(
                        gameObjectCollection,
                        bricksCounter,
                        imageReader,
                        this.windowDimensions
                ),
                new SizeChangersCollisionStrategy(
                        gameObjectCollection,
                        bricksCounter,
                        false,
                        imageReader,
                        this.paddleToTarget,
                        windowDimensions
                ),
                new SizeChangersCollisionStrategy(
                        gameObjectCollection,
                        bricksCounter,
                        true,
                        imageReader,
                        paddleToTarget,
                        windowDimensions
                ),
                new DoubleCollisionStrategy(
                        bricksCounter,
                        gameObjectCollection,
                        soundReader,
                        imageReader,
                        windowDimensions,
                        objToFollow,
                        ballCounter,
                        paddleToTarget
                )
        };
        int randIdx = this.rnd.nextInt(collisionStrategies.length);
        return collisionStrategies[randIdx];
    }
}
