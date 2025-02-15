package paddle_movement_strategies;

import danogl.GameObject;
import danogl.util.Vector2;
import game_objects.Paddle;

public interface MovementStrategy {

    Vector2 calcMovementDir(GameObject owner);
}
