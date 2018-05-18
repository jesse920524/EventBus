/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
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
package org.greenrobot.eventbus;

/**
 * Each subscriber method has a thread mode, which determines in which thread the method is to be called by EventBus.
 * EventBus takes care of threading independently from the posting thread.
 *
 * 每个订阅方法都有一个ThreadMode.ThreadMode决定了EventBus在哪个线程调用该方法.
 * EventBus处理方法的线程独立于事件发布的线程.
 *
 * 
 * @see EventBus#register(Object)
 * @author Markus
 */
public enum ThreadMode {
    /**
     * Subscriber will be called directly in the same thread, which is posting the event. This is the default. Event delivery
     * implies the least overhead because it avoids thread switching completely. Thus this is the recommended mode for
     * simple tasks that are known to complete in a very short time without requiring the main thread. Event handlers
     * using this mode must return quickly to avoid blocking the posting thread, which may be the main thread.
     *
     * 订阅方法会在事件发布的线程直接调用.(默认选项)
     * POSTING标记的订阅方法不能执行耗时操作, 因为可能在主线程(否则会阻塞主线程).
     */
    POSTING,

    /**
     * On Android, subscriber will be called in Android's main thread (UI thread). If the posting thread is
     * the main thread, subscriber methods will be called directly, blocking the posting thread. Otherwise the event
     * is queued for delivery (non-blocking). Subscribers using this mode must return quickly to avoid blocking the main thread.
     * If not on Android, behaves the same as {@link #POSTING}.
     *
     * 订阅方法会在Android的主线程(UI线程)调用.
     * MAIN编辑的订阅方法同样不能执行耗时操作.理由同上.
     */
    MAIN,

    /**
     * On Android, subscriber will be called in Android's main thread (UI thread). Different from {@link #MAIN},
     * the event will always be queued for delivery. This ensures that the post call is non-blocking.
     *
     * 和Main类似,在订阅方法在主线程调用
     * 与Main不同:MAIN_ORDERED保证不会阻塞
     * 因为MAIN_ORDERED标记的方法会被扔到MessageQueue中.
     */
    MAIN_ORDERED,

    /**
     * On Android, subscriber will be called in a background thread. If posting thread is not the main thread, subscriber methods
     * will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single
     * background thread, that will deliver all its events sequentially. Subscribers using this mode should try to
     * return quickly to avoid blocking the background thread. If not on Android, always uses a background thread.
     *
     * 订阅方法在后台线程调用.
     * 如果发送事件的线程不是主线程,订阅方法会直接在当前线程调用.
     * 如果发送事件的线程是主线程,EventBus在后台线程中调用订阅方法.
     */
    BACKGROUND,

    /**
     * Subscriber will be called in a separate thread. This is always independent from the posting thread and the
     * main thread. Posting events never wait for subscriber methods using this mode. Subscriber methods should
     * use this mode if their execution might take some time, e.g. for network access. Avoid triggering a large number
     * of long running asynchronous subscriber methods at the same time to limit the number of concurrent threads. EventBus
     * uses a thread pool to efficiently reuse threads from completed asynchronous subscriber notifications.
     *
     * 订阅方法会在一个独立的线程调用.
     * 该线程既不是发送事件的线程,又不是主线程.
     * 常用于耗时操作
     */
    ASYNC
}