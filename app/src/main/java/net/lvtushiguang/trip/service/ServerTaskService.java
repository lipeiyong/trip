package net.lvtushiguang.trip.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class ServerTaskService extends IntentService {
    private static final String SERVICE_NAME = "ServerTaskService";
    public static final String ACTION_PUB_BLOG_COMMENT = "net.oschina.app.ACTION_PUB_BLOG_COMMENT";
    public static final String ACTION_PUB_COMMENT = "net.oschina.app.ACTION_PUB_COMMENT";
    public static final String ACTION_PUB_POST = "net.oschina.app.ACTION_PUB_POST";
    public static final String ACTION_PUB_TWEET = "net.oschina.app.ACTION_PUB_TWEET";
    public static final String ACTION_PUB_SOFTWARE_TWEET = "net.oschina.app.ACTION_PUB_SOFTWARE_TWEET";

    public static final String KEY_ADAPTER = "adapter";

    public static final String BUNDLE_PUB_COMMENT_TASK = "BUNDLE_PUB_COMMENT_TASK";
    public static final String BUNDLE_PUB_POST_TASK = "BUNDLE_PUB_POST_TASK";
    public static final String BUNDLE_PUB_TWEET_TASK = "BUNDLE_PUB_TWEET_TASK";
    public static final String BUNDLE_PUB_SOFTWARE_TWEET_TASK = "BUNDLE_PUB_SOFTWARE_TWEET_TASK";
    public static final String KEY_SOFTID = "soft_id";

    private static final String KEY_COMMENT = "comment_";
    private static final String KEY_TWEET = "tweet_";
    private static final String KEY_SOFTWARE_TWEET = "software_tweet_";
    private static final String KEY_POST = "post_";

    public static List<String> penddingTasks = new ArrayList<String>();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServerTaskService(String name) {
        super(name);
    }

    private void cancellNotification(int id) {
        NotificationManagerCompat.from(this).cancel(id);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
