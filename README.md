**It's a bot for pairing up people for peer review.**

A user should be able to send the slackbot some code to be peer reviewed.

The first person to do that gets a message of thanks and is told to wait until another person sends their code.

The second person to send their code gets a message of thanks and the two people are paired up for peer review.

The slackbot creates a DM with the two people, shares both codebases and some guidance on how to do the peer review.

Some challenges:

- The same bot would be used by multiple cohorts for multiple exercises, so how would it know who to pair with who?