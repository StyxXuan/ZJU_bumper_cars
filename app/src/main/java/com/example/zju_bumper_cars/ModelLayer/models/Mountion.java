package com.example.zju_bumper_cars.ModelLayer.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.ShaderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mountion extends BaseModel{
    float UNIT_SIZE=1.0f;

    //自定义渲染管线的id
    int mProgram;
    //总变化矩阵引用的id
    int muMVPMatrixHandle;
    //顶点位置属性引用id
    int maPositionHandle;
    //顶点纹理坐标属性引用id
    int maTexCoorHandle;

    //草地的id
    int sTextureGrassHandle;

    //顶点数据缓冲和纹理坐标数据缓冲
    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;
    //顶点数量
    int vCount=0;

    int texId;

    public Mountion(MySurfaceView mv,float[][] yArray,int rows,int cols, int drawableId)
    {
        initVertexData(yArray,rows,cols);
        initShader(mv);
        texId = initTexture(mv, drawableId);
    }

    public int initTexture(MySurfaceView mv, int drawableId)
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //ST方向纹理拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        //通过输入流加载图片
        InputStream is = mv.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp, 			  //纹理图像
                        0					  //纹理边框尺寸
                );
        //自动生成Mipmap纹理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //释放纹理图
        bitmapTmp.recycle();
        //返回纹理ID
        return textureId;
    }


    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float[][] yArray,int rows,int cols)
    {
        vCount=cols*rows*2*3;//每个格子两个三角形，每个三角形3个顶点
        float vertices[]=new float[vCount*3];//每个顶点XYZ三个坐标
        int count=0;
        for (int j=0;j<rows;j++){
            for (int i=0;i<cols;i++){
                //计算当前格子左上侧点坐标,坐标中心点在图形中心点
                //故顶点坐标 x,z的计算需要x(-1*1/2)
                float zsx=-UNIT_SIZE*cols/2+i*UNIT_SIZE;
                float zsz=-UNIT_SIZE*rows/2+j*UNIT_SIZE;

                //第一个三角形--start
                vertices[count++]=zsx;
                vertices[count++]=yArray[j][i];
                vertices[count++]=zsz;

                vertices[count++]=zsx;
                vertices[count++]=yArray[j+1][i];
                vertices[count++]=zsz+UNIT_SIZE;

                vertices[count++]=zsx+UNIT_SIZE;
                vertices[count++]=yArray[j][i+1];
                vertices[count++]=zsz;

                //第二个三角形start
                vertices[count++]=zsx+UNIT_SIZE;
                vertices[count++]=yArray[j][i+1];
                vertices[count++]=zsz;

                vertices[count++]=zsx;
                vertices[count++]=yArray[j+1][i];
                vertices[count++]=zsz+UNIT_SIZE;

                vertices[count++]=zsx+UNIT_SIZE;
                vertices[count++]=yArray[j+1][i+1];
                vertices[count++]=zsz+UNIT_SIZE;
            }
        }

        //创建顶点坐标数据缓冲
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer=vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        //顶点纹理坐标数据的初始化
        float[] texCoor=generateTexCoor(cols,rows);
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
    }


    //初始化Shader的方法
    public void initShader(MySurfaceView mv)
    {
        String mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        String mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //草地纹理
        sTextureGrassHandle=GLES20.glGetUniformLocation(mProgram, "vTextureCoord");
    }

    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor(int bw,int bh){
        float[] result=new float[bw*bh*6*2];
        float sizew=16.0f/bw;//列数
        float sizeh=16.0f/bh;//行数
        int c=0;
        for (int i=0;i<bh;i++){
            for (int j=0;j<bw;j++){
                //每行列一个矩形，由两个三角形构成,共六个点,12个纹理坐标
                float s=j*sizew;
                float t=i*sizeh;

                //贴第一个三角形
                result[c++]=s;
                result[c++]=t;

                result[c++]=s;
                result[c++]=t+sizeh;

                result[c++]=s+sizew;
                result[c++]=t;

                //贴第二个三角形
                result[c++]=s+sizew;
                result[c++]=t;

                result[c++]=s;
                result[c++]=t+sizeh;

                result[c++]=s+sizew;
                result[c++]=t+sizeh;
            }
        }
        return  result;
    }

    @Override
    public void draw() {
        //指定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //传送顶点位置数据
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        //传送顶点纹理坐标数据
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2*4,
                        mTexCoorBuffer
                );
        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glUniform1i(sTextureGrassHandle, 0);//使用0号纹理

        //绘制纹理矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
