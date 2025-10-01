/**
 * Pact interaction fixtures
 * Centralized place for defining all interactions between consumer and provider
 */

const interactions = {
  createReport: {
    state: 'a report request can be created',
    uponReceiving: 'a request to create a report',
    withRequest: {
      method: 'POST',
      path: '/api/admin/reports',
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        dateFrom: '2024-01-01',
        dateTo: '2024-01-31',
        reportType: 'USER_ACTIVITY',
        description: 'USER_ACTIVITY report',
        priority: 'NORMAL',
      },
    },
    willRespondWith: {
      status: 201,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
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
    },
  },

  createReportWithError: {
    state: 'invalid request data',
    uponReceiving: 'a request to create a report with invalid data',
    withRequest: {
      method: 'POST',
      path: '/api/admin/reports',
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        dateFrom: 'invalid-date',
        dateTo: '2024-01-31',
        reportType: 'INVALID_TYPE',
        description: '',
        priority: 'NORMAL',
      },
    },
    willRespondWith: {
      status: 400,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
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
    },
  },

  getReports: {
    state: 'reports exist',
    uponReceiving: 'a request to get all reports',
    withRequest: {
      method: 'GET',
      path: '/api/admin/reports',
    },
    willRespondWith: {
      status: 200,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
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
    },
  },

  getReportsWithFilter: {
    state: 'reports exist with different statuses',
    uponReceiving: 'a request to get reports with status filter',
    withRequest: {
      method: 'GET',
      path: '/api/admin/reports',
      query: 'status=PENDING',
    },
    willRespondWith: {
      status: 200,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        content: [
          {
            id: '123e4567-e89b-12d3-a456-426614174001',
            dateFrom: '2024-01-01',
            dateTo: '2024-01-31',
            reportType: 'USER_ACTIVITY',
            status: 'PENDING',
            priority: 'HIGH',
            description: 'Pending report',
            createdAt: '2024-01-15T12:00:00Z',
            updatedAt: '2024-01-15T12:00:00Z',
          },
        ],
        totalElements: 1,
        totalPages: 1,
        currentPage: 0,
        pageSize: 20,
      },
    },
  },

  getReportById: {
    state: 'a report with id 123e4567-e89b-12d3-a456-426614174000 exists',
    uponReceiving: 'a request to get report details',
    withRequest: {
      method: 'GET',
      path: '/api/admin/reports/123e4567-e89b-12d3-a456-426614174000',
    },
    willRespondWith: {
      status: 200,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
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
    },
  },

  getReportByIdNotFound: {
    state: 'report does not exist',
    uponReceiving: 'a request to get non-existent report',
    withRequest: {
      method: 'GET',
      path: '/api/admin/reports/00000000-0000-0000-0000-000000000000',
    },
    willRespondWith: {
      status: 404,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        error: 'REPORT_NOT_FOUND',
        message: 'Report with id 00000000-0000-0000-0000-000000000000 not found',
      },
    },
  },

  cancelReport: {
    state: 'a report with id 123e4567-e89b-12d3-a456-426614174000 can be cancelled',
    uponReceiving: 'a request to cancel a report',
    withRequest: {
      method: 'DELETE',
      path: '/api/admin/reports/123e4567-e89b-12d3-a456-426614174000',
    },
    willRespondWith: {
      status: 200,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
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
    },
  },

  downloadReport: {
    state: 'a report with id 123e4567-e89b-12d3-a456-426614174000 is ready for download',
    uponReceiving: 'a request to download a report',
    withRequest: {
      method: 'GET',
      path: '/api/admin/reports/123e4567-e89b-12d3-a456-426614174000/download',
      query: {
        format: 'csv'
      },
    },
    willRespondWith: {
      status: 200,
      headers: {
        'Content-Type': 'application/octet-stream',
        'Content-Disposition': 'attachment; filename="report.csv"',
      },
      body: 'id,date,user,action\n1,2024-01-01,user1,login\n2,2024-01-02,user2,logout',
    },
  },

  downloadReportNotReady: {
    state: 'report with id 123e4567-e89b-12d3-a456-426614174000 is not ready',
    uponReceiving: 'a request to download a report that is not ready',
    withRequest: {
      method: 'GET',
      path: '/api/admin/reports/123e4567-e89b-12d3-a456-426614174000/download',
    },
    willRespondWith: {
      status: 409,
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        error: 'REPORT_NOT_READY',
        message: 'Report is not ready for download',
        status: 'PROCESSING',
      },
    },
  },
};

module.exports = interactions;
