# Project foundations

- This application helps scrum teams consolidate structured, distributed information into a structured draft for a backlog item
- The process is triggered via a web request that contains multiple notes and wiki entries
- The response of a consolidation request contains the consolidated acceptance criteria, contradictions between requirements and open questions
- For each entry the response contains the source(s) where the information originates from (note 1, wiki 2, etc.)

# API

- A consolidation request looks like the following:
``` 
{
  "participantNotes": {
    "Peter": "Der Button soll blau sein\nWenn der Button gedrückt wurde, ist er für 5s disabled",
    "Jane": "Der rote Button wird nach dem klick für 5 Sekunden deaktiviert"
  },
  "projectWiki": [
    {
      "id": "W1",
      "statements": [
        "Alle Buttons müssen immer WCAG 2.0 kompatibel sein",
        "Buttons müssen einen WCAG-konformen Kontrast zum Hintergrund aufweisen."
      ]
    }
  ],
  "provider": "local"
}
```
- Provider is always `local`
- A response looks like this:
```
{
  "acceptanceCriteria": [
    {
      "text": "Nach einem Klick ist der Button für 5 Sekunden deaktiviert.",
      "sources": [
        "Jane-0",
        "Peter-1"
      ]
    },
    {
      "text": "Der Button muss in allen Zuständen WCAG 2.0 entsprechen und einen WCAG-konformen Kontrast zum Hintergrund aufweisen.",
      "sources": [
        "W1-1",
        "W1-2"
      ]
    }
  ],
  "openQuestions": [
    {
      "text": "Welches WCAG-2.0-Konformitätsniveau soll für die Prüfung des Buttons und seines Kontrasts zugrunde gelegt werden?",
      "sources": [
        "W1-1",
        "W1-2"
      ]
    }
  ],
  "contradictions": [
    {
      "text": "Peter fordert einen blauen Button, während Jane den Button als rot bezeichnet. Ob Jane damit den Ist-Zustand beschreibt oder eine gewünschte Farbe vorgibt, ist nicht eindeutig.",
      "sources": [
        "Jane-0",
        "Peter-0"
      ]
    }
  ]
}
```

