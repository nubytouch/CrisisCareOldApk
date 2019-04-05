package com.nubytouch.crisiscare.ui.utils;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import timber.log.Timber;


public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior
{
    private static final Interpolator INTERPOLATOR    = new FastOutSlowInInterpolator();
    private              boolean      mIsAnimatingOut = false;


    public ScrollAwareFABBehavior(Context context, AttributeSet attrs)
    {
        super();
    }


    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes)
    {
        Timber.d("onStartNestedScroll");
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
               super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                                         nestedScrollAxes);
    }


    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child, final
    View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed)
    {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                             dyUnconsumed);
        Timber.d("onNestedScroll");
        if (dyConsumed > 0 && !this.mIsAnimatingOut &&
            child.getVisibility() == View.VISIBLE)
        {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            animateOut(child);
        }
        else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE)
        {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            animateIn(child);
        }
    }


    // Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
    private void animateOut(final FloatingActionButton button)
    {
        ViewCompat.animate(button)
                .scaleX(0.0F)
                .scaleY(0.0F)
                .alpha(0.0F)
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener()
                {
                    public void onAnimationStart(View view)
                    {
                        ScrollAwareFABBehavior.this.mIsAnimatingOut = true;
                    }


                    public void onAnimationCancel(View view)
                    {
                        ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                    }


                    public void onAnimationEnd(View view)
                    {
                        ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                        view.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }


    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    private void animateIn(FloatingActionButton button)
    {
        button.setVisibility(View.VISIBLE);
        ViewCompat.animate(button)
                .scaleX(1.0F)
                .scaleY(1.0F)
                .alpha(1.0F)
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(null)
                .start();

    }
}
