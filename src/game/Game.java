package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/** Main entry point for the game. */
public class Game {

    private static final float GRAVITY = 120f;
    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 500;
    private static final float VIEW_CENTER_Y = 12.5f;
    private static final float BIRD_RADIUS = 0.7f;
    private static final float BIRD_SPEED = 5f;
    private static final float JUMP_IMPULSE = 40f;
    private static final float TOP_BOUND = 25f;
    private static final float BOTTOM_BOUND = 0f;
    private static final float WALL_SPACING = 8f;
    private static final float WALL_WIDTH = 2f;

    private final World world;
    private final UserView view;
    private final ArrayList<StaticBody> walls = new ArrayList<>();
    private DynamicBody bird;

    public Game() {
        world = new World();
        world.setGravity(GRAVITY);

        createWorld();

        view = new UserView(world, VIEW_WIDTH, VIEW_HEIGHT);
        view.setCentre(new Vec2(0, VIEW_CENTER_Y));
        //view.setGridResolution(1);

        JFrame frame = new JFrame("City Game");
        frame.add(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bird.setLinearVelocity(new Vec2(BIRD_SPEED, 0));
                    bird.applyImpulse(new Vec2(0, JUMP_IMPULSE));
                }
            }
        });

        world.addStepListener(new StepListener() {
            @Override
            public void preStep(StepEvent stepEvent) { }

            @Override
            public void postStep(StepEvent stepEvent) {
                updateGame();
            }
        });

        world.start();
    }

    private void createWorld() {
        for (int i = 1; i < 10; i++) {
            createWalls(i);
        }
        createBird();
    }

    private void createBird() {
        Shape circle = new CircleShape(BIRD_RADIUS);
        bird = new DynamicBody(world, circle);
        bird.setPosition(new Vec2(0, 0));
        bird.addCollisionListener(new StudentCollision(bird));
    }

    private void createWalls(int index) {
        float topWallHeight = ((int) (Math.random() * 10) + 2) / 2f;
        float gap = ((int) (Math.random() * 4) + 5) / 2f;

        Shape topWallShape = new BoxShape(WALL_WIDTH, topWallHeight);
        StaticBody topWall = new StaticBody(world, topWallShape);
        topWall.setPosition(new Vec2(index * WALL_SPACING, TOP_BOUND - topWallHeight));

        float bottomWallHeight = (VIEW_CENTER_Y - gap - topWallHeight) / 2f;
        Shape bottomWallShape = new BoxShape(WALL_WIDTH, bottomWallHeight);
        StaticBody bottomWall = new StaticBody(world, bottomWallShape);
        bottomWall.setPosition(new Vec2(index * WALL_SPACING, bottomWallHeight));

        walls.add(topWall);
        walls.add(bottomWall);
    }

    private void updateGame() {
        Vec2 birdPos = bird.getPosition();
        if (birdPos.y < BOTTOM_BOUND || birdPos.y > TOP_BOUND) {
            bird.setPosition(new Vec2(birdPos.x, VIEW_CENTER_Y));
            bird.setLinearVelocity(new Vec2(0, 0));
        }
        view.setCentre(new Vec2(birdPos.x, VIEW_CENTER_Y));

        Vec2 currentVelocity = bird.getLinearVelocity();
        bird.setLinearVelocity(new Vec2(BIRD_SPEED, currentVelocity.y));
    }

    public static void main(String[] args) {
        new Game();
    }
}
