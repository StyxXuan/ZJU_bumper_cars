precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
uniform vec3 uLightLocation;	//光源位置
uniform vec3 uCamera;	//摄像机位置

varying vec2 vTextureCoord;
varying vec3 normal;
varying vec3 position;



void main()
{
      vec3 color = texture2D(sTexture, vTextureCoord).rgb;
      vec3 ambient = 0.05 * color;

      vec3 lightColor = vec3(0.4);
      vec3 l = normalize(uLightLocation - position);
      vec3 n = normalize(normal);
      vec3 v = normalize(uCamera - position);

      float tmp  = dot(l,n);
      tmp = tmp > 0.0 ? tmp : 0.0;
      vec3 diffuse = tmp * color;

      vec3 r = reflect(-l, n);

      vec3 h = normalize(v + l);
      tmp = dot(n,h);
      tmp = tmp > 0.0 ? tmp : 0.0;
      float specFactor = pow(tmp, 32.0);
      //给此片元颜色值
      vec3 frag = ambient + diffuse + specFactor * lightColor;
      gl_FragColor = vec4(frag, 1.0);
}   