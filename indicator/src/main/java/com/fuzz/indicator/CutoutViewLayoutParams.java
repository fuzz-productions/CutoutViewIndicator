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

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * Plain Old Java Object representing the configuration of the child views inside
 * {@link CutoutViewIndicator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutViewLayoutParams extends LinearLayout.LayoutParams {
    @DrawableRes
    public int cellBackgroundId;
    /**
     * This is the id of the drawable currently acting as indicator. If 0, no indicator will be shown.
     */
    @DrawableRes
    public int indicatorDrawableId;

    @NonNull
    protected WeakReference<CutoutCell> viewHolder = new WeakReference<>(null);

    /**
     * Set this to true to prevent the attached view from being
     * removed during the next call to {@link CutoutViewIndicator#rebuildChildViews()}.
     * <p>
     *     Should be false if the attached view was added by a generator.
     * </p>
     */
    public boolean preservedDuringRebuild;

    /**
     * Amount of space between this view and the one before it
     * <p>
     *     Treat this like a conditional margin - it is added to the
     *     {@link #getMarginStart() start/left/top margin} only if it
     *     is <em>not</em> the first View in the CutoutViewIndicator.
     * </p>
     */
    public int internalSpacing;
    /**
     * This is the resolved length of the view (in pixels)
     *
     * @see #perpendicularLength
     */
    public int cellLength;
    /**
     * This is the height or width bounding all child views when
     * {@link CutoutViewIndicator#HORIZONTAL HORIZONTAL} or
     * {@link CutoutViewIndicator#VERTICAL VERTICAL}, respectively.
     * <p/>
     * Typically equal to the height/width of the {@link CutoutViewIndicator}, minus padding.
     */
    public int perpendicularLength;

    /**
     * Utility method for setting height and width in an orientation-independent way
     *
     * @param dimension the new value for either height or width, in pixels
     * @param isHeight true if this is height, false if this is width
     */
    public void setParamDimension(int dimension, boolean isHeight) {
        if (isHeight) {
            height = dimension;
        } else {
            width = dimension;
        }
    }

    public CutoutViewLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
        init(c, attrs);
    }

    public CutoutViewLayoutParams(@NonNull ViewGroup.LayoutParams source) {
        super(source);
        if (source instanceof CutoutViewLayoutParams) {
            CutoutViewLayoutParams cutoutSource = (CutoutViewLayoutParams) source;
            setFrom(cutoutSource);
        }
    }

    public CutoutViewLayoutParams(int width, int height) {
        super(width, height);
    }

    @SuppressWarnings("ResourceType")
    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CutoutViewIndicator_Layout);
            indicatorDrawableId = a.getResourceId(R.styleable.CutoutViewIndicator_Layout_layout_rcv_drawable, 0);

            perpendicularLength = a.getDimensionPixelSize(R.styleable.CutoutViewIndicator_Layout_layout_rcv_perpendicular_length, WRAP_CONTENT);
            cellLength = a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_Layout_layout_rcv_cell_length, WRAP_CONTENT);

            cellBackgroundId = a.getResourceId(R.styleable.CutoutViewIndicator_Layout_layout_rcv_drawable_unselected, 0);

            a.recycle();
        }
    }

    /**
     * Use this to set the attached {@link CutoutCell} reference. This can
     * be retrieved by {@link CutoutCellGenerator} implementations during
     * {@link CutoutCellGenerator#onBindChild(View, CutoutViewLayoutParams, View)}
     * if they so wish.
     *
     * @param cutoutCell    a CutoutCell that would hopefully be defined
     *                       such that {@link CutoutCell#getItemView()}
     *                       returns the same object that these LayoutParams
     *                       are set on.
     */
    public void setViewHolder(@Nullable CutoutCell cutoutCell) {
        this.viewHolder = new WeakReference<>(cutoutCell);
    }

    /**
     * Getter counterpart to {@link #setViewHolder(CutoutCell)}. In general,
     * it is safe to assume that the value returned here is the same as that
     * most recently set.
     *
     * @return the associated CutoutCell if it has not been garbage-collected.
     */
    @Nullable
    public CutoutCell getViewHolder() {
        return viewHolder.get();
    }

    public void setFrom(@NonNull CutoutViewLayoutParams cutoutSource) {
        cellBackgroundId = cutoutSource.cellBackgroundId;
        indicatorDrawableId = cutoutSource.indicatorDrawableId;
        internalSpacing = cutoutSource.internalSpacing;
        cellLength = cutoutSource.cellLength;
        perpendicularLength = cutoutSource.perpendicularLength;
        viewHolder = cutoutSource.viewHolder;
    }

    /**
     * Unlike calling the constructor, if the passed parameters are already of this
     * type they will be returned directly.
     *
     * @param layoutParams any layoutParams, ideally descendants of {@link CutoutViewLayoutParams}
     * @return valid CutoutViewLayoutParams
     */
    public static CutoutViewLayoutParams from(@NonNull ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof CutoutViewLayoutParams) {
            return (CutoutViewLayoutParams) layoutParams;
        } else {
            return new CutoutViewLayoutParams(layoutParams);
        }
    }
}
