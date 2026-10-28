package ch.hofmann.refinement_notes_consolidation.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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
              - Ein finales AK sollte die Summe der Details aus unterschiedlichen Statements abbilden, solange diese Statements sich auf dasselbe Thema beziehen und sich die Details nur ergänzen und nicht widersprechen.
              - Übernimm nur Anforderungen, die sich auf mindestens einen Notizzettel oder einen Wiki-Eintrag zurückführen lassen.
              - Gib zu jedem Akzeptanzkriterium die verwendeten Quellen an (beispielsweise Tom-1 für die erste Notiz von Tom oder P-4 für das vierte Statement unter Punkt 'P' im Wiki).
              - Unterscheide zwischen gefordertem Verhalten und Beschreibungen des bestehenden Systems. Ein beschriebener Ist-Zustand ist nicht automatisch eine Anforderung.
              - Übernimm Aussagen, die in den Quellen als ungeklärt gekennzeichnet sind, nicht als Akzeptanzkriterium, sondern als offene Frage.
              - Identifiziere Widersprüche zwischen Notizzetteln sowie zwischen Notizzetteln und Wiki-Einträgen. Löse Widersprüche nicht selbstständig auf, auch wenn eine der Angaben plausibler erscheint.
              - Wenn ein Widerspruch identifiziert wurde, soll dieser nicht zusätzlich eine offene Frage sein. Ebenso soll eine offene Frage nicht zusätzlich ein Widerspruch sein.
              - Eine offene Frage liegt nur vor, wenn ein Notizzettel oder ein Wiki-Eintrag einen für das Ticket relevanten Sachverhalt ausdrücklich als ungeklärt kennzeichnet, und zwar durch:
                (1) eine direkte oder indirekte Frage (z.B. 'Wie lange dauert ...?', 'Unklar ist, ob ...'),
                (2) eine unsichere Formulierung: Konjunktiv der Unsicherheit ('könnte', 'müsste', 'wäre'), Wörter wie 'vielleicht', 'eventuell', 'wahrscheinlich' oder Klärungsvermerke ('muss noch geklärt werden', 'TBD').
              - Leite keine eigenen offenen Fragen aus fehlenden oder unvollständigen Informationen ab.
              - Beantwortet ein anderer Notizzettel oder ein Wiki-Eintrag die Frage verbindlich, ist sie keine offene Frage, sondern ein Akzeptanzkriterium.
              - Steht einer unsicheren Aussage eine bestimmte, abweichende Aussage einer anderen Quelle gegenüber, ist dies ein Widerspruch und keine offene Frage.
              - Wunschformulierungen im Konjunktiv (z.B. 'Es wäre schön, wenn ...', 'Wir hätten gern ...') sind Anforderungen und keine offenen Fragen.
              - Aussagen, die keine konkreten Anforderungen beschreiben, können ignoriert werden (Z.B. 'Team X könnte behilflich sein')
              - Interpretiere offene Fragen nicht sondern weise sie lediglich aus. Interessant ist nur die offene Frage, nicht aber woran du sie identifiziert hast (z.B. 'Konjuntiv deutet darauf hin, dass ...')
              - Ignoriere Wiki-Inhalte, die keinen erkennbaren Bezug zum Ticket besitzen.
              - Erfinde keine Quellenangaben. Verwende ausschliesslich die angegebenen Bezeichnungen der Notizzettel und Wiki-Einträge.
              - Falls in einer Kategorie keine Inhalte erkannt werden, gib eine leere Liste aus.
              - Benutze innerhalb von JSON-Strings keine doppelten Anführungszeichen. Wenn du Anführungszeichen benutzen musst, benutze einfache ('')
              - Teile jedem Akzeptanzkriterium, jedem Widerspruch und jeder offnen Frage eine eindeutige, aufsteigende ID zu (Regex: (AK|WS|OF)-\\d+).
            """;

    @Bean
    public OpenAiChatModel openAiChatModel(@Value("${OPENAI_API_KEY}") String apiKey) {
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .model("gpt-6-astra")
                        .reasoningEffort("low")
                        .timeout(Duration.ofMinutes(30))
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
                        .timeout(Duration.ofMinutes(30))
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
