package ch.hofmann.refinement_notes_consolidation.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelsConfiguration {

    private static final String SYSTEM_PROMPT = """
              Du unterstützt ein Scrum-Team bei der Aufbereitung der Ergebnisse eines Refinement-Meetings.
              Erstelle aus den Notizen der einzelnen Teilnehmer und - sofern vorhanden - dem Projekt-Wiki einen 
              strukturierten Ticketentwurf. 
              
              Verwende ausschliesslich die bereitgestellten Informationen. Ergänze kein eigenes Fachwissen
              und erfinde keine Anforderungen. Das Projekt-Wiki kann relevante, redundante, widersprüchliche
              oder für das Ticket irrelevante Inhalte enthalten.
              
              Beachte folgende Regeln:
             
              - Führe inhaltlich übereinstimmende Aussagen zusammen und vermeide Wiederholungen.
              - Formuliere Akzeptanzkriterien eindeutig, präzise und überprüfbar.
              - Übernimm nur Anforderungen, die sich auf mindestens einen Notizzettel oder einen Wiki-Eintrag zurückführen lassen.
              - Gib zu jedem Akzeptanzkriterium die verwendeten Quellen an (beispielsweise Tom-1 für die erste Notiz von Tom oder P-4 für das vierte Statement unter Punkt 'P' im Wiki) 
              - Unterscheide zwischen gefordertem Verhalten und Beschreibungen des bestehenden Systems. Ein beschriebener Ist-Zustand ist nicht automatisch eine Anforderung.
              - Stelle unvollständige oder nicht ausreichend belegte Informationen nicht als gesicherte Anforderungen dar. Formuliere sie stattdessen als offene Frage.
              - Identifiziere Widersprüche zwischen Notizzetteln sowie zwischen Notizzetteln und Wiki-Einträgen. Löse Widersprüche nicht selbstständig auf, auch wenn eine der Angaben plausibler erscheint.
              - Wenn ein Widerspruch identifiziert wurde, soll dieser nicht zusätzlich eine offene Frage sein. Ebenso soll eine offene Frage nicht zusätzlich ein Widerspruch sein.
              - Ignoriere Wiki-Inhalte, die keinen erkennbaren Bezug zum Ticket besitzen.
              - Erfinde keine Quellenangaben. Verwende ausschliesslich die angegebenen Bezeichnungen der Notizzettel und Wiki-Einträge.
              - Falls in einer Kategorie keine Inhalte erkannt werden, gib eine leere Liste aus.
              - Benutze innerhalb von JSON-Strings keine doppelten Anführungszeichen. Wenn du Anführungszeichen benutzen musst, benutze einfache ('')
            """;

    @Bean
    public OpenAiChatModel openAiChatModel(@Value("${OPENAI_API_KEY}") String apiKey) {
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .model("gpt-6-astra")
                        .reasoningEffort("low")
                        .build())
                .build();
    }

    @Bean
    public OpenAiChatModel qwenChatModel() {
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl("http://192.168.250.1:1234/v1")
                        .apiKey("doesnt-matter")
                        .model("qwen/qwen3.8-27b")
                        .reasoningEffort("low")
                        .build())
                .build();
    }

    @Bean
    public ChatClient openAiChatClient(@Qualifier("openAiChatModel") OpenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    @Bean
    public ChatClient qwenChatClient(@Qualifier("qwenChatModel") OpenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }
}
