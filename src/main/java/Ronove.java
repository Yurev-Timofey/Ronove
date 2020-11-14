import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

public class Ronove {

    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create("Nzc3MjYzNjU1MTM3NTc0OTUy.X7A5MA.uTKTAwzOc_6GtMTU0J2rhwvsN9U").build().login().block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("Ronove"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Psh... Ho-ho-ho"))
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMember)
                .flatMap(member -> member.get().getVoiceState())
                .flatMap(VoiceState::getChannel)
                .flatMap(voiceChannel -> voiceChannel.join(voiceChannelJoinSpec -> voiceChannelJoinSpec.setProvider(null)))
                .subscribe();

        client.onDisconnect().block();
    }
}