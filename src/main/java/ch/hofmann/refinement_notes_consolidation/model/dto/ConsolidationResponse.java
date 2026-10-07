package ch.hofmann.refinement_notes_consolidation.model.dto;

import java.util.List;

public record ConsolidationResponse(
    List<ConsolidationResponseEntry> acceptanceCriteria,
    List<ConsolidationResponseEntry> openQuestions,
    List<ConsolidationResponseEntry> contradictions
) {
}
