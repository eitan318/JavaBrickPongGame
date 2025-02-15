package game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

public class SizeChanger extends StatusDefiner{
    private final Paddle paddleToTarget;
    private final boolean isNegative;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner        Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           Width and height in window coordinates.
     * @param gameObjectCollection
     * @param windowDimensions
     */
    public SizeChanger(Vector2 topLeftCorner, Vector2 dimensions, boolean isNegative, ImageReader imageReader,
                       GameObjectCollection gameObjectCollection,
                       Paddle paddleToTarget, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions,
                imageReader.readImage(isNegative ? "assets/buffNarrow.png" : "assets/buffWiden.png", false),
                gameObjectCollection,
                paddleToTarget,
                windowDimensions);
        this.isNegative = isNegative;
        this.paddleToTarget = paddleToTarget;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 origDim = paddleToTarget.getDimensions();
        Vector2 newDim = new Vector2(origDim.x() * (isNegative ? 0.8f : 1.1f), origDim.y());
        paddleToTarget.setDimensions(newDim);

    }
}
