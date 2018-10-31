package com.sandeepsingh.imageupload;

import android.support.test.rule.ActivityTestRule;
import com.sandeepsingh.imageupload.feature.comment.view.CommentActivity;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Sandeep on 10/31/18.
 */
public class CommentActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<CommentActivity> mActivityRule = new ActivityTestRule<>(CommentActivity.class);

    @Test
    public void ensureEditTextEmptyAfterSendClick(){
        onView(withId(R.id.et_comment)).perform(typeText("Hello"),closeSoftKeyboard());
        onView(withId(R.id.iv_send_comment)).perform(click());
        onView(withId(R.id.et_comment)).check(matches(withText("")));
    }

    @Test
    public void ensureBackButtonFinishesActivity(){
        onView(withId(R.id.iv_back)).perform(click());
    }

    @Test
    public void ensurePostCommentButtonColorChanges(){

        //When user is typing something
        onView(withId(R.id.iv_back)).perform(typeText("Hello"),closeSoftKeyboard());
        onView(withId(R.id.iv_send_comment)).check(matches(hasTextColor(R.color.colorPrimary)));

        //When user hits send or default view
        onView(withId(R.id.iv_back)).perform(typeText("Hello"),closeSoftKeyboard());
        onView(withId(R.id.iv_send_comment)).perform(click(),closeSoftKeyboard());
        onView(withId(R.id.iv_send_comment)).check(matches(hasTextColor(R.color.light_grey3)));
    }
}
