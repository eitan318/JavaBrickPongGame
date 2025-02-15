package game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Ball extends GameObject {
    private Sound collisionSound;
    private Vector2 windowDimensions;
    private Counter ballCounter;
    private final GameObjectCollection gameObjectCollection;
    private static final float CONSTANT_SPEED = 400f; // Define a constant speed
    private static final float MIN_Y_VELOCITY = 200f; // Define a minimum Y velocity

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                Vector2 windowDimensions, Counter ballCounter, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);

        this.collisionSound = collisionSound;
        this.windowDimensions = windowDimensions;
        this.ballCounter = ballCounter;
        this.gameObjectCollection = gameObjectCollection;

        // Set the initial velocity with a constant speed
        setVelocity(Vector2.RIGHT.mult(CONSTANT_SPEED));
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        Vector2 postCollisionVelocity = getVelocity().flipped(collision.getNormal());
        postCollisionVelocity = adjustVelocity(postCollisionVelocity);
        setVelocity(postCollisionVelocity);

        collisionSound.play();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Ensure the velocity keeps a constant speed with a minimum Y velocity
        setVelocity(adjustVelocity(getVelocity()));

        double ballHeight = this.getCenter().y();
        if (ballHeight > this.windowDimensions.y()) {
            this.ballCounter.increaseBy(-1);
            this.gameObjectCollection.removeGameObject(this);
        }
    }

    private Vector2 adjustVelocity(Vector2 velocity) {
        if (Math.abs(velocity.y()) < MIN_Y_VELOCITY) {
            float xComponent = (float) Math.sqrt(CONSTANT_SPEED * CONSTANT_SPEED - MIN_Y_VELOCITY * MIN_Y_VELOCITY);
            float sign = Math.signum(velocity.x());
            return new Vector2(sign * xComponent, MIN_Y_VELOCITY * Math.signum(velocity.y()));
        }
        return velocity.normalized().mult(CONSTANT_SPEED);
    }
}
