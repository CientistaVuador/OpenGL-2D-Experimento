/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cien.opengl2dexperimento;

import static org.lwjgl.opengl.GL33C.*;

/**
 *
 * @author Cien
 */
public class SpriteRenderer {

    public static final String VERTEX_SHADER = 
            """
            #version 330 core
            
            uniform vec2 viewport;
            uniform vec2 position;
            uniform sampler2D sprite;
            
            layout (location = 0) in vec2 vertexPosition;
            
            void main() {
                vec2 relation = vec2(textureSize(sprite, 0)) / viewport;
                vec2 spriteVertexPosition = ((relation * vertexPosition) - 1.0) + relation;
                gl_Position = vec4(spriteVertexPosition + ((position / viewport) * 2.0), 0.0, 1.0);
            }
            """;

    public static final String FRAGMENT_SHADER = 
            """
            #version 330 core
            
            uniform vec2 viewport;
            uniform vec2 position;
            uniform sampler2D sprite;
            
            layout (location = 0) out vec4 colorOutput;
            
            void main() {
                ivec2 relativePixelPos = ivec2(gl_FragCoord.xy - position);
                vec4 color = texelFetch(sprite, relativePixelPos, 0);
                if (color.a == 0.0) {
                    discard;
                }
                colorOutput = color;
            }
            """;

    public static final int SPRITE_SHADER;
    public static final int SPRITE_VERTEX_ARRAYS;

    private static int setupShader() {
        int program = glCreateProgram();
        
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, VERTEX_SHADER);
        glCompileShader(vertexShader);
        
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, FRAGMENT_SHADER);
        glCompileShader(fragmentShader);
        
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        
        glLinkProgram(program);
        
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        
        return program;
    }

    private static int setupVertexArrays() {
        int vertexArrays = glGenVertexArrays();

        glBindVertexArray(vertexArrays);
        {
            int buffer = glGenBuffers();

            glBindBuffer(GL_ARRAY_BUFFER, buffer);
            {
                glBufferData(GL_ARRAY_BUFFER, new float[] {
                    -1f, 1f,
                    -1f, -1f,
                    1f, -1f,
                    
                    -1f, 1f,
                    1f, -1f,
                    1f, 1f
                }, GL_STATIC_DRAW);
                
                glEnableVertexAttribArray(0);
                glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
        }
        glBindVertexArray(0);

        return vertexArrays;
    }

    static {
        SPRITE_SHADER = setupShader();
        SPRITE_VERTEX_ARRAYS = setupVertexArrays();
    }

    public static void render(int spriteTexture, int x, int y) {
        glUseProgram(SPRITE_SHADER);
        
        int viewportLocation = glGetUniformLocation(SPRITE_SHADER, "viewport");
        int positionLocation = glGetUniformLocation(SPRITE_SHADER, "position");
        int spriteLocation = glGetUniformLocation(SPRITE_SHADER, "sprite");
        
        glUniform2f(viewportLocation, Game.WIDTH, Game.HEIGHT);
        glUniform2f(positionLocation, x, y);
        glUniform1i(spriteLocation, 0);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, spriteTexture);
        
        glBindVertexArray(SPRITE_VERTEX_ARRAYS);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        
        glUseProgram(0);
        glBindVertexArray(0);
    }

    private SpriteRenderer() {
        throw new RuntimeException();
    }
}
