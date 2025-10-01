#!/usr/bin/env node

const { Publisher } = require('@pact-foundation/pact-node');
const path = require('path');

const opts = {
  pactFilesOrDirs: [path.resolve(process.cwd(), 'pacts')],
  pactBroker: 'http://localhost:9292',
  pactBrokerUsername: 'pact',
  pactBrokerPassword: 'password',
  consumerVersion: '1.0.1',
  tags: ['main', 'latest'],
};

new Publisher(opts)
  .publish()
  .then(() => {
    console.log('✅ Pact contracts published successfully to Pact Broker');
    console.log('🌐 View contracts at: http://localhost:9292');
    console.log('💡 Username: pact, Password: password');
  })
  .catch((e) => {
    console.error('❌ Failed to publish Pact contracts:', e.message);
    process.exit(1);
  });