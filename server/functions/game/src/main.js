import { Client, Databases } from 'node-appwrite';
import GameModel from './gameModel.js';

export default async ({ req, res, log, error }) => {
  const client = new Client()
    .setEndpoint(process.env.APPWRITE_FUNCTION_API_ENDPOINT)
    .setProject(process.env.APPWRITE_FUNCTION_PROJECT_ID)
    .setKey(req.headers['x-appwrite-key'] ?? '');

  const databases = new Databases(client);
  const gameModel = new GameModel(databases);

  if (req.method === 'POST' && req.path === '/create') {
    if (!req.bodyJson || !req.bodyJson.hostPlayer) {
      return res.json(
        {
          error: 'Missing hostPlayer in request body',
        },
        500
      );
    }

    try {
      const game = await gameModel.create(req.bodyJson.hostPlayer);

      return res.json(game);
    } catch (err) {
      error('Could not create game: ' + err.message);
      return res.json(
        {
          error: 'Could not create game: ' + err.message,
        },
        500
      );
    }
  }

  if (req.method === 'POST' && req.path === '/join') {
    if (!req.bodyJson || !req.bodyJson.gameId || !req.bodyJson.player) {
      error('Missing gameId or player in request body');
      return res.json(
        {
          error: 'Missing gameId or player in request body',
        },
        500
      );
    }

    const { gameId, player } = req.bodyJson;

    try {
      const game = await gameModel.join(gameId, player);
      return res.json(game);
    } catch (err) {
      error('Could not join game: ' + err.message);
      return res.json(
        {
          error: 'Could not join game: ' + err.message,
        },
        500
      );
    }
  }

  return res.json(
    {
      error: 'Invalid request method or path',
    },
    400
  );
};
