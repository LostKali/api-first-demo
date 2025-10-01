const { pactWith } = require('jest-pact');
const axios = require('axios').default;
const interactions = require('./fixtures/interactions');
const testData = require('./fixtures/test-data');

pactWith(
  {
    consumer: 'ReportUI',
    provider: 'AdminService',
    logLevel: 'INFO',
    dir: './pacts',
    logDir: './logs',
  },
  (provider) => {
    describe('Admin Service API', () => {
      describe('POST /api/admin/reports', () => {
        it('should create a report successfully', async () => {
          await provider.addInteraction(interactions.createReport);

          const response = await axios.post(
            `${provider.mockService.baseUrl}/api/admin/reports`,
            testData.validReportRequest,
            {
              headers: {
                'Content-Type': 'application/json',
              },
            }
          );

          expect(response.status).toBe(201);
          expect(response.data.id).toBe(testData.reportId);
          expect(response.data.status).toBe('PENDING');
          expect(response.data.reportType).toBe('USER_ACTIVITY');
        });
      });

      describe('GET /api/admin/reports', () => {
        it('should return a list of reports', async () => {
          await provider.addInteraction(interactions.getReports);

          const response = await axios.get(`${provider.mockService.baseUrl}/api/admin/reports`);

          expect(response.status).toBe(200);
          expect(response.data.content).toHaveLength(1);
          expect(response.data.content[0].status).toBe('COMPLETED');
          expect(response.data.totalElements).toBe(1);
        });
      });

      describe('GET /api/admin/reports/{reportId}', () => {
        it('should return report details', async () => {
          await provider.addInteraction(interactions.getReportById);

          const response = await axios.get(
            `${provider.mockService.baseUrl}/api/admin/reports/${testData.reportId}`
          );

          expect(response.status).toBe(200);
          expect(response.data.id).toBe(testData.reportId);
          expect(response.data.status).toBe('PROCESSING');
          expect(response.data.progress).toBe(75);
        });
      });

      describe('DELETE /api/admin/reports/{reportId}', () => {
        it('should cancel the report successfully', async () => {
          await provider.addInteraction(interactions.cancelReport);

          const response = await axios.delete(
            `${provider.mockService.baseUrl}/api/admin/reports/${testData.reportId}`
          );

          expect(response.status).toBe(200);
          expect(response.data.status).toBe('CANCELLED');
        });
      });

      describe('GET /api/admin/reports/{reportId}/download', () => {
        it('should download the report file', async () => {
          await provider.addInteraction(interactions.downloadReport);

          const response = await axios.get(
            `${provider.mockService.baseUrl}/api/admin/reports/${testData.reportId}/download`,
            {
              params: { format: 'csv' },
            }
          );

          expect(response.status).toBe(200);
          expect(response.data).toContain('user1,login');
        });
      });
    });
  }
);