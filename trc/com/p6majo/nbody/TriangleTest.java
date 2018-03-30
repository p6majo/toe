package com.p6majo.nbody;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TriangleTest {
        private static long window;
        private static int WIDTH = 1280;
        private static int HEIGHT = 720;

        public static void main (String[] args)
        {
            if (!GLFW.glfwInit())
            {
                System.out.println ("GLFW initialization failed.");
                System.exit (1);
            }

            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_RESIZABLE, GL11.GL_FALSE);
            window = glfwCreateWindow (WIDTH, HEIGHT, "GLFW Window", MemoryUtil.NULL, MemoryUtil.NULL);
            glfwMakeContextCurrent (window);
            glfwSwapInterval (1);
            GL.createCapabilities();
            glMatrixMode (GL_PROJECTION);
            glLoadIdentity();
            glOrtho (0, 12, 12, 0, 1, -1);
            glClearColor (0, 0.7f, 1, 0);
            glMatrixMode (GL_MODELVIEW);
            glfwShowWindow (window);

            while (!glfwWindowShouldClose (window))
            {
                renderSphere();

                glfwSwapBuffers (window);
                glfwPollEvents();
            }

            glfwDestroyWindow (window);
            glfwTerminate();
        }

        private static void renderSphere(){
            glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glBegin (GL_TRIANGLES);
            glColor3f (1, 0, 0.7f);
            glVertex3f (6, 4, 0); // Vertex one
            glColor3f (1, 0, 0.7f);
            glVertex3f (4, 8, 0); // Vertex two
            glColor3f (1, 0, 0.0f);
            glVertex3f (8, 8, 0); // Vertex three
            glColor3f (1, 0, 0.4f);
            glVertex3f (8, 8, 8); // Vertex three
            glEnd();
        }
}

