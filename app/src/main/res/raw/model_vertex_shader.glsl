uniform mat4 u_InverseModelMatrix;
uniform mat4 u_MVP;
uniform mat4 u_ModelMatrix;

attribute vec3 a_Position;
attribute vec2 a_Uv;
attribute vec3 a_Normal;

varying vec3 v_Normal;
varying vec2 v_Uv;
varying vec3 v_Position;


void main() {
    gl_Position = u_MVP * vec4(a_Position, 1.0);
    v_Position = (u_ModelMatrix * vec4(a_Position, 1.0)).xyz;
    v_Uv = a_Uv;
    v_Normal = (u_InverseModelMatrix * vec4(a_Normal, 0.0)).xyz;
}
