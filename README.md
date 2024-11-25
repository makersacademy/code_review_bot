### It's a bot for pairing up people for peer review. ###

A user should be able to send the slackbot some code to be peer reviewed.

The first person to do that gets a message of thanks and is told to wait until another person sends their code.

The second person to send their code gets a message of thanks and the two people are paired up for peer review.

The slackbot creates a DM with the two people, shares both codebases and some guidance on how to do the peer review.

#### Notes: ####
- Run with ./gradlew run
- Requires the following env variables:
  - SLACK_BOT_TOKEN
  - SLACK_APP_TOKEN

### Slack Configuration: ###

#### Display Information: ####
- App name: Code Review Bot
- Short description: Pairs students for code reviews
#### Slash Commands: ####
- Name: /code-review
- Description: 	
  Submit your code and get it reviewed by a peer!

- Name: /pairing-history
- Returns a list of occasions this user has paired for code review.
- Usage hint: @<user_name>
#### App-Level Tokens: ####
- basic-write-token (connections:write)
#### Scopes: ####
##### Bot Token Scopes #####
| OAuth Scope | Description |
|---|---|
| channels:history | View messages and other content in public channels that Code Review Bot has been added to |
| chat:write | Send messages as @Code Review Bot |
| commands | Add shortcuts and/or slash commands that people can use |
| im:write | Start direct messages with people |
| mpim:write | Start group direct messages with people |
| users:read | View people in a workspace

#### App Manifest ####

```json
{
    "display_information": {
        "name": "Code Review Bot",
        "description": "Pairs students for code reviews",
        "background_color": "#1e2b42"
    },
    "features": {
        "app_home": {
            "home_tab_enabled": true,
            "messages_tab_enabled": false,
            "messages_tab_read_only_enabled": true
        },
        "bot_user": {
            "display_name": "Code Review Bot",
            "always_online": false
        },
        "shortcuts": [
            {
                "name": "Run sample shortcut",
                "type": "global",
                "callback_id": "sample-shortcut-id",
                "description": "Runs a sample shortcut"
            }
        ],
        "slash_commands": [
            {
                "command": "/code-review",
                "description": "Submit your code and get it reviewed by a peer!",
                "should_escape": false
            },
            {
                "command": "/pairing-history",
                "description": "Returns a list of occasions this user has paired for code review.",
                "usage_hint": "@<user>",
                "should_escape": false
            }
        ]
    },
    "oauth_config": {
        "scopes": {
            "bot": [
                "channels:history",
                "chat:write",
                "commands",
                "im:write",
                "mpim:write",
                "users:read"
            ]
        }
    },
    "settings": {
        "event_subscriptions": {
            "bot_events": [
                "app_home_opened",
                "message.channels"
            ]
        },
        "interactivity": {
            "is_enabled": true
        },
        "org_deploy_enabled": false,
        "socket_mode_enabled": true,
        "token_rotation_enabled": false
    }
}

```