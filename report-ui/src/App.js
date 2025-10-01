import React, { useState } from 'react';
import AdminService from './services/adminService';

function App() {
  const [adminService] = useState(() => new AdminService());
  const [formData, setFormData] = useState({
    dateFrom: '2024-01-01',
    dateTo: '2024-01-31',
    reportType: 'USER_ACTIVITY'
  });

  const [reportId, setReportId] = useState(null);
  const [status, setStatus] = useState(null);
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const reportTypes = [
    { value: 'USER_ACTIVITY', label: 'User Activity' },
    { value: 'SALES_ANALYSIS', label: 'Sales Analysis' },
    { value: 'SYSTEM_METRICS', label: 'System Metrics' }
  ];

  const handleInputChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const createReport = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await adminService.createReportRequest({
        ...formData,
        description: `${formData.reportType} report`,
        priority: 'NORMAL'
      });

      setReportId(response.id);
      setStatus(response.status);
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const getReports = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await adminService.getReports();
      setReports(response.content || []);
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const getReport = async () => {
    if (!reportId) return;
    
    setLoading(true);
    setError(null);

    try {
      const response = await adminService.getReport(reportId);
      setStatus(response.status);
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const cancelReport = async () => {
    if (!reportId) return;
    
    setLoading(true);
    setError(null);

    try {
      await adminService.cancelReport(reportId);
      setStatus('CANCELLED');
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const downloadReport = async () => {
    if (!reportId) return;
    
    setLoading(true);
    setError(null);

    try {
      const blob = await adminService.downloadReport(reportId, 'csv');
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `report-${reportId}.csv`;
      link.click();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <h1>Report Management System</h1>
      
      <div style={{ marginBottom: '20px' }}>
        <h2>Create Report</h2>
        
        <div style={{ marginBottom: '10px' }}>
          <label>Date From: </label>
          <input
            type="date"
            value={formData.dateFrom}
            onChange={(e) => handleInputChange('dateFrom', e.target.value)}
          />
        </div>

        <div style={{ marginBottom: '10px' }}>
          <label>Date To: </label>
          <input
            type="date"
            value={formData.dateTo}
            onChange={(e) => handleInputChange('dateTo', e.target.value)}
          />
        </div>

        <div style={{ marginBottom: '10px' }}>
          <label>Report Type: </label>
          <select
            value={formData.reportType}
            onChange={(e) => handleInputChange('reportType', e.target.value)}
          >
            {reportTypes.map(type => (
              <option key={type.value} value={type.value}>
                {type.label}
              </option>
            ))}
          </select>
        </div>

        <button onClick={createReport} disabled={loading}>
          {loading ? 'Creating...' : 'Create Report'}
        </button>
      </div>

      {reportId && (
        <div style={{ marginBottom: '20px' }}>
          <h2>Report Status</h2>
          <p>Report ID: {reportId}</p>
          <p>Status: {status}</p>
          
          <div style={{ marginTop: '10px' }}>
            <button onClick={getReport} disabled={loading} style={{ marginRight: '10px' }}>
              Refresh Status
            </button>
            {status === 'PENDING' || status === 'PROCESSING' ? (
              <button onClick={cancelReport} disabled={loading} style={{ backgroundColor: '#dc3545', color: 'white' }}>
                Cancel Report
              </button>
            ) : status === 'COMPLETED' ? (
              <button onClick={downloadReport} disabled={loading} style={{ backgroundColor: '#28a745', color: 'white' }}>
                Download Report
              </button>
            ) : null}
          </div>
        </div>
      )}

      <div style={{ marginBottom: '20px' }}>
        <h2>Reports List</h2>
        <button onClick={getReports} disabled={loading}>
          {loading ? 'Loading...' : 'Get Reports'}
        </button>
        
        {reports.length > 0 && (
          <div style={{ marginTop: '10px' }}>
            <table style={{ border: '1px solid #ccc', borderCollapse: 'collapse', width: '100%' }}>
              <thead>
                <tr style={{ backgroundColor: '#f5f5f5' }}>
                  <th style={{ border: '1px solid #ccc', padding: '8px' }}>ID</th>
                  <th style={{ border: '1px solid #ccc', padding: '8px' }}>Type</th>
                  <th style={{ border: '1px solid #ccc', padding: '8px' }}>Status</th>
                  <th style={{ border: '1px solid #ccc', padding: '8px' }}>Date From</th>
                  <th style={{ border: '1px solid #ccc', padding: '8px' }}>Date To</th>
                </tr>
              </thead>
              <tbody>
                {reports.map((report) => (
                  <tr key={report.id}>
                    <td style={{ border: '1px solid #ccc', padding: '8px' }}>{report.id}</td>
                    <td style={{ border: '1px solid #ccc', padding: '8px' }}>{report.reportType}</td>
                    <td style={{ border: '1px solid #ccc', padding: '8px' }}>{report.status}</td>
                    <td style={{ border: '1px solid #ccc', padding: '8px' }}>{report.dateFrom}</td>
                    <td style={{ border: '1px solid #ccc', padding: '8px' }}>{report.dateTo}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {error && (
        <div style={{ color: 'red', marginTop: '20px' }}>
          <strong>Error:</strong> {error}
        </div>
      )}
    </div>
  );
}

export default App;