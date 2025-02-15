package collision_strategies.brick;

import collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import game_objects.Ball;

import java.util.Random;

public class AdditionalBallsCollisionStrategy extends RemoveBrickCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollections;
    private final SoundReader soundReader;
    private final Counter ballCounter;
    private final ImageReader imageReader;
    private static final float BALL_VELOCITY = 300;
    private Vector2 windowDimensions;
    private Random rnd = new Random();

    public AdditionalBallsCollisionStrategy(GameObjectCollection gameObjectCollections, Counter bricksCounter,
                                            SoundReader soundReader, Counter ballCounter, ImageReader imageReader, Vector2 windowDimensions) {
        super(gameObjectCollections, bricksCounter);
        this.gameObjectCollections = gameObjectCollections;
        this.soundReader = soundReader;
        this.ballCounter = ballCounter;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
    }

    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Vector2 ballsEnterPos = thisObj.getCenter();
        int ballsAmount = rnd.nextInt(2);
        shootBalls(ballsEnterPos, ballsAmount);
    }

    private void shootBalls(Vector2 ballsEnterPos, int ballsAmount){
        final int BALL_SIZE = 20;

        Sound collisionSound = soundReader.readSound("assets/blop.wav");

        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        ballCounter.increaseBy(ballsAmount);

        for(int i = 0; i < ballsAmount; i++){
            Ball ball = new Ball(
                    ballsEnterPos,
                    new Vector2(BALL_SIZE, BALL_SIZE),
                    ballImage,
                    collisionSound,
                    windowDimensions,
                    ballCounter,
                    gameObjectCollections
            );
            Random rnd = new Random();
            final float x_velocity = rnd.nextBoolean() ? BALL_VELOCITY : -BALL_VELOCITY;
            final float y_velocity = BALL_VELOCITY;
            ball.setVelocity(new Vector2(x_velocity, y_velocity));
            this.gameObjectCollections.addGameObject(ball);
        }


    }
}
