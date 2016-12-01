/*
 * Copyright 2016 Philip Cohn-Cort
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuzz.indicator;

import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * A simple CutoutCell implementation that performs a 2D-shift of
 * ImageView content over its background.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutImageCell extends TypicalCutoutCell<ImageView> {

    @DrawableRes
    protected int latestResourceId;

    public CutoutImageCell(ImageView itemView) {
        super(itemView);
    }

    @Override
    public void offsetContentBy(@NonNull IndicatorOffsetEvent event) {
        OffSetters.offsetImageBy(itemView, event.getOrientation(), event.getFraction(), new Matrix());
    }

    /**
     * Call this whenever the view's bounds might have changed, or the layout params are different.
     * <p>
     *     This method delegates the choice of Drawable to
     *     {@link #chooseDrawable(CutoutViewLayoutParams)}.
     * </p>
     *
     * @param lp    the LayoutParams {@link #itemView} should be assumed to have
     */
    public void updateDrawable(@NonNull CutoutViewLayoutParams lp) {
        Drawable chosen = chooseDrawable(lp);

        if (chosen != null && (chosen.getIntrinsicWidth() <= 0 || chosen.getIntrinsicHeight() <= 0) && chosen instanceof ColorDrawable) {
            ResizeableColorDrawable rCD = new ResizeableColorDrawable((ColorDrawable) chosen);
            rCD.setIntrinsicWidth(lp.width);
            rCD.setIntrinsicHeight(lp.height);
            chosen = rCD;
        }
        itemView.setImageDrawable(chosen);
    }

    /**
     * Attach the appropriate drawable, specified by {@code lp}, on {@link #itemView}.
     *
     * @param lp    the LayoutParams {@link #itemView} should be assumed to have
     * @return a resolved drawable, or null if none can be found
     * @see CutoutViewLayoutParams#indicatorDrawableId
     */
    @Nullable
    protected Drawable chooseDrawable(@NonNull CutoutViewLayoutParams lp) {
        Drawable chosen;
        if (latestResourceId != lp.indicatorDrawableId) {
            latestResourceId = lp.indicatorDrawableId;
            chosen = ContextCompat.getDrawable(itemView.getContext(), lp.indicatorDrawableId);
        } else {
            chosen = itemView.getDrawable();
        }
        return chosen;
    }
}
