package com.coreui.utils

/**
 * Copyright 2017 Bo Song
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * Set dividers' properties(horizontal and vertical space...) of item with type.
 * 通过item type 设置边框属性
 * Created by bosong on 2017/3/10.
 */

class SCommonItemDecoration : RecyclerView.ItemDecoration {
    private var rows: Int = 0
    private  var divideRecyclerHeight: Boolean = false
    private var mPropMap: SparseArray<ItemDecorationProps>? = null // itemType -> prop

    constructor(propMap: SparseArray<ItemDecorationProps>) {
        mPropMap = propMap
    }


    constructor(propMap: SparseArray<ItemDecorationProps>, dividRecyclerHeight: Boolean, rows: Int) {
        mPropMap = propMap
        this.divideRecyclerHeight = dividRecyclerHeight
        this.rows = rows
    }



    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {


        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter
        val itemType = adapter?.getItemViewType(position)
        if (divideRecyclerHeight) {
            val para = view.layoutParams
            para.height = parent.height / 2 - rows * mPropMap!!.get(itemType!!).verticalSpace
            view.layoutParams = para
        }
        val props: ItemDecorationProps?
        if (mPropMap != null) {
            props = mPropMap!!.get(itemType!!)
        } else {
            return
        }
        if (props == null) {
            return
        }
        var spanIndex = 0
        var spanSize = 1
        var spanCount = 1
        var orientation = OrientationHelper.VERTICAL
        if (parent.layoutManager is GridLayoutManager) {
            val lp = view.layoutParams as GridLayoutManager.LayoutParams
            spanIndex = lp.spanIndex
            spanSize = lp.spanSize
            val layoutManager = parent.layoutManager as GridLayoutManager
            spanCount = layoutManager.spanCount // Assume that there're spanCount items in this row/column.
            orientation = layoutManager.orientation
        } else if (parent.layoutManager is StaggeredGridLayoutManager) {
            val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            spanIndex = lp.spanIndex
            val layoutManager = parent.layoutManager as StaggeredGridLayoutManager
            spanCount = layoutManager.spanCount // Assume that there're spanCount items in this row/column.
            spanSize = if (lp.isFullSpan) spanCount else 1
            orientation = layoutManager.orientation
        }

        val isFirstRowOrColumn: Boolean
        val isLastRowOrColumn: Boolean
        val prePos = if (position > 0) position - 1 else -1
        val nextPos = if (position < adapter?.itemCount!! - 1) position + 1 else -1
        // Last position on the last row 上一行的最后一个位置
        val preRowPos = if (position > spanIndex) position - (1 + spanIndex) else -1
        // First position on the next row 下一行的第一个位置
        val nextRowPos = if (position < adapter?.itemCount!! - (spanCount - spanIndex)) position + (spanCount - spanIndex) else -1
        isFirstRowOrColumn = position == 0 || prePos == -1 || itemType != adapter?.getItemViewType(prePos) ||
                preRowPos == -1 || itemType != adapter.getItemViewType(preRowPos)
        isLastRowOrColumn = position == adapter?.itemCount!! - 1 || nextPos == -1 || itemType != adapter?.getItemViewType(nextPos) ||
                nextRowPos == -1 || itemType != adapter.getItemViewType(nextRowPos)

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        if (orientation == GridLayoutManager.VERTICAL) {

            if (props.hasVerticalEdge) {
                left = props.verticalSpace * (spanCount - spanIndex) / spanCount
                right = props.verticalSpace * (spanIndex + (spanSize - 1) + 1) / spanCount
            } else {
                left = props.verticalSpace * spanIndex / spanCount
                right = props.verticalSpace * (spanCount - (spanIndex + spanSize - 1) - 1) / spanCount
            }

            if (isFirstRowOrColumn) { // First row
                if (props.hasHorizontalEdge) {
                    top = props.horizontalSpace
                }
            }
            if (isLastRowOrColumn) { // Last row
                if (props.hasHorizontalEdge) {
                    bottom = props.horizontalSpace
                }
            } else {
                bottom = props.horizontalSpace
            }
        } else {

            if (props.hasHorizontalEdge) {
                top = props.horizontalSpace * (spanCount - spanIndex) / spanCount
                bottom = props.horizontalSpace * (spanIndex + (spanSize - 1) + 1) / spanCount
            } else {
                top = props.horizontalSpace * spanIndex / spanCount
                bottom = props.horizontalSpace * (spanCount - (spanIndex + spanSize - 1) - 1) / spanCount
            }

            if (isFirstRowOrColumn) { // First column
                if (props.hasVerticalEdge) {
                    left = props.verticalSpace
                }
            }
            if (isLastRowOrColumn) { // Last column
                if (props.hasVerticalEdge) {
                    right = props.verticalSpace
                }
            } else {
                right = props.verticalSpace
            }
        }

        outRect.set(left, top, right, bottom)
    }

    class ItemDecorationProps(val horizontalSpace: Int, val verticalSpace: Int, val hasHorizontalEdge: Boolean, val hasVerticalEdge: Boolean)
}