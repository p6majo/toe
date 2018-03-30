package com.nbodygl_gravity;

/*
    Todo:
    "implement" mpi code
    touch friendly
    Zoom and move
    focus on  object
    move object
    set velocity to zero
    save and open
    bhut lines
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class nbodysim implements Runnable {

    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback keyCallback;
    private static GLFWMouseButtonCallback mouseCallback;
    private static GLFWScrollCallback scrollCallback;
    private static GLFWFramebufferSizeCallback sizeCallback;


    boolean mouseIsDown = false;
    int drawPot = 0;
    int oId = 0;

    Random rand = new Random();
    private long window;
    private Simulation sim = null;
    private double radius = 10;
    private boolean focus = false;
    private int fID = 0;
    private float dx = 0;
    private float dy = 0;

    public static void main(String[] args) {
        new nbodysim().start();
    }

    public void start() {

        try {
            init();

            sim = new Simulation();

            new Thread(this).start();

            loop();

            glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            glfwTerminate();
            errorCallback.free();
        }
    }

    public void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        // glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        int WIDTH = 800;
        int HEIGHT = 600;

        window = glfwCreateWindow(WIDTH, HEIGHT, "nbodysim", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
                if (key == GLFW_KEY_R && action == GLFW_PRESS) {
                    focus = false;
                    sim.reset();
                    mouseIsDown = false;
                }
                if (key == GLFW_KEY_P && action == GLFW_PRESS) {
                    sim.togglePause();
                }
                if (key == GLFW_KEY_D && action == GLFW_PRESS) {
                    drawPot = (drawPot + 1) % 3;
                }
            }
        });

        glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS && !mouseIsDown) {
                    mouseIsDown = true;

                    DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(window, x, y);
                    x.rewind();
                    y.rewind();

                    sim.pause();
                    oId = sim.addObject(new SObject(x.get() + dx, y.get() + dy, radius, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())));
                }

                if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                    if (mouseIsDown) {
                        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
                        glfwGetCursorPos(window, x, y);
                        x.rewind();
                        y.rewind();

                        sim.setVelocity(oId, x.get() + dx, y.get() + dy);
                        sim.unPause();
                    }

                    mouseIsDown = false;
                }

                if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
                    DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                    int i;

                    glfwGetCursorPos(window, x, y);
                    x.rewind();
                    y.rewind();

                    if ((i = sim.getHitObj(x.get() - dx, y.get() - dy)) != -1) {
                        focus = true;
                        fID = i;
                    } else {
                        focus = false;
                    }
                }
            }
        });

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                radius += yoffset;
                if (radius < 2) radius = 2;
            }
        });

        glfwSetFramebufferSizeCallback(window, sizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        });

       GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    private float dist(float px, float py, float width, float height, float x, float y) {
        float dx = Math.max(Math.abs(px - x) - width / 2, 0);
        float dy = Math.max(Math.abs(py - y) - height / 2, 0);
        return dx * dx + dy * dy + 1;
    }

    private void loop() {

        GL.createCapabilities();

        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        FloatBuffer vertices = BufferUtils.createFloatBuffer(30000);

        vertices.put(new float[]{0.0f, 0.5f, 0.0f, 0.5f, -0.5f, 0.0f, -0.5f, -0.5f, 0.0f});

        vertices.rewind();

        int drawvbo = glGenBuffers();
        int objvao = glGenVertexArrays();
        int backvbo = glGenBuffers();
        int backvao = glGenVertexArrays();

        glBindBuffer(GL_ARRAY_BUFFER, drawvbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
        glBindVertexArray(objvao);

        final String vertex_shader1 =
                "#version 150\n" +
                        "in vec3 vp;\n" +
                        "in vec3 color;\n" +
                        "out vec3 Color;\n" +
                        "out float point;\n" +
                        "void main() {\n" +
                        "   Color = color;\n" +
                        "   point = vp.z;\n" +
                        "   gl_Position = vec4(vp.x, vp.y, 0.0, 1.0);\n" +
                        "   gl_PointSize = vp.z;\n" +
                        "}\n";

        final String frag_shader1 =
                "#version 150\n" +
                        "in vec3 Color;\n" +
                        "in float point;\n" +
                        "out vec4 frag_colour;\n" +
                        "void main () {\n" +
                        "  if(point > 0) {\n" +
                        "      vec2 circCoord = 2.0 * gl_PointCoord - 1.0;\n" +
                        "      if(dot(circCoord, circCoord) > 1.0) {\n" +
                        "          discard;\n" +
                        "      }\n" +
                        "      else {\n" +
                        "          frag_colour = vec4(Color, 1.0);\n" +
                        "      }\n" +
                        "  } else {" +
                        "      frag_colour = vec4(Color, 1.0);\n" +
                        "  }" +
                        "}\n";

        int shader_programme1 = glCreateProgram();

        int vertexShaderID1 = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID1, vertex_shader1);
        glCompileShader(vertexShaderID1);

        if (glGetShaderi(vertexShaderID1, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(vertexShaderID1, 1024));
            System.exit(1);
        }

        glAttachShader(shader_programme1, vertexShaderID1);

        int fragmentShaderID1 = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID1, frag_shader1);
        glCompileShader(fragmentShaderID1);

        if (glGetShaderi(fragmentShaderID1, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(fragmentShaderID1, 1024));
            System.exit(1);
        }

        glAttachShader(shader_programme1, fragmentShaderID1);

        glLinkProgram(shader_programme1);

        if (glGetProgrami(shader_programme1, GL_LINK_STATUS) == 0) {
            System.err.println(glGetProgrami(shader_programme1, 1024));
            System.exit(1);
        }

        glValidateProgram(shader_programme1);

        if (glGetProgrami(shader_programme1, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgrami(shader_programme1, 1024));
            System.exit(1);
        }

        glEnable(GL_VERTEX_PROGRAM_POINT_SIZE);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POINT_SMOOTH);

        int colorAttrib = glGetAttribLocation(shader_programme1, "color");
        int posAttrib = glGetAttribLocation(shader_programme1, "vp");

        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 24, 0);

        glVertexAttribPointer(colorAttrib, 3, GL_FLOAT, false, 24, 12);

        glEnableVertexAttribArray(posAttrib);
        glEnableVertexAttribArray(colorAttrib);

        glBindBuffer(GL_ARRAY_BUFFER, drawvbo);

        FloatBuffer vertices2 = BufferUtils.createFloatBuffer(12);

        vertices2.put(new float[]{1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f});
        vertices2.rewind();


        glBindBuffer(GL_ARRAY_BUFFER, backvbo);
        glBufferData(GL_ARRAY_BUFFER, vertices2, GL_STATIC_DRAW);
        glBindVertexArray(backvao);
        final String vertex_shader2 =
                "#version 150\n" +
                        "in vec3 p;\n" +
                        "void main() {\n" +
                        "   gl_Position = vec4(p, 1.0);\n" +
                        "}\n";

        final String frag_shader2 =
                "#version 150\n" +
                        "uniform vec3 bp[64];\n" +
                        "uniform float c;\n" +
                        "uniform float steps;\n" +
                        "out vec4 frag_colour;\n" +
                        "void main () {\n" +
                        "   int i = 0;\n" +
                        "   float d = 0;\n" +
                        "   frag_colour = vec4(1.0, 1.0, 1.0, 1.0);" +
                        "   for(i = 0; i < c; i++) {\n" +
                        "       d = distance(vec4(bp[i].x, bp[i].y, 0.0, 0.0), gl_FragCoord);\n" +
                        "       d = bp[i].z / d;\n" +
                        "       frag_colour -= vec4(d, d, 0.0, 0.0);\n" +
                        "   }\n" +
                        "   if(steps > 1) frag_colour = vec4(floor(frag_colour.r * 10) * 0.1, floor(frag_colour.g * 10) * 0.1, 1.0, 1.0);\n" +
                        "}\n";

        int shader_programme2 = glCreateProgram();

        int vertexShaderID2 = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID2, vertex_shader2);
        glCompileShader(vertexShaderID2);

        if (glGetShaderi(vertexShaderID2, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(vertexShaderID2, 1024));
            System.exit(1);
        }

        glAttachShader(shader_programme2, vertexShaderID2);

        int fragmentShaderID2 = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID2, frag_shader2);
        glCompileShader(fragmentShaderID2);

        if (glGetShaderi(fragmentShaderID2, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(fragmentShaderID2, 1024));
            System.exit(1);
        }

        glAttachShader(shader_programme2, fragmentShaderID2);

        glLinkProgram(shader_programme2);

        if (glGetProgrami(shader_programme2, GL_LINK_STATUS) == 0) {
            System.err.println(glGetProgrami(shader_programme2, 1024));
            System.exit(1);
        }

        glValidateProgram(shader_programme2);

        if (glGetProgrami(shader_programme2, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgrami(shader_programme2, 1024));
            System.exit(1);
        }

        int pAttrib = glGetAttribLocation(shader_programme2, "p");
        glVertexAttribPointer(pAttrib, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(pAttrib);

        int uniBp = glGetUniformLocation(shader_programme2, "bp");
        int uniC = glGetUniformLocation(shader_programme2, "c");
        int uniSteps = glGetUniformLocation(shader_programme2, "steps");

        FloatBuffer bp = BufferUtils.createFloatBuffer(64 * 3);

        int count = 0;

        ArrayList<SObject> tmp;

        float maxMass;

        glBindVertexArray(drawvbo);

        while (!glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            tmp = sim.getObjects();

            vertices.clear();
            bp.clear();

            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            glfwGetWindowSize(window, w, h);
            w.rewind();
            h.rewind();

            if (focus) {
                dx = (float) (tmp.get(fID).getX() - (w.get(0) / 2));
                dy = (float) (tmp.get(fID).getY() - (h.get(0) / 2));
            } else {
                dx = 0;
                dy = 0;
            }

            for (SObject o : tmp) {
                vertices.put(new float[]{(float) ((((2 * (o.getX() - dx)) / w.get()) - 1) * 1), (float) ((((2 * (o.getY() - dy)) / h.get()) - 1) * -1), (float) (2 * o.getRadius()), (float) o.getC().getRed() / 255, (float) o.getC().getGreen() / 255, (float) o.getC().getBlue() / 255});
                w.rewind();
                h.rewind();
            }

            vertices.put(new float[]{0.85f, 0.85f, (float) radius * 2, 0.8f, 0.8f, 0.8f});

            count = 0;

            maxMass = 0;

            if (drawPot != 0) {
                for (SObject o : tmp) {
                    if (maxMass < o.getMass()) {
                        maxMass = (float) o.getMass();
                    }
                }

                for (SObject o : tmp) {
                    if (count > 63) break;

                    //if ((float) (o.getMass() * o.getRadius()) / maxMass / dist((float) o.getX(), (float) o.getY(), w.get(0), h.get(0), w.get(0) / 2, h.get(0) / 2) > 0.001) {
                    bp.put(new float[]{(float) o.getX() - dx, (float) (h.get(0) - (o.getY() - dy)), (float) (o.getMass() * o.getRadius()) / maxMass});

                    count++;
                    //}
                }

                bp.rewind();
            }

            if (mouseIsDown) {
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, x, y);
                x.rewind();
                y.rewind();

                vertices.put(new float[]{(float) ((((2 * (tmp.get(oId).getX() - dx) - 0) / w.get(0)) - 1) * 1), (float) ((((2 * (tmp.get(oId).getY() - dy) - 0) / h.get(0)) - 1) * -1), 0.0f, 0.0f, 0.0f, 1.0f});
                vertices.put(new float[]{(float) ((((2 * (x.get(0)) - 0) / w.get(0)) - 1) * 1), (float) ((((2 * (y.get(0)) - 0) / h.get(0)) - 1) * -1), 0.0f, 0.0f, 0.0f, 1.0f});
            }

            vertices.rewind();

            if (drawPot != 0) {
                glUseProgram(shader_programme2);

                glBindVertexArray(backvao);

                glUniform3fv(uniBp, bp);
                glUniform1f(uniC, count % 64);
                glUniform1f(uniSteps, drawPot);

                glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            }

            glBindBuffer(GL_ARRAY_BUFFER, drawvbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glUseProgram(shader_programme1);

            glBindVertexArray(objvao);

            glDrawArrays(GL11.GL_POINTS, 0, tmp.size() + 1);

            if (mouseIsDown) {
                glDrawArrays(GL11.GL_LINES, tmp.size() + 1, 2);
            }

            glfwPollEvents();

            glfwSwapBuffers(window);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sim.evolve(0.04);
        }
    }
}
