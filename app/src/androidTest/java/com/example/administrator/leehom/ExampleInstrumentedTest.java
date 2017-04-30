package com.example.administrator.leehom;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.administrator.leehom.db.dao.MusicDao;
import com.example.administrator.leehom.model.MusicModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.administrator.leehom", appContext.getPackageName());
    }

    @Test
    public void testAdd() {
        List<MusicModel> models = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            MusicModel musicModel = new MusicModel();
            musicModel.setUrl("url==" + i);
            musicModel.setName("name==" + i);
            models.add(musicModel);
        }
        long insert = MusicDao.getInstance(InstrumentationRegistry.getTargetContext()).insert(models);
        Log.i("wzy", insert + "insert");
    }

    @Test
    public void testDelete() {
        MusicModel musicModel = new MusicModel();
        musicModel.setUrl("url==" + 5);
        List<MusicModel> models = new ArrayList<>();
        models.add(musicModel);
        MusicDao.getInstance(InstrumentationRegistry.getTargetContext()).delete(models);
    }

    @Test
    public void testQuery() {
        List<MusicModel> models = MusicDao.getInstance(InstrumentationRegistry.getTargetContext()).queryAll();
        Log.i("wzy", models.toString());
    }
}
