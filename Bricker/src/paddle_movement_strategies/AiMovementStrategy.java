package paddle_movement_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import game_objects.Ball;

public class AiMovementStrategy implements MovementStrategy {

    private final GameObjectCollection gameObjectCollection;
    private final int TOLERANCE = 30;
    private GameObject objectToFollow;


    public AiMovementStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
        setObjToFollow();

    }
    private void setObjToFollow() {
        Ball highestYBall = null;
        double highestY = Double.NEGATIVE_INFINITY;

        for (GameObject gameObject : gameObjectCollection) {
            if (gameObject instanceof Ball) {
                Ball ball = (Ball) gameObject;
                double y = ball.getCenter().y();
                if (y > highestY) {
                    highestY = y;
                    highestYBall = ball;
                }
            }
        }

        if (highestYBall != null) {
            objectToFollow = highestYBall;
        }
    }

    @Override
    public Vector2 calcMovementDir(GameObject owner) {
        Vector2 movementDirection = Vector2.ZERO;
        if(this.objectToFollow.getCenter().x() + TOLERANCE < owner.getCenter().x()){
            movementDirection = Vector2.LEFT;
        }else if (objectToFollow.getCenter().x() - TOLERANCE> owner.getCenter().x()) {
            movementDirection = Vector2.RIGHT;
        }
        return movementDirection;
    }
}
