package listeners.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import utils.Pairing;
import utils.PairingHistory;

public class PairingHistoryListener implements SlashCommandHandler {
    @Override
    public Response apply(SlashCommandRequest req, SlashCommandContext ctx) throws IOException, SlackApiException {
        if (!userIsAdmin(ctx)) {
            return ctx.ack("Sorry, you must be a workspace admin to use this feature.");
        }

        String cmd = req.getPayload().getText();
        if (commandHasErrors(cmd)) {
            return ctx.ack("Error: please follow the format /pairing-history @<username>");
        }

        String userName = cmd.replace("@", "");
        UsersListResponse usersList = ctx.client().usersList(r -> r);

        String userId = getUserId(usersList, userName);
        if (userId.equals("Not found")) {
            return ctx.ack("Error: no such user in this workspace");
        }

        String response = buildResponse(userId, PairingHistory.getHistory(userId));
        return ctx.ack(response);
    }

    private String buildResponse(String userId, ArrayList<Pairing> history) {
        StringBuilder output = new StringBuilder();
        for (Pairing pairing : history) {
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

    private String getUserId(UsersListResponse response, String userName) {
        Optional<User> user = response.getMembers().stream()
                .filter(u -> userName.equals(u.getName()))
                .findFirst();
        return user.map(User::getId).orElse("Not found");
    }

    private boolean commandHasErrors(String text) {
        System.out.println(String.format("The text you tried was: %s", text));
        String regex = "^@(\\w|[.-])+$";
        return !text.matches(regex);
    }
}
