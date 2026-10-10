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
    "Peter": [
      "Der Button soll blau sein",
      "Wenn der Button gedrückt wurde, ist er für 5s disabled"
    ],
    "Jane": [
      "Der rote Button wird nach dem klick für 5 Sekunden deaktiviert",
      "Man könnte den Button allenfalls farblich abgrenzen wenn disabled"
    ]
  },
  "projectWiki": [
    {
      "id": "A11Y",
      "statements": [
        "Alle Buttons müssen immer WCAG 2.0 kompatibel sein",
        "Buttons müssen einen WCAG-konformen Kontrast zum Hintergrund aufweisen."
      ]
    }
  ],
  "provider": "local"
}
}
```
- Provider is always `local`
- A response looks like this:
```
{
  "acceptanceCriteria": [
    {
      "id": "AK-1",
      "text": "Nach dem Klick auf den Button ist dieser für 5 Sekunden deaktiviert.",
      "sources": [
        "Jane-0",
        "Peter-1"
      ]
    },
    {
      "id": "AK-2",
      "text": "Der Button muss einen WCAG-konformen Kontrast zum Hintergrund aufweisen.",
      "sources": [
        "A11Y-2"
      ]
    },
    {
      "id": "AK-3",
      "text": "Der Button muss WCAG 2.0-kompatibel sein.",
      "sources": [
        "A11Y-1"
      ]
    }
  ],
  "openQuestions": [
    {
      "id": "OF-1",
      "text": "Soll der Button im deaktivierten Zustand farblich abgegrenzt werden?",
      "sources": [
        "Jane-1"
      ]
    }
  ],
  "contradictions": [
    {
      "id": "WS-1",
      "text": "Peter gibt an, dass der Button blau sein soll, während Jane ihn als 'roten Button' bezeichnet.",
      "sources": [
        "Jane-0",
        "Peter-0"
      ]
    }
  ]
}
```

# Test cases

- All test cases must use German language

