/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cien.opengl2dexperimento.sprites;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.stb.STBImage.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author Cien
 */
public class SpriteLoader {

    static {
        stbi_set_flip_vertically_on_load(true);
    }
    
    public static int createTexture(String spriteImageName) {
        URL url = SpriteLoader.class.getResource(spriteImageName);

        if (url == null) {
            throw new RuntimeException(spriteImageName + " not found.");
        }

        ByteBuffer imageData = null;
        try {
            URLConnection conn = url.openConnection();
            conn.connect();

            imageData = memAlloc(conn.getContentLength());

            byte[] buffer = new byte[16384];
            try ( InputStream in = conn.getInputStream()) {
                int r;
                while ((r = in.read(buffer)) != -1) {
                    imageData.put(buffer, 0, r);
                }
            }

            imageData.flip();
            
            try ( MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer xBuffer = stack.mallocInt(1);
                IntBuffer yBuffer = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                
                ByteBuffer pureImageData = stbi_load_from_memory(
                        imageData,
                        xBuffer,
                        yBuffer,
                        channels,
                        4
                );
                
                memFree(imageData);
                
                if (pureImageData == null) {
                    throw new RuntimeException("Failed to load "+spriteImageName+", "+stbi_failure_reason());
                }
                
                int imageWidth = xBuffer.get();
                int imageHeight = yBuffer.get();
                
                int texture = glGenTextures();
                
                glBindTexture(GL_TEXTURE_2D, texture);
                glTexImage2D(
                        GL_TEXTURE_2D,
                        0,
                        GL_RGBA8,
                        imageWidth,
                        imageHeight,
                        0,
                        GL_RGBA,
                        GL_UNSIGNED_BYTE,
                        pureImageData
                );
                
                stbi_image_free(pureImageData);
                
                int error = glGetError();
                if (error != 0) {
                    throw new RuntimeException("GL Error "+error);
                }
                
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                
                return texture;
            }
        } catch (IOException ex) {
            if (imageData != null) {
                memFree(imageData);
            }
            throw new RuntimeException(ex);
        }
    }

    private SpriteLoader() {
        throw new RuntimeException();
    }
}
