# ZJU bumper cars

[TOC]



## 一、项目简介

​		我们制作了一个Android端的碰碰车3D小游戏，游戏基于OpenGL es，我们需要通过屏幕上的按钮来控制小车的行进，通过碰撞其他小车使其掉落到悬崖。同时添加了VR模式，由于时间有限，我们最后只能将天空盒在VR中进行渲染。

## 二、游戏整体介绍

​		游戏在安卓端进行开发，同时VR模式可以使用android手机在google CardBoard上进行体验。

​		在游戏控制方面有四个按钮分别控制小车的前进、后退以及左右移动；同时AI小车会对晚间进行进攻。在掉落悬崖之后会过一段时间在随机位置从天上掉落来进行复活。同时游戏右上角有暂停按钮可以控制游戏的开始与暂停，左上角按钮可以开启VR模式。小车在进行碰撞之后会有简单的粒子系统来模拟烟雾。

​		通过触摸屏幕可以移动视角，在屏幕上上滑可以拉近视角，下滑会拉远视角，左有滑动会调整视线。游戏再开始后还会有音效播放。游戏是在一个大的六边形平台上进行的，整体会在一个峡谷的天空盒中进行。

## 三、游戏整体架构分析

### 2.1 总体架构		

​		为了解耦合和便于扩展，我们将代码整体分为4层，其结构和功能如下：

<img src="https://tva1.sinaimg.cn/large/006tNbRwly1gadya0ntrtj30pi0l03zr.jpg" alt="7" style="zoom: 50%;" />

​		视图层主要是进行图像的绘制，并将这些呈现给玩家，同时接收玩家的指令，将这些指令交给控制层处理；控制层会对玩家和AI进行控制，主要是其控制其位置和方向，同时会对所有玩家的状态进行判断；模型层进行模型的初始化，模型的绘制与渲染，同时调用IO层来进行模型的加载。IO层主要负责加载obj文件模型。

### 2.2 功能设计

#### 2.2.1 场景布局

初始的欢迎界面：

![image-20191230152851532](https://tva1.sinaimg.cn/large/006tNbRwly1gaes6lw2f5j30bj05sgot.jpg)

App中全部使用obj格式的3D模型，将其加载进游戏场景。主要包括：天空盒、汽车、游戏场地、粒子系统模型。总体布局效果如下：

![image-20191230152920753](https://tva1.sinaimg.cn/large/006tNbRwly1gaes71x64rj30al05badd.jpg)

#### 2.2.2 游戏控制

游戏的操作主要通过控制按钮实现：

![image-20191230153007870](https://tva1.sinaimg.cn/large/006tNbRwly1gaes7vwpo6j30bj05s78e.jpg)

​		在Render中，每次调用OnDrawFrame函数，对整个界面进行重绘，以更新模型的位置、方向等属性。汽车的运动也是通过这个方式实现的。在对汽车的位置属性进行更新之后，重绘时汽车在界面中的位置也发生相应的变化。

​		其中，前进、后退按钮控制汽车沿车身方向的位置，而左转、右转按钮则改变车头朝向。

​		左上方按钮即可进入VR模式，同时注意了Activity的生命周期问题，由VR模式退回之后，仍可继续游戏。

​		右上角按钮可控制游戏状态，单击暂停/开始，循环箭头按钮可重新开始游戏。		

​		游戏过程中，只要汽车处于未被击败的状态，就是可活动的。

#### 2.2.3 AI对战

作为一个游戏，仅有一名玩家孤独的进行操作显然是没有任何意义的，而受限于手机屏幕尺寸的原因，双人对战的用户体验不佳，我们加入了AI对手。AI能够实时监测玩家所处的位置，并主动对其发起攻击，因此玩家需要规避AI的攻击，并且想办法击败AI，提升了游戏的趣味性。

![image-20191230153116404](https://tva1.sinaimg.cn/large/006tNbRwly1gaes924i52j30bj05stcn.jpg)

#### 2.2.4 碰撞、击败、复活机制

​		既然是一个对抗类的游戏，就要分出胜负。

​		本游戏胜负的评判标准是将对手撞出地图边缘之外。

​		我们实现了体验较好的碰撞检测功能，尽可能的模仿现实世界中的真实情况，模拟碰撞的情形。

​		一旦一方被撞出地形边缘，判定其死亡，做平抛运动向下坠落，一段时间之后即可复活，从地形上方随机位置降落，重新进行对抗。

![image-20191230153213338](https://tva1.sinaimg.cn/large/006tNbRwly1gaesa1hxr8j30bj05swih.jpg)

#### 2.2.5 VR模式

​		为了进一步丰富app的内容，我们对基于Google Cardboard的VR模式进行整合，玩家可以通过切换按钮一键从游戏模型切换至VR模式，佩戴Cardboard对游戏场景进行观察。

![image-20191230153247693](https://tva1.sinaimg.cn/large/006tNbRwly1gaesamx0xkj30bj05r77n.jpg)

#### 2.2.6 BGM

添加了合适的BGM以提高用户体验。详情见演示视频。

#### 2.2.7 场景漫游

固定的游戏视角过于死板，我们提供了视角转换与场景漫游的方式。

在按钮区域之外，左右滑动即可改变观测点的角度，而上下滑动能够改变观测点与世界中点的距离。

![image-20191230153344615](https://tva1.sinaimg.cn/large/006tNbRwly1gaesbmg0v9j30bj05sq65.jpg)

![image-20191230153351192](https://tva1.sinaimg.cn/large/006tNbRwly1gaesbq2a1bj30bj05sdji.jpg)

## 四、具体实现

### 1. obj文件的加载

​		模型加载由IO层进行加载，主要是通过ObjLoaderUtil.java来进行读取。

​		通过C4D、3D MAX等软件，制作所需的模型并生成可读取的obj、mtl文件。

![image-20191230153820287](https://tva1.sinaimg.cn/large/006tNbRwly1gaesgel4l5j305o04zjsd.jpg)![image-20191230153831538](https://tva1.sinaimg.cn/large/006tNbRwly1gaesglma9fj305l049t9o.jpg)

​		我们通过以下两个类来进行模型信息和材质信息的传递：

```java
public static class MtlData {
        public String name;
        // 环境光
        public int Ka_Color;
        // 散射光
        public int Kd_Color;
        // 镜面光
        public int Ks_Color;
        // 高光调整参数
        public float ns;
        public float alpha = 1f;
        public String Ka_Texture;
        public String Kd_Texture;
        public String Ks_ColorTexture;
        public String Ns_Texture;
        public String alphaTexture;
        public String bumpTexture;
    }
```

```java
public static class ObjData {

        public String name;
        // 材质
        public MtlLoaderUtil.MtlData mtlData;
        /**
         * 顶点、纹理、法向量
         */
        public float[] aVertices;
        public float[] aTexCoords;
        public float[] aNormals;
        // 顶点index数组
        public ArrayList<Integer> vertexIndices = new ArrayList<Integer>();
        // 纹理index数组
        public ArrayList<Integer> texCoordIndices = new ArrayList<Integer>();
        // 法向量index数组
        public ArrayList<Integer> normalIndices = new ArrayList<Integer>();

    }
```



### 2. 模型的几何变换

​		模型的几何变换主要是在文件MatrixState.java中，我们将一些API进行了重新的封装，使其更能符合我们的需求。

```java
public static Stack<float[]> mStack = new Stack<float[]>();//保护变换矩阵的栈

public static void setInitStack()//获取不变换初始矩阵

public static void pushMatrix()//保护变换矩阵

public static void popMatrix()//恢复变换矩阵

public static void translate(float x, float y, float z)//设置沿xyz轴移动

public static void rotate(float angle, float x, float y, float z)//设置绕xyz轴移动

public static void scale(float x, float y, float z) //放大缩小

```



### 3. 场景漫游

​		场景漫游我们记录玩家在屏幕上的滑动，并对滑动进行判断，根据滑动的方向来判断移动的方向和距离。具体来说，我们会记录一个x值和一个y值，其含义为触碰时候在屏幕上的坐标值，通过滑动之后的坐标值来判断其手指究竟画过多少，根据$\Delta x$和$\Delta y$来调节视角。glConfig中记录了全局变量，其中包含视角信息。

```java
@Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if(mPreviousY - y >= 10){
                    glConfig.distance++;
                }else if(mPreviousY - y <= -10){
                    glConfig.distance--;
                }

                if(mPreviousX - x >= 10){
                    glConfig.angle++;
                }else if(mPreviousX - x <= -10){
                    glConfig.angle--;
                }

            this.requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }
```

### 4. 光照模型

​		在shader中，环境光的强度不发生变化。之后计算进过变换之后的法向量：

```java
vec3 normalTarget=aPosition+normal;
vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
newNormal=normalize(newNormal); 
```

​		之后需要计算从表面到相机的向量：

```java
vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
```

​		计算从表面到光源的向量：

```java
vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
vp=normalize(vp);
```

​		计算漫反射的强度

```java
float nDotViewPosition=max(0.0,dot(newNormal,vp));
diffuse=lightDiffuse*nDotViewPosition;	
```

​		求视线与光线的半向量：

```java
vec3 halfVector=normalize(vp+eye);
```

​		计算镜面光的强度，使用Blinn-Phong模型进行计算
$$
specular = K_s * lightColor*(dot(N,H))^{shinines}
$$
​		其中$K_s$为物体对于反射光的衰减系数，N为表面法向量，H为光线入射方向和视角方向的半向量，shininess为高光系数。

```java
float nDotViewHalfVector=dot(newNormal,halfVector);	
float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));
specular=lightSpecular*powerFactor;
```

​		之后传递给片元着色器进行着色

```java
gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
```

## 

### 5. 运动系统

​		小车模型需要两个向量进行初始化，一个是其位置向量，一个时期方向向量。通过这两个向量我们可以得到小车的位置和朝向。同时小车还具s有一个速度成员变量，这个变量是一个向量。在运动的时候我们使小车朝着速度的方向进行移动，具体的就是改变其位置向量的值。同时为了模拟摩擦力的作用，速度是会随着每次移动下降的。

​		我们在游戏初始化之后，调用每个小车的driving函数，给其开启一个线程控制其位置和状态信息，位置信息的变化便是运动系统所需要做的。

```java
public void driving(){
    new Thread(){
        @Override
        public void run() {
            super.run();
            while(true){
                Log.d("Car Position", ""+pos.x +" "+pos.y + " " + pos.z);
                if(!ModelGroup.initDown) {
                    try {
                        Thread.sleep(100);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(!isPlayer){ // 判断是否为玩家
                    AI_controler.attack(Cars.this);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(RunState && canMove) { // 判断其状态
                    Log.d("state", "RunState is true");
                    Log.d("state", ""+ Math.abs(Velocity.sum()));
                    pos = pos.add(Velocity.mul(0.01f)); // 根据速度进行移动
                    Velocity = Velocity.sub(Velocity.mul(0.05f)); // 模拟摩擦力的作用
                    if (Math.abs(Velocity.AbsSum()) < 0.01) {
                        Velocity = new vec(0, 0, 0);
                        RunState = false;
                    }
                    ModelGroup.CollisionDetect(Cars.this); // 进行碰撞检测
                    Boolean outfBound = Judger.detectBorder(Cars.this); // 判断是否出界
                    Cars.this.canMove = outfBound;
                    Cars.this.isLive = outfBound;
                }else if(!canMove && !isLive){
                    FallingTime = 0;
                    for(int i=0; i<200; i++){
                        rotateDown(); // 模拟重力掉落
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    reBirth = true;
                    isLive = true;
                    direction = new vec(270, 0, 0);
                    pos = new vec((Math.random()-0.5)*40, 0, (Math.random()-0.5)*40);
                    Velocity = new vec(0, 0, 0);
                    normal = new vec(Math.cos(Math.toRadians(90-direction.y)), 0, Math.sin(Math.toRadians(90-direction.y)));
                }
                if(isLive && reBirth){
                    if(pos.y > - 17.5)
                        pos.y -= 0.1;
                    else{
                        reBirth = false;
                        canMove = true;
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }.start();
}
```

### 6. 碰撞检测

​		碰撞检测我们通过AABB的bounding Box来进行实现。其具体实现为，通过一个矩形来包围物体，判断矩形是否有边相交，如果有则判定其为碰撞。在建模时，我们通过两个向量来代表模型的基本信息，一个模型的位置向量，一个是指向模型前进方向的向量。通过这两个向量来获得小车四个顶点的坐标，其计算公式如下：
$$
\vec{coordinate} = \pm \frac{1}{2} \vec{normal} \cdot width\pm \frac{1}{2} \vec{vertical\_normal} \cdot length
$$
​		通过计算得到的四个顶点坐标，来计算出其bounding Box所确定的矩形位置，之后进行碰撞检测。

​		我们把小车视作刚体，碰撞之后刚体互相交换速度并移动一定距离，移动的方向为两个小车位置向量的反向连线。

```java
public Boolean detectCollistion(Cars b){
        vec b_normal = b.getNormal();
        vec b_vertical = b_normal.rotate(Math.toRadians(90), 0, 1, 0);
        vec b_pos = b.pos;
        Log.d("point b_vertical", ""+b_vertical.x +" "+b_vertical.y + " " + b_vertical.z);
        Log.d("point b_position", ""+b_pos.x +" "+b_pos.y + " " + b_pos.z);

  /*计算小车四个顶点的坐标*/
        vec RightDownPoint = b_pos.add(b_normal.mul(bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftDownPoint = b_pos.add(b_normal.mul(-bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec LeftUpPoint = b_pos.add(b_normal.mul(-bounding_box_scale*Cars.bouningBox.y)).add(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        vec RightUpPoint = b_pos.add(b_normal.mul(bounding_box_scale*Cars.bouningBox.y)).sub(b_vertical.mul(bounding_box_scale*Cars.bouningBox.x));
        RightDownPoint.y = 0;
        LeftDownPoint.y = 0;
        LeftUpPoint.y = 0;
        RightUpPoint.y = 0;

        Log.d("point RightDownPoint", ""+RightDownPoint.x +" "+RightDownPoint.y + " " + RightDownPoint.z);
        Log.d("point LeftDownPoint", ""+LeftDownPoint.x +" "+LeftDownPoint.y + " " + LeftDownPoint.z);
        Log.d("point LeftUpPoint", ""+LeftUpPoint.x +" "+LeftUpPoint.y + " " + LeftUpPoint.z);
        Log.d("point RightUpPoint", ""+RightUpPoint.x +" "+RightUpPoint.y + " " + RightUpPoint.z);

        Log.d("point Position", ""+pos.x +" "+pos.y + " " + pos.z);
        boolean collision = false;
        collision |= this.checkInBox(RightDownPoint);
        collision |= this.checkInBox(LeftDownPoint);
        collision |= this.checkInBox(LeftUpPoint);
        collision |= this.checkInBox(RightUpPoint);
        

        return collision;
    }
```



### 7. 边界识别

​		在编写代码过程中，由于要识别地形边缘，需要知道地形的六个顶点坐标，而由于在模型制作过程中定义的模型尺寸单位与GpenGL绘制时使用的单位不同，难以通过模型尺寸直接得到坐标。

​		在调试过程中，我们使小车遍历这六个顶点，同时打印出其中点坐标pos的各个值，由此得到了六个顶点的准确坐标

![image-20191230154852510](https://tva1.sinaimg.cn/large/006tNbRwly1gaesrd7oboj30bj072gpo.jpg)

​		计算了地形底座的尺寸以及顶点位置，对汽车模型进行实时监测，一旦汽车中心点（即重心）越过边界，则判定汽车掉落出地图，将其状态设定为死亡。

​		在此之后，汽车会做一个平抛运动向下掉落，并在一定时间之后复活，重新从上空掉落到地图的随机位置，继续游戏。

​		边缘检测的具体方法：使用的底座是一个正六边形立体模型，因此只需要判断汽车位于组成正六边形的三个长方形中的任意一个即可。（由于汽车处在x/z平面上，我们只关注它的x值和z值。通过向量叉积来判断点位于矩形内部。

![image-20191230154927873](https://tva1.sinaimg.cn/large/006tNbRwly1gaesryymfwj305b044weq.jpg)

汽车掉落与复活过程可见演示视频。

### 8. 粒子系统

​		我们最终实现的粒子系统并不是十分完善，考虑到android的性能限制，我们只是简单绘制了30个小球作为粒子，随着时间会不断地扩散以及变大，随着粒子系统生命周期结束，进程会收回粒子系统的使用权限，并解除其上面的占有锁。

​		粒子是随着小车碰撞发生的，所以会牵扯到线程同步的问题。和容易会遇到离子系统还没初始化完成但是主线程以及调用绘制的api或者是还在绘制的离子系统被新的绘制请求占用。我们设计了一个两阶段的上锁机制来避免以上两个问题。具体而言是每一个粒子系统会记录其是否被占用以及是否准备好进行绘制，主线程会查看这两个字段来决定是否绘制，新的添加粒子系统的请求会找到未被占用的粒子系统对象，标记为占用并设其无法绘制，在初始化结束之后设置其能够绘制，最后保证了线程之间的同步性。

```java
public void drawSelf(){
        if(!inUse | !readyToDraw) {
            Log.d("draw", "but not inUse");
            return;
        }else{
            Log.d("draw", "inUse " + particles.size());
            Log.d("draw", "inUse LifeTime " + LifeTime);
            Log.d("draw", "inUse Pos " + particles.get(0).pos.x + " "+particles.get(0).pos.y + " " + particles.get(0).pos.z);
        }

        LifeTime--; // 生命周期
        for(Particle particle : particles){
            vec randVec = new vec(Math.random()*0.8, 0.1, Math.random()*0.8); // 随机扩散
            double randNum = Math.random() / 50;
            particle.draw();
            particle.setPos(particle.getPos().add(normal.mul(randNum)));
            particle.setPos(particle.getPos().add(randVec));
        }
    }
```



### 9. VR的实现

​		最后的VR系统，我们调用Google Vr的API进行渲染。但是由于之前的设计中并没有将VR作为主要的任务，所以在最后考虑添加的时候发现我们之前的模型不能在其中进行渲染。由于时间有限，我们最后只将天空盒通过立方体贴图的方式添加进了gvrview中。

​		其实VR所要实现的和AR有一点相似之处，我们都需要获取手机加速度传感器的信息进行视角的变化，在这里我们读取手机加速度传感器后的x, y, z三个轴的加速度，通过加速度来计算需要在这三个轴旋转多少。

```java
private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.accuracy != 0) {
                int type = sensorEvent.sensor.getType();
                switch (type) {
                    case Sensor.TYPE_GYROSCOPE:
                        if (timestamp != 0) {
                            final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                            angle[0] += sensorEvent.values[0] * dT;
                            angle[1] += sensorEvent.values[1] * dT;
                            angle[2] += sensorEvent.values[2] * dT;

                            float anglex = (float) Math.toDegrees(angle[0]);
                            float angley = (float) Math.toDegrees(angle[1]);
                            float anglez = (float) Math.toDegrees(angle[2]);

                            if (gx != 0) {
                                float c = gx - anglex;
                                if (Math.abs(c) >= 0.5) {
                                    gx = anglex;
                                }

                            } else {
                                gx = anglex;
                            }
                            if (gy != 0) {
                                float c = gy - angley;
                                if (Math.abs(c) >= 0.5) {
                                    gy = angley;
                                }
                            } else {
                                gy = angley;
                            }
                            if(gz != 0){
                            }
                            gz = anglez;

                        }
                        timestamp = sensorEvent.timestamp;
                        break;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
```

## 五、总结

​		由于我们之前并没有游戏相关的开发经验，在整个app的开发过程中还是遇到了许多的困难和问题。
​		作为一个游戏app，最重要的是其可玩性。需要注意游戏界面的设计，实现良好的UI系统，还要针对用户进行人性化的设计。需要首先对用户的需求进行了解，对app做出详细的整体架构。由于我们对项目进行了合适的分层，基本做到高内聚低耦合。
​		同时，整个app的性能也至关重要。由于手机的功能不如pc那样强大，需要在开发过程中不断的进行优化，让游戏尽可能的占用更少的内存，有更快的运行速度，才能给予用户更好的游戏体验。
​		在开发过程中，遇到的诸如模型加载、几何变换、运动模拟、线程同步等问题，都在我们的努力下得以解决，并最终实现了用户与app之间良好的交互。由于本项目涉及的图形学知识较多，安卓相关部分反倒显得没那么复杂，理清了Activity与SurfaceView、IntentService以及各个线程等之间的关系后，运用OpenGL ES相关知识去开发。
​		最终实现的这个碰碰车小游戏我们对其也是比较满意的，基本实现了当初立项时所设想的主要功能，具有较强的趣味性和可操作性，还能结合Google Cardboard实现VR漫游效果，迎合了当前的技术趋势。
​		当然，我们的碰碰车游戏还存在一些未能解决的问题。例如AI的设计问题，当前仅实现了一辆AI车，并且它的攻击方式比较单一。希望以后能应用更好的AI算法，在增加AI车数目的同时，丰富攻击手段，提供更好的用户体验。除此之外，穿模问题偶有出现，由于使用AABB的碰撞检测，检测结果还并不是完全精确，想要更加真实的模拟汽车碰撞情况，需要更好的计算公式。同时，一些想法由于时间、精力所限未能完成，例如多种车辆模型，多个可选地图，在VR中渲染模型等，希望以后在学有余力时，能够把这款app 进一步的完善。
