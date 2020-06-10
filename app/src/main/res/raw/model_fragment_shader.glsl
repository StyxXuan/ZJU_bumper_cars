uniform sampler2D u_Albedo;
uniform vec3 u_LightLocation;
uniform vec3 u_CameraPosition;
uniform vec3 u_LightColor;
uniform float u_Shiness;

varying vec3 v_Normal;
varying vec2 v_Uv;
varying vec3 v_Position;

void main() {
    vec3 color = texture2D(u_Albedo, v_Uv).rgb;
    vec3 ambient = 0.05 * color;

    vec3 l = normalize(u_LightLocation - v_Position);
    vec3 n = normalize(v_Normal);
    vec3 v = normalize(u_CameraPosition - v_Position);

    float tmp  = dot(l,n);
    tmp = tmp > 0.0 ? tmp : 0.0;
    vec3 diffuse = tmp * color;

    vec3 h = normalize(v + l);
    tmp = dot(n,h);
    tmp = tmp > 0.0 ? tmp : 0.0;
    float specFactor = pow(tmp,u_Shiness);
    //给此片元颜色值
    vec3 frag = ambient + diffuse + specFactor * u_LightColor * color;
    gl_FragColor = vec4(frag, 1);
}
