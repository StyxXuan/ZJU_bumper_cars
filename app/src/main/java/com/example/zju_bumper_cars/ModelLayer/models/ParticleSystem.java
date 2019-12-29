package com.example.zju_bumper_cars.ModelLayer.models;

import android.util.Log;

import com.example.zju_bumper_cars.ViewLayer.MySurfaceView;
import com.example.zju_bumper_cars.utils.MatrixState;
import com.example.zju_bumper_cars.utils.vec;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {
    public String FileName = "red.obj";
    public int LifeTime;
    public vec pos;
    public vec normal;
    public boolean inUse;
    public MySurfaceView mySurfaceView;
    public boolean readyToDraw;
    List<Particle> particles;

    public ParticleSystem(MySurfaceView mySurfaceView){
        this.mySurfaceView = mySurfaceView;
        particles = new ArrayList<>();
        inUse = false;
        initParticles();
        readyToDraw = false;
        LifeTime = 50;
    }


    public void setParam(String FileName, vec pos, vec normal){

        readyToDraw = false;
        inUse = true;
        this.FileName = FileName;
        this.pos = pos;
        this.normal = normal;
        for(int i=0; i<particles.size(); i++){
            particles.get(i).setPos(pos);
            particles.get(i).setNormal(normal);
        }
        LifeTime = 50;
        readyToDraw = true;
    }

    public ParticleSystem(MySurfaceView mySurfaceView, String FileName, vec pos, vec normal){
        this.mySurfaceView = mySurfaceView;
        this.FileName = FileName;
        this.pos = pos;
        this.normal = normal;
        inUse = false;
        readyToDraw = false;
        initParticles();
        LifeTime = 50;
    }

    public void initParticles(){
        Log.d("draw", "inUse initParticles");
        particles = new ArrayList<>();
        if(particles.size()==0){
            for(int i=0; i<30; i++){
                particles.add(new Particle(mySurfaceView, pos, normal, normal));
            }
        }
    }

    public void drawSelf(){
        if(!inUse | !readyToDraw) {
            Log.d("draw", "but not inUse");
            return;
        }else{
            Log.d("draw", "inUse " + particles.size());
            Log.d("draw", "inUse LifeTime " + LifeTime);
            Log.d("draw", "inUse Pos " + particles.get(0).pos.x + " "+particles.get(0).pos.y + " " + particles.get(0).pos.z);
        }

        LifeTime--;
        for(Particle particle : particles){
            vec randVec = new vec(Math.random()*0.8, 0.1, Math.random()*0.8);
            double randNum = Math.random() / 50;
            particle.draw();
            particle.setPos(particle.getPos().add(normal.mul(randNum)));
            particle.setPos(particle.getPos().add(randVec));
        }
    }
}
