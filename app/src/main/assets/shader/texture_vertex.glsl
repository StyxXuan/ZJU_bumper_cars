uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵
attribute vec3 aPosition;  //顶点位置
attribute vec3 aNormal;    //顶点法向量
attribute vec2 aTexCoor;    //顶点纹理坐标

varying vec2 vTextureCoord;
varying vec3 normal;
varying vec3 position;

void main()
{
    gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
    position = vec3(uMMatrix * vec4(aPosition, 1)).xyz;
    //normal = mat3(transpose(inverse(uMMatrix))) * aNormal;
    normal = aNormal;
    vTextureCoord = aTexCoor;
}                      