package ch.hofmann.refinement_notes_consolidation.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consolidate")
public class ConsolidationController {

    private final ChatClient openAiChatClient;
    private final ChatClient localChatClient;

    public ConsolidationController(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("qwenChatClient") ChatClient qwenChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.localChatClient = qwenChatClient;
    }


    @GetMapping
    public String chat(@RequestParam String message, @RequestParam String provider) {
        if ("local".equals(provider)) {
            return localChatClient.prompt().user(message).call().content();
        } else if ("openai".equals(provider)) {
            return openAiChatClient.prompt().user(message).call().content();
        }
        throw new IllegalArgumentException("Unsupported provider");
    }
}
