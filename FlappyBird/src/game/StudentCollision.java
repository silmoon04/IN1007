package game;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;

public class StudentCollision implements CollisionListener {
    private final DynamicBody student;

    public StudentCollision(DynamicBody student) {
        this.student = student;
    }

    @Override
    public void collide(CollisionEvent e) {
        // Check if the student collides with a wall
        if (e.getOtherBody() instanceof StaticBody) {

            // Example: Stop movement (simulate game over)
            student.setLinearVelocity(new Vec2(0, 0));
            student.setAngularVelocity(0);
            student.setPosition(new Vec2(0f, 12.5f));
        }
    }
}
