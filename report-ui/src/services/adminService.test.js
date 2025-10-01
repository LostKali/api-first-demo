import AdminService from './adminService';

describe('AdminService', () => {
  let adminService;

  beforeEach(() => {
    adminService = new AdminService();
  });

  it('should create an instance with default base URL', () => {
    expect(adminService).toBeInstanceOf(AdminService);
    expect(adminService.client.defaults.baseURL).toBe('/api/admin');
  });

  it('should create an instance with custom base URL', () => {
    const customService = new AdminService('http://localhost:8080/api/admin');
    expect(customService.client.defaults.baseURL).toBe('http://localhost:8080/api/admin');
  });

  it('should have all required methods', () => {
    expect(typeof adminService.createReportRequest).toBe('function');
    expect(typeof adminService.getReports).toBe('function');
    expect(typeof adminService.getReport).toBe('function');
    expect(typeof adminService.cancelReport).toBe('function');
    expect(typeof adminService.downloadReport).toBe('function');
  });
});
