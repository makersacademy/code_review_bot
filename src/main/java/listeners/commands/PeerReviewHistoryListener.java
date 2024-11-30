package listeners.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.PeerReviewHistory;
import utils.PeerReviewPair;

public class PeerReviewHistoryListener implements SlashCommandHandler {
    @Override
    public Response apply(SlashCommandRequest req, SlashCommandContext ctx) throws IOException, SlackApiException {
        if (!userIsAdmin(ctx)) {
            return ctx.ack("Sorry, you must be a workspace admin to use this feature.");
        }

        String cmd = req.getPayload().getText();
        String regex = "<@(.*)\\|";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cmd);

        if (matcher.find()) {
            String userId = matcher.group(1);
            String response = buildResponse(userId, PeerReviewHistory.getHistory(userId));
            return ctx.ack(response);
        } else {
            return ctx.ack("Error: please follow the format /pairing-history @<username>"
                    + "and ensure the username is valid.");
        }
    }

    private String buildResponse(String userId, ArrayList<PeerReviewPair> history) {
        StringBuilder output = new StringBuilder();
        for (PeerReviewPair pairing : history) {
            output.append(pairing.stringFor(userId));
            output.append("\n");
        }
        return output.isEmpty() ? "This student has not yet paired." : output.toString();
    }

    private boolean userIsAdmin(SlashCommandContext ctx) throws SlackApiException, IOException {
        String userId = ctx.getRequestUserId();
        UsersInfoResponse response = ctx.client().usersInfo(r -> r.user(userId));
        User user = response.getUser();
        return user.isAdmin();
    }
}
