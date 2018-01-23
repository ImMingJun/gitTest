/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.luck.picture.lib.loader;

/**
 * Simple callback only cares about success/fail.
 *
 */
public interface IPictureCallback {

    /**
     * Successfully handle a task;
     */
    void onSuccess();

    /**
     * Error happened when running a task;
     */
    void onFail(Throwable t);
}