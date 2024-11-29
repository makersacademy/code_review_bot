package listeners.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import java.io.*;
import java.util.*;
import utils.CodeSubmission;

public class CodeReviewListener implements SlashCommandHandler {
    @Override
    public Response apply(SlashCommandRequest req, SlashCommandContext ctx) throws IOException {
        SlashCommandPayload payload = req.getPayload();

        String channelId = payload.getChannelId();
        System.out.println("Channel ID: " + channelId);
        String channelName = payload.getChannelName();
        System.out.println("Channel Name: " + channelName);
        String userId = payload.getUserId();
        System.out.println("User ID: " + userId);
        String code = payload.getText();
        System.out.println("Code: " + code);

        CodeSubmission currentSubmission = new CodeSubmission(userId, code);
        Queue<CodeSubmission> submissionQueue = readQueue(channelId);

        if (submissionQueue.isEmpty()) {
            submissionQueue.add(currentSubmission);
            updateQueue(channelId, submissionQueue);
            return ctx.ack("Thank you! Please wait while we pair you with another user.");
        }

        CodeSubmission pairedSubmission = submissionQueue.poll();

        if (Objects.equals(pairedSubmission.userId, userId)) {
            // The user has resubmitted his/her code
            submissionQueue.add(currentSubmission);
            updateQueue(channelId, submissionQueue);
            return ctx.ack("Thank you, your code has been updated! Please wait while we pair you with another user.");
        } else {
            // Pair the current user with the first user in the current channel's queue
            updateQueue(channelId, submissionQueue);
            createPairChannel(ctx, currentSubmission, pairedSubmission);
            logPairing(channelName, currentSubmission, pairedSubmission);
            return ctx.ack("You have been paired with <@" + pairedSubmission.userId + ">. Check your DMs!");
        }
    }

    private void createPairChannel(SlashCommandContext ctx, CodeSubmission a, CodeSubmission b) {
        try {
            ConversationsOpenResponse response =
                    ctx.client().conversationsOpen(r -> r.users(Arrays.asList(a.userId, b.userId)));

            String channelId = response.getChannel().getId();
            ctx.client().chatPostMessage(r -> r.channel(channelId)
                    .text("You've been paired for peer review!\n\n"
                            + "*Code from <@" + a.userId + ">*:\n" + a.code + "\n\n"
                            + "*Code from <@" + b.userId + ">*:\n" + b.code + "\n\n"
                            + "### Peer Review Guidance:\n"
                            + "- Review the code for clarity and best practices.\n"
                            + "- Provide constructive feedback.\n"
                            + "- Collaborate and learn from each other."));
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
    }

    private void logPairing(String channelName, CodeSubmission a, CodeSubmission b) {
        String fileName = "pairing_history.csv";
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append(a.userId + "," + b.userId + "," + channelName + "," + new Date() + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateQueue(String channelId, Queue<CodeSubmission> submissionQueue) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(pathnameFor(channelId)), submissionQueue);
        } catch (FileNotFoundException e) {
            //            create the channelSubmissions directory
            new File("./channelSubmissions").mkdir();
            //            create the file
            new File(pathnameFor(channelId)).createNewFile();
            mapper.writeValue(new File(pathnameFor(channelId)), submissionQueue);
        }
    }

    private Queue<CodeSubmission> readQueue(String channelId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(pathnameFor(channelId)), new TypeReference<Queue<CodeSubmission>>() {});
        } catch (FileNotFoundException e) {
            return new LinkedList<>();
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    private String pathnameFor(String channelId) {
        return "./channelSubmissions/submissions_" + channelId + ".json";
    }
}
