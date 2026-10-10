package ch.hofmann.refinement_notes_consolidation.model.prompt;

import ch.hofmann.refinement_notes_consolidation.model.dto.ConsolidationRequest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsolidationPrompt {

    public static String from(ConsolidationRequest request) {
        String prompt;

        if (!request.projectWiki().isEmpty()) {
            prompt = """
                    # Notizzettel
                    
                    {notes}
                    
                    # Projekt-Wiki
                    
                    {wiki}
                    """;
        } else {
            prompt = """
                    # Notizzettel
                    
                    {notes}
                    """;
        }

        StringBuilder sb = new StringBuilder();

        request.participantNotes().entrySet().forEach(e -> {
            String user = e.getKey();

            String[] lines = e.getValue().split("\n");
            String notizenStatements = IntStream.range(0, lines.length)
                    .mapToObj(i -> "%d. %s".formatted(i, lines[i]))
                    .collect(Collectors.joining("\n"));

            sb.append("""
                    ## {user}
                    {notes}
                    
                    """
                    .replace("{user}", user)
                    .replace("{notes}", notizenStatements)
            );
        });

        prompt = prompt.replace("{notes}", sb.toString());

        if (!request.projectWiki().isEmpty()) {
            StringBuilder wikiBuilder = new StringBuilder();

            request.projectWiki().forEach(entry -> {
                var statements = IntStream.range(0, entry.statements().size())
                        .mapToObj(i -> "%d. %s".formatted((i+1), entry.statements().get(i)))
                        .collect(Collectors.joining("\n"));

                wikiBuilder.append("""
                        ## {id}
                        {statements}
                        """
                        .replace("{id}", entry.id())
                        .replace("{statements}", statements)
                );
            });


            prompt = prompt.replace("{wiki}", wikiBuilder.toString());
        }
        return prompt;
    }

}
