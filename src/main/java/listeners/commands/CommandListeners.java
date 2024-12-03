package listeners.commands;

import com.slack.api.bolt.App;
import listeners.ListenerProvider;

public class CommandListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.command("/code-review", new PeerReviewListener());
        app.command("/pairing-history", new PeerReviewHistoryListener());
        app.command("/flush-code-review-queue", new FlushCodeReviewQueueListener());
    }
}
