package com.example.zju_bumper_cars.ModelLayer.VrModules;

import android.content.Context;

import com.example.zju_bumper_cars.R;
import com.example.zju_bumper_cars.utils.TextureUtil;


/**
 * 类名
 * 创建时间 2016/12/12
 * 实现的主要功能
 *
 * @author season
 */

public class SkyBox extends BaseSkyBox {


    public SkyBox(Context context) {
        super(context);
    }

    @Override
    protected int getTextureId() {
        return TextureUtil.loadCubeMap(mContext,
                new int[]{R.drawable.left_1, R.drawable.right_1,
                        R.drawable.down_1, R.drawable.top_1,
                        R.drawable.front_1, R.drawable.back_1});
    }
}
