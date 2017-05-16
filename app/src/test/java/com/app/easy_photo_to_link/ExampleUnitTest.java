package com.app.easy_photo_to_link;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Mock
    List mockedlist;
    @Mock
    Context mockContext;
    @Test
    public void addition_correct() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test(expected = AssertionError.class)
    public void addition_isNotCorrect() throws Exception {
        assertEquals("Numbers isn't equals!", 5, 2 + 2);
    }

    @Test
    public  void myTest()
    {
        when(mockContext.getString(R.string.app_name))
                .thenReturn("Fake name");
        String str = mockContext.getString(R.string.app_name);
        assertEquals("Numbers isn't equals!", str, "Fake name");
    }
}