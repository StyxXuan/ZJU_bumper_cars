uniform sampler2D u_Albedo;

varying vec3 v_Normal;
varying vec2 v_Uv;
varying vec3 v_Position;

void main() {
    vec3 color = texture2D(u_Albedo, v_Uv).xyz;
    gl_FragColor = vec4(color, 1.0);
}
