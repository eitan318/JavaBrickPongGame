package collision_strategies.paddle;

import collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import game_objects.Ball;


public class RemovePaddleColisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjectCollections;
    private Counter collisionCounter;

    public RemovePaddleColisionStrategy(GameObjectCollection gameObjectCollections, Counter collisionCounter) {
        this.gameObjectCollections = gameObjectCollections;
        this.collisionCounter = collisionCounter;
    }


    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println(collisionCounter.value());
        if(otherObj instanceof Ball){
            collisionCounter.increaseBy(-1);
        }
        if(collisionCounter.value() == 0){
            gameObjectCollections.removeGameObject(thisObj);
        }

    }
}
