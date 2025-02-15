package collision_strategies.brick;

import collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;



public class RemoveBrickCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollections;
    private final Counter bricksCounter;

    public RemoveBrickCollisionStrategy(GameObjectCollection gameObjectCollections, Counter bricksCounter) {
        this.gameObjectCollections = gameObjectCollections;
        this.bricksCounter = bricksCounter;
    }



    public void onCollision(GameObject thisObj, GameObject otherObj) {
        gameObjectCollections.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
        this.bricksCounter.increaseBy(-1);

    }
}

