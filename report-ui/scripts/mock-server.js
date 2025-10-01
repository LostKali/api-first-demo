const express = require('express');
const cors = require('cors');
const { v4: uuidv4 } = require('uuid');

const app = express();
const PORT = 8080;

// Middleware
app.use(cors());
app.use(express.json());

// In-memory storage for reports
let reports = [];
let nextId = 1;

// Helper function to create a report
const createReport = (data) => ({
  id: uuidv4(),
  dateFrom: data.dateFrom,
  dateTo: data.dateTo,
  reportType: data.reportType,
  status: 'PENDING',
  priority: data.priority || 'NORMAL',
  description: data.description || `${data.reportType} report`,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString(),
  estimatedCompletionTime: new Date(Date.now() + 15 * 60 * 1000).toISOString(), // 15 minutes from now
  progress: 0,
});

// POST /api/admin/reports - Create a new report request
app.post('/api/admin/reports', (req, res) => {
  try {
    const { dateFrom, dateTo, reportType, description, priority } = req.body;

    // Validation
    if (!dateFrom || !dateTo || !reportType) {
      return res.status(400).json({
        timestamp: new Date().toISOString(),
        status: 400,
        error: 'Bad Request',
        message: 'Missing required fields: dateFrom, dateTo, reportType',
        path: '/api/admin/reports',
        details: []
      });
    }

    // Check date range
    if (new Date(dateFrom) > new Date(dateTo)) {
      return res.status(400).json({
        timestamp: new Date().toISOString(),
        status: 400,
        error: 'Bad Request',
        message: 'Invalid date range',
        path: '/api/admin/reports',
        details: [
          {
            field: 'dateTo',
            message: 'End date must be after start date'
          }
        ]
      });
    }

    const report = createReport({ dateFrom, dateTo, reportType, description, priority });
    reports.push(report);

    res.status(201).json(report);
  } catch (error) {
    res.status(500).json({
      timestamp: new Date().toISOString(),
      status: 500,
      error: 'Internal Server Error',
      message: error.message,
      path: '/api/admin/reports'
    });
  }
});

// GET /api/admin/reports - Get list of reports
app.get('/api/admin/reports', (req, res) => {
  try {
    const { status, page = 0, size = 20 } = req.query;
    
    let filteredReports = reports;
    
    // Filter by status if provided
    if (status) {
      filteredReports = reports.filter(report => report.status === status);
    }

    // Pagination
    const startIndex = parseInt(page) * parseInt(size);
    const endIndex = startIndex + parseInt(size);
    const paginatedReports = filteredReports.slice(startIndex, endIndex);

    res.json({
      content: paginatedReports,
      totalElements: filteredReports.length,
      totalPages: Math.ceil(filteredReports.length / parseInt(size)),
      currentPage: parseInt(page),
      pageSize: parseInt(size)
    });
  } catch (error) {
    res.status(500).json({
      timestamp: new Date().toISOString(),
      status: 500,
      error: 'Internal Server Error',
      message: error.message,
      path: '/api/admin/reports'
    });
  }
});

// GET /api/admin/reports/:reportId - Get specific report details
app.get('/api/admin/reports/:reportId', (req, res) => {
  try {
    const { reportId } = req.params;
    const report = reports.find(r => r.id === reportId);

    if (!report) {
      return res.status(404).json({
        timestamp: new Date().toISOString(),
        status: 404,
        error: 'Not Found',
        message: 'Report not found',
        path: `/api/admin/reports/${reportId}`
      });
    }

    res.json(report);
  } catch (error) {
    res.status(500).json({
      timestamp: new Date().toISOString(),
      status: 500,
      error: 'Internal Server Error',
      message: error.message,
      path: `/api/admin/reports/${req.params.reportId}`
    });
  }
});

// DELETE /api/admin/reports/:reportId - Cancel a report
app.delete('/api/admin/reports/:reportId', (req, res) => {
  try {
    const { reportId } = req.params;
    const reportIndex = reports.findIndex(r => r.id === reportId);

    if (reportIndex === -1) {
      return res.status(404).json({
        timestamp: new Date().toISOString(),
        status: 404,
        error: 'Not Found',
        message: 'Report not found',
        path: `/api/admin/reports/${reportId}`
      });
    }

    const report = reports[reportIndex];
    
    // Check if report can be cancelled
    if (report.status === 'COMPLETED' || report.status === 'CANCELLED') {
      return res.status(409).json({
        timestamp: new Date().toISOString(),
        status: 409,
        error: 'Conflict',
        message: 'Report is already completed or cancelled',
        path: `/api/admin/reports/${reportId}`
      });
    }

    // Cancel the report
    report.status = 'CANCELLED';
    report.updatedAt = new Date().toISOString();
    report.progress = 0;

    res.json(report);
  } catch (error) {
    res.status(500).json({
      timestamp: new Date().toISOString(),
      status: 500,
      error: 'Internal Server Error',
      message: error.message,
      path: `/api/admin/reports/${req.params.reportId}`
    });
  }
});

// GET /api/admin/reports/:reportId/download - Download a report
app.get('/api/admin/reports/:reportId/download', (req, res) => {
  try {
    const { reportId } = req.params;
    const { format = 'csv' } = req.query;
    
    const report = reports.find(r => r.id === reportId);

    if (!report) {
      return res.status(404).json({
        timestamp: new Date().toISOString(),
        status: 404,
        error: 'Not Found',
        message: 'Report not found',
        path: `/api/admin/reports/${reportId}/download`
      });
    }

    // Check if report is completed
    if (report.status !== 'COMPLETED') {
      return res.status(409).json({
        timestamp: new Date().toISOString(),
        status: 409,
        error: 'Conflict',
        message: 'Report is not ready for download',
        path: `/api/admin/reports/${reportId}/download`
      });
    }

    // Generate mock CSV content
    const csvContent = `date,user,action,count
2024-01-01,user1,login,5
2024-01-01,user2,logout,3
2024-01-02,user1,view,10
2024-01-02,user3,create,2`;

    res.setHeader('Content-Type', 'application/octet-stream');
    res.setHeader('Content-Disposition', `attachment; filename="report-${reportId}.${format}"`);
    res.send(csvContent);
  } catch (error) {
    res.status(500).json({
      timestamp: new Date().toISOString(),
      status: 500,
      error: 'Internal Server Error',
      message: error.message,
      path: `/api/admin/reports/${req.params.reportId}/download`
    });
  }
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

// Start server
app.listen(PORT, () => {
  console.log(`Mock Admin Service running on http://localhost:${PORT}`);
  console.log('Available endpoints:');
  console.log('  POST   /api/admin/reports');
  console.log('  GET    /api/admin/reports');
  console.log('  GET    /api/admin/reports/:id');
  console.log('  DELETE /api/admin/reports/:id');
  console.log('  GET    /api/admin/reports/:id/download');
  console.log('  GET    /health');
});

module.exports = app;
