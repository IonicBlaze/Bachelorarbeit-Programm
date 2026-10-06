package ch.hofmann.refinement_notes_consolidation.model;

import java.util.List;
import java.util.Map;

public record ConsolidationRequest(Map<String, String> participantNotes, List<WikiEntry> projectWiki) {
}
