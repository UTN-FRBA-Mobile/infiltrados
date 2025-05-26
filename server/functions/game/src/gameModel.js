const DATABASE_ID = '682a685b002e2cbbd56b';
const GAMES_COLLECTION_ID = '682a68600026de12cf19';

function createRandomString(length) {
  const chars =
    'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

export default class GameModel {
  constructor(databases) {
    this.databases = databases;
  }

  async create(hostPlayer) {
    const game = await this.databases.createDocument(
      DATABASE_ID,
      GAMES_COLLECTION_ID,
      createRandomString(5),
      {
        players: [hostPlayer],
        state: '{}',
      }
    );

    return game;
  }

  async join(gameId, player) {
    const game = await this.databases.getDocument(
      DATABASE_ID,
      GAMES_COLLECTION_ID,
      gameId
    );

    if (!game) {
      throw new Error('Game not found');
    }

    if (game.players.includes(player)) {
      throw new Error('Player already in game');
    }

    game.players.push(player);

    const updatedGame = await this.databases.updateDocument(
      DATABASE_ID,
      GAMES_COLLECTION_ID,
      gameId,
      { players: game.players }
    );

    return updatedGame;
  }
}
