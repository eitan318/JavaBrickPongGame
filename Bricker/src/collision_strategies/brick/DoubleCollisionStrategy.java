package collision_strategies.brick;

import collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import game_objects.Paddle;

public class DoubleCollisionStrategy implements CollisionStrategy {
    private final Counter bricksCounter;
    private final GameObjectCollection gameObjectCollection;
    private final SoundReader soundReader;
    private final ImageReader imageReader;
    private final Vector2 windowDimensions;
    private final GameObject objToFollow;
    private final Counter ballCounter;
    private final Paddle paddleToTarget;

    public DoubleCollisionStrategy(Counter lossCounter, GameObjectCollection gameObjectCollection,
                                   SoundReader soundReader, ImageReader imageReader, Vector2 windowDimensions,
                                   GameObject objToFollow, Counter ballCounter, Paddle paddleToTarget){

        this.bricksCounter = lossCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.objToFollow = objToFollow;
        this.ballCounter = ballCounter;
        this.paddleToTarget = paddleToTarget;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        BrickStrategyFactory factory = new BrickStrategyFactory(
                bricksCounter,
                gameObjectCollection,
                soundReader,
                imageReader,
                windowDimensions,
                objToFollow,
                ballCounter,
                paddleToTarget
        );
        factory.getStrategy().onCollision(thisObj, otherObj);
        factory.getStrategy().onCollision(thisObj, otherObj);
        this.bricksCounter.increaseBy(1);
    }


}
