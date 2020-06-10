package com.example.zju_bumper_cars.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glGetUniformLocation;

public class Shader {

    private int program;

    private Shader(Context context, int vertexShaderRes, int fragmentShaderRes){
        int vertexShader = ShaderProgramUtil.loadShader(context, GL_VERTEX_SHADER, vertexShaderRes);
        int fragmentShader = ShaderProgramUtil.loadShader(context, GL_FRAGMENT_SHADER, fragmentShaderRes);
        program = ShaderProgramUtil.newLinkProgram(vertexShader, fragmentShader);
    }

    public static Shader create(Context context, int vertexShaderRes, int fragmentShaderRes){
        return new Shader(context, vertexShaderRes, fragmentShaderRes);
    }

    public Shader use(){
        glUseProgram(program);
        return this;
    }

    public Shader setFloat4(String attr, float x, float y, float z, float w){
        int location = glGetUniformLocation(program, attr);
        glUniform4f(location, x, y, z, w);
        return this;
    }


    public Shader setMatrix4(String attr, float[] matrix){
        int location = glGetUniformLocation(program, attr);
        glUniformMatrix4fv(location,1,false, matrix, 0);
        return this;
    }

    public Shader setInt(String attr, int value){
        int location = glGetUniformLocation(program, attr);
        glUniform1i(location, value);
        return this;
    }

    public Shader setFloat3(String attr, float x, float y, float z){
        int location = glGetUniformLocation(program, attr);
        glUniform3f(location, x, y, z);
        return this;
    }

    public Shader setFloat(String attr, float value){
        int location = glGetUniformLocation(program, attr);
        glUniform1f(location, value);
        return this;
    }

    public int getAttribute(String attr){
        return glGetAttribLocation(program, attr);
    }

    public void destroy(){
        ShaderProgramUtil.delete(program);
    }

}
