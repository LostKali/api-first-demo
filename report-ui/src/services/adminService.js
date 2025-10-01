import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || '/api/admin';

class AdminService {
  constructor(baseURL = API_BASE_URL) {
    this.client = axios.create({
      baseURL,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  // Create a new report request
  async createReportRequest(reportRequest) {
    const response = await this.client.post('/reports', reportRequest);
    return response.data;
  }

  // Get list of reports
  async getReports(params = {}) {
    const response = await this.client.get('/reports', { params });
    return response.data;
  }

  // Get specific report details
  async getReport(reportId) {
    const response = await this.client.get(`/reports/${reportId}`);
    return response.data;
  }

  // Cancel a report generation
  async cancelReport(reportId) {
    const response = await this.client.delete(`/reports/${reportId}`);
    return response.data;
  }

  // Download a completed report
  async downloadReport(reportId, format = 'csv') {
    const response = await this.client.get(`/reports/${reportId}/download`, {
      params: { format },
      responseType: 'blob',
    });
    return response.data;
  }
}

export default AdminService;
