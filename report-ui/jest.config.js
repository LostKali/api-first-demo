module.exports = {
  testEnvironment: "node",
  testMatch: [
    "**/pact/**/admin-service.pact.test.js"
  ],
  testTimeout: 30000,
  transformIgnorePatterns: [
    "node_modules/(?!(@pact-foundation|axios)/)"
  ],
  // Ignore Pact files in watch mode
  watchPathIgnorePatterns: [
    "pacts/",
    "logs/"
  ],
  // Clear mocks between tests
  clearMocks: true,
  // Sequential execution to avoid port conflicts
  maxWorkers: 1
};
