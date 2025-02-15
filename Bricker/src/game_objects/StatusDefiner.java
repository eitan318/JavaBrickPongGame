package game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class StatusDefiner extends GameObject {
    private static final float VELOCITY = 100;
    private final GameObjectCollection gameObjectCollection;
    private final Paddle paddleToTarget;
    private Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public StatusDefiner(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                         GameObjectCollection gameObjectCollection, Paddle paddleToTarget, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
        this.paddleToTarget = paddleToTarget;
        this.windowDimensions = windowDimensions;
        this.setVelocity(Vector2.DOWN.mult(VELOCITY));
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other == paddleToTarget;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjectCollection.removeGameObject(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double ballHeight = this.getCenter().y();
        if(ballHeight > this.windowDimensions.y()){
            this.gameObjectCollection.removeGameObject(this);
        }
    }
}
