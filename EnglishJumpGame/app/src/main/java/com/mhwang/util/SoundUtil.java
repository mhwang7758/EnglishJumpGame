package com.mhwang.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.mhwang.englishjumpgame.R;

/** 控制音频类
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class SoundUtil {
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;


    // SoundPool对象
    private SoundPool mSoundPlayer;                  // 播放简短的声音使用SoundPool,一般用于内置音效
    private static SoundUtil sSoundUtil;
    private int[] mSounds = {R.raw.sound_success, R.raw.sound_failed};

    public static SoundUtil getInstance(Context context){
        if (sSoundUtil == null){
            synchronized (SoundUtil.class){
                if (sSoundUtil == null){
                    sSoundUtil = new SoundUtil(context);
                }
            }
        }
        return sSoundUtil;
    }

    private SoundUtil(Context context){
        mSoundPlayer = new SoundPool(1, AudioManager.STREAM_SYSTEM, 1);
        mSoundPlayer.load(context, R.raw.sound_success, 1);
        mSoundPlayer.load(context, R.raw.sound_failed, 1);
    }

    /** 播放声音
     * @param which
     */
    public void playSound(int which){
        if (which >= mSounds.length) return;
        // 这里的soundId为资源缓存的id，根据加载顺序生成，从1开始，因此这里为资源下标顺序+1
        mSoundPlayer.play(which + 1, 1, 1, 1, 0, 1);
    }
}
