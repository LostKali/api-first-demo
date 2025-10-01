# Simplified Report UI with Pact Contract Testing

This is a minimal React application with Pact contract testing for the AdminService API. The application has been simplified to the essential functionality only.

## Overview

The application provides a simple interface for:
- Creating report requests
- Viewing report status
- Listing reports
- Cancelling reports
- Downloading completed reports

## Dependencies

**Production Dependencies:**
- `axios` - HTTP client
- `react` - UI framework
- `react-dom` - React DOM rendering
- `react-scripts` - Build tools

**Development Dependencies:**
- `@pact-foundation/pact` - Contract testing
- `@pact-foundation/pact-node` - Pact publishing
- `@testing-library/jest-dom` - Jest DOM matchers
- `@testing-library/react` - React testing utilities
- `@testing-library/user-event` - User interaction testing
- `express` - Mock server framework
- `cors` - CORS middleware for mock server
- `uuid` - UUID generation for mock data
- `jest-pact` - Jest adapter for Pact testing

## Structure

- `src/App.js` - Main application component (simplified UI)
- `src/services/adminService.js` - API service layer
- `src/services/adminService.test.js` - Unit tests for AdminService
- `src/pact/admin-service.pact.test.js` - Pact contract tests
- `src/pact/fixtures/` - Pact test fixtures and data
- `scripts/mock-server.js` - Express mock server for testing
- `scripts/publish-pact.js` - Contract publishing script
- `scripts/clean-pact-artifacts.js` - Cleanup script
- `scripts/run-pact-tests.sh` - Full Pact testing cycle
- `pacts/` - Generated Pact contracts
- `logs/` - Pact logs

## Running the Application

```bash
npm start
```

## Running Tests

### Unit Tests
```bash
npm test
```

### Pact Contract Tests
```bash
npm run test:pact
```

### Pact Tests with Cleanup
```bash
npm run test:pact:clean
```

### Mock Server Only
```bash
npm run mock:server
```
Starts the mock server on http://localhost:8080

**Note**: 
- Pact tests use jest-pact for better integration with Jest
- All test scripts include `--runInBand` to prevent port conflicts

### Publish Contracts to Broker
```bash
npm run pact:publish
```

This will publish the generated contracts to the Pact Broker (configured via environment variables).

## Environment Variables

Configure the Pact Broker connection:

```bash
export PACT_BROKER_BASE_URL=http://localhost:9292
export PACT_BROKER_USERNAME=pact_workshop
export PACT_BROKER_PASSWORD=pact_workshop
export PUBLISH_PACT=true
```

## Contract Coverage

The Pact tests cover all major AdminService API endpoints:

### Report Management
- **POST /api/admin/reports** - Create report request
  - Valid request creation
  - Invalid request validation (400 errors)

- **GET /api/admin/reports** - List reports
  - Paginated results
  - Status filtering

- **GET /api/admin/reports/{reportId}** - Get report details
  - Successful retrieval
  - Not found (404 errors)

- **DELETE /api/admin/reports/{reportId}** - Cancel report
  - Successful cancellation

- **GET /api/admin/reports/{reportId}/download** - Download report
  - Successful download
  - Report not ready (409 errors)

## Integration with OpenAPI Spec

The Pact contracts are based on the OpenAPI specification in `docs/contracts/openapi-admin-service.yaml`, ensuring:
- Consistent data models
- Proper HTTP status codes
- Correct request/response schemas
- Validation rules

## Benefits

1. **API-First Development**: Contracts are defined before implementation
2. **Early Feedback**: Catch integration issues during development
3. **Independent Development**: Teams can work on consumer/provider separately
4. **Regression Prevention**: Breaking changes are detected automatically
5. **Documentation**: Contracts serve as living documentation

## Next Steps

1. Set up a Pact Broker instance
2. Configure CI/CD to run Pact tests
3. Implement provider verification tests in the AdminService
4. Use `can-i-deploy` checks to gate deployments
