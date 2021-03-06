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
package com.fuzz.emptyhusk.choosegenerator;

import android.support.annotation.NonNull;

/**
 * Counterpart to {@link android.widget.AdapterView.OnItemSelectedListener} that
 * isn't tied to {@link android.widget.AdapterView}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface OnSelectedListener<T> {
    /**
     * Whatever was asking the user to select will have disappeared by the
     * time this method is called
     * @param chosen the item chosen (may not be null)
     */
    void onSelected(@NonNull T chosen);
}
