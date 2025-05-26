# game

## 🧰 Usage

### POST /create

- Creates a new game

**Parameters**

```json
{
  "hostPlayer": "<player name>"
}
```

**Response**

Sample `200` Response:

```json
{
  "players": ["javier"],
  "state": "{}",
  "$id": "M2vhs",
  "$permissions": [],
  "$createdAt": "2025-05-26T21:28:07.408+00:00",
  "$updatedAt": "2025-05-26T21:28:07.408+00:00",
  "$databaseId": "682a685b002e2cbbd56b",
  "$collectionId": "682a68600026de12cf19"
}
```

### POST /join

- Joins a currently running game

**Parameters**

```json
{
  "gameId": "<game id>",
  "player": "<player name>"
}
```

**Response**

Sample `200` Response:

```json
{
  "players": ["javier", "pedro", "pedro", "pedro", "pedrito"],
  "state": "{}",
  "$id": "1voVj",
  "$createdAt": "2025-05-26T21:08:50.524+00:00",
  "$updatedAt": "2025-05-26T21:48:32.184+00:00",
  "$permissions": [],
  "$databaseId": "682a685b002e2cbbd56b",
  "$collectionId": "682a68600026de12cf19"
}
```
