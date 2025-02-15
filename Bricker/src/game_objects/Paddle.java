package game_objects;

import collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import paddle_movement_strategies.MovementStrategy;

public class Paddle extends GameObject {
    private static final float PADDLE_SPEED = 400;
    private static final float MIN_DISTANCE_FROM_SCREEN_EDGE = 20;
    private final Vector2 windowDimensions;
    private final MovementStrategy movementStrategy;
    private final int PADDLE_HEIGHT = 20;
    private final int PADDLE_WIDTH = 100;
    private CollisionStrategy collisionStrategy;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Vector2 windowDimensions, MovementStrategy movementStrategy, CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.windowDimensions = windowDimensions;
        this.movementStrategy = movementStrategy;
        this.collisionStrategy = collisionStrategy;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 movementDir = movementStrategy.calcMovementDir(this);
        this.setVelocity(movementDir.mult(PADDLE_SPEED));

        if(this.getTopLeftCorner().x() < MIN_DISTANCE_FROM_SCREEN_EDGE){
            transform().setTopLeftCornerX(MIN_DISTANCE_FROM_SCREEN_EDGE);
        }else if(this.getTopLeftCorner().x() > windowDimensions.x() - MIN_DISTANCE_FROM_SCREEN_EDGE - getDimensions().x()){
            transform().setTopLeftCornerX(windowDimensions.x() - MIN_DISTANCE_FROM_SCREEN_EDGE - getDimensions().x());
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(this.collisionStrategy != null){
            this.collisionStrategy.onCollision(this, other);
        }

    }
}
