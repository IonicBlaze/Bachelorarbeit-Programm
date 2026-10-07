package ch.hofmann.refinement_notes_consolidation.controller;

import ch.hofmann.refinement_notes_consolidation.model.dto.ConsolidationRequest;
import ch.hofmann.refinement_notes_consolidation.model.dto.ConsolidationResponse;
import ch.hofmann.refinement_notes_consolidation.model.prompt.ConsolidationPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConsolidationResponse chat(@RequestBody ConsolidationRequest consolidationRequest) {
        ChatClient client = null;
        if ("local".equals(consolidationRequest.provider())) {
            client = localChatClient;
        } else if ("openai".equals(consolidationRequest.provider())) {
            client = openAiChatClient;
        }

        if (client == null) {
            throw new IllegalArgumentException("Unsupported provider");
        }

        return client.prompt()
                .user(ConsolidationPrompt.from(consolidationRequest))
                .call()
                .entity(ConsolidationResponse.class);
    }
}
