/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.cien.opengl2dexperimento;

import com.cien.opengl2dexperimento.sprites.SpriteLoader;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author Cien
 */
public class Game {

    public static final List<Body> BODIES = new ArrayList<>();
    public static float TPF = 1 / 60f;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    public static long WINDOW_POINTER;

    private static void setup() {
        int grassTexture = SpriteLoader.createTexture("grassMid.png");
        int playerTexture = SpriteLoader.createTexture("p1_front.png");
        int brickTexture = SpriteLoader.createTexture("brickWall.png");
        int bridgeTexture = SpriteLoader.createTexture("bridge.png");

        for (int i = 0; i < 24; i++) {
            Sprite grass = new Sprite();
            grass.setTexture(grassTexture);
            grass.setStaticBody(true);
            grass.setX(grass.getWidth() * i);
            BODIES.add(grass);
        }

        

        Sprite player = new Sprite() {
            @Override
            public void willCollideWith(Body body, boolean xAxis) {
                if (body instanceof Sprite e) {
                    if (e.getTexture() == bridgeTexture && Math.abs(getSpeedY()) >= 100 && !xAxis) {
                        setSpeedY(getSpeedY() * -0.75f);
                    } else {
                        super.willCollideWith(body, xAxis);
                    }
                }
            }
            
            @Override
            public void update() {
                super.update();

                int speed = 0;

                if (glfwGetKey(WINDOW_POINTER, GLFW_KEY_A) == GLFW_PRESS) {
                    speed -= 300;
                }
                if (glfwGetKey(WINDOW_POINTER, GLFW_KEY_D) == GLFW_PRESS) {
                    speed += 300;
                }

                setSpeedX(speed);

                if (getY() < -200) {
                    setX(50);
                    setY(100);
                }
            }
        };
        player.setX(50);
        player.setY(100);
        player.setTexture(playerTexture);
        player.setStaticBody(false);
        BODIES.add(player);

        glfwSetKeyCallback(WINDOW_POINTER, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                player.setSpeedY(player.getSpeedY() + 300);
            }
        });

        {
            Sprite brick = new Sprite();
            brick.setTexture(brickTexture);
            brick.setX(150);
            brick.setY(80);
            brick.setStaticBody(true);

            BODIES.add(brick);
        }
        
        {
            Sprite brick = new Sprite();
            brick.setTexture(brickTexture);
            brick.setX(250);
            brick.setY(150);
            brick.setStaticBody(true);

            BODIES.add(brick);
        }
        
        {
            Sprite brick = new Sprite();
            brick.setTexture(brickTexture);
            brick.setX(350);
            brick.setY(250);
            brick.setStaticBody(true);

            BODIES.add(brick);
        }
        
        Sprite bridge = new Sprite();
        bridge.setStaticBody(true);
        bridge.setTexture(bridgeTexture);
        bridge.setX(480);
        bridge.setY(100);
        BODIES.add(bridge);
    }

    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new Error("Could not initialize glfw.");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        long window = glfwCreateWindow(800, 600, "Cien OpenGL 2D Experimento", NULL, NULL);

        if (window == NULL) {
            throw new Error("Could not create window.");
        }

        WINDOW_POINTER = window;

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glfwSwapInterval(1);

        glfwSetFramebufferSizeCallback(window, (ptr, width, height) -> {
            WIDTH = width;
            HEIGHT = height;
        });

        glClearColor(
                96 / 255f,
                130 / 255f,
                282 / 255f,
                1f
        );

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glDisable(GL_DEPTH_TEST);

        setup();

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glViewport(0, 0, WIDTH, HEIGHT);

            glfwPollEvents();

            Body[] bodies = BODIES.toArray(Body[]::new);

            for (Body b : bodies) {
                b.update();
            }

            AABB toTest = new AABB();

            for (Body a : bodies) {
                if (a.isStaticBody()) {
                    continue;
                }

                toTest.setWidth(a.getWidth());
                toTest.setHeight(a.getHeight());

                for (Body b : bodies) {
                    if (a == b) {
                        continue;
                    }

                    toTest.setX(a.getX() + (a.getSpeedX() * TPF));
                    toTest.setY(a.getY());
                    if (toTest.inCollision(b)) {
                        a.willCollideWith(b, true);
                    }

                    toTest.setX(a.getX());
                    toTest.setY(a.getY() + (a.getSpeedY() * TPF));
                    if (toTest.inCollision(b)) {
                        a.willCollideWith(b, false);
                    }
                }
            }

            for (Body b : bodies) {
                b.move();
            }

            for (Body b : bodies) {
                b.render();
            }

            glfwSwapBuffers(window);
        }

    }
}
