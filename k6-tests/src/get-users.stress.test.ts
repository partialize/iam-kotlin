import { Options } from 'k6/options';

import { UserGateway } from './gateway';

import client from './client';
import matrixType from './matrix-type';
export { default as handleSummary } from './handle-summary';

export const options: Options = {
  stages: [
    { duration: '2m', target: 4000 },
    { duration: '5m', target: 4000 },
    { duration: '2m', target: 6000 },
    { duration: '5m', target: 6000 },
    { duration: '2m', target: 8000 },
    { duration: '5m', target: 8000 },
    { duration: '2m', target: 10000 },
    { duration: '5m', target: 10000 },
    { duration: '10m', target: 0 },
  ],
};

matrixType(options, ['GET_users']);

const userGateway = new UserGateway({
  grantType: 'client_credentials',
  clientId: client.id,
  clientSecret: client.secret,
});

export default () => {
  userGateway.readAll();
};
