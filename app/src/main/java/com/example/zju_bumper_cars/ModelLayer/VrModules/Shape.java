package com.example.zju_bumper_cars.ModelLayer.VrModules;

import android.content.Context;

import com.example.zju_bumper_cars.utils.Shader;
import com.example.zju_bumper_cars.utils.ShaderProgramUtil;


/**
 * 类名
 * 创建时间 2016/12/12
 * 实现的主要功能
 *
 * @author zjc
 */

public abstract class Shape {

    protected Context mContext;
    protected Shader shader;

    protected Shape(Context context) {
        this.mContext = context;
    }

    protected abstract void createProgram();

    protected abstract void initData();

    public abstract void draw(float[] matrix);

    /**
     * 销毁
     */
    public void destroy() {
        if(shader != null){
            shader.destroy();
        }
    }
}
