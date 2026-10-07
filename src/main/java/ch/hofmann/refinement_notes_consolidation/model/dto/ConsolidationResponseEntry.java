package ch.hofmann.refinement_notes_consolidation.model.dto;

import java.util.Set;

public record ConsolidationResponseEntry(
        String text,
        Set<String> sources
) {
}
