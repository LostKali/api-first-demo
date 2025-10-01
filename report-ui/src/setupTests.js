// setupTests.js
import '@testing-library/jest-dom';

// Mock environment for Pact tests
if (typeof global.TextEncoder === 'undefined') {
  const { TextEncoder, TextDecoder } = require('util');
  global.TextEncoder = TextEncoder;
  global.TextDecoder = TextDecoder;
}
