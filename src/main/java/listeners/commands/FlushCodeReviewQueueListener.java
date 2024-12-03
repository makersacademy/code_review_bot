package listeners.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.User;

import java.io.File;
import java.io.IOException;

public class FlushCodeReviewQueueListener implements SlashCommandHandler {
    @Override
    public Response apply(SlashCommandRequest req, SlashCommandContext ctx) throws IOException, SlackApiException {

        if (!userIsAdmin(ctx)) {
            return ctx.ack("Sorry, you must be a workspace admin to use this feature.");
        }

        SlashCommandPayload payload = req.getPayload();
        String channelId = payload.getChannelId();
        System.out.println("Channel ID: " + channelId);
        String channelName = payload.getChannelName();
        System.out.println("Channel Name: " + channelName);
        flushQueue(channelId);

        return ctx.ack("Code review queue for " + channelName + " has been flushed.");
    }

    private void flushQueue(String channelId) {
        String filePath = "./channelSubmissions/" + channelId + ".json";
        new File(filePath).delete();
    }

    private boolean userIsAdmin(SlashCommandContext ctx) throws SlackApiException, IOException {
        String userId = ctx.getRequestUserId();
        UsersInfoResponse response = ctx.client().usersInfo(r -> r.user(userId));
        User user = response.getUser();
        return user.isAdmin();
    }
}
