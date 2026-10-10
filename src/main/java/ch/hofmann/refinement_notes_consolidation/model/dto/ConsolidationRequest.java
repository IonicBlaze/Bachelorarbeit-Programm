package ch.hofmann.refinement_notes_consolidation.model.dto;

import java.util.List;
import java.util.Map;

public record ConsolidationRequest(String provider, Map<String, List<String>> participantNotes, List<WikiEntry> projectWiki) {
}
