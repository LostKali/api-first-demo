/**
 * Test data for Pact tests
 * Centralized place for all test data
 */

const testData = {
  // UUID for testing
  reportId: '123e4567-e89b-12d3-a456-426614174000',
  nonExistentReportId: '00000000-0000-0000-0000-000000000000',

  // Valid request data
  validReportRequest: {
    dateFrom: '2024-01-01',
    dateTo: '2024-01-31',
    reportType: 'USER_ACTIVITY',
    description: 'USER_ACTIVITY report',
    priority: 'NORMAL',
  },

  // Invalid request data
  invalidReportRequest: {
    dateFrom: 'invalid-date',
    dateTo: '2024-01-31',
    reportType: 'INVALID_TYPE',
    description: '',
    priority: 'NORMAL',
  },

  // Expected responses
  expectedCreatedReport: {
    id: '123e4567-e89b-12d3-a456-426614174000',
    dateFrom: '2024-01-01',
    dateTo: '2024-01-31',
    reportType: 'USER_ACTIVITY',
    status: 'PENDING',
    priority: 'NORMAL',
    description: 'USER_ACTIVITY report',
    createdAt: '2024-01-15T10:30:00Z',
    updatedAt: '2024-01-15T10:30:00Z',
  },

  expectedCompletedReport: {
    id: '123e4567-e89b-12d3-a456-426614174000',
    dateFrom: '2024-01-01',
    dateTo: '2024-01-31',
    reportType: 'USER_ACTIVITY',
    status: 'COMPLETED',
    priority: 'NORMAL',
    description: 'USER_ACTIVITY report',
    createdAt: '2024-01-15T10:30:00Z',
    updatedAt: '2024-01-15T11:30:00Z',
  },

  expectedProcessingReport: {
    id: '123e4567-e89b-12d3-a456-426614174000',
    dateFrom: '2024-01-01',
    dateTo: '2024-01-31',
    reportType: 'USER_ACTIVITY',
    status: 'PROCESSING',
    priority: 'NORMAL',
    description: 'USER_ACTIVITY report',
    createdAt: '2024-01-15T10:30:00Z',
    updatedAt: '2024-01-15T10:35:00Z',
    progress: 75,
  },

  expectedCancelledReport: {
    id: '123e4567-e89b-12d3-a456-426614174000',
    dateFrom: '2024-01-01',
    dateTo: '2024-01-31',
    reportType: 'USER_ACTIVITY',
    status: 'CANCELLED',
    priority: 'NORMAL',
    description: 'USER_ACTIVITY report',
    createdAt: '2024-01-15T10:30:00Z',
    updatedAt: '2024-01-15T10:40:00Z',
  },

  // Pagination data
  paginationData: {
    content: [
      {
        id: '123e4567-e89b-12d3-a456-426614174000',
        dateFrom: '2024-01-01',
        dateTo: '2024-01-31',
        reportType: 'USER_ACTIVITY',
        status: 'COMPLETED',
        priority: 'NORMAL',
        description: 'USER_ACTIVITY report',
        createdAt: '2024-01-15T10:30:00Z',
        updatedAt: '2024-01-15T11:30:00Z',
      },
    ],
    totalElements: 1,
    totalPages: 1,
    currentPage: 0,
    pageSize: 20,
  },

  // CSV data for download
  csvReportData: 'id,date,user,action\n1,2024-01-01,user1,login\n2,2024-01-02,user2,logout',

  // Errors
  validationError: {
    error: 'VALIDATION_ERROR',
    message: 'Invalid request data',
    details: [
      {
        field: 'dateFrom',
        message: 'Invalid date format'
      },
      {
        field: 'reportType',
        message: 'Invalid report type'
      }
    ],
  },

  notFoundError: {
    error: 'REPORT_NOT_FOUND',
    message: 'Report with id 00000000-0000-0000-0000-000000000000 not found',
  },

  notReadyError: {
    error: 'REPORT_NOT_READY',
    message: 'Report is not ready for download',
    status: 'PROCESSING',
  },
};

module.exports = testData;
