package paddle_movement_strategies;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class UserMovementStrategy implements MovementStrategy {

    private final UserInputListener inputListener;

    public UserMovementStrategy(UserInputListener inputListener) {
    this.inputListener = inputListener;
    }

    @Override
    public Vector2 calcMovementDir(GameObject owner) {
        Vector2 movementDirection = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movementDirection = movementDirection.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDirection = movementDirection.add(Vector2.RIGHT);
        }
        return movementDirection;
    }
}
