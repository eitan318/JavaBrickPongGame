package collision_strategies.brick;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import game_objects.Paddle;
import game_objects.SizeChanger;

public class SizeChangersCollisionStrategy extends RemoveBrickCollisionStrategy {
    private final GameObjectCollection gameObjectCollections;
    private final boolean isNegative;
    private final ImageReader imageReader;
    private final Paddle paddleToTarget;
    private final Vector2 windowDimensions;

    public SizeChangersCollisionStrategy(GameObjectCollection gameObjectCollections, Counter counter,
                                         boolean isNegative, ImageReader imageReader, Paddle paddleToTarget, Vector2 windowDimensions) {
        super(gameObjectCollections, counter);
        this.gameObjectCollections = gameObjectCollections;
        this.isNegative = isNegative;


        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.paddleToTarget = paddleToTarget;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Vector2 dimensions = new Vector2(50, 20);
        SizeChanger sizeChanger = new SizeChanger(
                thisObj.getCenter(),
                dimensions,
                isNegative,
                imageReader ,
                gameObjectCollections,
                this.paddleToTarget,
                windowDimensions);
        gameObjectCollections.addGameObject(sizeChanger);
    }
}
